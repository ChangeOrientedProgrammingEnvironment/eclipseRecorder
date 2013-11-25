package edu.oregonstate.cope.eclipse.listeners;

import java.io.InputStream;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.eclipse.COPEPlugin;

public class ResourceListener implements IResourceChangeListener {

	ClientRecorder recorder = COPEPlugin.getDefault().getClientRecorder();

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (isSavedAction() || isRefactoringInProgress())
		
			COPEPlugin.getDefault().getLogger().info(this, "This is a save action, so I will ignore it");
		else
			recordRefresh(event.getDelta());
	}

	private void recordRefresh(IResourceDelta delta) {
		if (delta == null)
			return ;
		
		IResource affectedResource = delta.getResource();
		int type = affectedResource.getType();
		if (type == IResource.FILE) {
			IFile affectedFile = (IFile) affectedResource;
			if (isClassFile(affectedFile)) {
				return;
			}
			InputStream inputStream;
			try {
				inputStream = affectedFile.getContents();
				Scanner scanner = new Scanner(inputStream, affectedFile.getCharset());
				String contents = scanner.useDelimiter("\\A").next();
				scanner.close();
				recorder.recordTextChange(contents, 0, 0,affectedFile.getFullPath().toPortableString(), ClientRecorder.CHANGE_ORIGIN_REFRESH);
			} catch (CoreException e) {
			}
		}
		IResourceDelta[] children = delta.getAffectedChildren();
		for (IResourceDelta child : children) {
			recordRefresh(child);
		}
	}

	private boolean isClassFile(IFile affectedFile) {
		String fileExtension = affectedFile.getFileExtension();
		if (fileExtension.equals("class"))
			return true;
		return false;
	}

	private boolean isSavedAction() {
		long currentTime = System.currentTimeMillis();
		return currentTime - SaveCommandExecutionListener.getLastSaveAction() < 500;
	}

	private boolean isRefactoringInProgress() {
		return RefactoringExecutionListener.isRefactoringInProgress();
	}
}

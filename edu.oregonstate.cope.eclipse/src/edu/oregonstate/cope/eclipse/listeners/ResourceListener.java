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
		IResourceDelta delta = event.getDelta();
		recordChangedDelta(delta);
	}

	private void recordChangedDelta(IResourceDelta delta) {
		if (delta == null)
			return;
		
		IResource affectedResource = delta.getResource();
		if (affectedResource.getType() == IResource.FILE) {
			IFile affectedFile = (IFile) affectedResource;
			String filePath = affectedFile.getFullPath().toPortableString();
			if (isClassFile(affectedFile))
				return;
			if (delta.getKind() == IResourceDelta.REMOVED) {
				recorder.recordResourceDelete(filePath);
				return;
			}
			
			if (isSavedAction() || isRefactoringInProgress())
				recorder.recordFileSave(filePath);
			else
				recordFileRefresh(affectedFile);
			return;
		}
		
		IResourceDelta[] affectedChildren = delta.getAffectedChildren();
		for (IResourceDelta child : affectedChildren) {
			recordChangedDelta(child);
		}
	}

	private void recordFileRefresh(IFile affectedFile) {
		String filePath = affectedFile.getFullPath().toPortableString();
		String contents = getFileContentents(affectedFile);
		recorder.recordTextChange(contents, 0, 0,filePath, ClientRecorder.CHANGE_ORIGIN_REFRESH);
	}

	private String getFileContentents(IFile affectedFile) {
		InputStream inputStream;
		try {
			inputStream = affectedFile.getContents();
			Scanner scanner = new Scanner(inputStream, affectedFile.getCharset());
			String contents = scanner.useDelimiter("\\A").next();
			scanner.close();
			return contents;
		} catch (CoreException e) {
			COPEPlugin.getDefault().getLogger().error(this, "Could not get contents of file", e);
		}
		return "";
	}

	private boolean isClassFile(IFile affectedFile) {
		String fileExtension = affectedFile.getFileExtension();
		if (fileExtension.equals("class"))
			return true;
		return false;
	}

	private boolean isSavedAction() {
		return CommandExecutionListener.isSaveInProgress();
	}

	private boolean isRefactoringInProgress() {
		return RefactoringExecutionListener.isRefactoringInProgress();
	}
}

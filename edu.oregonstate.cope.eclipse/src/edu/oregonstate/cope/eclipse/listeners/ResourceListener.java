package edu.oregonstate.cope.eclipse.listeners;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.clientRecorder.util.FileUtil;
import edu.oregonstate.cope.eclipse.COPEPlugin;

public class ResourceListener implements IResourceChangeListener {
	
	ClientRecorder recorder = COPEPlugin.getDefault().getClientRecorder();
	
	private Map<String, Long> lastSavedVersion;
	
	public ResourceListener() {
		lastSavedVersion = new HashMap<String, Long>();
	}

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
			if (isProjectIgnored(affectedResource))
				return;
			IFile affectedFile = (IFile) affectedResource;
			String filePath = affectedFile.getFullPath().toPortableString();
			if (isClassFile(affectedFile))
				return;
			if (delta.getKind() == IResourceDelta.REMOVED) {
				recorder.recordResourceDelete(filePath);
				return;
			}
			if (delta.getKind() == IResourceDelta.ADDED) {
				recorder.recordResourceAdd(filePath, getFileContents(affectedFile));
				return;
			}
			
			if (isSavedAction() || isRefactoringInProgress())
				recordFileSave(delta, filePath);
			else
				recordFileRefresh(affectedFile);
			return;
		}
		
		IResourceDelta[] affectedChildren = delta.getAffectedChildren();
		for (IResourceDelta child : affectedChildren) {
			recordChangedDelta(child);
		}
	}

	private void recordFileSave(IResourceDelta delta, String filePath) {
		long modificationStamp = delta.getResource().getModificationStamp();
		recorder.recordFileSave(filePath, modificationStamp);
		lastSavedVersion.put(filePath, modificationStamp);
	}

	public boolean isProjectIgnored(IResource affectedResource) {
		IProject project = affectedResource.getProject();
		if (project == null)
			return true;
		return COPEPlugin.getDefault().getIgnoreProjectsList().contains(project.getName());
	}

	private void recordFileRefresh(IFile affectedFile) {
		String filePath = affectedFile.getFullPath().toPortableString();
		long modificationStamp = affectedFile.getModificationStamp();
		
		Long lastVersion = lastSavedVersion.get(filePath);
		if (lastVersion != null && modificationStamp == lastVersion)
			return;
		
		String contents = getFileContents(affectedFile);
		recorder.recordRefresh(contents, filePath, modificationStamp);
	}

	protected String getFileContents(IFile affectedFile) {
		String fileExtension = affectedFile.getFileExtension();
		InputStream inputStream;
		try {
			inputStream = affectedFile.getContents();
			return FileUtil.encodeStream(fileExtension, inputStream);
		} catch (CoreException | IOException e) {
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

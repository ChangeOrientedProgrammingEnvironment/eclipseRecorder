package edu.oregonstate.cope.eclipse.listeners;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.eclipse.COPEPlugin;

public class ResourceListener implements IResourceChangeListener {
	
	ClientRecorder recorder = COPEPlugin.getDefault().getClientRecorder();
	
	private Map<String, Long> lastSavedVersion;
	
	public void ResourceListener() {
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
				recorder.recordResourceAdd(filePath, getFileContentents(affectedFile));
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
		
		if (lastSavedVersion.get(filePath) == modificationStamp)
			return;
		
		String contents = getFileContentents(affectedFile);
		recorder.recordRefresh(contents, filePath, modificationStamp);
	}

	private String getFileContentents(IFile affectedFile) {
		InputStream inputStream;
		try {
			inputStream = affectedFile.getContents();
			Scanner scanner = new Scanner(inputStream, affectedFile.getCharset());
			String contents = "";
			try {
				contents = scanner.useDelimiter("\\A").next(); 
			} catch (NoSuchElementException e) {
				contents = "";
			}
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

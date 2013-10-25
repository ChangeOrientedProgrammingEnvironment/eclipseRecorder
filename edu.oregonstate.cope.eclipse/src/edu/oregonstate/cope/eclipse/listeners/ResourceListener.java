package edu.oregonstate.cope.eclipse.listeners;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.eclipse.COPEPlugin;

public class ResourceListener implements IResourceChangeListener {
	
	ClientRecorder recorder = COPEPlugin.getDefault().getClientRecorderInstance();

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (isSavedAction() || isRefactoringInProgress())
			System.out.println("This is a save action, so I will ignore it");
		else
			recordRefresh(event.getDelta());
	}

	private void recordRefresh(IResourceDelta delta) {
		IResource affectedResource = delta.getResource();
		int type = affectedResource.getType();
		if (type == IResource.FILE) {
			// do record change
		}
		IResourceDelta[] children = delta.getAffectedChildren();
	}

	private boolean isSavedAction() {
		long currentTime = System.currentTimeMillis();
		return currentTime - SaveCommandExecutionListener.getLastSaveAction() < 500;
	}
	
	private boolean isRefactoringInProgress() {
		return RefactoringExecutionListener.isRefactoringInProgress();
	}
}

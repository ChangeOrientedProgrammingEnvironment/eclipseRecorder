package edu.oregonstate.cope.eclipse.listeners;

import java.util.List;

import org.eclipse.core.filebuffers.IFileBuffer;
import org.eclipse.core.filebuffers.IFileBufferListener;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.internal.UIPlugin;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.eclipse.COPEPlugin;
import edu.oregonstate.cope.eclipse.SnapshotManager;

public class FileBufferListener implements IFileBufferListener {

	private ClientRecorder clientRecorderInstance;
	private SnapshotManager snapshotManager;
	
	public FileBufferListener() {
		clientRecorderInstance = COPEPlugin.getDefault().getClientRecorder();
		snapshotManager = COPEPlugin.getDefault().getSnapshotManager();
	}

	@Override
	public void bufferCreated(IFileBuffer buffer) {
		if (!(buffer instanceof ITextFileBuffer))
			return;
		
		if (isInIgnoreProjects(buffer)) {
			return;
		}
		
		ITextFileBuffer textFileBuffer = (ITextFileBuffer) buffer;
		IPath fileLocation = textFileBuffer.getLocation();
		
		clientRecorderInstance.recordFileOpen(fileLocation.toPortableString());
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getFile(fileLocation).getProject();
		if (COPEPlugin.getDefault().getIgnoreProjectsList().contains(project.getName()))
			return;
		textFileBuffer.getDocument().addDocumentListener(new DocumentListener());
		if (!snapshotManager.isProjectKnown(project))
			snapshotManager.takeSnapshot(project);
	}

	private boolean isInIgnoreProjects(IFileBuffer buffer) {
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = workspaceRoot.getFile(buffer.getLocation()).getProject();
		
		if (project != null) {
			List<String> ignoreProjectsList = COPEPlugin.getDefault().getIgnoreProjectsList();
			return ignoreProjectsList.contains(project.getName());
		}
		
		return false;
	}

	@Override
	public void bufferDisposed(IFileBuffer buffer) {
		if (isInIgnoreProjects(buffer)) {
			return;
		}
		
		IPath fileLocation = buffer.getLocation();
		clientRecorderInstance.recordFileClose(fileLocation.toPortableString());
	}

	@Override
	public void bufferContentAboutToBeReplaced(IFileBuffer buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bufferContentReplaced(IFileBuffer buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateChanging(IFileBuffer buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dirtyStateChanged(IFileBuffer buffer, boolean isDirty) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateValidationChanged(IFileBuffer buffer,
			boolean isStateValidated) {
		// TODO Auto-generated method stub

	}

	@Override
	public void underlyingFileMoved(IFileBuffer buffer, IPath path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void underlyingFileDeleted(IFileBuffer buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateChangeFailed(IFileBuffer buffer) {
		// TODO Auto-generated method stub

	}

}

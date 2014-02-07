package edu.oregonstate.cope.eclipse.listeners;

import org.eclipse.core.filebuffers.IFileBuffer;
import org.eclipse.core.filebuffers.IFileBufferListener;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

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

	@Override
	public void bufferDisposed(IFileBuffer buffer) {
		IPath fileLocation = buffer.getLocation();
		if (fileLocation.toFile().exists())
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

package edu.oregonstate.cope.eclipse.listeners;

import org.eclipse.core.filebuffers.IFileBuffer;
import org.eclipse.core.filebuffers.IFileBufferListener;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.runtime.IPath;

public class FileBufferListener implements IFileBufferListener {

	@Override
	public void bufferCreated(IFileBuffer buffer) {
		System.out.println("File opened: " + buffer.getLocation().toPortableString());
		if (!(buffer instanceof ITextFileBuffer))
			return;
		
		ITextFileBuffer textFileBuffer = (ITextFileBuffer) buffer;
		textFileBuffer.getDocument().addDocumentListener(new DocumentListener());
	}

	@Override
	public void bufferDisposed(IFileBuffer buffer) {
		System.out.println("File closed: " + buffer.getLocation().toPortableString());

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

package edu.oregonstate.cope.eclipse.listeners;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.eclipse.COPEPlugin;

public class DocumentListener implements IDocumentListener {

	private ClientRecorder clientRecorderInstance;

	public DocumentListener() {
		clientRecorderInstance = COPEPlugin.getDefault()
				.getClientRecorderInstance();
	}

	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void documentChanged(DocumentEvent event) {
		ITextFileBufferManager textFileBufferManager = FileBuffers
				.getTextFileBufferManager();
		ITextFileBuffer textFileBuffer = textFileBufferManager
				.getTextFileBuffer(event.fDocument);
		IPath fileLocation = textFileBuffer.getLocation();
		String changeType = ClientRecorder.USER_CHANGE;
		if (RefactoringExecutionListener.isRefactoringInProgress())
			changeType = ClientRecorder.REFACTORING_CHANGE;

		clientRecorderInstance.recordTextChange(event.fText, event.fOffset,
				event.fLength, fileLocation.toPortableString(), changeType);

	}
}

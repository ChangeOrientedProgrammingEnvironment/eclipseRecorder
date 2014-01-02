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
				.getClientRecorder();
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
		String changeType = ClientRecorder.CHANGE_ORIGIN_USER;
		
		if (RefactoringExecutionListener.isRefactoringInProgress())
			changeType = ClientRecorder.CHANGE_ORIGIN_REFACTORING;
		else if (CommandExecutionListener.isCutInProgress())
			changeType = ClientRecorder.CHANGE_ORIGIN_CUT;
		else if (CommandExecutionListener.isPasteInProgress())
			changeType = ClientRecorder.CHANGE_ORIGIN_PASTE;
		else if (CommandExecutionListener.isUndoInProgress())
			changeType = ClientRecorder.CHANGE_ORIGIN_UNDO;
		else if (CommandExecutionListener.isRedoInProgress())
			changeType = ClientRecorder.CHANGE_ORIGIN_REDO;

		String filePath = fileLocation.toPortableString();
		clientRecorderInstance.recordTextChange(event.fText, event.fOffset,
				event.fLength, filePath, changeType);

	}
}

package edu.oregonstate.cope.eclipse.listeners;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;

public class DocumentListener implements IDocumentListener {

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
		System.out.println("Recorded a change in "
				+ fileLocation.toPortableString() + " at offset "
				+ event.getOffset() + " of length " + event.getLength()
				+ " with the text: " + event.fText);
	}

}

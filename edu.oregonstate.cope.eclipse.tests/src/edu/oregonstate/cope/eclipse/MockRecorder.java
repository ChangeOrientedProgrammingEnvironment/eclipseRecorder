package edu.oregonstate.cope.eclipse;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.clientRecorder.Events;

public class MockRecorder extends ClientRecorder {
	
	public Events recordedEvent;
	public String recordedText = "bob";
	public int recordedOffset = 1000000;
	public int recordedLength = 10000000;
	public String recordedSourceFile = "nothing";
	public String recordedChangeOrigin = "somebody else";

	@Override
	public void recordTextChange(String text, int offset, int length, String sourceFile, String changeOrigin) {
		recordedEvent = Events.textChange;
		recordedText = text;
		recordedOffset = offset;
		recordedLength = length;
		recordedSourceFile = sourceFile;
		recordedChangeOrigin = changeOrigin;
	}

	@Override
	public void recordCopy(String entityAddress, int offset, int lenght, String copiedText) {
		recordedEvent = Events.copy;
		recordedSourceFile = entityAddress;
		recordedOffset = offset;
		recordedLength = lenght;
		recordedText = copiedText;
	}
}

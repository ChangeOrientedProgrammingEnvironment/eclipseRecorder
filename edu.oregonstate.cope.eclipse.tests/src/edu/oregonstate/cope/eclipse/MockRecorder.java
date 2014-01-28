package edu.oregonstate.cope.eclipse;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;

public class MockRecorder extends ClientRecorder {
	
	public String recordedText = "bob";
	public int recordedOffset = 1000000;
	public int recordedLength = 10000000;
	public String recordedSourceFile = "nothing";
	public String recordedChangeOrigin = "somebody else";

	@Override
	public void recordTextChange(String text, int offset, int length, String sourceFile, String changeOrigin) {
		recordedText = text;
		recordedOffset = offset;
		recordedLength = length;
		recordedSourceFile = sourceFile;
		recordedChangeOrigin = changeOrigin;
	}
}

package edu.oregonstate.cope.tests.util;

import java.io.IOException;

import edu.oregonstate.cope.clientRecorder.fileOps.FileProvider;

public class StubFileProvider extends FileProvider {
	private String stringWriter = "";

	@Override
	public void appendToCurrentFile(String string) {
		stringWriter = stringWriter.concat(string);
	}

	@Override
	public boolean isCurrentFileEmpty() {
		return stringWriter.isEmpty();
	}

	@Override
	public void deleteFiles() throws IOException {
	}

	@Override
	protected String getFileName() {
		return null;
	}

	public String testGetContent() {
		return stringWriter;
	}
}
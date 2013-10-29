package edu.oregonstate.cope.tests.util;

import java.io.IOException;

import edu.oregonstate.cope.clientRecorder.fileOps.FileProvider;

public class StubFileProvider extends FileProvider {
	private String fileStub = "";

	@Override
	public void appendToCurrentFile(String string) {
		fileStub = fileStub.concat(string);
	}
	
	@Override
	public void writeToCurrentFile(String string) {
		fileStub = string;
	}

	@Override
	public boolean isCurrentFileEmpty() {
		return fileStub.isEmpty();
	}

	@Override
	public void deleteFiles() throws IOException {
	}

	@Override
	protected String getFileName() {
		return null;
	}

	public String testGetContent() {
		return fileStub;
	}
}
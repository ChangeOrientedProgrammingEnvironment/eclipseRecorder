package edu.oregonstate.cope.tests.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	public List<String> readAllLines() {
		if (isCurrentFileEmpty())
			return new ArrayList<String>();

		return Arrays.asList(fileStub.split(System.lineSeparator()));
	}

	@Override
	public boolean isCurrentFileEmpty() {
		return fileStub.isEmpty();
	}

	@Override
	public void deleteFiles() throws IOException {
		fileStub = "";
	}

	@Override
	protected String getFileName() {
		return null;
	}

	public String testGetContent() {
		return fileStub;
	}
}
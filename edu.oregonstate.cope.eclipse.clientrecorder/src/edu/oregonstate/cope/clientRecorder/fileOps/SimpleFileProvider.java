package edu.oregonstate.cope.clientRecorder.fileOps;

import java.io.IOException;
import java.nio.file.Files;

public class SimpleFileProvider extends FileProvider {

	private String fileName;

	public SimpleFileProvider(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void deleteFiles() throws IOException {
		if (Files.exists(getCurrentFilePath()))
			Files.delete(getCurrentFilePath());
	}

	@Override
	protected String getFileName() {
		return fileName;
	}

}

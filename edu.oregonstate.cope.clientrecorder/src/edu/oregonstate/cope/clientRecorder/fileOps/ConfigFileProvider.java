package edu.oregonstate.cope.clientRecorder.fileOps;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Represents the configuration file.
 * It is called "config".
 */
public class ConfigFileProvider extends FileProvider {

	private static final String fileName = "config";

	@Override
	public void deleteFiles() throws IOException {
		Files.delete(getCurrentFilePath());
	}

	protected String getFileName() {
		return fileName;
	}
}

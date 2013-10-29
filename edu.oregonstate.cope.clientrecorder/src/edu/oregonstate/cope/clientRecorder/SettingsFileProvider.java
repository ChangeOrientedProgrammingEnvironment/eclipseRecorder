package edu.oregonstate.cope.clientRecorder;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Represents the configuration file. 
 *
 */
public class SettingsFileProvider extends FileProvider{

	private static final String fileName = "config";

	@Override
	public void deleteFiles() throws IOException {
		Files.delete(getCurrentFilePath());
	}

	protected String getFileName() {
		return fileName;
	}
}

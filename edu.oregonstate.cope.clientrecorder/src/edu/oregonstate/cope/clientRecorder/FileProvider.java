package edu.oregonstate.cope.clientRecorder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * The file provider encapsulates file persistence rules. Each subclass provides
 * different rules. <br>
 * <br>
 * This entity behaves as a cursor: at each moment it points to a particular
 * file. When it switches to a new file, or how it does that is transparent to
 * its clients.
 */
public abstract class FileProvider {

	protected Path rootDirectory;

	/**
	 * Deletes all managed file or files. If this provider creates a directory
	 * hierarchy, the directory hierarchy is not deleted.
	 * 
	 * @throws IOException
	 */
	public abstract void deleteFiles() throws IOException;

	/**
	 * Sets the parent directory under which this provider will create its
	 * structure
	 * 
	 * @param rootDirectory
	 */
	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = Paths.get(rootDirectory).toAbsolutePath();

		try {
			Files.createDirectories(this.rootDirectory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.err.println("set the root to " + this.rootDirectory.toString());
	}

	public void appendToCurrentFile(String string) {
		try {
			Files.write(getCurrentFilePath(), string.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isCurrentFileEmpty() {
		try {
			return getCurrentFilePath().toFile().length() == 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	protected abstract String getFileName();

	protected Path getCurrentFilePath() throws IOException {
		return rootDirectory.resolve(getFileName());
	}
}
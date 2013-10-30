package edu.oregonstate.cope.clientRecorder.fileOps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * The file provider encapsulates file persistence rules. Each subclass provides
 * different rules. Each class could also represent an important file to the
 * system. <br>
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
		setLongerRootDirectory(rootDirectory);
	}

	/**
	 * Sets the parent directory under which this provider will create its
	 * structure
	 * 
	 * @param first
	 *            First directory name in path
	 * @param more
	 *            Other directory names for the rest of the path
	 */
	protected void setLongerRootDirectory(String first, String... more) {
		this.rootDirectory = Paths.get(first, more).toAbsolutePath();

		try {
			Files.createDirectories(this.rootDirectory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.err.println(getClass().getSimpleName() + " set the root to " + this.rootDirectory.toString());
	}

	public void appendToCurrentFile(String string) {
		doFileWriteOperation(string, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
	}

	public void writeToCurrentFile(String string) {
		doFileWriteOperation(string, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
	}

	private void doFileWriteOperation(String string, StandardOpenOption... options) {
		try {
			Files.write(getCurrentFilePath(), string.getBytes(), options);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> readAllLines() {
		if (isCurrentFileEmpty())
			return new ArrayList<String>();

		try {
			return Files.readAllLines(getCurrentFilePath(), null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
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
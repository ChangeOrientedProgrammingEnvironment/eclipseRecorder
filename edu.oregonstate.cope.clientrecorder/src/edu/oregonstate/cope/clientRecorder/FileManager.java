package edu.oregonstate.cope.clientRecorder;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Calendar;

/**
 * Class responsible with file persistence policy: <br>
 * <ul>
 * <li>Writing to files</li>
 * <li>Where to store files.</li>
 * <li>When to switch to a new file</li>
 * <li>File naming policy</li>
 * </ul>
 * 
 * Root location must be set.
 */
public class FileManager {

	private Path rootDirectory;
	
	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = Paths.get(rootDirectory).toAbsolutePath();
		
		try {
			Files.createDirectories(this.rootDirectory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.err.println("set the event root to " + this.rootDirectory.toString());
	}

	public void write(String string) {
		try {
			Files.write(getFilePath(), string.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isCurrentFileEmpty() {
		try {
			return getFilePath().toFile().length() == 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void deleteFiles() throws IOException {
		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(rootDirectory);

		for (Path path : directoryStream) {
			Files.delete(path);
		}
	}

	protected Path getFilePath() throws IOException {
		return rootDirectory.resolve(getFileName());
	}

	protected String getFileName() {
		Calendar cal = Calendar.getInstance();
		String pathName = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);

		return pathName;
	}
}

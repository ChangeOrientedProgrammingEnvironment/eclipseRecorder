package edu.oregonstate.cope.clientRecorder.fileOps;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;

/**
 * Class that represents event files. <br>
 * It makes a new file each day.<br>
 * The file name format is: YYYY-MM-DD<br>
 * <br>
 * All event files are stored in rootDirectory/eventFiles/
 */
public class EventFilesProvider extends FileProvider {

	public static final String EVENTFILE_ROOTDIR = "eventFiles";

	/**
	 * Deletes all the event files. Does not delete the parent directory.
	 */
	@Override
	public void deleteFiles() throws IOException {
		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(rootDirectory);

		for (Path path : directoryStream) {
			Files.delete(path);
		}
	}

	@Override
	protected String getFileName() {
		Calendar cal = Calendar.getInstance();
		String pathName = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);

		return pathName;
	}

	@Override
	public void setRootDirectory(String rootDirectory) {
		super.setLongerRootDirectory(rootDirectory, EVENTFILE_ROOTDIR);
	}
}

package edu.oregonstate.cope.clientRecorder.fileOps;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
public class EventFilesProvider extends FileProvider {

	/**
	 *	Deletes all the event files.
	 *	Does not delete the parent directory. 
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
}

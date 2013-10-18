package edu.oregonstate.cope.clientRecorder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

public class FileManager {

	public Path getFilePath() throws IOException {
		String pathName = getFileName();
		Path path = Paths.get(pathName);
		
		if (!Files.exists(path)) {
			Files.createFile(path);
		}
		
		return path;
	}

	protected String getFileName() {
		Calendar cal = Calendar.getInstance();
		String pathName = cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH);
		
		return pathName;
	}
	
}

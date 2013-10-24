package edu.oregonstate.cope.clientRecorder;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

public class FileManager {

	private String rootDirectory = "outputFiles";

	//TODO do not change state and also return
	public Path getFilePath() throws IOException {
		Path filePath = Paths.get(rootDirectory, getFileName());
		
		if (!Files.exists(filePath)) {
			Files.createFile(filePath);
		}

		return filePath;
	}
	
	public void setRootDirectory(String rootDirectory){
		this.rootDirectory = rootDirectory;
	}

	public void deleteEventFiles() throws IOException{
		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(rootDirectory));
		
		for (Path path : directoryStream) {
			Files.delete(path);
		}
	}

	protected String getFileName() {
		Calendar cal = Calendar.getInstance();
		String pathName = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH);
		
		return pathName;
	}

	public void write(String string) {
		// TODO Auto-generated method stub
		
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

	public void commitChanges() {
		// TODO Auto-generated method stub
		
	}
}

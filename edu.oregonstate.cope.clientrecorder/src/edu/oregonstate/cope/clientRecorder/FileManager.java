package edu.oregonstate.cope.clientRecorder;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

public class FileManager {

	private String rootDirectory = "outputFiles";

	public void setRootDirectory(String rootDirectory){
		this.rootDirectory = rootDirectory;
	}

	public void write(String string) {
		try {
			Files.write(getFilePath(), string.getBytes(), StandardOpenOption.APPEND);
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

	public void deleteEventFiles() throws IOException{
		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(rootDirectory));
		
		for (Path path : directoryStream) {
			Files.delete(path);
		}
	}

	public void commitChanges() {
		// TODO Auto-generated method stub
		
	}

	//TODO modify this method so it does not return AND also change the state
	protected Path getFilePath() throws IOException {
		Path filePath = Paths.get(rootDirectory, getFileName());
		
		if (!Files.exists(filePath)) {
			Files.createDirectories(filePath.getParent());
			Files.createFile(filePath);
		}
	
		return filePath;
	}

	protected String getFileName() {
		Calendar cal = Calendar.getInstance();
		String pathName = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH);
		
		return pathName;
	}
}

package edu.oregonstate.cope.eclipse.installer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import edu.oregonstate.cope.clientRecorder.RecorderFacadeInterface;

public abstract class InstallerOperation {

	protected Path workspaceDirectory;
	protected Path permanentDirectory;
	protected RecorderFacadeInterface recorder;
	
	public InstallerOperation() {
	}

	public InstallerOperation(RecorderFacadeInterface recorderFacade, Path permanentDirectory, Path workspaceDirectory) {
		init(recorderFacade, permanentDirectory, workspaceDirectory);
	}
	
	public void init(RecorderFacadeInterface recorderFacade, Path permanentDirectory, Path workspaceDirectory){
		this.recorder = recorderFacade;
		
		this.permanentDirectory = permanentDirectory;
		this.workspaceDirectory = workspaceDirectory;
	}

	public void perform() throws IOException {
		String fileName = getFileName();
		File workspaceFile = workspaceDirectory.resolve(fileName).toFile();
		File permanentFile = permanentDirectory.resolve(fileName).toFile();

		if (workspaceFile.exists() && permanentFile.exists()) {
			// System.out.println(this.getClass() + " both files exist");
			doBothFilesExists();
		}

		else if (!workspaceFile.exists() && permanentFile.exists()) {
			// System.out.println(this.getClass() + " only permanent");
			doOnlyPermanentFileExists(workspaceFile, permanentFile);
		}

		else if (workspaceFile.exists() && !permanentFile.exists()) {
			// System.out.println(this.getClass() + " only workspace");
			doOnlyWorkspaceFileExists(workspaceFile, permanentFile);
		}

		else if (!workspaceFile.exists() && !permanentFile.exists()) {
			// System.out.println(this.getClass() + " neither files exist");
			doNoFileExists(workspaceFile, permanentFile);
		}
	}

	protected void doOnlyWorkspaceFileExists(File workspaceFile, File permanentFile) throws IOException {
		Files.copy(workspaceFile.toPath(), permanentFile.toPath());
	}

	protected void doOnlyPermanentFileExists(File workspaceFile, File permanentFile) throws IOException {
		Files.copy(permanentFile.toPath(), workspaceFile.toPath());
	}

	protected void doBothFilesExists() {
	}

	protected abstract void doNoFileExists(File workspaceFile, File permanentFile) throws IOException;
	
	protected abstract String getFileName();

	protected void writeContentsToFile(Path filePath, String fileContents) throws IOException {
		Files.write(filePath, fileContents.getBytes(), StandardOpenOption.CREATE);
	}
}
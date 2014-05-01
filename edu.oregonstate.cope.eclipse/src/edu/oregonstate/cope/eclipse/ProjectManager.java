package edu.oregonstate.cope.eclipse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;

import edu.oregonstate.cope.clientRecorder.util.LoggerInterface;

public class ProjectManager {
	public String knownProjectsFileName = "known-projects";
	public List<String> knownProjects;
	public List<String> sessionTouchedProjects;
	private LoggerInterface logger;
	private String parentDirectory;

	public ProjectManager(String parentDirectory, LoggerInterface logger) {
		this.parentDirectory = parentDirectory;
		this.logger = logger;
		File knownProjectsFile = new File(parentDirectory, knownProjectsFileName);
		try {
			knownProjectsFile.createNewFile();
			knownProjects = Files.readAllLines(knownProjectsFile.toPath(), Charset.defaultCharset());
		} catch (IOException e) {
			logger.error(this, e.getMessage(), e);
		}
		
		sessionTouchedProjects = new ArrayList<String>();
	}

	public boolean isProjectKnown(SnapshotManager snapshotManager, String name) {
		makeProjectAsTouched(snapshotManager, name);
		return knownProjects.contains(name);
	}

	void makeProjectAsTouched(SnapshotManager snapshotManager, String projectName) {
		if (!sessionTouchedProjects.contains(projectName))
			sessionTouchedProjects.add(projectName);
	}
	
	public void knowProject(String string) {
		knownProjects.add(string);
		try {
			Files.write(Paths.get(parentDirectory, knownProjectsFileName), (string + "\n").getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			COPEPlugin.getDefault().getLogger().error(this, e.getMessage(), e);
		}
	}
}
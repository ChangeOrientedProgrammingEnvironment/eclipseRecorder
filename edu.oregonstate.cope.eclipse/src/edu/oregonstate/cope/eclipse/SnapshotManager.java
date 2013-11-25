package edu.oregonstate.cope.eclipse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class SnapshotManager {
	
	private String knownProjectsFileName = "known-projects";
	private List<String> knownProjects;

	public SnapshotManager() {
		File knownProjectsFile = new File(COPEPlugin.getLocalStorage(), knownProjectsFileName);
		try {
			knownProjectsFile.createNewFile();
			knownProjects = Files.readAllLines(knownProjectsFile.toPath(), Charset.defaultCharset());
		} catch (IOException e) {
		}
	}
	
	public boolean isProjectKnown(String name) {
		return knownProjects.contains(name);
	}
}

package edu.oregonstate.cope.eclipse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.ui.internal.wizards.datatransfer.ArchiveFileExportOperation;

public class SnapshotManager {

	private String knownProjectsFileName = "known-projects";
	private List<String> knownProjects;
	private List<String> sessionTouchedProjects;
	private String parentDirectory;

	protected SnapshotManager(String parentDirectory) {
		this.parentDirectory = parentDirectory;
		File knownProjectsFile = new File(parentDirectory, knownProjectsFileName);
		try {
			knownProjectsFile.createNewFile();
			knownProjects = Files.readAllLines(knownProjectsFile.toPath(), Charset.defaultCharset());
		} catch (IOException e) {
			COPEPlugin.getDefault().getLogger().error(this, e.getMessage(), e);
		}
		
		sessionTouchedProjects = new ArrayList<String>();
	}

	public boolean isProjectKnown(String name) {
		makeProjectAsTouched(name);
		return knownProjects.contains(name);
	}
	
	public boolean isProjectKnown(IProject project) {
		if (project == null)
			return true;
		return isProjectKnown(project.getName());
	}
	
	private void makeProjectAsTouched(String projectName) {
		if (!sessionTouchedProjects.contains(projectName))
			sessionTouchedProjects.add(projectName);
	}

	protected void knowProject(String string) {
		knownProjects.add(string);
		try {
			Files.write(Paths.get(parentDirectory, knownProjectsFileName), (string + "\n").getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			COPEPlugin.getDefault().getLogger().error(this, e.getMessage(), e);
		}
	}
	
	private void knowProject(IProject project) {
		knowProject(project.getName());
	}

	@SuppressWarnings("restriction")
	public String takeSnapshot(IProject project) {
		if (!isProjectKnown(project))
			knowProject(project);
		String zipFile = parentDirectory + File.separator + project.getName() + "-" + System.currentTimeMillis() + ".zip";
		archiveProjectToFile(project, zipFile);
		COPEPlugin.getDefault().getClientRecorder().recordSnapshot(zipFile);
		if (JavaProject.hasJavaNature(project)) {
			IJavaProject javaProject = addExternalLibrariesToZipFile(project, zipFile);
			snapshotRequiredProjects(javaProject);
		}
		return zipFile;
	}

	private IJavaProject addExternalLibrariesToZipFile(IProject project, String zipFile) {
		IJavaProject javaProject = JavaCore.create(project);
		List<String> nonWorkspaceLibraries = getNonWorkspaceLibraries(javaProject);
		addLibsToZipFile(nonWorkspaceLibraries, zipFile);
		return javaProject;
	}

	@SuppressWarnings("restriction")
	private void archiveProjectToFile(IProject project, String zipFile) {
		ArchiveFileExportOperation archiveFileExportOperation = new ArchiveFileExportOperation(project, zipFile);
		archiveFileExportOperation.setUseCompression(true);
		archiveFileExportOperation.setUseTarFormat(false);
		archiveFileExportOperation.setCreateLeadupStructure(true);
		try {
			archiveFileExportOperation.run(new NullProgressMonitor());
		} catch (InvocationTargetException | InterruptedException e) {
			COPEPlugin.getDefault().getLogger().error(this, e.getMessage(), e);
		}
	}

	private void snapshotRequiredProjects(IJavaProject javaProject) {
		try {
			String[] requiredProjectNames = javaProject.getRequiredProjectNames();
			for (String requiredProjectName : requiredProjectNames) {
				if(!isProjectKnown(requiredProjectName))
					takeSnapshot(requiredProjectName);
			}
		} catch (JavaModelException e) {
		}
	}

	private String takeSnapshot(String projectName) {
		IProject requiredProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		return takeSnapshot(requiredProject);
	}

	public List<String> getNonWorkspaceLibraries(IJavaProject project) {
		IClasspathEntry[] resolvedClasspath = null;
		try {
			resolvedClasspath = project.getRawClasspath();
		} catch (JavaModelException e) {
			COPEPlugin.getDefault().getLogger().error(this, e.getMessage(), e);
			return new ArrayList<String>();
		}
		List<String> pathsOfLibraries = new ArrayList<String>();
		for (IClasspathEntry iClasspathEntry : resolvedClasspath) {
			if (iClasspathEntry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
				pathsOfLibraries.add(iClasspathEntry.getPath().toPortableString());
			}
		}
		return pathsOfLibraries;
	}
	
	public void addLibsToZipFile(List<String> pathOfLibraries, String zipFilePath) {
		try {
			String libFolder = "libs" + File.separator;
			ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath+"-libs", true));
			copyExistingEntries(zipFilePath, zipOutputStream);
			for (String library : pathOfLibraries) {
				Path path = Paths.get(library);
				if(!Files.exists(path)) //if the project is in the workspace
					continue;
				ZipEntry libraryZipEntry = new ZipEntry(libFolder + path.getFileName());
				zipOutputStream.putNextEntry(libraryZipEntry);
				byte[] libraryContents = Files.readAllBytes(path);
				zipOutputStream.write(libraryContents);
			}
			zipOutputStream.close();
			new File(zipFilePath).delete();
			new File(zipFilePath+"-libs").renameTo(new File(zipFilePath));
		} catch (IOException e) {
			COPEPlugin.getDefault().getLogger().error(this, e.getMessage(), e);
		} finally {
		}
	}

	private void copyExistingEntries(String zipFilePath, ZipOutputStream zipOutputStream) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath));
			while(zipInputStream.available() == 1) {
				ZipEntry entry = zipInputStream.getNextEntry();
				if (entry == null)
					continue;
				zipOutputStream.putNextEntry(new ZipEntry(entry.getName()));
				long entrySize = entry.getSize();
				if (entrySize < 0)
					continue;
				byte[] contents = new byte[(int) entrySize];
				int count = 0;
				do {
					count = zipInputStream.read(contents, count, (int) entrySize);
				} while (count < entrySize);
				zipOutputStream.write(contents);
			}
			zipInputStream.close();
		} catch (IOException e) {
			COPEPlugin.getDefault().getLogger().error(this, e.getMessage(), e);
		}
	}
	
	protected void takeSnapshotOfKnownProjects() {
		for (String project : sessionTouchedProjects) {
			takeSnapshot(project);
		}
	}
}

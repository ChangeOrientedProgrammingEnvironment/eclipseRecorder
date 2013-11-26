package edu.oregonstate.cope.eclipse;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class OutsideLibrariesTest {

	@Rule
	public TestName name = new TestName();
	private static IJavaProject javaProject;
	private static SnapshotManager snapshotManager = new SnapshotManager(COPEPlugin.getLocalStorage().getAbsolutePath());

	@BeforeClass
	public static void setUp() throws Exception {
		ImportOperation importOperation = new ImportOperation(new Path("librariesTest"), 
				new File(Paths.get("projects" + File.separator + "librariesTest").toAbsolutePath().toString()), 
				FileSystemStructureProvider.INSTANCE, 
				new IOverwriteQuery() {

			@Override
			public String queryOverwrite(String pathString) {
				return ALL;
			}
		});
		importOperation.setCreateContainerStructure(false);
		importOperation.run(new NullProgressMonitor());
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		if (projects.length > 1)
			fail("More than 1 project in the workspace");

		IProject project = projects[0];
		project.open(new NullProgressMonitor());
		String[] natureIds = project.getDescription().getNatureIds();
		if (JavaProject.hasJavaNature(project)) {
			javaProject = JavaCore.create(project);
		} else {
			fail("Project does not have java nature");
		}
	}

	@Test
	public void testGetNonWorkspaceLibrary() throws Exception {
		List<String> nonWorkspaceLibraries = snapshotManager.getNonWorkspaceLibraries(javaProject);
		assertEquals(1, nonWorkspaceLibraries.size());
		assertEquals("/Users/caius/osu/COPE/clientRecorder/edu.oregonstate.cope.eclipse.tests/projects/json-simple-1.1.1.jar",nonWorkspaceLibraries.get(0));
	}
	
	@Test
	public void testAddLibraryToZip() throws Exception {
		String zipFilePath = snapshotManager.takeSnapshot(javaProject.getProject());
		assertNotNull(zipFilePath);
		
		ArrayList<String> initialEntries = getEntriesInZipFile(zipFilePath);
		System.out.println(initialEntries);
		
		List<String> libraries = snapshotManager.getNonWorkspaceLibraries(javaProject);
		snapshotManager.addLibsToZipFile(libraries, zipFilePath);
		
		ArrayList<String> entriesNames = getEntriesInZipFile(zipFilePath);
		System.out.println(entriesNames);
		assertTrue(entriesNames.containsAll(initialEntries));
		assertTrue(entriesNames.contains("libs/json-simple-1.1.1.jar"));
	}

	private ArrayList<String> getEntriesInZipFile(String zipFilePath) throws IOException {
		ZipFile zipFile = new ZipFile(zipFilePath);
		ArrayList<String> entriesNames = getEntriesInZipFile(zipFile);
		zipFile.close();
		return entriesNames;
	}

	private ArrayList<String> getEntriesInZipFile(ZipFile zipFile) {
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		ArrayList<? extends ZipEntry> usefulEntries = Collections.list(entries);
		ArrayList<String> entriesNames = new ArrayList<>();
		for (ZipEntry zipEntry : usefulEntries) {
			entriesNames.add(zipEntry.getName());
		}
		return entriesNames;
	}

}

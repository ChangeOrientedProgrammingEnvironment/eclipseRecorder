package edu.oregonstate.cope.eclipse.installer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.oregonstate.cope.clientRecorder.Properties;
import edu.oregonstate.cope.eclipse.COPEPlugin;
import edu.oregonstate.cope.eclipse.FileUtil;
import edu.oregonstate.cope.eclipse.PopulatedWorkspaceTest;
import edu.oregonstate.cope.eclipse.SnapshotManager;

public class PluginUpdateTest extends PopulatedWorkspaceTest {

	private static COPEPlugin plugin;
	private static SnapshotManager snapshotManager;
	private static List<String> allowedUnversionedFiles;

	@Before
	public void before() throws Exception {
		plugin = COPEPlugin.getDefault();
		plugin.getSnapshotManager().getProjectManager().knowProject(PopulatedWorkspaceTest.javaProject.getProject().getName());

		allowedUnversionedFiles = new ArrayList<>();
		allowedUnversionedFiles.add("workspace_id");
		allowedUnversionedFiles.add("known-projects");
		allowedUnversionedFiles.add("log");
		allowedUnversionedFiles.add("config");
		allowedUnversionedFiles.add(COPEPlugin.getDefault()._getInstallationConfigFileName());
		allowedUnversionedFiles.add(Installer.SURVEY_FILENAME);
		allowedUnversionedFiles.add(Installer.EMAIL_FILENAME);
	}
	
	@After
	public void tearDown() throws Exception {
		File[] zipFiles = FileUtil.listZipFilesInDir(plugin.getLocalStorage());
		for (File zipFile : zipFiles) {
			zipFile.delete();
		}
	}

	@Test
	public void testVersionedLocalStorage() throws Exception {
		assertPathHasCurrentVersion(plugin.getVersionedLocalStorage().toPath());
		assertPathHasCurrentVersion(plugin.getVersionedBundleStorage().toPath());
	}

	private void assertPathHasCurrentVersion(Path versionedPath) {
		Path path = versionedPath;
		assertTrue(path.endsWith(plugin.getPluginVersion().toString()));
	}

	@Test
	public void testVersioningFilePlacement() throws Exception {
		for (File file : plugin.getLocalStorage().listFiles()) {
			if (file.isDirectory())
				checkDirectory(file);

			if (file.isFile())
				checkFile(file);
		}
	}

	private void checkFile(File file) {
		if (!file.getName().endsWith("zip") && !file.getName().contains(".fuse_hidden"))
			assertTrue(allowedUnversionedFiles.contains(file.getName()));
	}

	private void checkDirectory(File file) {
		assertTrue(file.getName().matches("\\d+\\.\\d+\\.\\d+\\.qualifier"));

		List<String> versionedFileChildren = Arrays.asList(file.list());

		assertEquals(1, versionedFileChildren.size());
		assertTrue(versionedFileChildren.contains("eventFiles"));
	}

	@SuppressWarnings("static-access")
	@Test
	public void testSnapshotAtUpdate() throws Exception {
		Properties properties = plugin.getWorkspaceProperties();

		new Installer().doUpdate("v1", "v2");

		boolean zipExists = false;
		
		Thread.sleep(1000);

		for (File file : plugin.getLocalStorage().listFiles()) {
			if (file.toPath().toString().endsWith(".zip"))
				zipExists = true;
		}

		assertTrue(zipExists);
	}
	
	@Test
	public void testSnapshotAtUpdateWithDependendantProjects() throws Exception {
		Properties properties = plugin.getWorkspaceProperties();
		
		IJavaProject depProject = FileUtil.createTestJavaProject("depedendentProject");
		FileUtil.addProjectDepedency(javaProject, depProject);

		new Installer().doUpdate("v1", "v2");
		
		Thread.sleep(2000);

		File[] zipFiles = FileUtil.listZipFilesInDir(plugin.getLocalStorage());
		assertEquals(2, zipFiles.length);
	}

}

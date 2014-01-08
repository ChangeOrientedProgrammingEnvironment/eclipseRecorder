package edu.oregonstate.cope.eclipse.installer;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.oregonstate.cope.clientRecorder.Properties;
import edu.oregonstate.cope.eclipse.COPEPlugin;
import edu.oregonstate.cope.eclipse.PopulatedWorkspaceTest;

public class InstallerTest extends PopulatedWorkspaceTest {

	private File[] rootFiles;
	private COPEPlugin plugin;

	@Before 
	public void setup() {
		plugin = COPEPlugin.getDefault();
		rootFiles = plugin.getLocalStorage().listFiles();
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
	public void testEverythingIsInVersionedFiles() throws Exception {
		for (File file : rootFiles) {
			assertTrue(file.isDirectory());
			assertTrue(file.getName().matches("\\d+\\.\\d+\\.\\d+\\.qualifier"));

			List<String> versionedFileChildren = Arrays.asList(file.list());

			assertTrue(versionedFileChildren.contains("eventFiles"));
			assertTrue(versionedFileChildren.contains("workspace_id"));
			assertTrue(versionedFileChildren.contains("known-projects"));
		}
	}
	
	@Test
	public void testSnapshotAtUpdate() throws Exception {
		Properties properties = plugin.getWorkspaceProperties();
		
		new Installer().checkForPluginUpdate("v1", "v2");
		
		boolean zipExists = false;

		for(File file : plugin.getLocalStorage().listFiles()){
			if(file.toPath().endsWith(".zip"))
				zipExists = true;
		}
		
		assertTrue(zipExists);
	}
}

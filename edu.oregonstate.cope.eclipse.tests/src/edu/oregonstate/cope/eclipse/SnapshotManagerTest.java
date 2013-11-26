package edu.oregonstate.cope.eclipse;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SnapshotManagerTest {
	
	private SnapshotManager snapshotManager;

	@Before
	public void setUp() throws Exception {
		File file = new File(COPEPlugin.getLocalStorage(), "known-projects");
		file.createNewFile();
		Files.write(file.toPath(), "known1\nknown2\n".getBytes(), StandardOpenOption.WRITE);
		snapshotManager = new SnapshotManager(COPEPlugin.getLocalStorage().getAbsolutePath());
	}
	
	@After
	public void tearDown() throws Exception {
		File[] zipFiles = listZipFilesInDir(COPEPlugin.getLocalStorage());
		for (File zipFile : zipFiles) {
			zipFile.delete();
		}
	}

	@Test
	public void testNotKnowProject() {
		assertFalse(snapshotManager.isProjectKnown("test"));
	}
	
	@Test
	public void testIsProjectKnown() {
		assertTrue(snapshotManager.isProjectKnown("known1"));
		assertTrue(snapshotManager.isProjectKnown("known2"));
	}
	
	@Test
	public void testKnowProject() throws Exception {
		snapshotManager.knowProject("known3");
		assertTrue(snapshotManager.isProjectKnown("known3"));
		assertEquals("known1\nknown2\nknown3\n",new String(Files.readAllBytes(Paths.get(COPEPlugin.getLocalStorage().getAbsolutePath(), "known-projects"))));
	}
	
	@Test
	public void testTouchProjectInSession() {
		snapshotManager.isProjectKnown("known1");
		snapshotManager.takeSnapshotOfKnownProjects();
		File fileDir = COPEPlugin.getLocalStorage();
		assertTrue(fileDir.isDirectory());
		File[] listFiles = listZipFilesInDir(fileDir);
		assertEquals(1,listFiles.length);
		assertTrue(listFiles[0].getName().matches("known1-[0-9]*\\.zip"));
	}

	private File[] listZipFilesInDir(File fileDir) {
		File[] listFiles = fileDir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".zip");
			}
		});
		return listFiles;
	}
	
}

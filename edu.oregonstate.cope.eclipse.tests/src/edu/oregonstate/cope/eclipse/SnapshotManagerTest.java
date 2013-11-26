package edu.oregonstate.cope.eclipse;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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
	
}

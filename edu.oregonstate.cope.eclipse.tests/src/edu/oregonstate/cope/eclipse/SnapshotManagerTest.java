package edu.oregonstate.cope.eclipse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
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
		snapshotManager = new SnapshotManager();
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
	
}

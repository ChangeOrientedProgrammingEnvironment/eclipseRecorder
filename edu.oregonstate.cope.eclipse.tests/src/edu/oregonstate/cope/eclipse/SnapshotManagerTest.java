package edu.oregonstate.cope.eclipse;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SnapshotManagerTest {
	
	private SnapshotManager snapshotManager;

	@Before
	public void setUp() {
		snapshotManager = new SnapshotManager();
	}

	@Test
	public void testNotKnowProject() {
		assertFalse(snapshotManager.isProjectKnown("test"));
	}
}

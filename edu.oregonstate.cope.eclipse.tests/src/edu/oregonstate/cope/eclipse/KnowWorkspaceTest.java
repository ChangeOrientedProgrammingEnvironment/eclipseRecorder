package edu.oregonstate.cope.eclipse;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class KnowWorkspaceTest {

	private StartPluginUIJob job;

	@Before
	public void setUp() {
		job = new StartPluginUIJob(COPEPlugin.getDefault(), "");
	}

	@Test
	public void testIKnowThisWorkspace() {
		assertTrue(job.isWorkspaceKnown());
	}

	@Test
	public void testIDontKnowThisWorkspace() {
		File workspaceIdFile = job.copePlugin.getWorkspaceIdFile();
		workspaceIdFile.delete();
		assertFalse(job.isWorkspaceKnown());
	}

	@Test
	public void testGetToKnowWorkspace() {
		testIDontKnowThisWorkspace();
		job.getToKnowWorkspace();
		assertTrue(job.isWorkspaceKnown());
	}
}

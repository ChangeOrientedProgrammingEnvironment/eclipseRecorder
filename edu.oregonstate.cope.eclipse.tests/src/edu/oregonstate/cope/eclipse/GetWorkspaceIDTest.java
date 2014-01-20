package edu.oregonstate.cope.eclipse;


import static org.junit.Assert.*;

import org.junit.Test;

public class GetWorkspaceIDTest {
	
	//@Test
	public void testGetWorkspaceID() {
		String workspaceID = COPEPlugin.getDefault().getWorkspaceID();
		assertNotNull(workspaceID);
		//assertNotEquals("", workspaceID);
		assertTrue("" == workspaceID);
		String secondWorkspaceID = COPEPlugin.getDefault().getWorkspaceID();
		assertEquals(workspaceID, secondWorkspaceID);
	}
}

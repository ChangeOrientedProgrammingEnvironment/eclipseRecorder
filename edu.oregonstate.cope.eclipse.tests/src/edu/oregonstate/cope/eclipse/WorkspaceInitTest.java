package edu.oregonstate.cope.eclipse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import edu.oregonstate.cope.clientRecorder.RecorderFacade;
import edu.oregonstate.cope.clientRecorder.RecorderFacadeInterface;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WorkspaceInitTest {

	@Test
	public void testIsWorkspaceFirstStarted() {
		assertTrue(COPEPlugin.getDefault().getRecorder().isFirstStart());
	}
	
	@Test
	public void testIsWorkspaceSecondStarted() {
		RecorderFacadeInterface recorderFacade = new RecorderFacade(COPEPlugin.getDefault(), "");
		assertFalse(recorderFacade.isFirstStart());
	}
}

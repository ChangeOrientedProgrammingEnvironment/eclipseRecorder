package edu.oregonstate.cope.eclipse;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class IgnoredProjectsTest {

	@Before
	public void setUp() {
		COPEPlugin.getDefault().setIgnoredProjectsList(Arrays.asList(new String[]{"test1"}));
	}
	
	@Test
	public void testIsIgnored() {
		COPEPlugin.getDefault().readIgnoredProjects();
		List<String> ignoreProjectsList = COPEPlugin.getDefault().getIgnoreProjectsList();
		assertTrue(ignoreProjectsList.contains("test1"));
	}
}

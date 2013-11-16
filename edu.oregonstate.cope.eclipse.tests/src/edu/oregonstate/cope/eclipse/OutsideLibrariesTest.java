package edu.oregonstate.cope.eclipse;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;

import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class OutsideLibrariesTest {
	
	@Rule public TestName name = new TestName();
	private IJavaProject project;
	
	@Before
	public void setUp() throws Exception {
		InputStream stream = new FileInputStream(new File(Paths.get("projects/" + name.getMethodName() + "/.project").toAbsolutePath().toString()));
		IProjectDescription projectDescription = ResourcesPlugin.getWorkspace().loadProjectDescription(stream);
		
	}
	
	@Test
	public void testGetNonWorkspaceLibrary() throws Exception {
	}

}

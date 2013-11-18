package edu.oregonstate.cope.eclipse;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class OutsideLibrariesTest {

	@Rule
	public TestName name = new TestName();
	private static IJavaProject javaProject;

	@BeforeClass
	public static void setUp() throws Exception {
		ImportOperation importOperation = new ImportOperation(new Path("librariesTest"), 
				new File(Paths.get("projects/" + "librariesTest").toAbsolutePath().toString()), 
				FileSystemStructureProvider.INSTANCE, 
				new IOverwriteQuery() {

			@Override
			public String queryOverwrite(String pathString) {
				return ALL;
			}
		});
		importOperation.setCreateContainerStructure(false);
		importOperation.run(new NullProgressMonitor());
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		if (projects.length > 1)
			fail("More than 1 project in the workspace");

		IProject project = projects[0];
		project.open(new NullProgressMonitor());
		String[] natureIds = project.getDescription().getNatureIds();
		if (JavaProject.hasJavaNature(project)) {
			javaProject = JavaCore.create(project);
		} else {
			fail("Project does not have java nature");
		}
	}

	@Test
	public void testGetNonWorkspaceLibrary() throws Exception {
		StartPluginUIJob job = new StartPluginUIJob(COPEPlugin.getDefault(), "");
		List<String> nonWorkspaceLibraries = job.getNonWorkspaceLibraries(javaProject);
		assertEquals(1, nonWorkspaceLibraries.size());
		assertEquals("/Users/caius/osu/COPE/clientRecorder/edu.oregonstate.cope.eclipse.tests/projects/json-simple-1.1.1.jar",nonWorkspaceLibraries.get(0));
	}

}

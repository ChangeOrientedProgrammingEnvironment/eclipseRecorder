package edu.oregonstate.cope.eclipse;

import static org.junit.Assert.fail;

import java.io.File;
import java.nio.file.Paths;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.junit.After;
import org.junit.Before;

public class PopulatedWorkspaceTest {

	protected static IJavaProject javaProject;
	protected static SnapshotManager snapshotManager = new SnapshotManager(COPEPlugin.getDefault().getVersionedLocalStorage().getAbsolutePath());
	
	protected String getProjectPath() {
		return "projects" + File.separator + getProjectName();
	}

	protected String getProjectName() {
		return "librariesTest";
	}

	@SuppressWarnings("restriction")
	@Before
	public void before() throws Exception {
		ImportOperation importOperation = new ImportOperation(new Path(getProjectName()), 
				new File(Paths.get(getProjectPath()).toAbsolutePath().toString()), 
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
		if (JavaProject.hasJavaNature(project)) {
			javaProject = JavaCore.create(project);
		} else {
			fail("Project does not have java nature");
		}
	}
	
	@After
	public void after() {
		try {
			javaProject.getProject().delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
		}
	}

	public PopulatedWorkspaceTest() {
		super();
	}

}
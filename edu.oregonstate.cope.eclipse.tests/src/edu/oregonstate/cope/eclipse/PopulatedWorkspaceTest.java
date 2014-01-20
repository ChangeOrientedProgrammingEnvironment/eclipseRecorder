package edu.oregonstate.cope.eclipse;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.IImportStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.junit.After;
import org.junit.Before;

public class PopulatedWorkspaceTest {

	private final class DoneProgressMonitor implements IProgressMonitor {
		private boolean done = false;

		@Override
		public void worked(int work) {
		}

		@Override
		public void subTask(String name) {
		}

		@Override
		public void setTaskName(String name) {
		}

		@Override
		public void setCanceled(boolean value) {
		}

		@Override
		public boolean isCanceled() {
			return false;
		}

		@Override
		public void internalWorked(double work) {
		}

		@Override
		public void done() {
			done = true;
		}

		@Override
		public void beginTask(String name, int totalWork) {
			// TODO Auto-generated method stub
		}

		public boolean isDone() {
			return done;
		}
	}

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
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProjectDescription newProjectDescription = workspace.newProjectDescription(getProjectName());
		IProject newProject = workspace.getRoot().getProject(getProjectName());
		newProject.create(newProjectDescription, null);
		newProject.open(null);
		ImportOperation importOperation = new ImportOperation(
				newProject.getFullPath(), 
				getRoot(), 
				getImportStructureProvider(), 
				new IOverwriteQuery() {

			@Override
			public String queryOverwrite(String pathString) {
				return ALL;
			}
		}, getFileSystemObjects());
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

	protected Object getRoot() {
		return new File(Paths.get(getProjectPath()).toAbsolutePath().toString());
	}

	protected IImportStructureProvider getImportStructureProvider() {
		return FileSystemStructureProvider.INSTANCE;
	}
	
	protected List getFileSystemObjects() {
		return null;
	}

	@After
	public void after() {
		try {
			DoneProgressMonitor monitor = new DoneProgressMonitor();
			String projectPath = javaProject.getProject().getLocation().toPortableString();
			System.out.println(projectPath);
			deleteFolder(projectPath);
			javaProject.getProject().delete(true, monitor);
			while (!monitor.isDone()) {
				Thread.sleep(100);
			}
		} catch (CoreException e) {
			System.out.println(e);
		} catch (InterruptedException e) {
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private void deleteFolder(String folderPath) throws IOException {
		Files.walkFileTree(Paths.get(folderPath), new FileVisitor<Path>() {

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				// TODO Auto-generated method stub
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				// TODO Auto-generated method stub
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	public PopulatedWorkspaceTest() {
		super();
	}

}
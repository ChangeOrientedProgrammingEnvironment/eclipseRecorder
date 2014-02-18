package edu.oregonstate.cope.eclipse;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;


/**
 * Got this from org.eclipse.ui.tests.harness. It's useful
 * to have around.
 * 
 * @author Caius Brindescu
 *
 */
public class FileUtil {

    /**
     * Creates a new project.
     * 
     * @param name the project name
     */
    public static IProject createProject(String name) throws CoreException {
        IWorkspace ws = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = ws.getRoot();
        IProject proj = root.getProject(name);
        if (!proj.exists())
            proj.create(null);
        if (!proj.isOpen())
            proj.open(null);
        return proj;
    }

    /**
     * Deletes a project.
     * 
     * @param proj the project
     */
    public static void deleteProject(IProject proj) throws CoreException {
        proj.delete(true, null);
    }

    /**
     * Creates a new file in a project.
     * 
     * @param name the new file name
     * @param proj the existing project
     * @return the new file
     */
    public static IFile createFile(String name, IProject proj)
            throws CoreException {
        IFile file = proj.getFile(name);
        if (!file.exists()) {
            String str = "";
            InputStream in = new ByteArrayInputStream(str.getBytes());
            file.create(in, true, null);
        }
        return file;
    }

	public static IJavaProject createTestJavaProject(String projectName) throws CoreException {
		IJavaProject javaProject = JavaCore.create(FileUtil.createProject(projectName));
		IProjectDescription description = javaProject.getProject().getDescription();
		description.setNatureIds(new String[]{JavaCore.NATURE_ID});
		javaProject.getProject().setDescription(description, new NullProgressMonitor());
		javaProject.setRawClasspath(new IClasspathEntry[0], new NullProgressMonitor());
		return javaProject;
	}

	public static IPackageFragmentRoot createSourceFolder(IJavaProject javaProject) throws CoreException, JavaModelException {
		IFolder srcFolder = javaProject.getProject().getFolder("src");
		srcFolder.create(true, false, new NullProgressMonitor());
		IPackageFragmentRoot srcFolderPkg = javaProject.getPackageFragmentRoot(srcFolder);
		IClasspathEntry newSourceEntry = JavaCore.newSourceEntry(srcFolder.getFullPath());
		addEntryToClassPath(newSourceEntry, javaProject);
		return srcFolderPkg;
	}
	
	public static void addEntryToClassPath(IClasspathEntry entry, IJavaProject project) throws JavaModelException {
		IClasspathEntry[] currentClasspath = project.getRawClasspath();
		IClasspathEntry[] newClasspath = new IClasspathEntry[currentClasspath.length + 1];
		for (int i = 0; i<currentClasspath.length; i++)
			newClasspath[i] = currentClasspath[i];
		newClasspath[currentClasspath.length] = entry;
		project.setRawClasspath(newClasspath, new NullProgressMonitor());
	}
	
	public static void addProjectDepedency(IJavaProject mainProject, IJavaProject projectDepedency) throws JavaModelException {
		IClasspathEntry referencedProjectEntry = JavaCore.newProjectEntry(projectDepedency.getPath());
		FileUtil.addEntryToClassPath(referencedProjectEntry, mainProject);
	}
	
	public static File[] listZipFilesInDir(File fileDir) {
		File[] listFiles = fileDir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".zip");
			}
		});
		return listFiles;
	}

}

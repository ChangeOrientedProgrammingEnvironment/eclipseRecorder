package edu.oregonstate.cope.eclipse.listeners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.ui.internal.wizards.datatransfer.ZipLeveledStructureProvider;
import org.eclipse.ui.wizards.datatransfer.IImportStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ZipFileStructureProvider;
import org.junit.Before;
import org.junit.Test;

import edu.oregonstate.cope.eclipse.PopulatedWorkspaceTest;

public class GitRepoListenerTest extends PopulatedWorkspaceTest {
	
	private GitRepoListner testListener;
	private Git gitRepo;
	private ZipFile sourceFile;
	private ZipLeveledStructureProvider structureProvider;
	
	public GitRepoListenerTest() {
		try {
			sourceFile = new ZipFile(getProjectPath());
			structureProvider = new ZipLeveledStructureProvider(sourceFile);
		} catch (IOException e) {
		}
	}

	@Override
	protected String getProjectPath() {
		return "projects/git-project.zip";
	}

	 @Override
	protected String getProjectName() {
		return "git-project";
	}
	 
	@Override
	protected Object getRoot() {
//		try {
//			ZipFile zipFile = new ZipFile(getProjectPath());
//			return zipFile.getEntry("git-project/");
//		} catch (IOException e) {
//			System.out.println(e);
//		}
		return new ZipEntry(getProjectName());
	}
	 
	@Override
	protected IImportStructureProvider getImportStructureProvider() {
		return structureProvider;
	}
	
	@Override
	protected List<Object> getFileSystemObjects() {
		List<Object> fileSystemObjects = new ArrayList<Object>();
		Enumeration<? extends ZipEntry> entries = sourceFile.entries();
		while (entries.hasMoreElements()) {
		    fileSystemObjects.add((Object)entries.nextElement());
		}
		return fileSystemObjects;
	}
	 
	@Before
	public void setUp() throws Exception {
		testListener = new GitRepoListner(ResourcesPlugin.getWorkspace().getRoot().getProjects());
		Repository.getGlobalListenerList().addRefsChangedListener(testListener);
		
		String projectPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().makeAbsolute().toOSString() + javaProject.getProject().getFullPath().toOSString();
		gitRepo = Git.open(new File(projectPath));
	}
	 
	@Test
	public void testRepoIsProperlyThere() throws Exception {
		List<Ref> branches = gitRepo.branchList().call();
		assertEquals(2,branches.size());
	}
	
	@Test
	public void testCorrectGitListenerInitialization() {
		GitRepoStatus repoStatus = testListener.getRepoStatus(getIndexFile());
		assertNotNull(repoStatus);
		assertEquals("master", repoStatus.getBranch());
		assertEquals("26a049ef59cf72b5cbfce718d77a095ee9fd955a",repoStatus.getCommitSHA1());
	}
	
	@Test
	public void testDetectBranchChange() throws Exception {
		gitRepo.checkout().setName("test").call();
		String branch = testListener.getCurrentBranch(getIndexFile());
		assertEquals("test",branch);
	}

	private String getIndexFile() {
		return gitRepo.getRepository().getIndexFile().getAbsolutePath();
	}
}

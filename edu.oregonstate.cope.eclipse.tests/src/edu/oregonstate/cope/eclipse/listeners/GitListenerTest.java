package edu.oregonstate.cope.eclipse.listeners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.junit.Before;
import org.junit.Test;

import edu.oregonstate.cope.eclipse.PopulatedWorkspaceTest;

public class GitListenerTest extends PopulatedWorkspaceTest {
	
	private GitRefsChangedListener testListener;
	private Git gitRepo;

	@Override
	protected String getProjectPath() {
		return "projects/git-project";
	}

	 @Override
	protected String getProjectName() {
		return "git-project";
	}
	 
	@Before
	public void setUp() throws Exception {
		testListener = new GitRefsChangedListener(ResourcesPlugin.getWorkspace().getRoot().getProjects());
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

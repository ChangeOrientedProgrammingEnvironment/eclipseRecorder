package edu.oregonstate.cope.eclipse.listeners;

import static org.junit.Assert.assertEquals;

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
	
	private GitListener testListener;
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
		testListener = new GitListener();
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
	public void testDetectBranchChange() throws Exception {
		gitRepo.checkout().setName("test").call();
		String branch = testListener.getCurrentBranch();
		assertEquals("test",branch);
	}

}

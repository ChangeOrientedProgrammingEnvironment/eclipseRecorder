package edu.oregonstate.cope.eclipse.listeners;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.junit.Test;

import edu.oregonstate.cope.eclipse.PopulatedWorkspaceTest;

public class GitListenerTest extends PopulatedWorkspaceTest {
	
	@Override
	protected String getProjectPath() {
		return "projects/git-project";
	}

	 @Override
	protected String getProjectName() {
		return "git-project";
	}
	 
	@Test
	public void testRepoIsPropperlyThere() throws Exception {
		String projectPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().makeAbsolute().toOSString() + javaProject.getProject().getFullPath().toOSString();
		System.out.println(projectPath);
		Git git = Git.open(new File(projectPath));
		List<Ref> branches = git.branchList().call();
		assertEquals(2,branches.size());
	}
	
	@Test
	public void testDetectBranchChange() {
		
	}

}

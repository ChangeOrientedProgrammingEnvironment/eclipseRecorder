package edu.oregonstate.cope.eclipse.listeners;

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.jgit.events.RefsChangedEvent;
import org.eclipse.jgit.events.RefsChangedListener;

public class GitListener implements RefsChangedListener {
	
	private String currentBranch;
	private Map<String, GitRepoStatus> repoStatus;
	
	public GitListener(IProject[] projects) {
		
		repoStatus = new HashMap<String, GitRepoStatus>();
		for (IProject project : projects) {
			String projectPath = project.getLocation().makeAbsolute().toPortableString();
			try {
				Git gitRepo = Git.open(new File(projectPath));
				String gitPath = gitRepo.getRepository().getDirectory().getAbsolutePath();
				String repoUnderGit = removeLastPathElement(gitPath);
				if (repoStatus.get(repoUnderGit) == null)
					repoStatus.put(repoUnderGit, getGitRepoStatus(repoUnderGit));
			} catch (IOException e) {
			}
		}
	}

	private String removeLastPathElement(String fullPath) {
		return fullPath.substring(0, fullPath.lastIndexOf("/"));
	}

	private GitRepoStatus getGitRepoStatus(String repoUnderGit) {
		try {
			Git gitRepo = Git.open(new File(repoUnderGit));
			String branch = gitRepo.getRepository().getBranch();
			String head = getHeadCommitSHA1(gitRepo.getRepository());
			return new GitRepoStatus(branch, head);
		} catch (IOException e) {
		}
		return null;
	}

	private String getHeadCommitSHA1(Repository repository) throws IOException {
		return repository.getRef("HEAD").getObjectId().getName();
	}
	
	public GitRepoStatus getRepoStatus(String indexFile) {
		String dirUnderGit = getRepoUnderGitFromIndexFilePath(indexFile);
		return repoStatus.get(dirUnderGit);
	}

	private String getRepoUnderGitFromIndexFilePath(String indexFile) {
		return removeLastPathElement(removeLastPathElement(indexFile));
	}

	@Override
	public void onRefsChanged(RefsChangedEvent event) {
		try {
			currentBranch = event.getRepository().getBranch();
		} catch (IOException e) {
		}
	}

	public String getCurrentBranch(String indexFile) {
		return repoStatus.get(getRepoUnderGitFromIndexFilePath(indexFile)).getBranch();
	}
}

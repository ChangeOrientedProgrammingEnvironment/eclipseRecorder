package edu.oregonstate.cope.eclipse.listeners;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.events.IndexChangedEvent;
import org.eclipse.jgit.events.IndexChangedListener;
import org.eclipse.jgit.events.RefsChangedEvent;
import org.eclipse.jgit.events.RefsChangedListener;
import org.eclipse.jgit.lib.Repository;

public class GitRepoListener implements RefsChangedListener, IndexChangedListener {
	
	private Map<String, GitRepoStatus> repoStatus;
	
	public GitRepoListener(IProject[] projects) {
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
		Git gitRepo = null;
		try {
			gitRepo = Git.open(new File(repoUnderGit));
		} catch (IOException e1) {
		}
		return getGitRepoStatus(gitRepo);
	}

	private GitRepoStatus getGitRepoStatus(Git gitRepo) {
		try {
			String branch = gitRepo.getRepository().getBranch();
			String head = getHeadCommitSHA1(gitRepo.getRepository());
			Status repoStatus = gitRepo.status().call();
			return new GitRepoStatus(branch, head, repoStatus.getAdded(), repoStatus.getModified(), repoStatus.getRemoved());
		} catch (IOException e) {
		} catch (NoWorkTreeException e) {
		} catch (GitAPIException e) {
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
		GitRepoStatus currentRepoStatus = getGitRepoStatus(Git.wrap(event.getRepository()));
		repoStatus.put(getRepoUnderGitFromIndexFilePath(event.getRepository().getIndexFile().getAbsolutePath()), currentRepoStatus);
	}

	public String getCurrentBranch(String indexFile) {
		return repoStatus.get(getRepoUnderGitFromIndexFilePath(indexFile)).getBranch();
	}

	@Override
	public void onIndexChanged(IndexChangedEvent event) {
		Repository repository = event.getRepository();
		repoStatus.put(getRepoUnderGitFromIndexFilePath(repository.getIndexFile().getAbsolutePath()), getGitRepoStatus(Git.wrap(repository)));
	}
}

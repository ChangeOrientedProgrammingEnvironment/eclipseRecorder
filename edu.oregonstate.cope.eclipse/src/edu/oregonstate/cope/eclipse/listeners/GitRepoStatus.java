package edu.oregonstate.cope.eclipse.listeners;

public class GitRepoStatus {
	
	private String branch;
	private String commitSHA1;

	public GitRepoStatus(String branch, String commitSHA1) {
		this.branch = branch;
		this.commitSHA1 = commitSHA1;
	}
	
	public String getBranch() {
		return branch;
	}
	
	public String getCommitSHA1() {
		return commitSHA1;
	}

}

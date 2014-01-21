package edu.oregonstate.cope.clientRecorder;

import java.util.Set;

public class GitRepoStatus {
	
	private String branch;
	private String commitSHA1;
	private Set<String> filesModified;
	private Set<String> filesAdded;
	private Set<String> filesDeleted;

	public GitRepoStatus(String branch, String commitSHA1, Set<String> filesModified, Set<String> filesAdded, Set<String> filesDeleted) {
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

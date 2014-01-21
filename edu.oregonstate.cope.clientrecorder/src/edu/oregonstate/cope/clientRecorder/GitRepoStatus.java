package edu.oregonstate.cope.clientRecorder;

import java.util.ArrayList;
import java.util.Set;

import org.json.simple.JSONObject;

public class GitRepoStatus {
	
	private String branch;
	private String commitSHA1;
	private Set<String> filesModified;
	private Set<String> filesAdded;
	private Set<String> filesDeleted;

	public GitRepoStatus(String branch, String commitSHA1, Set<String> filesModified, Set<String> filesAdded, Set<String> filesDeleted) {
		this.branch = branch;
		this.commitSHA1 = commitSHA1;
		this.filesModified = filesModified;
		this.filesAdded = filesAdded;
		this.filesDeleted = filesDeleted;
	}
	
	public String getBranch() {
		return branch;
	}
	
	public String getCommitSHA1() {
		return commitSHA1;
	}
	
	public JSONObject getJSON() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JSONConstants.JSON_GIT_HEAD_COMMIT, commitSHA1);
		jsonObject.put(JSONConstants.JSON_GIT_BRANCH, branch);
		jsonObject.put(JSONConstants.JSON_GIT_FILES_MODIFIED, new ArrayList<String>(filesModified));
		jsonObject.put(JSONConstants.JSON_GIT_FILES_ADDED, new ArrayList<String>(filesAdded));
		jsonObject.put(JSONConstants.JSON_GIT_FILES_REMOVED, new ArrayList<String>(filesDeleted));
		return jsonObject;
	}

}

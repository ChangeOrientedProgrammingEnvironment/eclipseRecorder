package edu.oregonstate.cope.clientRecorder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.json.simple.JSONObject;
import org.junit.Test;

import static edu.oregonstate.cope.clientRecorder.JSONConstants.*;

public class GitRepoStatusTest extends JSONTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testJSONGeneration() {
		HashSet<String> filesModified = new HashSet<>(Arrays.asList(new String[] { "/fileA" }));
		HashSet<String> filesAdded = new HashSet<>();
		HashSet<String> filesDeleted = new HashSet<>();
		
		GitRepoStatus repoStatus = new GitRepoStatus("branch", "abc02", filesModified, filesAdded, filesDeleted);
		JSONObject actual = repoStatus.getJSON();
		
		JSONObject expected = new JSONObject();
		expected.put(JSON_GIT_BRANCH, "branch");
		expected.put(JSON_GIT_HEAD_COMMIT, "abc02");
		expected.put(JSON_GIT_FILES_ADDED, new ArrayList<String>(filesAdded));
		expected.put(JSON_GIT_FILES_REMOVED, new ArrayList<String>(filesDeleted));
		expected.put(JSON_GIT_FILES_MODIFIED, new ArrayList<String>(filesModified));
		
		assertJSONEquals(expected, actual);
	}

}

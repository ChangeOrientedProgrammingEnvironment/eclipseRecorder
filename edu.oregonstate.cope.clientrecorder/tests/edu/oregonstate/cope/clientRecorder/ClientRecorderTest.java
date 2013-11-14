package edu.oregonstate.cope.clientRecorder;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import edu.oregonstate.cope.clientRecorder.ClientRecorder.EventType;
import static edu.oregonstate.cope.clientRecorder.ClientRecorder.*;
import static org.junit.Assert.*;

//TODO refactor this test class. Too many hardcoded strings. Too much duplication with tested class.
public class ClientRecorderTest {

	private ClientRecorder clientRecorder;

	@Before
	public void setup() {
		clientRecorder = new ClientRecorder();
		clientRecorder.setIDE("IDEA");
	}

	/* Text Change Tests */
	@Test(expected = RuntimeException.class)
	public void testRecordTextChangeNull() throws Exception {
		clientRecorder.buildTextChangeJSON(null, 0, 0, null, null);
	}

	@Test(expected = RuntimeException.class)
	public void testRecordTextChangeNoSourceFile() throws Exception {
		clientRecorder.buildTextChangeJSON("", 0, 0, "", "");
	}

	@Test(expected = RuntimeException.class)
	public void testRecordTextChangeNoOrigin() throws Exception {
		clientRecorder.buildTextChangeJSON("", 0, 0, "/sampleFile", "");
	}

	@Test
	public void testRecordTextChangeNoOp() throws Exception {
		JSONObject result1 = clientRecorder.buildTextChangeJSON("", 0, 0, "/sampleFile", "changeOrigin");
		JSONObject obj = createChangeJSON("", 0, 0, "/sampleFile", "changeOrigin");
		assertJSONEquals(result1, obj);
	}

	@Test
	public void testRecordTextChangeAdd() throws Exception {
		JSONObject result1 = clientRecorder.buildTextChangeJSON("addedText", 0, 0, "/sampleFile", "changeOrigin");
		JSONObject obj = createChangeJSON("addedText", 0, 0, "/sampleFile", "changeOrigin");
		assertJSONEquals(result1, obj);
	}

	@Test
	public void testRecordTextChangeDelete() throws Exception {
		JSONObject result1 = clientRecorder.buildTextChangeJSON("", 0, 0, "/sampleFile", "changeOrigin");
		JSONObject obj = createChangeJSON("", 0, 0, "/sampleFile", "changeOrigin");
		assertJSONEquals(result1, obj);
	}

	@Test
	public void testRecordTextChangeReplace() throws Exception {
		JSONObject result1 = clientRecorder.buildTextChangeJSON("addedText", 3, 0, "/sampleFile", "changeOrigin");
		JSONObject obj = createChangeJSON("addedText", 3, 0, "/sampleFile", "changeOrigin");
		assertJSONEquals(result1, obj);
	}

	private JSONObject createChangeJSON(String text, int offset, int length, String sourceFile, String changeOrigin) {
		JSONObject j = new JSONObject();
		j.put(JSON_EVENT_TYPE, EventType.textChange.toString());
		j.put(JSON_TEXT, text);
		j.put(JSON_OFFSET, offset);
		j.put(JSON_LENGTH, length);
		j.put(JSON_ENTITY_ADDRESS, sourceFile);
		j.put(JSON_CHANGE_ORIGIN, changeOrigin);
		j.put(JSON_IDE, clientRecorder.getIDE());
		addTimeStamp(j);
		return j;
	}

	/* Test DebugLaunch */
	@Test(expected = RuntimeException.class)
	public void testDebugLaunchNull() throws Exception {
		clientRecorder.buildIDEEventJSON(null, null);
	}

	@Test
	public void testDebugLaunch() throws Exception {
		JSONObject retObj = clientRecorder.buildIDEEventJSON(ClientRecorder.EventType.debugLaunch, "/workspace/package/filename.java");
		JSONObject expected = new JSONObject();
		expected.put(JSON_IDE, "IDEA");
		expected.put(JSON_EVENT_TYPE, ClientRecorder.EventType.debugLaunch.toString());
		expected.put(JSON_ENTITY_ADDRESS, "/workspace/package/filename.java");
		addTimeStamp(expected);

		assertJSONEquals(expected, retObj);
	}

	@Test
	public void testStdLaunch() throws Exception {
		JSONObject retObj = clientRecorder.buildIDEEventJSON(ClientRecorder.EventType.normalLaunch, "/workspace/package/filename.java");
		JSONObject expected = new JSONObject();
		expected.put(JSON_IDE, "IDEA");
		expected.put(JSON_EVENT_TYPE, ClientRecorder.EventType.normalLaunch.toString());
		expected.put(JSON_ENTITY_ADDRESS, "/workspace/package/filename.java");
		addTimeStamp(expected);

		assertJSONEquals(expected, retObj);
	}

	@Test
	public void testFileOpen() throws Exception {
		JSONObject retObj = clientRecorder.buildIDEEventJSON(ClientRecorder.EventType.fileOpen, "/workspace/package/filename.java");
		JSONObject expected = new JSONObject();
		expected.put(JSON_IDE, "IDEA");
		expected.put(JSON_EVENT_TYPE, ClientRecorder.EventType.fileOpen.toString());
		expected.put(JSON_ENTITY_ADDRESS, "/workspace/package/filename.java");
		addTimeStamp(expected);

		assertJSONEquals(expected, retObj);
	}

	@Test
	public void testFileClose() throws Exception {
		JSONObject retObj = clientRecorder.buildIDEEventJSON(ClientRecorder.EventType.fileClose, "/workspace/package/filename.java");
		JSONObject expected = new JSONObject();
		expected.put(JSON_IDE, "IDEA");
		expected.put(JSON_EVENT_TYPE, ClientRecorder.EventType.fileClose.toString());
		expected.put(JSON_ENTITY_ADDRESS, "/workspace/package/filename.java");
		addTimeStamp(expected);

		assertJSONEquals(expected, retObj);
	}

	@Test(expected = RuntimeException.class)
	public void testTestRunNull() throws Exception {
		clientRecorder.buildTestEventJSON(null, null);
	}

	@Test(expected = RuntimeException.class)
	public void testTestRunEmpty() throws Exception {
		clientRecorder.buildTestEventJSON("", "");
	}

	@Test
	public void testTestRun() throws Exception {
		JSONObject actual = clientRecorder.buildTestEventJSON("/workspace/package/TestFoo/testBar", "success");
		JSONObject expected = new JSONObject();

		expected.put(JSON_EVENT_TYPE, EventType.testRun.toString());
		expected.put(JSON_IDE, clientRecorder.getIDE());
		expected.put(JSON_ENTITY_ADDRESS, "/workspace/package/TestFoo/testBar");
		expected.put(JSON_TEST_RESULT, "success");
		addTimeStamp(expected);

		assertJSONEquals(expected, actual);
	}
	
	@Test(expected = RuntimeException.class)
	public void testSnapshotNull() throws Exception {
		clientRecorder.buildSnapshotJSON(null);
	}
	
	@Test(expected = RuntimeException.class)
	public void testSnapshotEmpty() throws Exception {
		clientRecorder.buildSnapshotJSON("");
	}
	
	@Test
	public void testSnapshot() throws Exception {
		JSONObject actual = clientRecorder.buildSnapshotJSON("/path/to/snapshot/theSnapshot");
		
		JSONObject expected = new JSONObject();
		expected.put(JSON_EVENT_TYPE, EventType.snapshot + "");
		expected.put(JSON_ENTITY_ADDRESS, "/path/to/snapshot/theSnapshot");
		expected.put(JSON_IDE, clientRecorder.getIDE());
		addTimeStamp(expected);
		
		assertJSONEquals(expected, actual);
	}

	private void addTimeStamp(JSONObject expected) {
		expected.put(JSON_TIMESTAMP, (System.currentTimeMillis() / 1000) + "");
	}

	private void assertJSONEquals(JSONObject expected, JSONObject actual) {

		assertEquals(expected.keySet(), actual.keySet());

		for (Object key : expected.keySet()) {
			if (key.equals(JSON_TIMESTAMP)) {
				assertTimestampsEqual(expected.get(key), actual.get(key));
			} else {
				assertEquals(expected.get(key), actual.get(key));
			}
		}
	}

	private void assertTimestampsEqual(Object expected, Object actual) {
		int oneSecond = 3600;
		
		Long expectedTimestamp = Long.parseLong((String) expected);
		Long actualTimestamp = Long.parseLong((String) actual);
		
		assertTrue(expectedTimestamp > actualTimestamp - oneSecond);
	}
}

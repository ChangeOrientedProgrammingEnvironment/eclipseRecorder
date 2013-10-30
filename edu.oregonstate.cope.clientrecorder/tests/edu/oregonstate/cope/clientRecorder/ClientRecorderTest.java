package edu.oregonstate.cope.clientRecorder;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import edu.oregonstate.cope.clientRecorder.ClientRecorder.EventType;
import static org.junit.Assert.*;

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
		j.put("eventType", EventType.textChange.toString());
		j.put("text", text);
		j.put("offset", offset);
		j.put("len", length);
		j.put("sourceFile", sourceFile);
		j.put("changeOrigin", changeOrigin);
		j.put("IDE", clientRecorder.getIDE());
		j.put("timestamp", System.currentTimeMillis() / 1000);
		return j;
	}

	/* Test DebugLaunch */
	@Test(expected = RuntimeException.class)
	public void testDebugLaunchNull() throws Exception {
		clientRecorder.buildIDEFileEventJSON(null, null);
	}

	@Test
	public void testDebugLaunch() throws Exception {
		JSONObject retObj = clientRecorder.buildIDEFileEventJSON(ClientRecorder.EventType.debugLaunch, "/workspace/package/filename.java");
		JSONObject expected = new JSONObject();
		expected.put("IDE", "IDEA");
		expected.put("eventType", ClientRecorder.EventType.debugLaunch.toString());
		expected.put("fullyQualifiedMain", "/workspace/package/filename.java");
		expected.put("timestamp", System.currentTimeMillis() / 1000);

		assertJSONEquals(expected, retObj);
	}

	@Test
	public void testStdLaunch() throws Exception {
		JSONObject retObj = clientRecorder.buildIDEFileEventJSON(ClientRecorder.EventType.normalLaunch, "/workspace/package/filename.java");
		JSONObject expected = new JSONObject();
		expected.put("IDE", "IDEA");
		expected.put("eventType", ClientRecorder.EventType.normalLaunch.toString());
		expected.put("fullyQualifiedMain", "/workspace/package/filename.java");
		expected.put("timestamp", System.currentTimeMillis() / 1000);

		assertJSONEquals(expected, retObj);
	}

	@Test
	public void testFileOpen() throws Exception {
		JSONObject retObj = clientRecorder.buildIDEFileEventJSON(ClientRecorder.EventType.fileOpen, "/workspace/package/filename.java");
		JSONObject expected = new JSONObject();
		expected.put("IDE", "IDEA");
		expected.put("eventType", ClientRecorder.EventType.fileOpen.toString());
		expected.put("fullyQualifiedMain", "/workspace/package/filename.java");
		expected.put("timestamp", System.currentTimeMillis() / 1000);

		assertJSONEquals(expected, retObj);
	}

	@Test
	public void testFileClose() throws Exception {
		JSONObject retObj = clientRecorder.buildIDEFileEventJSON(ClientRecorder.EventType.fileClose, "/workspace/package/filename.java");
		JSONObject expected = new JSONObject();
		expected.put("IDE", "IDEA");
		expected.put("eventType", ClientRecorder.EventType.fileClose.toString());
		expected.put("fullyQualifiedMain", "/workspace/package/filename.java");
		expected.put("timestamp", System.currentTimeMillis() / 1000);

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

		expected.put("eventType", EventType.testRun.toString());
		expected.put("IDE", clientRecorder.getIDE());
		expected.put("fullyQualifiedTestMethod", "/workspace/package/TestFoo/testBar");
		expected.put("testResult", "success");
		expected.put("timestamp", System.currentTimeMillis() / 1000);

		assertJSONEquals(expected, actual);
	}

	private void assertJSONEquals(JSONObject expected, JSONObject actual) {

		assertEquals(expected.keySet(), actual.keySet());

		for (Object key : expected.keySet()) {
			if (key.equals("timestamp")) {
				continue;
			} else {
				assertEquals(expected.get(key), actual.get(key));
			}
		}
	}
}

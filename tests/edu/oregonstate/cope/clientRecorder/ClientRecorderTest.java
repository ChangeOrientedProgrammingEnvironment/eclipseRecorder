package edu.oregonstate.cope.clientRecorder;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClientRecorderTest {


    private ClientRecorder clientRecorder;

    @Before
    public void setup(){
        clientRecorder = new ClientRecorder();
        clientRecorder.setIDE("IDEA");
    }

	/* Text Change Tests */
	@Test(expected = RuntimeException.class)
	public void testRecordTextChangeNull() throws Exception {
        clientRecorder.buildJSONTextChange(null, 0, 0, null, null, null);
	}

	@Test(expected = RuntimeException.class)
	public void testRecordTextChangeNoSourceFile() throws Exception {
        clientRecorder.buildJSONTextChange("", 0, 0, "", "", "");
	}

	@Test(expected = RuntimeException.class)
	public void testRecordTextChangeNoOrigin() throws Exception {
        clientRecorder.buildJSONTextChange("", 0, 0, "/sampleFile", "", "");
	}

	@Test
	public void testRecordTextChangeNoOp() throws Exception {
		JSONObject result1 = clientRecorder.buildJSONTextChange("", 0, 0, "/sampleFile", "changeOrigin", "IDEA");
		JSONObject obj = createChangeJSON("", 0, 0, "/sampleFile", "changeOrigin", "IDEA");
		assertEquals(result1, obj);
	}

	@Test
	public void testRecordTextChangeAdd() throws Exception {
		JSONObject result1 = clientRecorder.buildJSONTextChange("addedText", 0, 0, "/sampleFile", "changeOrigin", "IDEA");
		JSONObject obj = createChangeJSON("addedText", 0, 0, "/sampleFile", "changeOrigin", "IDEA");
		assertEquals(result1, obj);
	}

	@Test
	public void testRecordTextChangeDelete() throws Exception {
		JSONObject result1 = clientRecorder.buildJSONTextChange("", 0, 0, "/sampleFile", "changeOrigin", "IDEA");
		JSONObject obj = createChangeJSON("", 0, 0, "/sampleFile", "changeOrigin", "IDEA");
		assertEquals(result1, obj);
	}

	@Test
	public void testRecordTextChangeReplace() throws Exception {
		JSONObject result1 = clientRecorder.buildJSONTextChange("addedText", 3, 0, "/sampleFile", "changeOrigin", "IDEA");
		JSONObject obj = createChangeJSON("addedText", 3, 0, "/sampleFile", "changeOrigin", "IDEA");
		assertEquals(result1, obj);
	}

	private JSONObject createChangeJSON(String text, int offset, int length, String sourceFile, String changeOrigin, String IDE) {
		JSONObject j = new JSONObject();
		j.put("type", "Text");
		j.put("text", text);
		j.put("offset", offset);
		j.put("len", length);
		j.put("sourceFile", sourceFile);
		j.put("changeOrigin", changeOrigin);
		j.put("ide", IDE);
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
        expected.put("IDE","IDEA");
        expected.put("eventType",ClientRecorder.EventType.debugLaunch);
        expected.put("fullyQualifiedMain","/workspace/package/filename.java") ;
        assertEquals(expected, retObj);
    }

    @Test
    public void testStdLaunch() throws Exception {
        JSONObject retObj = clientRecorder.buildIDEFileEventJSON(ClientRecorder.EventType.normalLaunch, "/workspace/package/filename.java");
        JSONObject expected = new JSONObject();
        expected.put("IDE","IDEA");
        expected.put("eventType",ClientRecorder.EventType.normalLaunch);
        expected.put("fullyQualifiedMain","/workspace/package/filename.java") ;
        assertEquals(expected, retObj);
    }

    @Test
    public void testFileOpen() throws Exception {
        JSONObject retObj = clientRecorder.buildIDEFileEventJSON(ClientRecorder.EventType.fileOpen, "/workspace/package/filename.java");
        JSONObject expected = new JSONObject();
        expected.put("IDE","IDEA");
        expected.put("eventType",ClientRecorder.EventType.fileOpen);
        expected.put("fullyQualifiedMain","/workspace/package/filename.java") ;
        assertEquals(expected, retObj);
    }

    @Test
    public void testFileClose() throws Exception {
        JSONObject retObj = clientRecorder.buildIDEFileEventJSON(ClientRecorder.EventType.fileClose, "/workspace/package/filename.java");
        JSONObject expected = new JSONObject();
        expected.put("IDE","IDEA");
        expected.put("eventType",ClientRecorder.EventType.fileClose);
        expected.put("fullyQualifiedMain","/workspace/package/filename.java") ;
        assertEquals(expected, retObj);
    }
}

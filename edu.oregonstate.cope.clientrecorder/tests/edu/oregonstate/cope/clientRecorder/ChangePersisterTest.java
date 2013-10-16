package edu.oregonstate.cope.clientRecorder;

import org.json.simple.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.StringWriter;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

public class ChangePersisterTest {

	private StringWriter stringWriter;

	@Before
	public void setup() {
		stringWriter = new StringWriter();
		ChangePersister.instance().setWriter(stringWriter);
		ChangePersister.instance().init();
		testInit();
	}

	private void testInit() {
		JSONArray jarr = getJsonArray();
		assertEquals(jarr.size(), 1);
		assertEquals(((JSONObject) jarr.get(0)).get("eventType"), "FileInit");
	}

	private JSONArray getJsonArray() {
		return (JSONArray) JSONValue.parse(stringWriter.toString());
	}
	
	@Test
	public void testPersistNull() throws Exception {
		RuntimeException caughtException = null;
		
		try{
			ChangePersister.instance().persist(null);
		}
		catch(RuntimeException exception){
			caughtException = exception;
		}
		assertEquals(caughtException.getClass(), RuntimeException.class);
		testInit();
	}
	
	@Test
	public void testPersistRecord() {
		JSONObject objToRecord = new JSONObject();
		objToRecord.put("test", "fileIO");
		
		ChangePersister.instance().persist(objToRecord);
		
		JSONArray jarr = getJsonArray();
		assertEquals(jarr.size(), 2);
		assertEquals(((JSONObject) jarr.get(1)).get("test"), "FileIO");
	}
}

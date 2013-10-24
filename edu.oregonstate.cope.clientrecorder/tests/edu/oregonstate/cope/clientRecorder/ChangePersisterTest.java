package edu.oregonstate.cope.clientRecorder;

import static junit.framework.Assert.assertEquals;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Before;
import org.junit.Test;

public class ChangePersisterTest {

	private StringWriter stringWriter;

	@Before
	public void setup() {
		stringWriter = new StringWriter();

		ChangePersister.instance().setFileManager(new FileManager() {
			@Override
			public void write(String string) {
				stringWriter.write(string);
			}

			@Override
			public boolean isCurrentFileEmpty() {
				return stringWriter.toString().isEmpty();
			}

			@Override
			public void commitChanges() {
			}
		});

		testInit();
	}

	@Test
	public void testInit() {
		List<JSONObject> jarr = getJsonArray();
		assertEquals(jarr.size(), 1);
		testMarkerJSON(jarr);
	}

	private void testMarkerJSON(List<JSONObject> jarr) {
		assertEquals(jarr.get(0).get("eventType"), "FileInit");
	}

	private List<JSONObject> getJsonArray() {
		// return (JSONArray) JSONValue.parse(stringWriter.toString());
		List<String> allMatches = new ArrayList<>();
		Matcher m = ChangePersister.ELEMENT_REGEX.matcher(stringWriter.toString());
		while (m.find()) {
			allMatches.add(m.group(1));
		}

		List<JSONObject> jsonEvents = new ArrayList<>();
		for (String jsonString : allMatches) {
			JSONObject obj = (JSONObject) JSONValue.parse(jsonString);
			jsonEvents.add(obj);
		}

		return jsonEvents;
	}

	@Test
	public void testPersistNull() throws Exception {
		RuntimeException caughtException = null;

		try {
			ChangePersister.instance().persist(null);
		} catch (RuntimeException exception) {
			caughtException = exception;
		}
		assertEquals(caughtException.getClass(), RuntimeException.class);
		testInit();
	}

	@Test
	public void testPersistOneRecord() {
		JSONObject objToRecord = new JSONObject();
		objToRecord.put("test", "fileIO");

		ChangePersister.instance().persist(objToRecord);

		List<JSONObject> jarr = getJsonArray();
		assertEquals(jarr.size(), 2);
		assertEquals(jarr.get(1).get("test"), "fileIO");

		testMarkerJSON(jarr);
	}

	@Test
	public void testPersistTwoRecords() {
		JSONObject objToRecord1 = new JSONObject();
		objToRecord1.put("test", "fileIO");

		JSONObject objToRecord2 = new JSONObject();
		objToRecord1.put("test2", "fileIO2");

		ChangePersister.instance().persist(objToRecord1);
		ChangePersister.instance().persist(objToRecord2);

		List<JSONObject> jarr = getJsonArray();
		assertEquals(jarr.size(), 3);
		assertEquals(jarr.get(1).get("test"), "fileIO");
		assertEquals(jarr.get(1).get("test2"), "fileIO2");

		testMarkerJSON(jarr);
	}
}

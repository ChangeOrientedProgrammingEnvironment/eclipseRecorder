package edu.oregonstate.cope.clientRecorder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.json.simple.JSONObject;

public class JSONTest {

	public void assertJSONEquals(JSONObject expected, JSONObject actual) {
	
		assertEquals(expected.keySet(), actual.keySet());
	
		for (Object key : expected.keySet()) {
			if (key.equals(JSONConstants.JSON_TIMESTAMP)) {
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
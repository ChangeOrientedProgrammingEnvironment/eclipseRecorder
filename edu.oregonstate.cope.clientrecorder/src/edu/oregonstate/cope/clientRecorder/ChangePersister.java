package edu.oregonstate.cope.clientRecorder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.management.RuntimeErrorException;

/**
 * Persists JSON objects to disc. This class is a Singleton.
 */
public class ChangePersister {

	private Writer writer;

	private static class Instance {
		public static final ChangePersister instance = new ChangePersister();
	}

	private ChangePersister() {
		try {
			writer = new BufferedWriter(new FileWriter("testFileWrite.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		init();
	}

	public void init() {
		JSONArray jsonArr = new JSONArray();
		JSONObject markerObject = createInitJSON();
		jsonArr.add(markerObject);

		try {
			writer.write(jsonArr.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private JSONObject createInitJSON() {
		JSONObject markerObject = new JSONObject();
		markerObject.put("eventType", "FileInit");
		return markerObject;
	}

	public static ChangePersister instance() {
		return Instance.instance;
	}

	public void persist(JSONObject jsonObject) {
		if (jsonObject == null) {
			throw new RuntimeException("Argument cannot be null");
		}

		JSONArray persistedContent = (JSONArray) JSONValue.parse(writer.toString());
		persistedContent.add(jsonObject);

		try {
			writer.write(persistedContent.toJSONString());
			writer.flush();
		} catch (IOException e) {
			throw new RuntimeException("could not write JSON to file");
		}
	}

	protected void setWriter(Writer stringWriter) {
		this.writer = stringWriter;
	}
}

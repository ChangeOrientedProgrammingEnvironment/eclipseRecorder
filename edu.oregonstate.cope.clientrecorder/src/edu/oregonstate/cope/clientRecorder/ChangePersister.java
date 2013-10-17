package edu.oregonstate.cope.clientRecorder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;

/**
 * Persists JSON objects to disc. This class is a Singleton.
 */
public class ChangePersister {

	private static final String SEPARATOR = "$@$";
	public static final Pattern ELEMENT_REGEX = Pattern.compile(Pattern.quote(SEPARATOR) + "(\\{.*?\\})");
	
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
		try {
			writer.write(ChangePersister.SEPARATOR);
			JSONObject markerObject = createInitJSON();
			writer.write(markerObject.toJSONString());
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

		try {
			writer.write(ChangePersister.SEPARATOR);
			writer.write(jsonObject.toJSONString());
			writer.flush();
		} catch (IOException e) {
			throw new RuntimeException("could not write JSON to file");
		}
	}

	protected void setWriter(Writer stringWriter) {
		this.writer = stringWriter;
	}
}

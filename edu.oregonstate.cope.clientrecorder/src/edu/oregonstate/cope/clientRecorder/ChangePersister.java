package edu.oregonstate.cope.clientRecorder;

import java.io.Writer;

import org.json.simple.JSONObject;

/**
 * Persists JSON objects to disc. This class is a Singleton.
 */
public class ChangePersister {
	
	private Writer writer;

	private static class Instance {
		public static final ChangePersister instance = new ChangePersister();
	}

	private ChangePersister() {
		initFile();
	}

	private void initFile() {
	}

	public static ChangePersister instance() {
		return Instance.instance;
	}

	public void persist(JSONObject change) {
		
	}

	protected void testSetWriter(Writer stringWriter) {
		this.writer = stringWriter;
	}
}

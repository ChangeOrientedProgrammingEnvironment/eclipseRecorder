package edu.oregonstate.cope.clientRecorder;

import org.json.simple.JSONObject;

/**
 * Persists JSON objects to disc. This class is a Singleton.
 */
public class ChangePersister {

	private static class Instance {
		public static final ChangePersister instance = new ChangePersister();
	}

	private ChangePersister() {
	}

	public static ChangePersister instance() {
		return Instance.instance;
	}

	public void persist(JSONObject change) {

	}
}

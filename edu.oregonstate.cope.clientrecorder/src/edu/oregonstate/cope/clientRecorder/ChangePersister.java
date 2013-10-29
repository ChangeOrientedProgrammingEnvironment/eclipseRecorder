package edu.oregonstate.cope.clientRecorder;

import java.util.regex.Pattern;

import org.json.simple.JSONObject;

/**
 * Persists JSON objects. This class is a Singleton. A FileManager must be set
 * in order for the ChangePersister to function.
 */
public class ChangePersister {

	private static final String SEPARATOR = "\n$@$";
	public static final Pattern ELEMENT_REGEX = Pattern.compile(Pattern.quote(SEPARATOR) + "(\\{.*?\\})");

	private FileProvider fileManager;

	private static class Instance {
		public static final ChangePersister instance = new ChangePersister();
	}

	private ChangePersister() {
		fileManager = new EventFilesProvider();
	}

	public void init() {
		if (fileManager.isCurrentFileEmpty()) {
			JSONObject markerObject = createInitJSON();

			fileManager.appendToCurrentFile(ChangePersister.SEPARATOR);
			fileManager.appendToCurrentFile(markerObject.toJSONString());
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

		fileManager.appendToCurrentFile(ChangePersister.SEPARATOR);
		fileManager.appendToCurrentFile(jsonObject.toJSONString());
	}

	public void setFileManager(FileProvider fileManager) {
		this.fileManager = fileManager;
		init();
	}

	public void setRootDirectory(String rootDirectory) {
		fileManager.setRootDirectory(rootDirectory);
	}
}

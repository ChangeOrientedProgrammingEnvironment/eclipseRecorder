package edu.oregonstate.cope.clientRecorder;

import java.util.regex.Pattern;

import org.json.simple.JSONObject;

import edu.oregonstate.cope.clientRecorder.fileOps.FileProvider;

/**
 * Defines and implements JSON event persistence format. A {@link FileProvider} must be
 * set in order for the ChangePersister to function.
 * 
 * <br>
 * This class is a Singleton.
 */
public class ChangePersister {

	private static final String SEPARATOR = "\n$@$";
	public static final Pattern ELEMENT_REGEX = Pattern.compile(Pattern.quote(SEPARATOR) + "(\\{.*?\\})");

	private FileProvider fileManager;

	private static class Instance {
		public static final ChangePersister instance = new ChangePersister();
	}

	private ChangePersister() {
	}

	// TODO This gets called on every persist. Maybe create a special
	// FileProvider that knows how to initialize things on file swap
	public void addInitEventIfAbsent() {
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

	public synchronized void persist(JSONObject jsonObject) throws RecordException {
		if (jsonObject == null) {
			throw new RecordException("Argument cannot be null");
		}

		addInitEventIfAbsent();

		fileManager.appendToCurrentFile(ChangePersister.SEPARATOR);
		fileManager.appendToCurrentFile(jsonObject.toJSONString());
	}

	public void setFileManager(FileProvider fileManager) {
		this.fileManager = fileManager;
		addInitEventIfAbsent();
	}
}

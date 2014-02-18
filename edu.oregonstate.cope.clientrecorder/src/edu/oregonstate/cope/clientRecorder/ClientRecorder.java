package edu.oregonstate.cope.clientRecorder;

import java.util.Map;

import org.json.simple.JSONObject;

import edu.oregonstate.cope.clientRecorder.util.LoggerInterface;

/**
 * Records text changes and test runs from the IDE. Encodes changes in JSON
 * format.
 */

public class ClientRecorder {

	public static final String ECLIPSE_IDE = "eclipse";

	private String IDE;

	private LoggerInterface logger;

	public String getIDE() {
		return IDE;
	}

	public void setIDE(String IDE) {
		this.IDE = IDE;
	}

	public ClientRecorder() {
		logger = RecorderFacade.instance().getLogger();
	}

	/**
	 * Parameter values are not checked for consistency. Fully qualified names
	 * include the workspace of the file.
	 * 
	 * @param text
	 *            This is the text that was added to the document
	 * @param offset
	 *            This is the location that the text was added in the doc
	 * @param length
	 *            length of the text that was removed
	 * @param sourceFile
	 *            fully qualified name of the file
	 * @param changeOrigin
	 *            who originated the change, ie user, refactoring engine, source
	 *            control
	 */
	public void recordTextChange(String text, int offset, int length, String sourceFile, String changeOrigin) {
		try {
			ChangePersister.instance().persist(buildTextChangeJSON(text, offset, length, sourceFile, changeOrigin));
		} catch (RecordException e) {
			logger.error(this, e.getMessage(), e);
		}
	}

	public void recordRefresh(String text, String fileName, long modificationStamp) {
		try {
			ChangePersister.instance().persist(buildRefreshJSON(text, fileName, modificationStamp));
		} catch (RecordException e) {
			logger.error(this, e.getMessage(), e);
		}
		;
	}

	public void recordDebugLaunch(String launchTime, String launchName, String launchFile, String launchConfiguration, Map launchAttributes) {
		try {
			ChangePersister.instance().persist(buildLaunchEventJSON(Events.debugLaunch, launchTime, launchName, launchFile, launchConfiguration, launchAttributes));
		} catch (RecordException e) {
			logger.error(this, e.getMessage(), e);
		}
	}

	public void recordNormalLaunch(String launchTime, String launchName, String launchFile, String launchConfiguration, Map launchAttributes) {
		try {
			ChangePersister.instance().persist(buildLaunchEventJSON(Events.normalLaunch, launchTime, launchName, launchFile, launchConfiguration, launchAttributes));
		} catch (RecordException e) {
			logger.error(this, e.getMessage(), e);
		}
	}

	public void recordLaunchEnd(String launchTime) {
		try {
			ChangePersister.instance().persist(buildLaunchEndEventJSON(Events.launchEnd, launchTime));
		} catch (RecordException e) {
			logger.error(this, e.getMessage(), e);
		}
	}

	public void recordFileOpen(String fullyQualifiedFileAddress) {
		try {
			ChangePersister.instance().persist(buildIDEEventJSON(Events.fileOpen, fullyQualifiedFileAddress));
		} catch (RecordException e) {
			logger.error(this, e.getMessage(), e);
		}
	}

	public void recordFileClose(String fullyQualifiedFileAddress) {
		try {
			ChangePersister.instance().persist(buildIDEEventJSON(Events.fileClose, fullyQualifiedFileAddress));
		} catch (RecordException e) {
			logger.error(this, e.getMessage(), e);
		}
	}

	public void recordTestRun(String fullyQualifiedTestMethod, String testResult, double elapsedTime) {
		try {
			ChangePersister.instance().persist(buildTestEventJSON(fullyQualifiedTestMethod, testResult, elapsedTime));
		} catch (RecordException e) {
			logger.error(this, e.getMessage(), e);
		}
	}

	public void recordSnapshot(String snapshotPath) {
		try {
			ChangePersister.instance().persist(buildSnapshotJSON(snapshotPath));
		} catch (RecordException e) {
			logger.error(this, e.getMessage(), e);
		}
	}

	public void recordFileSave(String filePath, long modificationStamp) {
		try {
			ChangePersister.instance().persist(buildSaveEvent(Events.fileSave, filePath, modificationStamp));
		} catch (RecordException e) {
			logger.error(this, e.getMessage(), e);
		}
	}

	public void recordCopy(String entityAddress, int offset, int lenght, String copiedText) {
		try {
			ChangePersister.instance().persist(buildCopyJSON(Events.copy, entityAddress, offset, lenght, copiedText));
		} catch (RecordException e) {
			logger.error(this, e.getMessage(), e);
		}
	}

	public void recordResourceAdd(String entityAddress, String initialText) {
		try {
			ChangePersister.instance().persist(buildResourceAddJSON(entityAddress, initialText));
		} catch (RecordException e) {
			logger.error(this, e.getMessage(), e);
		}
	}

	public void recordResourceDelete(String entityAddress) {
		try {
			ChangePersister.instance().persist(buildResourceDeleteJSON(entityAddress));
		} catch (RecordException e) {
			logger.error(this, e.getMessage(), e);
		}
	}

	public void recordGitEvent(String repoPath, GitRepoStatus status) {
		try {
			ChangePersister.instance().persist(buildGitStatusJSON(repoPath, status));
		} catch (RecordException e) {
			logger.error(this, e.getMessage(), e);
		}
	}

	
	public void recordExternalLibraryAdd(String libraryFullpath, String libraryFileContentsBase64) {
		try {
			ChangePersister.instance().persist(buildExternalLibraryJSON(libraryFullpath, libraryFileContentsBase64));
		} catch (RecordException e) {
		}
	}

	protected JSONObject buildCommonJSONObj(Enum eventType) {
		JSONObject obj;
		obj = new JSONObject();
		obj.put(JSONConstants.JSON_IDE, this.getIDE());
		obj.put(JSONConstants.JSON_EVENT_TYPE, eventType.toString());
		obj.put(JSONConstants.JSON_TIMESTAMP, System.currentTimeMillis() + "");

		return obj;
	}

	protected JSONObject buildTextChangeJSON(String text, int offset, int length, String sourceFile, String changeOrigin) throws RecordException {
		if (text == null || sourceFile == null || changeOrigin == null) {
			throw new RecordException("Change parameters cannot be null");
		}
		if (sourceFile.isEmpty())
			throw new RecordException("Source File cannot be empty");
		if (changeOrigin.isEmpty())
			throw new RecordException("Change Origin cannot be empty");

		JSONObject obj = buildCommonJSONObj(Events.textChange);
		obj.put(JSONConstants.JSON_TEXT, text);
		obj.put(JSONConstants.JSON_OFFSET, offset);
		obj.put(JSONConstants.JSON_LENGTH, length);
		obj.put(JSONConstants.JSON_ENTITY_ADDRESS, sourceFile);
		obj.put(JSONConstants.JSON_CHANGE_ORIGIN, changeOrigin);

		return obj;
	}

	protected JSONObject buildRefreshJSON(String text, String fileName, long modificationStamp) {
		JSONObject jsonObject = buildCommonJSONObj(Events.refresh);
		jsonObject.put(JSONConstants.JSON_ENTITY_ADDRESS, fileName);
		jsonObject.put(JSONConstants.JSON_TEXT, text);
		jsonObject.put(JSONConstants.JSON_MODIFICATION_STAMP, modificationStamp);
		return jsonObject;
	}

	protected JSONObject buildIDEEventJSON(Enum EventType, String fullyQualifiedEntityAddress) throws RecordException {
		if (fullyQualifiedEntityAddress == null) {
			throw new RecordException("Fully Qualified Entity address cannot be null");
		}

		JSONObject obj;
		obj = buildCommonJSONObj(EventType);
		obj.put(JSONConstants.JSON_ENTITY_ADDRESS, fullyQualifiedEntityAddress);

		return obj;
	}

	protected JSONObject buildSaveEvent(Enum EventType, String fullyQualifiedEntityAddress, long modificationStamp) throws RecordException {
		JSONObject object = buildIDEEventJSON(EventType, fullyQualifiedEntityAddress);
		object.put(JSONConstants.JSON_MODIFICATION_STAMP, modificationStamp);

		return object;
	}

	protected JSONObject buildLaunchEventJSON(Enum EventType, String launchTime, String launchName, String launchFile, String launchConfiguration, Map launchAttributes) {
		JSONObject json = buildCommonJSONObj(EventType);
		json.put(JSONConstants.JSON_LAUNCH_ATTRIBUTES, launchAttributes);
		json.put(JSONConstants.JSON_LAUNCH_NAME, launchName);
		json.put(JSONConstants.JSON_LAUNCH_FILE, launchFile);
		json.put(JSONConstants.JSON_LAUNCH_TIMESTAMP, launchTime);
		json.put(JSONConstants.JSON_LAUNCH_CONFIGURATION, launchConfiguration);
		return json;
	}

	protected JSONObject buildLaunchEndEventJSON(Enum eventType, String launchTime) {
		JSONObject jsonObject = buildCommonJSONObj(eventType);
		jsonObject.put(JSONConstants.JSON_LAUNCH_TIMESTAMP, launchTime);
		return jsonObject;
	}

	protected JSONObject buildTestEventJSON(String fullyQualifiedTestMethod, String testResult, double elapsedTime) throws RecordException {
		if (fullyQualifiedTestMethod == null || testResult == null)
			throw new RecordException("Arguments cannot be null");
		if (fullyQualifiedTestMethod.isEmpty() || testResult.isEmpty())
			throw new RecordException("Arguments cannot be empty");

		JSONObject obj = buildCommonJSONObj(Events.testRun);
		obj.put(JSONConstants.JSON_ENTITY_ADDRESS, fullyQualifiedTestMethod);
		obj.put(JSONConstants.JSON_TEST_RESULT, testResult);
		obj.put(JSONConstants.JSON_TEST_ELAPSED_TIME, elapsedTime);

		return obj;
	}

	protected JSONObject buildSnapshotJSON(String snapshotPath) throws RecordException {
		if (snapshotPath == null)
			throw new RecordException("Arguments cannot be null");

		if (snapshotPath.isEmpty())
			throw new RecordException("Arguments cannot be empty");

		JSONObject obj = buildCommonJSONObj(Events.snapshot);
		obj.put(JSONConstants.JSON_ENTITY_ADDRESS, snapshotPath);

		return obj;
	}

	public void recordRefactoring(String refactoringName, Map argumentMap) {
		try {
			ChangePersister.instance().persist(buildRefactoringEvent(Events.refactoringLaunch, refactoringName, argumentMap));
		} catch (RecordException e) {
			logger.error(this, e.getMessage(), e);
		}
	}

	public void recordRefactoringUndo(String refactoringName, Map argumentsMap) {
		try {
			ChangePersister.instance().persist(buildRefactoringEvent(Events.refactoringUndo, refactoringName, argumentsMap));
		} catch (RecordException e) {
			logger.error(this, e.getMessage(), e);
		}
	}

	public void recordRefactoringEnd(String refactoringName, Map argumentsMap) {
		try {
			ChangePersister.instance().persist(buildRefactoringEvent(Events.refactoringEnd, refactoringName, argumentsMap));
		} catch (RecordException e) {
			logger.error(this, e.getMessage(), e);
		}
	}

	protected JSONObject buildRefactoringEvent(Enum eventType, String refactoringID, Map argumentsMap) {
		JSONObject jsonObj = buildCommonJSONObj(eventType);
		jsonObj.put(JSONConstants.JSON_REFACTORING_ID, refactoringID);
		jsonObj.put(JSONConstants.JSON_REFACTORING_ARGUMENTS, argumentsMap);

		return jsonObj;
	}

	protected JSONObject buildCopyJSON(Events copy, String entityAddress, int offset, int lenght, String copiedText) {
		JSONObject jsonObj = buildCommonJSONObj(copy);
		jsonObj.put(JSONConstants.JSON_ENTITY_ADDRESS, entityAddress);
		jsonObj.put(JSONConstants.JSON_OFFSET, offset);
		jsonObj.put(JSONConstants.JSON_LENGTH, lenght);
		jsonObj.put(JSONConstants.JSON_TEXT, copiedText);
		return jsonObj;
	}

	protected JSONObject buildResourceDeleteJSON(String entityAddress) {
		JSONObject jsonObj = buildCommonJSONObj(Events.resourceRemoved);
		jsonObj.put(JSONConstants.JSON_ENTITY_ADDRESS, entityAddress);
		return jsonObj;
	}

	protected JSONObject buildResourceAddJSON(String entityAddress, String initialText) {
		JSONObject jsonObj = buildCommonJSONObj(Events.resourceAdded);
		jsonObj.put(JSONConstants.JSON_ENTITY_ADDRESS, entityAddress);
		jsonObj.put(JSONConstants.JSON_TEXT, initialText);
		return jsonObj;
	}

	protected JSONObject buildGitStatusJSON(String repoPath, GitRepoStatus status) {
		JSONObject json = buildCommonJSONObj(Events.gitEvent);
		json.put(JSONConstants.JSON_GIT_REPO_PATH, repoPath);
		json.put(JSONConstants.JSON_GIT_STATUS, status.getJSON());
		return json;
	}

	protected JSONObject buildExternalLibraryJSON(String libraryFullpath, String libraryFileContentsBase64) {
		JSONObject jsonObject = buildCommonJSONObj(Events.externalLibraryAdd);
		jsonObject.put(JSONConstants.JSON_ENTITY_ADDRESS, libraryFullpath);
		jsonObject.put(JSONConstants.JSON_TEXT, libraryFileContentsBase64);
		return jsonObject;
	}
}

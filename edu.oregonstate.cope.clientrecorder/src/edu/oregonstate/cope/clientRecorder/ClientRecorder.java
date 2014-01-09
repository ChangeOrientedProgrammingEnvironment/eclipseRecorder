package edu.oregonstate.cope.clientRecorder;

import java.util.Map;

import org.json.simple.JSONObject;

/**
 * Records text changes and test runs from the IDE. Encodes changes in JSON
 * format.
 */

public class ClientRecorder {

	public static final String ECLIPSE_IDE = "eclipse";

	private String IDE;

	public String getIDE() {
		return IDE;
	}

	public void setIDE(String IDE) {
		this.IDE = IDE;
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
		ChangePersister.instance().persist(buildTextChangeJSON(text, offset, length, sourceFile, changeOrigin));
	}
	
	public void recordRefresh(String text, String fileName) {
		ChangePersister.instance().persist(buildRefreshJSON(text, fileName));;
	}

	public void recordDebugLaunch(String launchTime, String fullyQualifiedMainMethod, String launchConfiguration, Map launchAttributes) {
		ChangePersister.instance().persist(buildLaunchEventJSON(Events.debugLaunch, launchTime, fullyQualifiedMainMethod, launchConfiguration, launchAttributes));
	}

	public void recordNormalLaunch(String launchTime, String fullyQualifiedMainMethod, String launchConfiguration, Map launchAttributes) {
		ChangePersister.instance().persist(buildLaunchEventJSON(Events.normalLaunch, launchTime, fullyQualifiedMainMethod, launchConfiguration, launchAttributes));
	}
	
	public void recordLaunchEnd(String launchTime) {
		ChangePersister.instance().persist(buildLaunchEndEventJSON(Events.launchEnd, launchTime));
	}

	public void recordFileOpen(String fullyQualifiedFileAddress) {
		ChangePersister.instance().persist(buildIDEEventJSON(Events.fileOpen, fullyQualifiedFileAddress));
	}

	public void recordFileClose(String fullyQualifiedFileAddress) {
		ChangePersister.instance().persist(buildIDEEventJSON(Events.fileClose, fullyQualifiedFileAddress));
	}

	public void recordTestRun(String fullyQualifiedTestMethod, String testResult, double elapsedTime) {
		ChangePersister.instance().persist(buildTestEventJSON(fullyQualifiedTestMethod, testResult, elapsedTime));
	}
	
	public void recordSnapshot(String snapshotPath) {
		ChangePersister.instance().persist(buildSnapshotJSON(snapshotPath));
	}
	
	public void recordFileSave(String filePath) {
		ChangePersister.instance().persist(buildIDEEventJSON(Events.fileSave, filePath));
	}
	
	public void recordCopy(String entityAddress, int offset, int lenght, String copiedText) {
		ChangePersister.instance().persist(buildCopyJSON(Events.copy, entityAddress, offset, lenght, copiedText));
	}
	
	public void recordResourceAdd(String entityAddress, String initialText) {
		ChangePersister.instance().persist(buildResourceAddJSON(entityAddress, initialText));
	}
	
	public void recordResourceDelete(String entityAddress) {
		ChangePersister.instance().persist(buildResourceDeleteJSON(entityAddress));
	}

	protected JSONObject buildCommonJSONObj(Enum eventType) {
		JSONObject obj;
		obj = new JSONObject();
		obj.put(JSONConstants.JSON_IDE, this.getIDE());
		obj.put(JSONConstants.JSON_EVENT_TYPE, eventType.toString());
		obj.put(JSONConstants.JSON_TIMESTAMP, (System.currentTimeMillis() / 1000) + "");

		return obj;
	}

	protected JSONObject buildTextChangeJSON(String text, int offset, int length, String sourceFile, String changeOrigin) {
		if (text == null || sourceFile == null || changeOrigin == null) {
			throw new RuntimeException("Change parameters cannot be null");
		}
		if (sourceFile.isEmpty())
			throw new RuntimeException("Source File cannot be empty");
		if (changeOrigin.isEmpty())
			throw new RuntimeException("Change Origin cannot be empty");

		JSONObject obj = buildCommonJSONObj(Events.textChange);
		obj.put(JSONConstants.JSON_TEXT, text);
		obj.put(JSONConstants.JSON_OFFSET, offset);
		obj.put(JSONConstants.JSON_LENGTH, length);
		obj.put(JSONConstants.JSON_ENTITY_ADDRESS, sourceFile);
		obj.put(JSONConstants.JSON_CHANGE_ORIGIN, changeOrigin);

		return obj;
	}
	
	protected JSONObject buildRefreshJSON(String text, String fileName) {
		JSONObject jsonObject = buildCommonJSONObj(Events.refresh);
		jsonObject.put(JSONConstants.JSON_ENTITY_ADDRESS, fileName);
		jsonObject.put(JSONConstants.JSON_TEXT, text);
		return jsonObject;
	}

	protected JSONObject buildIDEEventJSON(Enum EventType, String fullyQualifiedEntityAddress) {
		if (fullyQualifiedEntityAddress == null) {
			throw new RuntimeException("Fully Qualified Entity address cannot be null");
		}

		JSONObject obj;
		obj = buildCommonJSONObj(EventType);
		obj.put(JSONConstants.JSON_ENTITY_ADDRESS, fullyQualifiedEntityAddress);

		return obj;
	}
	
	protected JSONObject buildLaunchEventJSON(Enum EventType, String launchTime, String fullyQualifiedEntityAddress, String launchConfiguration, Map launchAttributes) {
		JSONObject json = buildIDEEventJSON(EventType, fullyQualifiedEntityAddress);
		json.put(JSONConstants.JSON_LAUNCH_ATTRIBUTES, launchAttributes);
		json.put(JSONConstants.JSON_LAUNCH_TIMESTAMP, launchTime);
		json.put(JSONConstants.JSON_LAUNCH_CONFIGURATION, launchConfiguration);
		return json;
	}
	
	protected JSONObject buildLaunchEndEventJSON(Enum eventType, String launchTime) {
		JSONObject jsonObject = buildCommonJSONObj(eventType);
		jsonObject.put(JSONConstants.JSON_LAUNCH_TIMESTAMP, launchTime);
		return jsonObject;
	}

	protected JSONObject buildTestEventJSON(String fullyQualifiedTestMethod, String testResult, double elapsedTime) {
		if (fullyQualifiedTestMethod == null || testResult == null)
			throw new RuntimeException("Arguments cannot be null");
		if (fullyQualifiedTestMethod.isEmpty() || testResult.isEmpty())
			throw new RuntimeException("Arguments cannot be empty");

		JSONObject obj = buildCommonJSONObj(Events.testRun);
		obj.put(JSONConstants.JSON_ENTITY_ADDRESS, fullyQualifiedTestMethod);
		obj.put(JSONConstants.JSON_TEST_RESULT, testResult);
		obj.put(JSONConstants.JSON_TEST_ELAPSED_TIME, elapsedTime);

		return obj;
	}

	protected JSONObject buildSnapshotJSON(String snapshotPath) {
		if (snapshotPath == null)
			throw new RuntimeException("Arguments cannot be null");
		
		if(snapshotPath.isEmpty())
			throw new RuntimeException("Arguments cannot be empty");
		
		JSONObject obj = buildCommonJSONObj(Events.snapshot);
		obj.put(JSONConstants.JSON_ENTITY_ADDRESS, snapshotPath);
		
		return obj;
	}

	public void recordRefactoring(String refactoringName, Map argumentMap) {
		ChangePersister.instance().persist(buildRefactoringEvent(Events.refactoringLaunch, refactoringName, argumentMap));
	}
	
	public void recordRefactoringUndo(String refactoringName, Map argumentsMap) {
		ChangePersister.instance().persist(buildRefactoringEvent(Events.refactoringUndo, refactoringName, argumentsMap));
	}

	public void recordRefactoringEnd(String refactoringName, Map argumentsMap) {
		ChangePersister.instance().persist(buildRefactoringEvent(Events.refactoringEnd, refactoringName, argumentsMap));
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

}

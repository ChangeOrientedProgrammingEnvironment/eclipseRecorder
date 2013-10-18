package edu.oregonstate.cope.clientRecorder;

import org.json.simple.JSONObject;

/**
 * Records text changes and test runs from the IDE. Encodes changes in JSON
 * format.
 */

public class ClientRecorder {

	private String IDE;
	
	public static final String USER_CHANGE = "user";
	public static final String REFACTORING_CHANGE = "refactoring";
	public static final String UI_EVENT = "ui-event";
	public static final String ECLIPSE_IDE = "eclipse";

	protected enum EventType {
		debugLaunch, normalLaunch, fileOpen, fileClose, textChange, testRun
	};

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

	public void recordDebugLaunch(String fullyQualifiedMainFunction) {
		ChangePersister.instance().persist(buildIDEFileEventJSON(EventType.debugLaunch, fullyQualifiedMainFunction));
	}

	public void recordNormalLaunch(String fullyQualifiedMainFunction) {
		ChangePersister.instance().persist(buildIDEFileEventJSON(EventType.normalLaunch, fullyQualifiedMainFunction));
	}

	public void recordFileOpen(String fullyQualifiedMainFunction) {
		ChangePersister.instance().persist(buildIDEFileEventJSON(EventType.fileOpen, fullyQualifiedMainFunction));
	}

	public void recordFileClose(String fullyQualifiedMainFunction) {
		ChangePersister.instance().persist(buildIDEFileEventJSON(EventType.fileClose, fullyQualifiedMainFunction));
	}

	public void recordTestRun(String fullyQualifiedTestMethod, String testResult){
		ChangePersister.instance().persist(buildTestEventJSON(fullyQualifiedTestMethod, testResult));
	}

	protected JSONObject buildCommonJSONObj(Enum eventType) {
		JSONObject obj;
		obj = new JSONObject();
		obj.put("IDE", this.getIDE());
		obj.put("eventType", eventType.toString());
		
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

		JSONObject obj = buildCommonJSONObj(EventType.textChange);
		obj.put("text", text);
		obj.put("offset", offset);
		obj.put("len", length);
		obj.put("sourceFile", sourceFile);
		obj.put("changeOrigin", changeOrigin);
		
		return obj;
	}

	protected JSONObject buildIDEFileEventJSON(Enum EventType, String fullyQualifiedMainFunction) {
		if (fullyQualifiedMainFunction == null) {
			throw new RuntimeException("Fully Qualified Main Function cannot be null");
		}
		
		JSONObject obj;
		obj = buildCommonJSONObj(EventType);
		obj.put("fullyQualifiedMain", fullyQualifiedMainFunction);
		
		return obj;
	}

	protected JSONObject buildTestEventJSON(String fullyQualifiedTestMethod, String testResult) {
		if (fullyQualifiedTestMethod == null || testResult == null)
			throw new RuntimeException("Arguments cannot be null");
		if(fullyQualifiedTestMethod.isEmpty() || testResult.isEmpty())
			throw new RuntimeException("Arguments cannot be empty");
		
		JSONObject obj = buildCommonJSONObj(EventType.testRun);
		obj.put("fullyQualifiedTestMethod", fullyQualifiedTestMethod);
		obj.put("testResult", testResult);
		
		return obj;
	}
}

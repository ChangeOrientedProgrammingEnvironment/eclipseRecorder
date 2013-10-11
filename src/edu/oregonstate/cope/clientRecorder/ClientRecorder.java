package edu.oregonstate.cope.clientRecorder;

import org.json.simple.JSONObject;

/**
 * Records text changes and test runs from the IDE. Encodes changes in JSON
 * format.
 */

public class ClientRecorder {

	/**
	 * Parameter values are not checked for consistency.
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
	public void recordTextChange(String text, int offset, int length, String sourceFile, String changeOrigin, String IDE) {
		ChangePersister.instance().persist(buildJSONTextChange(text, offset, length, sourceFile, changeOrigin, IDE));
	}

	protected JSONObject buildJSONTextChange(String text, int offset, int length, String sourceFile, String changeOrigin, String IDE) {
		if (text == null || sourceFile == null || changeOrigin == null) {
			throw new RuntimeException("Change parameters cannot be null");
		}
		if (sourceFile.isEmpty())
			throw new RuntimeException("Source File cannot be empty");
		if (changeOrigin.isEmpty())
			throw new RuntimeException("Change Origin cannot be empty");

		JSONObject obj;
		obj = new JSONObject();
		obj.put("type", "Text");
		obj.put("text", text);
		obj.put("offset", offset);
		obj.put("len", length);
		obj.put("sourceFile", sourceFile);
		obj.put("changeOrigin", changeOrigin);
		obj.put("ide", IDE);
		return obj;
	}

	public void recordIDEEvent(String event, String eventClass, String eventMethod, String eventType) {

	}

	/*
	 * public void testRun(String testMethod, String testResult, String
	 * testClass) { ChangePersister cp = new ChangePersister(); cp.persist(
	 * buildJSONTestRun(testMethod,testResult,testClass)); }
	 * 
	 * protected JSONObject buildJSONTestRun(String testMethod, String
	 * testResult, String testClass){
	 * 
	 * if(testMethod == null || testResult == null || testClass == null){ throw
	 * new RuntimeException("Test Run parameters cannot be null"); } return
	 * null; }
	 */
}

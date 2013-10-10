package edu.oregonstate.cope.clientRecorder;

import org.json.simple.JSONObject;

/**
 * Created with IntelliJ IDEA. User: michael.hilton Date: 10/4/13
 * 
 * To change this template use File | Settings | File Templates.
 */

public class ClientRecorder {

	/**
	 * Parameter values are not checked for consistancy.
	 * 
	 * @param text
	 * @param offset
	 * @param length
	 * @param sourceFile
	 * @param changeOrigin
	 * @return
	 */
	public void recordTextChange(String text, int offset, int length, String sourceFile, String changeOrigin) {
		// TODO change cp to a singleton
		ChangePersister cp = new ChangePersister();
		cp.persist(buildJSONTextChange(text, offset, length, sourceFile, changeOrigin));

	}

	protected JSONObject buildJSONTextChange(String text, int offset, int length, String sourceFile, String changeOrigin) {
		if (text == null || sourceFile == null || changeOrigin == null) {
			throw new RuntimeException("Change parameters cannot be null");
		}
		if (sourceFile.isEmpty()) {
			throw new RuntimeException("Source File cannot be empty");
		}
		if (changeOrigin.isEmpty()) {
			throw new RuntimeException("Change Origin cannot be empty");
		}
		JSONObject obj = new JSONObject();
		obj.put("type", "Text");
		obj.put("text", text);
		obj.put("offset", offset);
		obj.put("len", length);
		obj.put("sourceFile", sourceFile);
		obj.put("changeOrigin", changeOrigin);
		return obj;
	}

    public void testRun(String testMethod, String testResult, String testClass) {

        if(testMethod == null || testResult == null || testClass == null){
            throw new RuntimeException("Test Run parameters cannot be null");
        }
    }
}

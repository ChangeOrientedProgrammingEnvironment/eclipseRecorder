package edu.oregonstate.cope.clientRecorder;

import org.json.simple.JSONObject;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: michaelhilton
 * Date: 10/8/13
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientRecorderTest {
    @Test(expected=RuntimeException.class)
    public void testRecordTextChangeNull() throws Exception {
        ClientRecorder cr = new ClientRecorder();
        JSONObject result1 = cr.buildJSONTextChange(null,null,0,0,null,null);
    }

    @Test(expected=RuntimeException.class)
    public void testRecordTextChangeNoSourceFile() throws Exception {
        ClientRecorder cr = new ClientRecorder();
        JSONObject result1 = cr.buildJSONTextChange("","",0,0,"","");
    }


    @Test(expected=RuntimeException.class)
    public void testRecordTextChangeNoOrigin() throws Exception {
        ClientRecorder cr = new ClientRecorder();
        JSONObject result1 = cr.buildJSONTextChange("","",0,0,"/sampleFile","");
    }


}

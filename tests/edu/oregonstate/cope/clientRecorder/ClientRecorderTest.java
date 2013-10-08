package edu.oregonstate.cope.clientRecorder;

import org.json.simple.JSONObject;
import org.junit.*;
import static org.junit.Assert.*;

import static org.junit.Assert.assertThat;
//import static net.sf.json.test.JSONAssert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: michaelhilton
 * Date: 10/4/13
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientRecorderTest {
    @Test(expected=RuntimeException.class)
    public void testRecordTextChangeNull() throws Exception {
        ClientRecorder cr = new ClientRecorder();
        JSONObject result1 = cr.buildJSONTextChange(null,null,0,0,null,null);
    }

    @Ignore
    @org.junit.Test
    public void testRecordTextChange() throws Exception {
        ClientRecorder cr = new ClientRecorder();
        JSONObject result1 = cr.buildJSONTextChange("","hello world",0,"hello world".length(),"/sample/source/file.java","user");
        JSONObject obj=new JSONObject();
        obj.put("type","Text");
      /*  obj.put("replacedText","");
        obj.put("newText","Hello World");
        obj.put("offset",0);
        obj.put("len","Hello World".length());
        obj.put("sourceFile","/source/file.java");
        obj.put("changeOrigin","user");
        assertEquals(result1, obj);
 */
    }

}

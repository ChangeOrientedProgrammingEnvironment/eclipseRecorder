package edu.oregonstate.cope.clientRecorder;
import org.json.simple.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: michael.hilton
 * Date: 10/4/13
 *
 * To change this template use File | Settings | File Templates.
 */
public class ClientRecorder {
     public void recordTextChange(String replacedText, String newText,int offset,int length,String sourceFile, String changeOrigen){
                //create text change record in JSON
                //call changePersister.persist(json)
     }
    protected JSONObject buildJSONTextChange(String text,int offset,int length,String sourceFile, String changeOrigin){
       if(text == null || sourceFile == null || changeOrigin == null){
           throw new RuntimeException("Change parameters cannot be null");
       }
        if(sourceFile.isEmpty()){
            throw new RuntimeException("Source File cannot be empty");
        }
        if(changeOrigin.isEmpty()){
            throw new RuntimeException("Change Origin cannot be empty");
        }
        JSONObject obj=new JSONObject();
        obj.put("type","Text");
        obj.put("text",text);
        obj.put("offset",offset);
        obj.put("len",length);
        obj.put("sourceFile",sourceFile);
        obj.put("changeOrigin",changeOrigin);
        return obj;
    }
}

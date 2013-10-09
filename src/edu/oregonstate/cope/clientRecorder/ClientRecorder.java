package edu.oregonstate.cope.clientRecorder;
import org.json.simple.JSONObject;

/**
 * Records text changes and test runs from the IDE.
 * Encodes changes in JSON format. 
 * 
 * Created with IntelliJ IDEA.
 * User: michael.hilton
 * Date: 10/4/13
 *
 * To change this template use File | Settings | File Templates.
 */


public class ClientRecorder {

    /**
     * Parameter values are not checked for consistency.
     * 
     * @param text - text that is replaced or added
     * @param offset - character offset in document where change begins
     * @param length - length of change
     * @param sourceFile
     * @param changeOrigin - (user, refresh, refactoring, initial code)
     * @return
     */
     public void recordTextChange(String text,int offset,int length,String sourceFile, String changeOrigin){
        ChangePersister.instance().persist(buildJSONTextChange(text,offset,length,sourceFile,changeOrigin));
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

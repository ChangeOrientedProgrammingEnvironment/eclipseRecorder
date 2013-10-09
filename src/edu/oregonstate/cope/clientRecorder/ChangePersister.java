package edu.oregonstate.cope.clientRecorder;

import org.json.simple.JSONObject;

/**
 * Persists JSON objects to disc.
 * This class is a Singleton.
 * 
 * Created with IntelliJ IDEA.
 * User: michaelhilton
 * Date: 10/4/13
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChangePersister {
	
	private static class Instance{
		public static final ChangePersister instance = new ChangePersister();
	}
	
	private ChangePersister(){
	}
	
	public static ChangePersister instance(){
		return Instance.instance;
	}
	
    public void persist(JSONObject change){

    }
}

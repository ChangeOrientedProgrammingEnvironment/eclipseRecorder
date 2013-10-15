package edu.oregonstate.cope.clientRecorder;

import org.json.simple.*;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static junit.framework.Assert.assertEquals;

public class ChangePersisterTest {
	
	private StringWriter stringWriter;

	@Before
	public void setup() {
		stringWriter = new StringWriter();
		ChangePersister.instance().setWriter(stringWriter);

	}

	@Test
	public void testPersistInit() {
        ChangePersister.instance().init();
        JSONArray jarr = (JSONArray) JSONValue.parse(stringWriter.toString());
        assertEquals(jarr.size(),1);
    }



}

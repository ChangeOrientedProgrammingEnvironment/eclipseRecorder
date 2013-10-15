package edu.oregonstate.cope.clientRecorder;

import static org.junit.Assert.*;

import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

public class ChangePersisterTest {
	
	private StringWriter stringWriter;

	@Before
	private void setup() {
		stringWriter = new StringWriter();
		ChangePersister.instance().testSetWriter(stringWriter);
	}

	@Test
	public void testPersistEmpty() {
		fail("Not yet implemented");
	}

}

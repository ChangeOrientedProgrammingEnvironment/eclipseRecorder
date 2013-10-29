package edu.oregonstate.cope.clientRecorder;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.oregonstate.cope.tests.util.StubFileProvider;

public class RecorderPropertiesTest {

	private RecorderProperties properties;
	private StubFileProvider fileProvider;

	@Before
	public void setup() {
		fileProvider = new StubFileProvider();
		properties = new RecorderProperties(fileProvider);
	}

	@Test
	public void testAddNull() {
		properties.addProperty(null, null);
		assertNull(properties.getProperty(null), null);
		assertTrue(fileProvider.isCurrentFileEmpty());
	}
	
	@Test
	public void testAddEmpty() throws Exception {
		properties.addProperty("", "");
		assertTrue(fileProvider.isCurrentFileEmpty());
	}
	
	@Test
	public void testAddNonEmpty() throws Exception {
		properties.addProperty("k", "v");
		assertEquals("v", properties.getProperty("k"));
		assertEquals("v", new RecorderProperties(fileProvider).getProperty("k"));
	}
}

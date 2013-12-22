package edu.oregonstate.cope.clientRecorder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.oregonstate.cope.tests.util.StubFileProvider;

public class PropertiesTest {

	private Properties properties;
	private StubFileProvider fileProvider;

	@Before
	public void setup() {
		fileProvider = new StubFileProvider();
		properties = new Properties(fileProvider);
	}

	@Test
	public void initFromEmpty() throws Exception {
		testForKey(null, null);
		testForKey("", null);
	}

	@Test
	public void testAddNull() {
		properties.addProperty(null, null);
		assertNull(properties.getProperty(null), null);
		assertTrue(fileProvider.isCurrentFileEmpty());
	}

	@Test
	public void testAddOneKey() throws Exception {
		properties.addProperty("k", "v");
		testForKey("k", "v");
	}

	@Test
	public void testAddTwoKeys() throws Exception {
		properties.addProperty("k1", "v1");
		properties.addProperty("k2", "v2");

		testForKey("k1", "v1");
		testForKey("k2", "v2");
	}

	@Test
	public void testStrangeValue() throws Exception {
		String strangeKey = " strange. !@#$%^&*() key ";
		String strangeValue = " = 123 \t strange = !@#$%^&*() value/= ";

		properties.addProperty("k1", "v1");
		properties.addProperty(strangeKey, strangeValue);

		testForKey("k1", "v1");
		testForKey(strangeKey, strangeValue);
	}

	@Test(expected = AssertionError.class)
	public void testStrangeBreakingKey() throws Exception {
		String strangeKey = " strange. !@#$%^&*() key ";
		String strangeValue = " = 123 \t strange \n = !@#$%^&*() value/= ";

		properties.addProperty("k1", "v1");
		properties.addProperty(strangeKey, strangeValue);

		testForKey("k1", "v1");
		testForKey(strangeKey, strangeValue);
	}

	private void testForKey(String key, String value) {
		assertEquals(value, properties.getProperty(key));
		assertEquals(value, new Properties(fileProvider).getProperty(key));
	}
}

package adder;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AdderTest {
	
	private Adder adder;

	@Before
	public void setUp() {
		adder = new Adder();
	}

	@Test
	public void testAddZeroPlusZero() {
		int result = adder.add(0, 0);
		assertEquals(0,result);
	}
	
	@Test
	public void testAddZeroPlusTwo() {
		int result = adder.add(2,0);
		assertEquals(2,result);
	}
	
	@Test
	public void testAddTwoPlusTwo() {
		int result = adder.add(2, 2);
		assertEquals(4, result);
	}
}

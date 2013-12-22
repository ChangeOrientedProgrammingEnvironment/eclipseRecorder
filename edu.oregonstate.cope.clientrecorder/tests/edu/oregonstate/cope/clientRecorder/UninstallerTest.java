package edu.oregonstate.cope.clientRecorder;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import edu.oregonstate.cope.tests.util.StubFileProvider;

public class UninstallerTest {
	
	private Uninstaller uninstaller;
	private Properties props;
	private Calendar uninstallDate;
	private Calendar currentDate;
	
	@Before
	public void setup() {
		props = new Properties(new StubFileProvider());
		uninstaller = new Uninstaller(props);

		uninstallDate = Calendar.getInstance();
		currentDate = Calendar.getInstance();
	}
	
	private void assertInit(int timeOffset, int timeUnit, Uninstaller uninstaller) {
		Calendar timeBase = uninstaller.getUninstallDate();
		
		Calendar expectedTimeBase = Calendar.getInstance();
		expectedTimeBase.add(timeUnit, timeOffset);
		
		assertEquals(expectedTimeBase.get(Calendar.YEAR), timeBase.get(Calendar.YEAR));
		assertEquals(expectedTimeBase.get(Calendar.MONTH), timeBase.get(Calendar.MONTH));
		assertEquals(expectedTimeBase.get(Calendar.DAY_OF_MONTH), timeBase.get(Calendar.DAY_OF_MONTH));
	}

	@Test
	public void testInitNoDateSet() throws Exception {
		Calendar actual = uninstaller.getUninstallDate();
		
		Calendar expected = Calendar.getInstance();
		expected.setTime(new Date(Long.MAX_VALUE));
		
		assertEquals(expected.get(Calendar.YEAR), actual.get(Calendar.YEAR));
		assertEquals(expected.get(Calendar.MONTH), actual.get(Calendar.MONTH));
		assertEquals(expected.get(Calendar.DAY_OF_MONTH), actual.get(Calendar.DAY_OF_MONTH));
	}
	
	@Test
	public void testInitInDays() {
		uninstaller.initUninstallInDays(3);
		
		assertInit(3, Calendar.DAY_OF_MONTH, uninstaller);
	}
	
	@Test
	public void testInitInMonths() {
		uninstaller.initUninstallInMonths(3);
		
		assertInit(3, Calendar.MONTH, uninstaller);
	}

	@Test
	public void testPersistedInit() throws Exception {
		uninstaller.initUninstallInDays(3);
		
		Uninstaller newUninstaller = new Uninstaller(uninstaller.testGetProps());
		
		assertInit(3, Calendar.DAY_OF_MONTH, newUninstaller);
	}
	
	@Test
	public void testInitTwice() throws Exception {
		uninstaller.initUninstallInDays(3);
		assertInit(3, Calendar.DAY_OF_MONTH, uninstaller);
		
		uninstaller.initUninstallInDays(5);
		assertInit(5, Calendar.DAY_OF_MONTH, uninstaller);
	}
	
	@Test
	public void testShouldUninstall() throws Exception {
		uninstallDate.add(Calendar.DAY_OF_MONTH, -1);
		assertTrue(uninstaller.shouldUninstall(uninstallDate, currentDate));
	}
	
	@Test
	public void testShouldUninstallMinutes() throws Exception {
		uninstallDate.add(Calendar.MINUTE, -1);
		assertTrue(uninstaller.shouldUninstall(uninstallDate, currentDate));
	}
	
	@Test
	public void testShouldNotUninstall() throws Exception {
		uninstallDate.add(Calendar.DAY_OF_MONTH, 1);
		assertFalse(uninstaller.shouldUninstall(uninstallDate, currentDate));
	}
	
	@Test
	public void testShouldNotUninstallMinutes() throws Exception {
		uninstallDate.add(Calendar.MINUTE, 1);
		assertFalse(uninstaller.shouldUninstall(uninstallDate, currentDate));
	}
	
	@Test
	public void testShouldNotUninstallWhenNoDateSet() throws Exception {
		assertFalse(uninstaller.shouldUninstall());
	}
	
	@Test
	public void testIsUninstalledNoInit() throws Exception {
		assertFalse(uninstaller.isUninstalled());
	}
	
	@Test
	public void testSetUninstall() throws Exception {
		uninstaller.setUninstall();
		assertTrue(uninstaller.isUninstalled());
		
		assertTrue(new Uninstaller(uninstaller.testGetProps()).isUninstalled());
	}
}

package edu.oregonstate.cope.clientRecorder;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import edu.oregonstate.cope.tests.util.StubFileProvider;

public class UninstallerTest {
	
	private Uninstaller uninstaller;
	private RecorderProperties props;
	
	@Before
	public void setup() {
		props = new RecorderProperties(new StubFileProvider());
		uninstaller = new Uninstaller(props);
	}
	
	private void assertInit(int days, Uninstaller uninstaller) {
		assertEquals(days, uninstaller.getUninstallOffset());
		
		Calendar timeBase = uninstaller.getUninstallBase();
		Calendar expectedTimeBase = Calendar.getInstance();
		
		assertEquals(expectedTimeBase.get(Calendar.YEAR), timeBase.get(Calendar.YEAR));
		assertEquals(expectedTimeBase.get(Calendar.MONTH), timeBase.get(Calendar.MONTH));
		assertEquals(expectedTimeBase.get(Calendar.DAY_OF_MONTH), timeBase.get(Calendar.DAY_OF_MONTH));
	}

	@Test
	public void testInit() {
		uninstaller.initUninstall(3);
		
		assertInit(3, uninstaller);
	}


	@Test
	public void testPersistedInit() throws Exception {
		uninstaller.initUninstall(3);
		
		Uninstaller newUninstaller = new Uninstaller(uninstaller.testGetProps());
		
		assertInit(3, newUninstaller);
	}
}

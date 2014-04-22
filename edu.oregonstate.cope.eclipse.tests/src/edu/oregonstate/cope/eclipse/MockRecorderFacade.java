package edu.oregonstate.cope.eclipse;

import java.io.File;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.clientRecorder.Properties;
import edu.oregonstate.cope.clientRecorder.RecorderFacade;
import edu.oregonstate.cope.clientRecorder.RecorderFacadeInterface;
import edu.oregonstate.cope.clientRecorder.StorageManager;
import edu.oregonstate.cope.clientRecorder.Uninstaller;
import edu.oregonstate.cope.clientRecorder.util.LoggerInterface;

public class MockRecorderFacade implements RecorderFacadeInterface{

	private static StorageManager mockStorageManager = new StorageManager(){

		@Override
		public File getLocalStorage() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public File getBundleStorage() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public File getVersionedLocalStorage() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public File getVersionedBundleStorage() {
			// TODO Auto-generated method stub
			return null;
		}
		
	};

	@Override
	public ClientRecorder getClientRecorder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getWorkspaceProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getInstallationProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uninstaller getUninstaller() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LoggerInterface getLogger() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInstallationConfigFilename() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWorkspaceID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getWorkspaceIdFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFirstStart() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public StorageManager getStorageManager() {
		// TODO Auto-generated method stub
		return null;
	}

}

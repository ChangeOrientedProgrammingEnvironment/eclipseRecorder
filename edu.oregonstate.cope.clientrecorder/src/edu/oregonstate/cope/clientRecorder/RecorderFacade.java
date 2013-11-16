package edu.oregonstate.cope.clientRecorder;

import edu.oregonstate.cope.clientRecorder.fileOps.ConfigFileProvider;
import edu.oregonstate.cope.clientRecorder.fileOps.EventFilesProvider;

public class RecorderFacade {
	private ClientRecorder clientRecorder;
	private Properties properties;
	private Uninstaller uninstaller;

	public RecorderFacade initialize(String rootDirectory, String IDE) {
		initPersister(rootDirectory);
		initProperties(rootDirectory);
		initUninstaller();
		initClientRecorder(IDE);

		return this;
	}

	private void initClientRecorder(String IDE) {
		clientRecorder = new ClientRecorder();
		clientRecorder.setIDE(IDE);
	}

	private void initUninstaller() {
		uninstaller = new Uninstaller(properties);
	}

	private void initProperties(String rootDirectory) {
		ConfigFileProvider configFileProvider = new ConfigFileProvider();
		configFileProvider.setRootDirectory(rootDirectory);
		properties = new Properties(configFileProvider);
	}

	private void initPersister(String rootDirectory) {
		EventFilesProvider eventFileProvider = new EventFilesProvider();
		eventFileProvider.setRootDirectory(rootDirectory);
		ChangePersister.instance().setFileManager(eventFileProvider);
	}

	public ClientRecorder getClientRecorder() {
		return clientRecorder;
	}

	public Properties getProperties() {
		return properties;
	}

	public Uninstaller getUninstaller() {
		return uninstaller;
	}
}

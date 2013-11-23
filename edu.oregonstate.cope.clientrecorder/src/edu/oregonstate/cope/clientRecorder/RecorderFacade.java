package edu.oregonstate.cope.clientRecorder;

import edu.oregonstate.cope.clientRecorder.fileOps.EventFilesProvider;
import edu.oregonstate.cope.clientRecorder.fileOps.SimpleFileProvider;

public class RecorderFacade {
	private static final String CONFIG = "config";
	
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
		SimpleFileProvider configFileProvider = new SimpleFileProvider(CONFIG);
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

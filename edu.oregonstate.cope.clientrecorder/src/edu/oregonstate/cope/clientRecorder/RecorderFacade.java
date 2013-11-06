package edu.oregonstate.cope.clientRecorder;

import edu.oregonstate.cope.clientRecorder.fileOps.ConfigFileProvider;
import edu.oregonstate.cope.clientRecorder.fileOps.EventFilesProvider;

public class RecorderFacade {
	private ClientRecorder clientRecorder;
	private RecorderProperties recorderProperties;

	public RecorderFacade initialize(String rootDirectory, String IDE) {
		EventFilesProvider eventFileProvider = new EventFilesProvider();
		eventFileProvider.setRootDirectory(rootDirectory);
		ChangePersister.instance().setFileManager(eventFileProvider);
		
		ConfigFileProvider configFileProvider = new ConfigFileProvider();
		configFileProvider.setRootDirectory(rootDirectory);
		recorderProperties = new RecorderProperties(configFileProvider);

		clientRecorder = new ClientRecorder();
		clientRecorder.setIDE(IDE);
		
		return this;
	}

	public ClientRecorder getClientRecorder() {
		return clientRecorder;
	}

	public RecorderProperties getRecorderProperties() {
		return recorderProperties;
	}
}

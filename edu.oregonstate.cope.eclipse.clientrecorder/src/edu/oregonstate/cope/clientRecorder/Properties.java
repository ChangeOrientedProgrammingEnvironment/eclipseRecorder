package edu.oregonstate.cope.clientRecorder;

import java.util.HashMap;
import java.util.List;

import edu.oregonstate.cope.clientRecorder.fileOps.FileProvider;

/**
 * Represents configuration properties.<br>
 * These configurations can be accessed and edited at runtime via this class, or
 * manually, directly in the configuration file.<br>
 * <br>
 * 
 * Manual edits to the configuration file during runtime will not be taken into
 * account, and lost if new properties are written during runtime. <br>
 * <br>
 * 
 * Property file format:<br>
 * {@literal<properties> ::= <property>*}<br>
 * {@literal<property> ::= <key>=<value>\n}<br>
 * {@literal<key> ::= any character but \n and =}<br>
 * {@literal<value> ::= any character but \n}<br>
 * 
 * <br>
 */
public class Properties {

	private FileProvider fileProvider;
	private HashMap<String, String> properties = new HashMap<>();

	public Properties(FileProvider fileProvider) {
		this.fileProvider = fileProvider;
	}

	private void initFromFile() {
		List<String> lines = fileProvider.readAllLines();

		for (String line : lines) {
			int separatorIndex = line.indexOf("=");
			String key = line.substring(0, separatorIndex);
			String value = line.substring(separatorIndex + 1);

			properties.put(key, value);
		}
	}

	private void persist() {
		StringBuffer sb = new StringBuffer();

		for (String key : properties.keySet()) {
			sb.append(key + "=" + properties.get(key) + System.lineSeparator());
		}

		fileProvider.writeToCurrentFile(sb.toString());
	}

	public void addProperty(String key, String value) {
		if (key == null)
			return;
		
		initFromFile();
		properties.put(key, value);
		persist();
	}

	public String getProperty(String key) {
		if (key == null)
			return null;
		
		initFromFile();
		return properties.get(key);
	}
}

package edu.oregonstate.cope.clientRecorder.util;

public class ConsoleLogger implements LoggerInterface{

	@Override
	public void enableFileLogging(String parentDir, String logFileName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableConsoleLogging() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logOnlyErrors() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logEverything() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(Object obj, String message, Throwable t) {
		t.printStackTrace(System.err);
	}

	@Override
	public void error(Object obj, String message) {
		System.err.println(message);
	}

	@Override
	public void info(Object obj, String message) {
		System.out.println(message);
		
	}

	@Override
	public void warning(Object obj, String msg) {
		System.out.println(msg);
		
	}

}

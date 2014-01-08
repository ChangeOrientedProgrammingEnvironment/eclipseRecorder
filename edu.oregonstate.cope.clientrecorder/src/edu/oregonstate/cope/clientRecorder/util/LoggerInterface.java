package edu.oregonstate.cope.clientRecorder.util;

public interface LoggerInterface {

	public abstract void enableFileLogging(String parentDir, String logFileName);

	public abstract void disableConsoleLogging();

	public abstract void logOnlyErrors();

	public abstract void logEverything();

	public abstract void error(Object obj, String message, Throwable t);

	public abstract void error(Object obj, String message);

	public abstract void info(Object obj, String message);

	public abstract void warning(Object obj, String msg);

}
/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.codingtracker.operations;

import java.util.Date;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.ui.IEditorPart;

import edu.illinois.codingtracker.helpers.Configuration;
import edu.illinois.codingtracker.helpers.Debugger;

/**
 * 
 * @author Stas Negara
 * 
 */
public class Event {

	//Made public to be able to assign when the replayer is loaded/reset
	
	private String eventName = "";
	

	private long timestamp;


	public Event() {
		timestamp= System.currentTimeMillis();
	}

	public Event(long timestamp) {
		this.timestamp= timestamp;
	}

	public long getTime() {
		return timestamp;
	}

	public Date getDate() {
		return new Date(timestamp);
	}

	
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	public String getDescription() {
		return "";
	}
	
	@Override
	public String toString() {
		return eventName + " (" + timestamp + ")";
	}

}

package edu.oregonstate.cope.clientRecorder;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Responsible with knowing whether the plugin should be uninstalled or not. If
 * no uninstall date is set, the maximum date allowed by GregorianCalendar is
 * used as the uninstall date.
 * 
 * This class persists its state across runs.
 * 
 */
public class Uninstaller {

	private static final String IS_UNINSTALLED = "isUninstalled";
	//TODO choose a prettier format
	private static final DateFormat DATE_FORMATTER = DateFormat.getDateInstance(DateFormat.FULL, Locale.ROOT); 
	private static final String UNINSTALL_DATE = "uninstallDate";

	private Properties props;

	public Uninstaller(Properties props) {
		this.props = props;
		initUninstallDateIfNull();
	}

	private void initUninstallDateIfNull() {
		if (getUninstallDate() == null) {
			Calendar lastDate = Calendar.getInstance();
			lastDate.setTime(new Date(Long.MAX_VALUE));

			setUninstallDate(lastDate);
		}
	}

	/**
	 * Sets the uninstall date as the number of days relative to current date.
	 * 
	 * @param dayOffset
	 */
	public void initUninstall(Integer dayOffset) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_MONTH, dayOffset);

		setUninstallDate(now);
	}

	public Boolean shouldUninstall() {
		return shouldUninstall(getUninstallDate(), Calendar.getInstance());
	}

	protected void setUninstallDate(Calendar date) {
		props.addProperty(UNINSTALL_DATE, getDateString(date));
	}

	private String getDateString(Calendar date) {
		return DATE_FORMATTER.format(date.getTime());
	}

	protected Calendar getUninstallDate() {
		Date date = null;
		String dateString = props.getProperty(UNINSTALL_DATE);

		if (dateString == null)
			return null;

		try {
			date = DATE_FORMATTER.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			RecorderFacade.instance().getLogger().error(this, e.getMessage(), e);
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal;
	}

	protected Properties testGetProps() {
		return props;
	}

	protected boolean shouldUninstall(Calendar uninstallDate, Calendar currentDate) {
		return currentDate.after(uninstallDate);
	}

	public boolean isUninstalled() {
		String string = props.getProperty(IS_UNINSTALLED);

		if (string == null)
			return false;

		return Boolean.parseBoolean(string);
	}

	public void setUninstall() {
		props.addProperty(IS_UNINSTALLED, "true");
	}
}

package edu.oregonstate.cope.clientRecorder;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Uninstaller {

	private static final DateFormat DATE_FORMATTER = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ROOT);
	private static final String UNINSTALL_DATE = "uninstallDate";

	private RecorderProperties props;

	public Uninstaller(RecorderProperties props) {
		this.props = props;
	}

	public void initUninstall(Integer dayOffset) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_MONTH, dayOffset);

		props.addProperty(UNINSTALL_DATE, getDateString(now));
	}

	public Boolean shouldUninstall() {
		return shouldUninstall(getUninstallDate(), Calendar.getInstance());
	}

	private String getDateString(Calendar date) {

		return DATE_FORMATTER.format(date.getTime());
	}

	protected Calendar getUninstallDate() {
		Date date = null;

		try {
			date = DATE_FORMATTER.parse(props.getProperty(UNINSTALL_DATE));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal;
	}

	protected RecorderProperties testGetProps() {
		return props;
	}

	protected boolean shouldUninstall(Calendar uninstallDate, Calendar currentDate) {
		return currentDate.after(uninstallDate);
	}
}

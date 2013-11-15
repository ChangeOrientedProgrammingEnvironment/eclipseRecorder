package edu.oregonstate.cope.clientRecorder;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Uninstaller {

	private static final DateFormat DATE_FORMATTER = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ROOT);
	private static final String UNINSTALL_BASE_TIME = "uninstallBaseTime";
	private static final String UNINSTALL_DAY_OFFSET = "uninstallDayOffset";

	private RecorderProperties props;

	public Uninstaller(RecorderProperties props) {
		this.props = props;
	}

	public void initUninstall(Integer i) {
		props.addProperty(UNINSTALL_DAY_OFFSET, i.toString());
		props.addProperty(UNINSTALL_BASE_TIME, getDateString());
	}

	private String getDateString() {
		return DATE_FORMATTER.format(Calendar.getInstance().getTime());
	}

	protected Object getUninstallOffset() {
		return Integer.parseInt(props.getProperty(UNINSTALL_DAY_OFFSET));
	}

	protected Calendar getUninstallBase() {
		Date date = null;

		try {
			date = DATE_FORMATTER.parse(props.getProperty(UNINSTALL_BASE_TIME));
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
}

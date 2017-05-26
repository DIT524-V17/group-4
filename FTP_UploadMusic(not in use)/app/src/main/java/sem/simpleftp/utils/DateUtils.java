package sem.simpleftp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;

public class DateUtils {
	public static final long ONE_SECOND = 1000;
	public static final long ONE_MINUTE = 60 * ONE_SECOND;
	public static final long ONE_HOUR = 60 * ONE_MINUTE;
	public static final long ONE_DAY = 24 * ONE_HOUR;

	public static String DATETIME_PATTERN_SIMPLE = "yyyyMMdd";
	public static String DATETIME_PATTERN = "yyyyMMddHHmmss";
	public static String TIME_PATTERN = "HHmmss";

	public static final String FORMAT_TIME = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_DATE = "yyyy-MM-dd";

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.CHINA);
	public static long timeInterval = 0;

	public synchronized static Date parseDate(String text)
			throws ParseException {
		if (text == null || text.length() == 0) {
			throw new IllegalArgumentException("Date text is empty.");
		}
		return DATE_FORMAT.parse(text);
	}

	public synchronized static String formatDate(Date date) {
		return DATE_FORMAT.format(date);
	}

	public synchronized static long parseDateTime(String dateTimeString) {
		long dateTime = -1L;
		try {
			dateTime = DATE_FORMAT.parse(dateTimeString).getTime();
		} catch (ParseException e) {
			e.printStackTrace();

		}
		return dateTime;
	}

	public static long parseDateTime(String dateTimeString, String pattern) {
		long dateTime = -1L;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);
		try {
			dateTime = sdf.parse(dateTimeString).getTime();
		} catch (ParseException e) {
			e.printStackTrace();

		}
		return dateTime;
	}

	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_PATTERN,
				Locale.CHINA);
		return sdf.format(new Date(System.currentTimeMillis() + timeInterval));
	}

	public static String getCurrentFormatTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIME, Locale.CHINA);
		return sdf.format(new Date(System.currentTimeMillis() + timeInterval));
	}

	public static String getCurrentSimpleDate() {
		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_PATTERN_SIMPLE,
				Locale.CHINA);
		return sdf.format(new Date(System.currentTimeMillis() + timeInterval));
	}

	public static String getFormatedDateTime(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);
		return sdf.format(new Date(System.currentTimeMillis() + timeInterval));
	}

	public static String getFormatedDateTime(String pattern, long dateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);
		return sdf.format(new Date(dateTime + timeInterval));
	}

	/**
	 * Roll some days at the Calendar
	 * 
	 * @param c
	 *            original calendar
	 * @param days
	 * @return new calendar
	 */
	public static Calendar rollSomeDays(Calendar c, int days) {
		Date now = c.getTime();
		Calendar newCalendar = Calendar.getInstance(Locale.CHINA);
		newCalendar.setTime(new Date(now.getTime() + days * ONE_DAY));
		return newCalendar;
	}

	public static void setSystemDateTime(Context context, String formateDatetime) {
		if (formateDatetime != null
				&& formateDatetime.length() == FORMAT_TIME.length()) {
			try {
				long datetime = DateUtils.parseDateTime(formateDatetime,
						FORMAT_TIME);
				Intent intent = new Intent("android.intent.action.SET_DATETIME");
				intent.putExtra("datetime", datetime);
				context.sendBroadcast(intent);
				timeInterval = datetime - System.currentTimeMillis();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void setSystemDateTime(Context context, String date,
			String formate) {
		if (date != null && formate != null
				&& date.length() == formate.length()) {
			try {
				long datetime = DateUtils.parseDateTime(date, formate);
				Intent intent = new Intent("android.intent.action.SET_DATETIME");
				intent.putExtra("datetime", datetime);
				context.sendBroadcast(intent);
				timeInterval = datetime - System.currentTimeMillis();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Make calendar to string
	 * 
	 * @param c
	 * @param pattern
	 *            format, if null or empty use default format(yyyy-MM-dd
	 *            HH:mm:ss)
	 * @return
	 */
	public static String toString(Calendar c, String pattern) {
		if (c == null) {
			return null;
		}

		String p = pattern;

		if (StringUtils.isEmpty(pattern)) {
			p = DATETIME_PATTERN;
		}

		SimpleDateFormat df = new SimpleDateFormat(p, Locale.CHINA);

		return df.format(c.getTime());
	}

}

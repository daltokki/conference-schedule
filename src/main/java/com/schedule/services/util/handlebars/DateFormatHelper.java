package com.schedule.services.util.handlebars;

import com.github.jknack.handlebars.Options;
import pl.allegro.tech.boot.autoconfigure.handlebars.HandlebarsHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

@HandlebarsHelper
public class DateFormatHelper {
	private static final String DEFAULT_VALUE = "";
	private static final String FORMAT_DATE = "yyyy-MM-dd";

	public static CharSequence date(Date context, Options options) {
		if (context != null) {
			String format = options.param(0, FORMAT_DATE);
			return format(context, format);
		} else {
			return DEFAULT_VALUE;
		}
	}

	private static String format(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
}

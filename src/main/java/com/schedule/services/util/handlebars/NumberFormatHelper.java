package com.schedule.services.util.handlebars;

import com.github.jknack.handlebars.Options;
import pl.allegro.tech.boot.autoconfigure.handlebars.HandlebarsHelper;

import java.text.DecimalFormat;

@HandlebarsHelper
public class NumberFormatHelper {
	private static final String DEFAULT_VALUE = "0";
	private static final String FORMAT_INTEGER = "###,###";

	public static CharSequence number(Number context, Options options) {
		if (context != null) {
			String format = options.param(0, FORMAT_INTEGER);
			return format(context, format);
		} else {
			return DEFAULT_VALUE;
		}
	}

	private static String format(Number number, String format) {
		DecimalFormat decimalFormat = new DecimalFormat(format);
		return decimalFormat.format(number);
	}
}

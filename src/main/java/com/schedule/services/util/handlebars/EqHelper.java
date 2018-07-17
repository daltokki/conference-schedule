package com.schedule.services.util.handlebars;

import com.github.jknack.handlebars.Options;
import pl.allegro.tech.boot.autoconfigure.handlebars.HandlebarsHelper;

import java.io.IOException;

@HandlebarsHelper
public class EqHelper {
	public static CharSequence eq(Object context, Options options) {
		try {
			String source = (String) context;
			String target = (String) options.params[0];
			if (source != null && target != null) {
				if (source.equals(target)) {
					return options.fn();
				} else {
					return options.inverse();
				}
			} else {
				return options.inverse();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("EqHelper error.");
		}
	}
}

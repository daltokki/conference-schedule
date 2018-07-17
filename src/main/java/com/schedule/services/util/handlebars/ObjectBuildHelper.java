package com.schedule.services.util.handlebars;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Options;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import pl.allegro.tech.boot.autoconfigure.handlebars.HandlebarsHelper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@HandlebarsHelper
public class ObjectBuildHelper {
	public static CharSequence build(Object context, Options options) {
		if (context != null) {
			try {
				Map<String, Object> map = new HashMap<>();
				for (Field field : context.getClass().getDeclaredFields()) {
					field.setAccessible(true);
					map.put(field.getName(), field.get(context));
				}
				return new Handlebars.SafeString(new ObjectMapper().writeValueAsString(map));
			} catch (Exception e) {
				log.warn("JsonProcessingException cause : ", e.getMessage());
				throw new RuntimeException("buildHelper Error.");
			}
		} else {
			return Strings.EMPTY;
		}
	}
}

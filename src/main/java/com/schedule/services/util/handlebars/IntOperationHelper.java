package com.schedule.services.util.handlebars;

import com.github.jknack.handlebars.Options;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.NumberUtils;
import pl.allegro.tech.boot.autoconfigure.handlebars.HandlebarsHelper;

import java.io.IOException;

@Slf4j
@HandlebarsHelper
public class IntOperationHelper {
	public static CharSequence intop(Number leftVal, Options options) throws IOException {
		String op = (String) options.params[0];
		if (leftVal == null || options.params[1] == null) {
			return options.inverse();
		}
		Number rightVal = (Number) options.params[1];
		for (Operators each : Operators.values()) {
			if (StringUtils.equals(op, each.getOp())) {
				return each.value(leftVal, rightVal);
			}
		}
		return leftVal + op + rightVal;
	}


	@Getter
	@AllArgsConstructor
	private enum Operators {
		ADD("+") {
			@Override
			public String value(Number lv, Number rv) {
				return convertDoubleToTargetClass(lv.doubleValue() + rv.doubleValue(), lv.getClass()).toString();
			}
		},
		SUB("-") {
			@Override
			public String value(Number lv, Number rv) {
				return convertDoubleToTargetClass(lv.doubleValue() - rv.doubleValue(), lv.getClass()).toString();
			}
		},
		MUL("*") {
			@Override
			public String value(Number lv, Number rv) {
				return convertDoubleToTargetClass(lv.doubleValue() * rv.doubleValue(), lv.getClass()).toString();
			}
		},
		DIV("/") {
			@Override
			public String value(Number lv, Number rv) {
				return convertDoubleToTargetClass(lv.doubleValue() / rv.doubleValue(), lv.getClass()).toString();
			}
		},
		MOD("%") {
			@Override
			public String value(Number lv, Number rv) {
				return convertDoubleToTargetClass(lv.doubleValue() % rv.doubleValue(), lv.getClass()).toString();
			}
		};
		private String op;

		private static <T extends Number> T convertDoubleToTargetClass(double value, Class<T> clazz) {
			return NumberUtils.convertNumberToTargetClass(value, clazz);
		}

		public abstract String value(Number lv, Number rv);
	}

}

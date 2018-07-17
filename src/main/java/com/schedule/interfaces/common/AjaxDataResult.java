package com.schedule.interfaces.common;

import lombok.Getter;

@Getter
public class AjaxDataResult<T> extends AjaxResult {
	private T result;

	private AjaxDataResult(boolean success, String message, T result) {
		super(success, message);
		this.result = result;
	}

	public static <T> AjaxDataResult success(T result) {
		return new AjaxDataResult<>(true, "success", result);
	}

	public static AjaxDataResult fail(String message) {
		return new AjaxDataResult<>(false, message, null);
	}
}

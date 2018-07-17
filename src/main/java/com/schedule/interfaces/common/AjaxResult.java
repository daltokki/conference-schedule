package com.schedule.interfaces.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AjaxResult {
	private boolean success;
	private String message;
}

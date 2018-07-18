package com.schedule.interfaces.conference.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConferenceScheduleViewData {
	private String id;
	private String resourceId;
	private String start;
	private String end;
	private String title;
}

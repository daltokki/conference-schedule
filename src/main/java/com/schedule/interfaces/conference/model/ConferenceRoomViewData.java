package com.schedule.interfaces.conference.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConferenceRoomViewData {
	private String id;
	private String title;
	private String eventColor;
}

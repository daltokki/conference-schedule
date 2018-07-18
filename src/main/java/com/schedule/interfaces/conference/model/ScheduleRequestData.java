package com.schedule.interfaces.conference.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleRequestData {
	private String conferenceTitle;
	private String conferenceRoom;
	private String scheduleDate;
	private String scheduleStartTime;
	private String scheduleEndTime;
	private Integer repeatCount;
}

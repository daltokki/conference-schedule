package com.schedule.services.domain.schedule;

import lombok.Getter;

@Getter
public enum DefaultConferenceRoom {
	ROOM_A("회의실 A", ""),
	ROOM_B("회의실 B", "green"),
	ROOM_C("회의실 C", "orange"),
	ROOM_D("회의실 D", "red"),
	ROOM_E("회의실 E", "yellow");

	private String desc;
	private String color;

	DefaultConferenceRoom(String desc, String color) {
		this.desc = desc;
		this.color = color;
	}
}

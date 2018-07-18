package com.schedule.repository.value;

import lombok.Getter;

@Getter
public enum ConferenceStatus {
	NONE(true), BOOKED(false);

	ConferenceStatus(boolean bookingAvailable) {
		this.bookingAvailable = bookingAvailable;
	}

	private boolean bookingAvailable;
}

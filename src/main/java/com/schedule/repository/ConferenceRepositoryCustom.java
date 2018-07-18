package com.schedule.repository;

public interface ConferenceRepositoryCustom {
	void makeConferenceBookedStatus(Long conferenceId, String conferenceTitle, Long memberId);
}

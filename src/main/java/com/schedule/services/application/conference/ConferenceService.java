package com.schedule.services.application.conference;

import com.schedule.repository.ConferenceRepository;
import com.schedule.repository.entity.Conference;
import com.schedule.repository.entity.ConferenceRoom;
import com.schedule.repository.entity.ScheduleTime;
import com.schedule.services.application.conference.exception.ConferenceScheduleException;
import com.schedule.services.domain.schedule.RepeatConferenceScheduler;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ConferenceService {
	@Autowired
	private ConferenceRepository conferenceRepository;
	@Autowired
	private RepeatConferenceScheduler repeatConferenceScheduler;

	@Transactional
	public void createConference(ConferenceRoom conferenceRoom, ScheduleTime scheduleTime) {
		Conference conference = Conference.create(conferenceRoom, scheduleTime);
		conferenceRepository.save(conference);
	}

	@Transactional
	public void makeConferenceBooking(ConferenceRoom conferenceRoom, List<ScheduleTime> scheduleTimeList, Integer repeatCount, String conferenceTitle,
		Long memberId) {
		for (ScheduleTime scheduleTime : scheduleTimeList) {
			if (repeatCount == 0) {
				Conference conference = conferenceRepository.findByConferenceRoomEqualsAndScheduleTimeEquals(conferenceRoom, scheduleTime);
				conferenceRepository.makeConferenceBookedStatus(conference.getConferenceId(), conferenceTitle, memberId);
				continue;
			}
			List<Pair<ConferenceRoom, ScheduleTime>> repeatSchedule = repeatConferenceScheduler.run(conferenceRoom.getConferenceRoomId(),
				scheduleTime.getScheduleTimeId(), repeatCount);

			boolean bookingAvailable = repeatSchedule.stream().allMatch(pair -> isBookingAvailable(pair.getLeft(), pair.getRight()));
			if (!bookingAvailable) {
				throw new ConferenceScheduleException("반복 예약 수 만큼 예약이 불가합니다. 반복 주기를 다시 설정해주세요.");
			}
			makeRepeatBooking(repeatSchedule, conferenceTitle, memberId);
		}
	}

	private void makeRepeatBooking(List<Pair<ConferenceRoom, ScheduleTime>> repeatSchedule, String conferenceTitle, Long memberId) {
		for (Pair<ConferenceRoom, ScheduleTime> pair : repeatSchedule) {
			Conference conference = conferenceRepository.findByConferenceRoomEqualsAndScheduleTimeEquals(pair.getLeft(), pair.getRight());
			conferenceRepository.makeConferenceBookedStatus(conference.getConferenceId(), conferenceTitle, memberId);
		}
	}

	private boolean isBookingAvailable(ConferenceRoom conferenceRoom, ScheduleTime scheduleTime) {
		boolean bookingAvailable = true;
		Iterator<Conference> iterator = scheduleTime.getConferenceList().stream().filter(
			conference -> conference.getConferenceRoom().getConferenceRoomId().equals(conferenceRoom.getConferenceRoomId())
				&& conference.getStatus().isBookingAvailable()).iterator();
		if (!iterator.hasNext()) {
			bookingAvailable = false;
		}
		return bookingAvailable;
	}

	public Conference findByRoomAndTime(ConferenceRoom conferenceRoom, ScheduleTime scheduleTime) {
		return conferenceRepository.findByConferenceRoomEqualsAndScheduleTimeEquals(conferenceRoom, scheduleTime);
	}
}

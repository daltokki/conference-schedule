package com.schedule.services.application.conference;

import com.google.common.collect.Lists;
import com.schedule.interfaces.conference.model.ScheduleRequestData;
import com.schedule.repository.ConferenceScheduleRepository;
import com.schedule.repository.entity.*;
import com.schedule.services.application.conference.exception.ConferenceScheduleException;
import com.schedule.services.domain.schedule.InitialConferenceScheduleMaker;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.LongStream;

@Service
public class ConferenceScheduleService {
	@Autowired
	private ConferenceScheduleRepository conferenceScheduleRepository;
	@Autowired
	private ConferenceService conferenceService;

	@PostConstruct
	public void setUpInit() {
		InitialConferenceScheduleMaker.setConferenceScheduleService(this);
	}

	public void makeConferenceSchedule(ConferenceSchedule schedule, ScheduleRequestData scheduleRequestData, Long memberId) {
		Pair<ConferenceRoom, List<ScheduleTime>> validResult = scheduleRequestDataValidation(schedule, scheduleRequestData);

		ConferenceRoom conferenceRoom = validResult.getLeft();
		List<ScheduleTime> scheduleTimeList = validResult.getRight();

		conferenceService.makeConferenceBooking(conferenceRoom, scheduleTimeList, scheduleRequestData.getRepeatCount(),
			scheduleRequestData.getConferenceTitle(), memberId);
	}

	@Transactional
	public ConferenceSchedule getOrMakeConferenceSchedule(String scheduleDate) {
		Optional<ConferenceSchedule> scheduleOptional = conferenceScheduleRepository.findByScheduleDateEquals(scheduleDate);
		if (scheduleOptional.isPresent()) {
			return scheduleOptional.get();
		} else {
			ConferenceSchedule schedule = ConferenceSchedule.create(scheduleDate);
			schedule.setConferenceRoomList(InitialConferenceScheduleMaker.generateConferenceRoom(schedule));
			schedule.setScheduleTimeList(InitialConferenceScheduleMaker.generateScheduleTime(schedule));

			ConferenceSchedule save = conferenceScheduleRepository.save(schedule);

			for (ConferenceRoom conferenceRoom : schedule.getConferenceRoomList()) {
				for (ScheduleTime scheduleTime : schedule.getScheduleTimeList()) {
					conferenceService.createConference(conferenceRoom, scheduleTime);
				}
			}
			return save;
		}
	}

	public ConferenceSchedule getConferenceSchedule(String scheduleDate) {
		return conferenceScheduleRepository.findByScheduleDateEquals(scheduleDate).orElseThrow(
			() -> new ConferenceScheduleException("ConferenceSchedule non exists. scheduleDate:" + scheduleDate));
	}

	private Pair<ConferenceRoom, List<ScheduleTime>> scheduleRequestDataValidation(ConferenceSchedule schedule, ScheduleRequestData scheduleRequestData) {
		if (schedule == null) {
			throw new ConferenceScheduleException("회의실 예약 일정이 존재하지 않습니다. 다시 시도해주세요.");
		}
		List<ConferenceRoom> conferenceRoomList = schedule.getConferenceRoomList();
		List<ScheduleTime> scheduleTimeList = schedule.getScheduleTimeList();

		ConferenceRoom requestConferenceRoom;
		List<ScheduleTime> requestScheduleTimeList = Lists.newArrayList();

		Iterator<ConferenceRoom> conferenceRoomIterator = conferenceRoomList.stream().filter(
			conferenceRoom -> conferenceRoom.getConferenceRoomName().equals(scheduleRequestData.getConferenceRoom())).iterator();
		if (conferenceRoomIterator.hasNext()) {
			requestConferenceRoom = conferenceRoomIterator.next();
		} else {
			throw new ConferenceScheduleException("예약일자에 존재하지 않는 회의실입니다. 다시 확인해주세요.");
		}

		LocalTime startTime = toLocalTime.apply(scheduleRequestData.getScheduleStartTime());
		LocalTime endTime = toLocalTime.apply(scheduleRequestData.getScheduleEndTime());

		if (startTime.isAfter(endTime)) {
			throw new ConferenceScheduleException("예약 종료시간은 시작시간보다 이후여야 합니다.");
		}

		long minDiff = startTime.until(endTime, ChronoUnit.MINUTES) / 30;

		LongStream.range(0, minDiff).forEach(
			operand -> {
				String compareTime = startTime.plusMinutes(30 * operand).format(DateTimeFormatter.ofPattern("HHmm"));
				Iterator<ScheduleTime> scheduleTimeIterator = scheduleTimeList.stream().filter(
					scheduleTime -> scheduleTime.getScheduleTime().equals(compareTime)).iterator();
				if (scheduleTimeIterator.hasNext()) {
					requestScheduleTimeList.add(scheduleTimeIterator.next());
				}
			});

		for (ScheduleTime scheduleTime : requestScheduleTimeList) {
			Conference conference = conferenceService.findByRoomAndTime(requestConferenceRoom, scheduleTime);
			if (!conference.getStatus().isBookingAvailable()) {
				throw new ConferenceScheduleException("이미 예약이 완료되었습니다. 다른 회의실 또는 시간을 선택해주세요.");
			}
		}
		return Pair.of(requestConferenceRoom, requestScheduleTimeList);
	}

	private Function<String, LocalTime> toLocalTime = timeStr ->
		LocalTime.of(Integer.parseInt(timeStr.substring(0, 2)), Integer.parseInt(timeStr.substring(2)));
}

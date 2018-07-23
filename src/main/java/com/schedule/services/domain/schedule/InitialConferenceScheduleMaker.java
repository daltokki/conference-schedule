package com.schedule.services.domain.schedule;

import com.google.common.collect.Lists;
import com.schedule.repository.entity.ConferenceRoom;
import com.schedule.repository.entity.ConferenceSchedule;
import com.schedule.repository.entity.ScheduleTime;
import com.schedule.services.application.conference.ConferenceScheduleService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InitialConferenceScheduleMaker {
	private static ConferenceScheduleService conferenceScheduleService;

	public static void setConferenceScheduleService(ConferenceScheduleService conferenceScheduleService) {
		InitialConferenceScheduleMaker.conferenceScheduleService = conferenceScheduleService;
	}

	public static void initConferenceSchedule() {
		LocalDate startDate = LocalDate.now();
		LocalDate limitDate = LocalDate.now().plusMonths(2);
		do {
			String day = startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			conferenceScheduleService.getOrMakeConferenceSchedule(day);
			startDate = startDate.plusDays(1);
		} while (!startDate.equals(limitDate));
	}

	public static List<ConferenceRoom> generateConferenceRoom(ConferenceSchedule schedule) {
		return Arrays.stream(DefaultConferenceRoom.values()).map(
			defaultConferenceRoom -> ConferenceRoom.create(schedule, defaultConferenceRoom.name())).collect(Collectors.toList());
	}

	public static List<ScheduleTime> generateScheduleTime(ConferenceSchedule schedule) {
		List<LocalTime> localTimeList = Lists.newArrayList();
		LocalTime timeOf = LocalTime.of(9, 0);
		do {
			localTimeList.add(timeOf);
			timeOf = timeOf.plusMinutes(30);
		} while (!timeOf.equals(LocalTime.of(18, 0)));

		return localTimeList.stream().map(localTime ->
			ScheduleTime.create(schedule, localTime.format(DateTimeFormatter.ofPattern("HHmm")))).collect(Collectors.toList());
	}
}

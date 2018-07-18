package com.schedule.services.application.conference;

import com.schedule.repository.ScheduleTimeRepository;
import com.schedule.repository.entity.ConferenceSchedule;
import com.schedule.repository.entity.ScheduleTime;
import com.schedule.services.domain.schedule.RepeatConferenceScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ScheduleTimeService {
	@Autowired
	private ScheduleTimeRepository scheduleTimeRepository;
	@Autowired
	private ConferenceScheduleService conferenceScheduleService;

	public List<ScheduleTime> getScheduleTimeListByRepeat(Long startScheduleTimeId, Integer repeatCount) {
		ScheduleTime scheduleTime = scheduleTimeRepository.findById(startScheduleTimeId).orElseThrow(RuntimeException::new);
		String repeatBaseTime = scheduleTime.getScheduleTime();

		LocalDate scheduleDate = LocalDate.parse(scheduleTime.getConferenceSchedule().getScheduleDate(), DateTimeFormatter.BASIC_ISO_DATE);
		List<String> nextScheduleList = RepeatConferenceScheduler.getNextScheduleListFunction.apply(scheduleDate, repeatCount);

		List<Long> nextScheduleTimeIdList = nextScheduleList.stream().map(
			nextScheduleDay -> {
				ConferenceSchedule conferenceSchedule = conferenceScheduleService.getConferenceSchedule(nextScheduleDay);
				return conferenceSchedule.getScheduleTimeList().stream().filter(time -> time.getScheduleTime().equals(repeatBaseTime))
					.map(ScheduleTime::getScheduleTimeId).collect(Collectors.toList());
			}).flatMap(Collection::stream).collect(Collectors.toList());

		return scheduleTimeRepository.findByScheduleTimeIdIn(nextScheduleTimeIdList);
	}
}

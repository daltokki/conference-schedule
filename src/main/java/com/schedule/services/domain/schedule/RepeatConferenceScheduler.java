package com.schedule.services.domain.schedule;

import com.schedule.repository.entity.ConferenceRoom;
import com.schedule.repository.entity.ScheduleTime;
import com.schedule.services.application.conference.ConferenceRoomService;
import com.schedule.services.application.conference.ScheduleTimeService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class RepeatConferenceScheduler {
	@Autowired
	private ScheduleTimeService scheduleTimeService;
	@Autowired
	private ConferenceRoomService conferenceRoomService;

	public static BiFunction<LocalDate, Integer, List<String>> getNextScheduleListFunction = (startScheduleDate, repeat) ->
		IntStream.range(0, repeat + 1).mapToObj(
			count -> startScheduleDate.plusWeeks(count).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
		).collect(Collectors.toList());

	public List<Pair<ConferenceRoom, ScheduleTime>> run(Long startConferenceRoomId, Long startScheduleTimeId, Integer repeatCount) {
		List<ScheduleTime> scheduleTimeListByRepeat = scheduleTimeService.getScheduleTimeListByRepeat(startScheduleTimeId, repeatCount);
		List<ConferenceRoom> conferenceRoomListByRepeat = conferenceRoomService.getConferenceRoomListByRepeat(startConferenceRoomId, repeatCount);

		return IntStream.range(0, repeatCount + 1)
			.mapToObj(operand -> Pair.of(conferenceRoomListByRepeat.get(operand), scheduleTimeListByRepeat.get(operand)))
			.collect(Collectors.toList());
	}
}

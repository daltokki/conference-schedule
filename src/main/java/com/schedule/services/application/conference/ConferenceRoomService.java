package com.schedule.services.application.conference;

import com.schedule.repository.ConferenceRoomRepository;
import com.schedule.repository.entity.ConferenceRoom;
import com.schedule.repository.entity.ConferenceSchedule;
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
public class ConferenceRoomService {
	@Autowired
	private ConferenceRoomRepository conferenceRoomRepository;
	@Autowired
	private ConferenceScheduleService conferenceScheduleService;

	public List<ConferenceRoom> getConferenceRoomListByRepeat(Long startConferenceRoomId, Integer repeatCount) {
		ConferenceRoom conferenceRoom = conferenceRoomRepository.findById(startConferenceRoomId).orElseThrow(RuntimeException::new);
		String baseConferenceRoomName = conferenceRoom.getConferenceRoomName();

		LocalDate scheduleDate = LocalDate.parse(conferenceRoom.getConferenceSchedule().getScheduleDate(), DateTimeFormatter.BASIC_ISO_DATE);
		List<String> nextScheduleList = RepeatConferenceScheduler.getNextScheduleListFunction.apply(scheduleDate, repeatCount);

		List<Long> nextScheduleRoomIdList = nextScheduleList.stream().map(
			nextScheduleDay -> {
				ConferenceSchedule conferenceSchedule = conferenceScheduleService.getConferenceSchedule(nextScheduleDay);
				return conferenceSchedule.getConferenceRoomList().stream().filter(room -> room.getConferenceRoomName().equals(baseConferenceRoomName))
					.map(ConferenceRoom::getConferenceRoomId).collect(Collectors.toList());
			}).flatMap(Collection::stream).collect(Collectors.toList());

		return conferenceRoomRepository.findByConferenceRoomIdIn(nextScheduleRoomIdList);
	}
}

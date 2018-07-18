package com.schedule.services.application.conference;

import com.schedule.repository.ConferenceRepository;
import com.schedule.repository.entity.Conference;
import com.schedule.repository.entity.ConferenceRoom;
import com.schedule.repository.entity.ConferenceSchedule;
import com.schedule.repository.entity.ScheduleTime;
import com.schedule.repository.value.ConferenceStatus;
import com.schedule.services.application.conference.exception.ConferenceScheduleException;
import com.schedule.services.domain.schedule.DefaultConferenceRoom;
import com.schedule.services.domain.schedule.RepeatConferenceScheduler;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConferenceServiceTest {
	@InjectMocks
	private ConferenceService conferenceService;
	@Mock
	private ConferenceRepository conferenceRepository;
	@Mock
	private RepeatConferenceScheduler repeatConferenceScheduler;

	private ConferenceSchedule conferenceSchedule;
	private ConferenceRoom conferenceRoom;
	private ScheduleTime scheduleTime;
	private Conference conference;

	private List<Pair<ConferenceRoom, ScheduleTime>> repeatSchedule;

	@Before
	public void setUp() throws Exception {
		conferenceSchedule = new ConferenceSchedule();
		conferenceSchedule.setScheduleDate("20180718");

		conferenceRoom = new ConferenceRoom();
		conferenceRoom.setConferenceRoomId(1L);
		conferenceRoom.setConferenceRoomName(DefaultConferenceRoom.ROOM_A.name());
		conferenceRoom.setConferenceSchedule(conferenceSchedule);
		conferenceSchedule.setScheduleDate("20180718");

		scheduleTime = new ScheduleTime();
		scheduleTime.setScheduleTimeId(1L);
		scheduleTime.setScheduleTime("0900");
		scheduleTime.setConferenceSchedule(conferenceSchedule);

		conferenceSchedule.setConferenceRoomList(Lists.newArrayList(conferenceRoom));
		conferenceSchedule.setScheduleTimeList(Lists.newArrayList(scheduleTime));

		conference = new Conference();
		conference.setConferenceId(1L);
		conference.setConferenceRoom(conferenceRoom);
		conference.setScheduleTime(scheduleTime);
		conference.setStatus(ConferenceStatus.NONE);
	}

	@Test
	public void createConferenceTest() {
		Conference conference = Conference.create(conferenceRoom, scheduleTime);
		conferenceRepository.save(conference);
	}

	@Test
	public void makeConferenceBooking_IfNonRepeatTest() {
		Long memberId = 1L;
		Integer repeatCount = 0;
		String conferenceTitle = "Title";

		Mockito.when(conferenceRepository.findByConferenceRoomEqualsAndScheduleTimeEquals(Mockito.any(ConferenceRoom.class),
			Mockito.any(ScheduleTime.class))).thenReturn(conference);

		conferenceService.makeConferenceBooking(conferenceRoom, Lists.newArrayList(scheduleTime), repeatCount, conferenceTitle, memberId);
	}
	
	@Test
	public void makeConferenceBooking_IfRepeat_BookingAvailableTest() {
		Long memberId = 1L;
		Integer repeatCount = 1;
		String conferenceTitle = "Title";

		scheduleTime.setConferenceList(Lists.newArrayList(conference));
		repeatSchedule = Lists.newArrayList(Pair.of(conferenceRoom, scheduleTime));
		Mockito.when(repeatConferenceScheduler.run(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt())).thenReturn(repeatSchedule);
		Mockito.when(conferenceRepository.findByConferenceRoomEqualsAndScheduleTimeEquals(Mockito.any(ConferenceRoom.class),
			Mockito.any(ScheduleTime.class))).thenReturn(conference);

		conferenceService.makeConferenceBooking(conferenceRoom, Lists.newArrayList(scheduleTime), repeatCount, conferenceTitle, memberId);
	}

	@Test(expected = ConferenceScheduleException.class)
	public void makeConferenceBooking_IfRepeat_BookingNotAvailableTest() {
		Long memberId = 1L;
		Integer repeatCount = 1;
		String conferenceTitle = "Title";

		Conference booked_conference = new Conference();
		booked_conference.setConferenceId(1L);
		booked_conference.setConferenceRoom(conferenceRoom);
		booked_conference.setScheduleTime(scheduleTime);
		booked_conference.setStatus(ConferenceStatus.BOOKED);
		scheduleTime.setConferenceList(Lists.newArrayList(booked_conference));
		repeatSchedule = Lists.newArrayList(Pair.of(conferenceRoom, scheduleTime));

		Mockito.when(repeatConferenceScheduler.run(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt())).thenReturn(repeatSchedule);

		conferenceService.makeConferenceBooking(conferenceRoom, Lists.newArrayList(scheduleTime), repeatCount, conferenceTitle, memberId);
	}

	@Test
	public void findByRoomAndTimeTest() {
		Mockito.when(conferenceRepository.findByConferenceRoomEqualsAndScheduleTimeEquals(Mockito.any(ConferenceRoom.class),
			Mockito.any(ScheduleTime.class))).thenReturn(conference);
		Conference byRoomAndTime = conferenceService.findByRoomAndTime(conferenceRoom, scheduleTime);
		Assert.assertNotNull(byRoomAndTime);
	}
}
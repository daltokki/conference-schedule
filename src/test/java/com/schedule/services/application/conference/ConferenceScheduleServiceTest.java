package com.schedule.services.application.conference;

import com.schedule.repository.ConferenceScheduleRepository;
import com.schedule.repository.entity.Conference;
import com.schedule.repository.entity.ConferenceRoom;
import com.schedule.repository.entity.ConferenceSchedule;
import com.schedule.repository.entity.ScheduleTime;
import com.schedule.repository.value.ConferenceStatus;
import com.schedule.services.domain.schedule.DefaultConferenceRoom;
import com.schedule.services.domain.schedule.InitialConferenceScheduleMaker;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConferenceScheduleServiceTest {
	@InjectMocks
	private ConferenceScheduleService conferenceScheduleService;
	@Mock
	private ConferenceScheduleRepository conferenceScheduleRepository;
	@Mock
	private ConferenceService conferenceService;

	private ConferenceSchedule conferenceSchedule;
	private ConferenceRoom conferenceRoom;
	private ScheduleTime scheduleTime;
	private Conference conference;
	private String scheduleDate;


	@Before
	public void setUp() throws Exception {
		scheduleDate = "20180718";
		conferenceSchedule = new ConferenceSchedule();
		conferenceSchedule.setScheduleDate(scheduleDate);

		conferenceRoom = new ConferenceRoom();
		conferenceRoom.setConferenceRoomId(1L);
		conferenceRoom.setConferenceRoomName(DefaultConferenceRoom.ROOM_A.name());
		conferenceRoom.setConferenceSchedule(conferenceSchedule);

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
	public void setUpInitTest() {
		InitialConferenceScheduleMaker.setConferenceScheduleService(conferenceScheduleService);
	}

	@Test
	public void getOrMakeConferenceScheduleTest_Exists() {
		Mockito.when(conferenceScheduleRepository.findByScheduleDateEquals(Mockito.anyString())).thenReturn(Optional.of(conferenceSchedule));
		ConferenceSchedule orMakeConferenceSchedule = conferenceScheduleService.getOrMakeConferenceSchedule(scheduleDate);
		Assert.assertEquals(orMakeConferenceSchedule.getScheduleDate(), scheduleDate);
	}

	@Test
	public void getConferenceScheduleTest() {
		Mockito.when(conferenceScheduleRepository.findByScheduleDateEquals(Mockito.anyString())).thenReturn(Optional.of(conferenceSchedule));
		ConferenceSchedule conferenceSchedule = conferenceScheduleService.getConferenceSchedule(scheduleDate);
		Assert.assertNotNull(conferenceSchedule);
		Assert.assertEquals(conferenceSchedule.getScheduleDate(), scheduleDate);
	}
}
package com.schedule.services.domain.schedule;

import com.google.common.collect.Iterables;
import com.schedule.repository.entity.ConferenceRoom;
import com.schedule.repository.entity.ConferenceSchedule;
import com.schedule.repository.entity.ScheduleTime;
import com.schedule.services.application.conference.ConferenceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitialConferenceScheduleMakerTest {
	private ConferenceSchedule conferenceSchedule;

	@Before
	public void init() {
		conferenceSchedule = Mockito.any(ConferenceSchedule.class);
	}

	@Test
	public void generateConferenceRoom() {
		List<ConferenceRoom> conferenceRooms = InitialConferenceScheduleMaker.generateConferenceRoom(conferenceSchedule);
		Assert.assertEquals(conferenceRooms.size(), DefaultConferenceRoom.values().length);
	}

	@Test
	public void generateScheduleTime() {
		List<ScheduleTime> scheduleTimes = InitialConferenceScheduleMaker.generateScheduleTime(conferenceSchedule);
		Assert.assertEquals(Iterables.getLast(scheduleTimes).getScheduleTime(), "1730");
	}
}
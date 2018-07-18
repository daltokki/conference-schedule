package com.schedule.services.application.conference;

import com.schedule.repository.ScheduleTimeRepository;
import com.schedule.repository.entity.ConferenceSchedule;
import com.schedule.repository.entity.ScheduleTime;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleTimeServiceTest {
	@InjectMocks
	private ScheduleTimeService scheduleTimeService;
	@Mock
	private ScheduleTimeRepository scheduleTimeRepository;
	@Mock
	private ConferenceScheduleService conferenceScheduleService;

	private ConferenceSchedule conferenceSchedule;
	private ScheduleTime scheduleTime;

	@Before
	public void setUp() throws Exception {
		conferenceSchedule = new ConferenceSchedule();
		conferenceSchedule.setScheduleDate("20180718");
		scheduleTime = new ScheduleTime();
		scheduleTime.setScheduleTimeId(1L);
		scheduleTime.setScheduleTime("0900");
		scheduleTime.setConferenceSchedule(conferenceSchedule);
		conferenceSchedule.setScheduleTimeList(Lists.newArrayList(scheduleTime));
	}

	@Test
	public void getScheduleTimeListByRepeatTest() {
		Long scheduleTimeId = 1L;
		Integer repeatCount = 1;

		ScheduleTime scheduleTime1 = new ScheduleTime();
		scheduleTime1.setScheduleTimeId(2L);
		scheduleTime1.setScheduleTime("0930");

		Mockito.when(scheduleTimeRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(this.scheduleTime));
		Mockito.when(conferenceScheduleService.getConferenceSchedule(Mockito.anyString())).thenReturn(conferenceSchedule);
		Mockito.when(scheduleTimeRepository.findByScheduleTimeIdIn(Mockito.anyCollection()))
			.thenReturn(Lists.newArrayList(scheduleTime, scheduleTime1));

		List<ScheduleTime> scheduleTimeListByRepeat = scheduleTimeService.getScheduleTimeListByRepeat(scheduleTimeId, repeatCount);
		Assert.assertNotNull(scheduleTimeListByRepeat);
		Assert.assertEquals(scheduleTimeListByRepeat.size(), repeatCount + 1);
	}
}
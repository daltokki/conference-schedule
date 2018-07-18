package com.schedule.services.application.conference;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedule.interfaces.conference.model.ConferenceScheduleViewData;
import com.schedule.repository.ConferenceRepository;
import com.schedule.repository.entity.ConferenceSchedule;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConferenceScheduleServiceTest {
	@Autowired
	private ConferenceScheduleService conferenceScheduleService;
	@Autowired
	private ConferenceRepository conferenceRepository;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void makeConferenceSchedule() throws JsonProcessingException {
		long until = LocalTime.of(9, 0).until(LocalTime.of(9, 30), ChronoUnit.MINUTES);
		System.out.println(until);
		System.out.println(until / 30);
	}

	@Test
	public void getConferenceSchedule() {
		String toDay = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		ConferenceSchedule conferenceSchedule = conferenceScheduleService.getConferenceSchedule(toDay);
		Assert.assertNotNull(conferenceSchedule);
	}
}
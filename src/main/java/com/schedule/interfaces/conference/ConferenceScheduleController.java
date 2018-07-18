package com.schedule.interfaces.conference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.schedule.interfaces.common.AjaxDataResult;
import com.schedule.interfaces.common.AjaxResult;
import com.schedule.interfaces.conference.model.ConferenceRoomViewData;
import com.schedule.interfaces.conference.model.ConferenceScheduleViewData;
import com.schedule.interfaces.conference.model.ScheduleRequestData;
import com.schedule.repository.entity.*;
import com.schedule.services.application.conference.ConferenceScheduleService;
import com.schedule.services.application.conference.exception.ConferenceScheduleException;
import com.schedule.services.application.member.MemberService;
import com.schedule.services.domain.schedule.DefaultConferenceRoom;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/schedule")
public class ConferenceScheduleController {
	@Autowired
	private ConferenceScheduleService conferenceScheduleService;
	@Autowired
	private MemberService memberService;

	@PostMapping("/save")
	public AjaxResult saveSchedule(@RequestBody ScheduleRequestData scheduleRequestData) {
		try {
			ConferenceSchedule conferenceSchedule = conferenceScheduleService.getConferenceSchedule(scheduleRequestData.getScheduleDate());
			Member memberProfile = memberService.getMemberProfile();
			conferenceScheduleService.makeConferenceSchedule(conferenceSchedule, scheduleRequestData, memberProfile.getMemberId());
			return AjaxResult.builder().success(true).message("예약이 완료되었습니다.").build();
		} catch (ConferenceScheduleException e) {
			return AjaxResult.builder().success(false).message(e.getMessage()).build();
		} catch (Exception e) {
			return AjaxResult.builder().success(false).message("회의실 예약에 실패하였습니다.").build();
		}
	}

	@GetMapping("/list/{scheduleDate}")
	public ModelAndView getList(@PathVariable String scheduleDate) {
		ModelAndView view = new ModelAndView("/conference/schedule-status");
		view.addObject("scheduleDate", scheduleDate);
		return view;
	}

	@GetMapping("/get")
	public String getSchedule(String scheduleDate) {
		try {
			ConferenceSchedule conferenceSchedule = conferenceScheduleService.getConferenceSchedule(scheduleDate);
			if (conferenceSchedule == null) {
				return Strings.EMPTY;
			}
			List<ConferenceScheduleViewData> scheduleViewData = buildViewData(conferenceSchedule);
			return new ObjectMapper().writeValueAsString(scheduleViewData);
		} catch (Exception e) {
			throw new RuntimeException("스케줄을 가져오는데 실패하였습니다. 새로고침 해주세요.");
		}
	}

	@GetMapping("/time/get")
	public AjaxDataResult getTime(String scheduleDate) {
		try {
			ConferenceSchedule conferenceSchedule = conferenceScheduleService.getConferenceSchedule(scheduleDate);
			List<String> timeList = conferenceSchedule.getScheduleTimeList().stream().map(scheduleTime -> {
				String hour = scheduleTime.getScheduleTime().substring(0, 2);
				String minutes = scheduleTime.getScheduleTime().substring(2);
				return hour + ":" + minutes;
			}).collect(Collectors.toList());
			return AjaxDataResult.success(timeList);
		} catch (Exception e) {
			return AjaxDataResult.fail("예약 시간을 가져오는데 실패하였습니다. 새로고침 해주세요.");
		}
	}

	@GetMapping("/room/get")
	public String getConferenceRoom(String scheduleDate) {
		try {
			ConferenceSchedule conferenceSchedule = conferenceScheduleService.getConferenceSchedule(scheduleDate);
			if (conferenceSchedule == null) {
				return Strings.EMPTY;
			}
			List<ConferenceRoomViewData> roomViewData = conferenceSchedule.getConferenceRoomList().stream()
				.map(roomViewDataMapper).collect(Collectors.toList());
			return new ObjectMapper().writeValueAsString(roomViewData);
		} catch (Exception e) {
			throw new RuntimeException("회의실 정보를 가져오는데 실패하였습니다. 새로고침 해주세요.");
		}
	}

	private Function<ConferenceRoom, ConferenceRoomViewData> roomViewDataMapper = conferenceRoom -> {
		DefaultConferenceRoom defaultConferenceRoom = DefaultConferenceRoom.valueOf(conferenceRoom.getConferenceRoomName());
		return ConferenceRoomViewData.builder()
			.id(conferenceRoom.getConferenceRoomId().toString()).title(defaultConferenceRoom.getDesc())
			.eventColor(defaultConferenceRoom.getColor())
			.build();
	};

	private List<ConferenceScheduleViewData> buildViewData(ConferenceSchedule conferenceSchedule) {
		LocalDate scheduleDate = LocalDate.parse(conferenceSchedule.getScheduleDate(), DateTimeFormatter.BASIC_ISO_DATE);

		Map<String, List<Conference>> memberGroupMap = Maps.newHashMap();
		for (ConferenceRoom conferenceRoom : conferenceSchedule.getConferenceRoomList()) {
			Map<String, List<Conference>> map = conferenceRoom.getConferenceList().stream().filter(o -> o.getMemberId() != null).collect(
				Collectors.groupingBy(keyFunction));
			memberGroupMap.putAll(map);
		}

		return memberGroupMap.entrySet().stream().map(
			entry -> {
				Conference first = Iterables.getFirst(entry.getValue(), null);
				Conference last = Iterables.getLast(entry.getValue(), null);
				return ConferenceScheduleViewData.builder()
					.id(Objects.requireNonNull(first).getConferenceId().toString())
					.resourceId(first.getConferenceRoom().getConferenceRoomId().toString())
					.title(first.getConferenceTitle())
					.start(LocalDateTime.of(scheduleDate, toLocalTime.apply(first.getScheduleTime().getScheduleTime())).toString())
					.end(LocalDateTime.of(scheduleDate, toLocalTime.apply(last.getScheduleTime().getScheduleTime()).plusMinutes(30)).toString())
					.build();
			}).collect(Collectors.toList());
	}

	private Function<Conference, String> keyFunction = conference -> new StringJoiner("_").add(conference.getMemberId().toString())
		.add(conference.getConferenceTitle()).add(conference.getConferenceRoom().getConferenceRoomName()).toString();

	private Function<String, LocalTime> toLocalTime = timeStr ->
		LocalTime.of(Integer.parseInt(timeStr.substring(0, 2)), Integer.parseInt(timeStr.substring(2)));
}

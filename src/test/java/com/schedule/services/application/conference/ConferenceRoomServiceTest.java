package com.schedule.services.application.conference;

import com.schedule.repository.ConferenceRoomRepository;
import com.schedule.repository.entity.ConferenceRoom;
import com.schedule.repository.entity.ConferenceSchedule;
import com.schedule.services.domain.schedule.DefaultConferenceRoom;
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

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConferenceRoomServiceTest {
	@InjectMocks
	private ConferenceRoomService conferenceRoomService;
	@Mock
	private ConferenceRoomRepository conferenceRoomRepository;
	@Mock
	private ConferenceScheduleService conferenceScheduleService;

	private ConferenceSchedule conferenceSchedule;
	private ConferenceRoom conferenceRoom;

	@Before
	public void setUp() throws Exception {
		conferenceSchedule = new ConferenceSchedule();
		conferenceSchedule.setScheduleDate("20180718");
		conferenceRoom = new ConferenceRoom();
		conferenceRoom.setConferenceRoomId(1L);
		conferenceRoom.setConferenceRoomName(DefaultConferenceRoom.ROOM_A.name());
		conferenceRoom.setConferenceSchedule(conferenceSchedule);
		conferenceSchedule.setConferenceRoomList(Lists.newArrayList(conferenceRoom));
	}

	@Test
	public void getConferenceRoomListByRepeatTest() {
		Long conferenceRoomId = 1L;
		Integer repeatCount = 1;

		ConferenceRoom conferenceRoom1 = new ConferenceRoom();
		conferenceRoom1.setConferenceRoomId(2L);
		conferenceRoom1.setConferenceRoomName(DefaultConferenceRoom.ROOM_B.name());

		Mockito.when(conferenceRoomRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(conferenceRoom));
		Mockito.when(conferenceScheduleService.getConferenceSchedule(Mockito.anyString())).thenReturn(conferenceSchedule);
		Mockito.when(conferenceRoomRepository.findByConferenceRoomIdIn(Mockito.anyCollection()))
			.thenReturn(Lists.newArrayList(conferenceRoom, conferenceRoom1));

		List<ConferenceRoom> conferenceRoomListByRepeat = conferenceRoomService.getConferenceRoomListByRepeat(conferenceRoomId, repeatCount);
		Assert.assertNotNull(conferenceRoomListByRepeat);
		Assert.assertEquals(conferenceRoomListByRepeat.size(), repeatCount + 1);
	}
}
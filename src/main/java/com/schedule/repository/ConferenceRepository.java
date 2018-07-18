package com.schedule.repository;

import com.schedule.repository.entity.Conference;
import com.schedule.repository.entity.ConferenceRoom;
import com.schedule.repository.entity.ScheduleTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long>, ConferenceRepositoryCustom {
	Conference findByConferenceRoomEqualsAndScheduleTimeEquals(ConferenceRoom conferenceRoom, ScheduleTime scheduleTime);
}

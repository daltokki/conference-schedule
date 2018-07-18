package com.schedule.repository;

import com.schedule.repository.entity.ConferenceRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoom, Long> {
	List<ConferenceRoom> findByConferenceRoomIdIn(Collection<Long> conferenceRoomId);
}

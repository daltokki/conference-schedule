package com.schedule.repository;

import com.schedule.repository.entity.ConferenceSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConferenceScheduleRepository extends JpaRepository<ConferenceSchedule, Long> {
	Optional<ConferenceSchedule> findByScheduleDateEquals(String scheduleDate);
}

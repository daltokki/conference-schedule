package com.schedule.repository;

import com.schedule.repository.entity.ScheduleTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ScheduleTimeRepository extends JpaRepository<ScheduleTime, Long> {
	List<ScheduleTime> findByScheduleTimeIdIn(Collection<Long> scheduleTimeId);
}

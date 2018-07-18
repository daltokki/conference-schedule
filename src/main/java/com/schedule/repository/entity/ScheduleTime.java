package com.schedule.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@ToString(exclude = "conferenceSchedule")
public class ScheduleTime {
	@Id
	@GeneratedValue
	@Column
	private Long scheduleTimeId;

	@ManyToOne(targetEntity=ConferenceSchedule.class, fetch=FetchType.LAZY)
	@JoinColumn(name = "conferenceScheduleId", nullable = false)
	private ConferenceSchedule conferenceSchedule;

	@Column
	private String scheduleTime;

	@CreationTimestamp
	@Column
	private Date createdAt;

	@UpdateTimestamp
	@Column
	private Date modifiedAt;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "scheduleTime")
	private List<Conference> conferenceList;

	public ScheduleTime() {}

	private ScheduleTime(ConferenceSchedule conferenceSchedule, String scheduleTime) {
		this.conferenceSchedule = conferenceSchedule;
		this.scheduleTime = scheduleTime;
	}

	public static ScheduleTime create(ConferenceSchedule conferenceSchedule, String scheduleTime) {
		return new ScheduleTime(conferenceSchedule, scheduleTime);
	}
}

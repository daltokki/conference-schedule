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
@ToString
public class ConferenceSchedule {
	@Id
	@GeneratedValue
	@Column
	private Long conferenceScheduleId;

	@Column
	private String scheduleDate;

	@CreationTimestamp
	@Column
	private Date createdAt;

	@UpdateTimestamp
	@Column
	private Date modifiedAt;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "conferenceSchedule", cascade = CascadeType.ALL)
	private List<ConferenceRoom> conferenceRoomList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "conferenceSchedule", cascade = CascadeType.ALL)
	private List<ScheduleTime> scheduleTimeList;

	public ConferenceSchedule() {}

	private ConferenceSchedule(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public static ConferenceSchedule create(String scheduleDate) {
		return new ConferenceSchedule(scheduleDate);
	}
}

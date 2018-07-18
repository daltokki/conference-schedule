package com.schedule.repository.entity;

import com.schedule.repository.value.ConferenceStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@ToString(exclude = {"conferenceRoom", "scheduleTime"})
public class Conference {
	@Id
	@GeneratedValue
	@Column
	private Long conferenceId;

	@ManyToOne(targetEntity=ConferenceRoom.class, fetch=FetchType.LAZY)
	@JoinColumn(name = "conferenceRoomId", nullable = false)
	private ConferenceRoom conferenceRoom;

	@ManyToOne(targetEntity=ScheduleTime.class, fetch=FetchType.LAZY)
	@JoinColumn(name = "scheduleTimeId", nullable = false)
	private ScheduleTime scheduleTime;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ConferenceStatus status;

	@Column
	private String conferenceTitle;

	@Column
	private Long memberId;

	@CreationTimestamp
	@Column
	private Date createdAt;

	@UpdateTimestamp
	@Column
	private Date modifiedAt;

	public Conference() {}

	public Conference(ConferenceRoom conferenceRoom, ScheduleTime scheduleTime) {
		this.conferenceRoom = conferenceRoom;
		this.scheduleTime = scheduleTime;
		this.status = ConferenceStatus.NONE;
	}

	public static Conference create(ConferenceRoom conferenceRoom, ScheduleTime scheduleTime) {
		return new Conference(conferenceRoom, scheduleTime);
	}
}

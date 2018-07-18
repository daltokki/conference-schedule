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
public class ConferenceRoom {
	@Id
	@GeneratedValue
	@Column
	private Long conferenceRoomId;

	@ManyToOne(targetEntity=ConferenceSchedule.class, fetch=FetchType.LAZY)
	@JoinColumn(name = "conferenceScheduleId", nullable = false)
	private ConferenceSchedule conferenceSchedule;

	@Column
	private String conferenceRoomName;

	@CreationTimestamp
	@Column
	private Date createdAt;

	@UpdateTimestamp
	@Column
	private Date modifiedAt;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "conferenceRoom")
	private List<Conference> conferenceList;

	public ConferenceRoom() {}

	private ConferenceRoom(ConferenceSchedule conferenceSchedule, String conferenceRoomName) {
		this.conferenceSchedule = conferenceSchedule;
		this.conferenceRoomName = conferenceRoomName;
	}

	public static ConferenceRoom create(ConferenceSchedule conferenceSchedule, String conferenceRoomName) {
		return new ConferenceRoom(conferenceSchedule, conferenceRoomName);
	}
}

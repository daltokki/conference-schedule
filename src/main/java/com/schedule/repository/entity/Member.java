package com.schedule.repository.entity;

import com.schedule.repository.value.RoleType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Member {
	@Id
	@GeneratedValue
	@Column(name = "member_id")
	private Long memberId;

	@Column(name = "email")
	private String email;

	@Column
	private String password;

	@Column
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private RoleType role;

	@CreationTimestamp
	@Column(name = "created_at")
	private Date createdAt;

	@UpdateTimestamp
	@Column(name = "modified_at")
	private Date modifiedAt;

	public Member() {}

	public Member(String email, String password, String name) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.role = RoleType.ROLE_ACTIVE_MEMBER;
	}

	public static Member create(String email, String password, String name) {
		return new Member(email, password, name);
	}

	public void updatePassword(String password) {
		this.password = password;
	}
}

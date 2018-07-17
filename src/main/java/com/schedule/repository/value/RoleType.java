package com.schedule.repository.value;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
	ROLE_ACTIVE_MEMBER("member"), ROLE_INACTIVE_MEMBER("non-member"), ROLE_ANONYMOUS("guest");

	private String desc;
}

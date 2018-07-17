package com.schedule.services.domain.security;

import com.google.common.collect.Lists;
import com.schedule.repository.entity.Member;
import com.schedule.repository.value.RoleType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class SecurityMember extends User {
	public SecurityMember(Member member) {
		super(member.getEmail(), member.getPassword(), makeGrantedAuthority(member.getRole()));
	}

	private static List<GrantedAuthority> makeGrantedAuthority(RoleType role){
		return Lists.newArrayList(new SimpleGrantedAuthority(role.name()));
	}

	public static Optional<UserDetails> getUserDetailsOptional() {
		try {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return Optional.of(userDetails);
		} catch (Exception e) {
			return Optional.empty();
		}
	}
}

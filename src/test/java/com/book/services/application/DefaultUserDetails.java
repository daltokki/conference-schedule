package com.book.services.application;

import com.book.repository.value.RoleType;
import com.google.common.collect.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class DefaultUserDetails {
	public static UserDetails get() {
		return new UserDetails() {
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return Lists.newArrayList(new SimpleGrantedAuthority(RoleType.ROLE_ACTIVE_MEMBER.name()));
			}

			@Override
			public String getPassword() {
				return null;
			}

			@Override
			public String getUsername() {
				return "example@gmail.com";
			}

			@Override
			public boolean isAccountNonExpired() {
				return false;
			}

			@Override
			public boolean isAccountNonLocked() {
				return false;
			}

			@Override
			public boolean isCredentialsNonExpired() {
				return false;
			}

			@Override
			public boolean isEnabled() {
				return false;
			}
		};
	}
}

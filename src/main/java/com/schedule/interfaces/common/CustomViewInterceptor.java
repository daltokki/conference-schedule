package com.schedule.interfaces.common;

import com.schedule.services.domain.security.SecurityMember;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class CustomViewInterceptor extends HandlerInterceptorAdapter {
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
		if (modelAndView != null) {
			Optional<UserDetails> userOptional = SecurityMember.getUserDetailsOptional();
			modelAndView.addObject("needLogin", !userOptional.isPresent());
			modelAndView.addObject("username", userOptional.map(UserDetails::getUsername).orElse("guest"));
		}
	}
}

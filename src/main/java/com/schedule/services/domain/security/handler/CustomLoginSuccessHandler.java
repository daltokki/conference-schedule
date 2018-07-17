package com.schedule.services.domain.security.handler;

import com.google.common.base.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	public CustomLoginSuccessHandler(String defaultTargetUrl) {
		setDefaultTargetUrl(defaultTargetUrl);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
		throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session != null) {
			String redirectUrl = (String) session.getAttribute("prevPage");
			if (!Strings.isNullOrEmpty(redirectUrl) && !redirectUrl.contains("login")) {
				session.removeAttribute("prevPage");
				getRedirectStrategy().sendRedirect(request, response, redirectUrl);
				return;
			}
		}
		super.onAuthenticationSuccess(request, response, authentication);
	}
}

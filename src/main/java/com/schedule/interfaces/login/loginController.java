package com.schedule.interfaces.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class loginController {
	@GetMapping("/login")
	public String login(Model model, boolean error, boolean logout, HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		request.getSession().setAttribute("prevPage", referer);
		model.addAttribute("error", error);
		model.addAttribute("logout", logout);
		return "login";
	}

}

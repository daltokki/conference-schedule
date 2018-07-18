package com.schedule.interfaces;

import com.schedule.services.domain.schedule.DefaultConferenceRoom;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	@GetMapping({"/", "/home"})
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("/conference/schedule-form");
		modelAndView.addObject("conferenceRoomList", DefaultConferenceRoom.values());
		return modelAndView;
	}
}

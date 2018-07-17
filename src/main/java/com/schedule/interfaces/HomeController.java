package com.schedule.interfaces;

import com.schedule.services.application.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	@Autowired
	private CategoryService categoryService;

	@GetMapping({"/", "/home"})
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("/book/search-main");
		return modelAndView;
	}
}

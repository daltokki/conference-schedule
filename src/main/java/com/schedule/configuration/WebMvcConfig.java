package com.schedule.configuration;

import com.schedule.interfaces.common.CustomViewInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/login/register").setViewName("/login/register");
		registry.addViewController("/login/forgot-password").setViewName("/login/forgot-password");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(customViewInterceptor());
	}

	@Bean
	public CustomViewInterceptor customViewInterceptor() {
		return new CustomViewInterceptor();
	}
}

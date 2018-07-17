package com.schedule.services.application.member.exception;

public class UnMatchedPasswordException extends RuntimeException {
	public UnMatchedPasswordException(String message) {
		super(message);
	}
}

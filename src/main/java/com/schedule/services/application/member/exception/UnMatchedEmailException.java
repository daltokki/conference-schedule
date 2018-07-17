package com.schedule.services.application.member.exception;

public class UnMatchedEmailException extends RuntimeException {
	public UnMatchedEmailException(String message) {
		super(message);
	}
}

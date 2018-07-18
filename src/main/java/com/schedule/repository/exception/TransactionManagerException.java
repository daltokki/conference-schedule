package com.schedule.repository.exception;

public class TransactionManagerException extends RuntimeException{
	public TransactionManagerException(String message, Exception e) {
		super(message);
	}
}

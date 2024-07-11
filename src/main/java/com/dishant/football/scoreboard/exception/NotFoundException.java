package com.dishant.football.scoreboard.exception;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7871275463761095366L;

	public NotFoundException() {
		this("Not Found");
	}

	public NotFoundException(String message) {
		this(message, null);
	}

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}

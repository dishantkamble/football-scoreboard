package com.dishant.football.scoreboard.exception;

public class ForbiddenException extends RuntimeException {

	private static final long serialVersionUID = -5669516167405524762L;

	public ForbiddenException() {
		this("Forbidden");
	}

	public ForbiddenException(String message) {
		this(message, null);
	}

	public ForbiddenException(String message, Throwable cause) {
		super(message, cause);
	}
}

package edu.virginia.sde.reviews;

public class LogInFailedException extends RuntimeException {
    public LogInFailedException(String message) {
        super(message);
    }
}

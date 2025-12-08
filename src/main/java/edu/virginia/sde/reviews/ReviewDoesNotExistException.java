package edu.virginia.sde.reviews;

public class ReviewDoesNotExistException extends Exception {
    public ReviewDoesNotExistException(String message) {
        super(message);
    }
}

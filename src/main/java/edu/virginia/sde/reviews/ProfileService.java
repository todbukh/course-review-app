package edu.virginia.sde.reviews;

/**
 * Handles business logic for the Log-In scene
 */
public class ProfileService {
    /**
     * Attempts to log into a user profile
     * @param username the username
     * @param password the password
     * @return A Profile if the username and password correspond to a Profile in the profiles database
     * @throws LogInFailedException if either the username or password do not match records
     */
    public Profile login(String username, String password) throws LogInFailedException {
        Profile profile = new Profile(username, password);
        if (Profile.profileExists(profile)) return profile;
        else throw new LogInFailedException("Log-in Failed: Invalid username or password.");
    }
    /**
     * Creates a new profile with a specified username and password.
     * @param username must be unique
     * @param password must be at least 8 characters in length.
     * @throws RuntimeException if username exists
     * @throws IllegalArgumentException if password is invalid.
     */
    public void createProfile(String username, String password) {
        // FIXME: need a method to check for username in DB
        // if username exists throw a runtime exception
        validatePassword(password);
        Profile profile = new Profile(username, password);
        Profile.insertProfile(profile);
    }
    /**
     * Checks if a password is valid.
     * @param password the password to be validated
     * @throws IllegalArgumentException if password is invalid.
     */
    private void validatePassword(String password) {
        if (password.length() < 8) throw new IllegalArgumentException("Invalid password: password must be at least 8 characters in length.");
        // FIXME: ask about any other password constraints
    }
}

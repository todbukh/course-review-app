package edu.virginia.sde.reviews;

/**
 * Handles business logic for the Log-In scene
 */
public class ProfileService {
    public Profile login(String username, String password) {
        Profile profile = new Profile(username, password);
        if (Profile.profileExists(profile)) return profile;
        else throw new LogInFailedException("Log-in Failed: Invalid username or password.");
    }
    /**
     * Creates a new profile with a specified username and password.
     * @param username
     * @param password must be at least 8 characters in length.
     * @throws RuntimeException if username exists or password is invalid.
     */
    public void createProfile(String username, String password) {
        // FIXME: need a method to check for username in DB
        // if username exists throw an exception
        validatePassword(password);
        Profile profile = new Profile(username, password);
        Profile.insertProfile(profile);
    }
    /**
     * Checks if a password is valid.
     * @param password the password to be validated
     * @throws RuntimeException if password is invalid.
     */
    private void validatePassword(String password) {
        if (password.length() < 8) throw new RuntimeException("Invalid password: password must be at least 8 characters in length.");
        // FIXME: ask about any other password constraints
    }
}

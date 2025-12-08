package edu.virginia.sde.reviews;

/**
 * Handles business logic for the Log-In scene
 */
public class ProfileService {
    public enum ProfileCreateResult {
        SUCCESS,
        FAILED_PASSWORD_TOO_SHORT,
        FAILED_USERNAME_TAKEN
    }
    /**
     * Attempts to log into a user profile
     * @param username the username
     * @param password the password
     * @return A Profile if the username and password correspond to a Profile in the profiles database
     * @throws LogInFailedException if either the username or password do not match records
     */
    public Profile login(String username, String password) throws LogInFailedException {
        // FIXME: need a DB method to find and return a Profile object given a username AND password, null if not found.
        Profile loggedProfile = null; // = Profile.getProfileByCredentials(username, password)
        return loggedProfile;
    }
    /**
     * Creates a new profile with a specified username and password.
     * @param username must be unique
     * @param password must be at least 8 characters in length.
     * @return {@code true} if profile was successfully added, or {@code false} if username is already taken
     * @throws IllegalArgumentException if password is invalid.
     */
    public ProfileCreateResult createProfile(String username, String password) {
        // FIXME: need a boolean method to check if username exists in DB
        // if username exists return ProfileCreateResult.FAILED_USERNAME_TAKEN
        if (password.length() < 8) return ProfileCreateResult.FAILED_PASSWORD_TOO_SHORT;
        Profile profile = new Profile(username, password);
        Profile.insertProfile(profile);
        return ProfileCreateResult.SUCCESS;
    }
    /**
     * Checks if a password is valid.
     * @param password the password to be validated
     * @throws IllegalArgumentException if password is invalid.
     */
    private void validatePassword(String password) {
        if (password.length() < 8) throw new IllegalArgumentException("Invalid password: password must be at least 8 characters in length.");
        // FIXME: ask TA about any other password constraints
    }
}

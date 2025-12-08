package edu.virginia.sde.reviews;

/**
 * Handles business logic for the Log-In scene.
 */
public class ProfileService {
    /**
     * Possible outcomes when attempting to create a new user profile
     *
     * @author Todd Burged
     */
    public enum ProfileCreateResult {
        /**
         * Profile successfully created
         */
        SUCCESS,
        /** Submitted password was less than 8 characters. */
        FAILED_PASSWORD_TOO_SHORT,
        /** Requested username is already registered in the database. */
        FAILED_USERNAME_TAKEN
    }
    /**
     * Attempts to log into a user profile
     * @param username the username
     * @param password the password
     * @return The {@link Profile} if login succeeds, or null if credentials don't match
     */
    public Profile login(String username, String password) {
        // FIXME: need a DB method to find and return a Profile object given a username AND password, null if not found.
        Profile loggedProfile = null; // = Profile.getProfileByCredentials(username, password)
        return loggedProfile;
    }
    /**
     * Attempts to create a new profile with a specified username and password.
     * Checks if the username is unique and if the password is at least 8 characters.
     *
     * @param username must be unique.
     * @param password must be at least 8 characters in length.
     * @return A {@link ProfileCreateResult} status indicating success or the specific reason for failure.
     */
    public ProfileCreateResult createProfile(String username, String password) {
        // FIXME: need a boolean method to check if username exists in DB
        // if username exists return ProfileCreateResult.FAILED_USERNAME_TAKEN
        if (password.length() < 8) return ProfileCreateResult.FAILED_PASSWORD_TOO_SHORT;
        Profile profile = new Profile(username, password);
        Profile.insertProfile(profile);
        return ProfileCreateResult.SUCCESS;
    }
}

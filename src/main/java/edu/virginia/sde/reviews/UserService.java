package edu.virginia.sde.reviews;

/**
 * Handles business logic for the Log-In scene.
 * @author Todd Burged
 */
public class UserService {
    /**
     * Possible outcomes when attempting to create a new user profile
     */
    public enum UserCreateResult {
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
     * @return The {@link User} if login succeeds, or null if credentials don't match
     * @see User#getUser(User)
     */
    public User login(String username, String password) {
        return User.getUser(new User(username, password));
    }
    /**
     * Attempts to create a new profile with a specified username and password.
     * Checks if the username is unique and if the password is at least 8 characters.
     *
     * @param username must be unique.
     * @param password must be at least 8 characters in length.
     * @return A {@link UserCreateResult} status indicating success or the specific reason for failure.
     */
    public UserCreateResult createUser(String username, String password) {
        if(User.usernameExists(username)) return UserCreateResult.FAILED_USERNAME_TAKEN;
        if (password.length() < 8) return UserCreateResult.FAILED_PASSWORD_TOO_SHORT;
        User user = new User(username, password);
        User.insertUser(user);
        return UserCreateResult.SUCCESS;
    }
}

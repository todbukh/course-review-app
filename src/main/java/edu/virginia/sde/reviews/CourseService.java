package edu.virginia.sde.reviews;
import java.util.*;
/**
 * Handles business logic for Course Search Scene
 */
public class CourseService {
    /**
     * Retrieves all courses currently in the catalog
     * @return a List of all courses in the database
     * @see Course#getCourseList()
     */
    public List<Course> getAllCourses() {
        return Course.getCourseList();
    }

    /**
     * Adds a course with a user-specified subject mnemonic, course number, and course title, with the following constraints:
     * <ul>
     *      <li> <b>Subject mnemonic</b>: strictly 2-4 letters</li>
     *      <li> <b>Course number</b>: strictly 4 digits</li>
     *      <li> <b>Course title</b>: between 1 and 50 characters</li>
     * </ul>
     * If any constraint is violated, an exception is thrown.
     * @param subject a 2-4 letter subject mnemonic
     * @param courseNumberStr a 4-digit course number
     * @param title a 1-50 character course title
     * @throws IllegalArgumentException if input is invalid.
     * @return {@code true} if course was successfully added, and {@code false} if course already exists
     */
    public boolean addCourse(String subject, String courseNumberStr, String title) {
        validateSubjectMnemonic(subject);
        validateCourseNumberStr(courseNumberStr);
        validateTitle(title);
        Course newCourse = new Course(subject.toUpperCase(), Integer.parseInt(courseNumberStr), title);
        if (Course.courseExists(newCourse)) return false;
        Course.insertCourse(newCourse);
        return true;
    }

    /**
     * Searches for courses that match a specified subject mnemonic, course number, and/or titleSubstring substring.
     * <i>Example</i>: you can search for CS courses that contain the substring "intro" (case-insensitive)
     * by calling {@code searchCourses("CS", null, "Intro")}.
     * @param subject subject mnemonic query
     * @param courseNumberStr course Number query
     * @param titleSubstring substring to query from course titles
     * @return a List of courses that match the specified criteria
     */
    public List<Course> searchCourses(String subject, String courseNumberStr, String titleSubstring ) {
        List<Course> allCourses = Course.getCourseList();
        return allCourses.stream()
                .filter(c -> {
                    if (subject == null || subject.isBlank()) return true;
                    return (c.getSubject().equalsIgnoreCase(subject.strip()));
                })
                .filter(c -> {
                    if (courseNumberStr == null || courseNumberStr.isBlank()) return true;
                    try {
                        int courseNumber = Integer.parseInt(courseNumberStr.strip());
                        return (c.getCourseNumber() == courseNumber);
                    }
                    catch (NumberFormatException e) {
                        return false;
                    }
                })
                .filter(c -> {
                        if (titleSubstring == null || titleSubstring.isBlank()) return true;
                        return c.getCourseName().toLowerCase().contains(titleSubstring.strip().toLowerCase());
                })
                .toList();
    }
    private void validateTitle(String title) {
        if (title == null) throw new IllegalArgumentException("The course title cannot be null.");
        title = title.strip();
        if (title.isEmpty()) throw new IllegalArgumentException("Course title cannot be empty.");
        if (title.length() > 50) throw new IllegalArgumentException("The course title cannot be longer than 50 characters.");
    }
    private void validateSubjectMnemonic(String subject) {
        if (subject == null) throw new IllegalArgumentException("Subject mnemonic cannot be null.");
        subject = subject.strip();
        if (subject.isEmpty()) throw new IllegalArgumentException("Subject mnemonic cannot be empty.");
        if (!subject.matches("[a-zA-Z]+")) throw new IllegalArgumentException("Invalid subject mnemonic: Only English letters allowed.");
        if (subject.length() < 2 || subject.length() > 4) throw new IllegalArgumentException("Invalid subject mnemonic: length must be between 2 and 4 characters.");
    }
    private void validateCourseNumberStr(String courseNumberStr) {
        if (courseNumberStr == null) throw new IllegalArgumentException("Course number cannot be null.");
        courseNumberStr = courseNumberStr.strip();
        if (courseNumberStr.isEmpty()) throw new IllegalArgumentException("Course number cannot be empty.");
        if (courseNumberStr.length() != 4) throw new IllegalArgumentException("Course number must be exactly 4 digits.");
        for (int i = 0; i < courseNumberStr.length(); i++) {
            if (!Character.isDigit(courseNumberStr.charAt(i))) throw new IllegalArgumentException("Only numeric characters are allowed for course number.");
        }
    }
}

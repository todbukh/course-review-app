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
    public static enum CourseAddResult {
        SUCCESS,
        FAILED_EMPTY_TITLE,
        FAILED_TITLE_TOO_LONG,
        FAILED_EMPTY_SUBJECT,
        FAILED_INVALID_SUBJECT_CHARS,
        FAILED_INVALID_SUBJECT_LENGTH,
        FAILED_EMPTY_COURSE_NUMBER,
        FAILED_INVALID_COURSE_NUMBER_CHARS,
        FAILED_INVALID_COURSE_NUMBER_LENGTH,
        FAILED_COURSE_ALREADY_EXISTS
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
    public CourseAddResult addCourse(String subject, String courseNumberStr, String title) {
        title = (title == null) ? "" : title.strip();
        subject = (subject == null) ? "" : subject.strip();
        courseNumberStr = (courseNumberStr == null) ? "" : courseNumberStr.strip();

        // validate title
        if (title.isEmpty()) return CourseAddResult.FAILED_EMPTY_TITLE;
        if (title.length() > 50) return CourseAddResult.FAILED_TITLE_TOO_LONG;

        //validate subject
        if (subject.isEmpty()) return CourseAddResult.FAILED_EMPTY_SUBJECT;
        if (!subject.matches("[a-zA-Z]+")) return CourseAddResult.FAILED_INVALID_SUBJECT_CHARS;
        if (subject.length() < 2 || subject.length() > 4) return CourseAddResult.FAILED_INVALID_SUBJECT_LENGTH;

        // validate course number
        if (courseNumberStr.isEmpty()) return CourseAddResult.FAILED_EMPTY_COURSE_NUMBER;
        if (courseNumberStr.length() != 4) return CourseAddResult.FAILED_INVALID_COURSE_NUMBER_LENGTH;
        for (int i = 0; i < courseNumberStr.length(); i++) {
            if (!Character.isDigit(courseNumberStr.charAt(i))) return CourseAddResult.FAILED_INVALID_COURSE_NUMBER_CHARS;
        }

        // check DB for course
        Course newCourse = new Course(subject.toUpperCase(), Integer.parseInt(courseNumberStr), title);
        if (Course.courseExists(newCourse)) return CourseAddResult.FAILED_COURSE_ALREADY_EXISTS;

        // add the course
        Course.insertCourse(newCourse);
        return CourseAddResult.SUCCESS;
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
}

package edu.virginia.sde.reviews;
import java.util.*;
/**
 * Handles business logic for Course Search Scene
 */
public class CourseService {
    /**
     * Possible outcomes of attempting to add a new course.
     *
     * @author Todd Burged
     */
    public enum CourseAddResult {
        /** Course was added successfully */
        SUCCESS,
        /** Course title is empty or null */
        FAILED_EMPTY_TITLE,
        /** Course title is longer than 50 characters */
        FAILED_TITLE_TOO_LONG,
        /** Subject mnemonic field is empty or null */
        FAILED_EMPTY_SUBJECT,
        /** Subject mnemonic contained non-letter characters */
        FAILED_INVALID_SUBJECT_CHARS,
        /** Subject mnemonic was not between 2 and 4 characters in length */
        FAILED_INVALID_SUBJECT_LENGTH,
        /** Course number field is empty or null */
        FAILED_EMPTY_COURSE_NUMBER,
        /** Course number contained non-numeric characters */
        FAILED_INVALID_COURSE_NUMBER_CHARS,
        /** Course Number was not exactly 4 digits long */
        FAILED_INVALID_COURSE_NUMBER_LENGTH,
        /** A course with the same subject, number, and title already exists */
        FAILED_COURSE_ALREADY_EXISTS;
    }
    /**
     * Retrieves all courses currently in the catalog
     * @return a List of all courses in the database
     * @see Course#getCourseList()
     */
    public List<Course> getAllCourses() {
        return Course.getCourseList();
    }
    /**
     * Attempts to add a course after validating the user-specified subject mnemonic,
     * course number, and course title against the following constraints:
     * <ul>
     * <li> <b>Subject mnemonic</b>: must be strictly 2-4 letters (will be stored as uppercase).</li>
     * <li> <b>Course number</b>: must be strictly 4 digits.</li>
     * <li> <b>Course title</b>: must be between 1 and 50 characters.</li>
     * </ul>
     * The method returns a status indicating success or the specific reason for failure.
     *
     * @param subject a 2-4 letter subject mnemonic.
     * @param courseNumberStr a 4-digit course number string.
     * @param title a 1-50 character course title.
     * @return a {@link CourseAddResult} indicating success or detailed failure.
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
     * Searches for courses that match a specified subject mnemonic, course number, and/or title substring.
     * <i>Example</i>: you can search for CS courses that contain the substring "intro" (case-insensitive)
     * by calling {@code searchCourses("CS", null, "Intro")}. Note that all criteria are optional and may be {@code null}.
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

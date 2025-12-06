package edu.virginia.sde.reviews;

import org.hibernate.Session;

import java.util.*;

/**
 * Handles business logic for Course Search Scene
 */
public class CourseService {

    public List<Course> getAllCourses() {
        return Course.getCourseList();
    }
    public boolean addCourse(String subject, String courseNumberStr, String title) {
        validateSubjectMnemonic(subject);
        validateCourseNumberStr(courseNumberStr);
        validateTitle(title);
        Course newCourse = new Course(subject.toUpperCase(), Integer.parseInt(courseNumberStr), title);
        if (Course.courseExists(newCourse)) return false;
        Course.insertCourse(newCourse);
        return true;
    }

    private void validateTitle(String title) {
        if (title == null) throw new RuntimeException("The course title cannot be null.");
        title = title.strip();
        if (title.isEmpty()) throw new RuntimeException("Course title cannot be empty.");
        if (title.length() > 50) throw new RuntimeException("The course title cannot be longer than 50 characters.");
    }
    private void validateSubjectMnemonic(String subject) {
        if (subject == null) throw new RuntimeException("Subject mnemonic cannot be null.");
        subject = subject.strip();
        if (subject.isEmpty()) throw new RuntimeException("Subject mnemonic cannot be empty.");
        if (!subject.matches("[a-zA-Z]+")) throw new RuntimeException("Invalid subject mnemonic: Only English letters allowed.");
        if (subject.length() < 2 || subject.length() > 4) throw new RuntimeException("Invalid subject mnemonic: length must be between 2 and 4 characters.");
    }
    private void validateCourseNumberStr(String courseNumberStr) {
        if (courseNumberStr == null) throw new RuntimeException("Course number cannot be null.");
        courseNumberStr = courseNumberStr.strip();
        if (courseNumberStr.isEmpty()) throw new RuntimeException("Course number cannot be empty.");
        if (courseNumberStr.length() != 4) throw new RuntimeException("Course number must be exactly 4 digits.");
        for (int i = 0; i < courseNumberStr.length(); i++) {
            if (!Character.isDigit(courseNumberStr.charAt(i))) throw new RuntimeException("Only numeric characters are allowed for course number.");
        }
    }
    public List<Course> searchCourses(String subject, String courseNumberStr, String title ) {
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
                        if (title == null || title.isBlank()) return true;
                        return c.getCourseName().toLowerCase().contains(title.strip().toLowerCase());
                })
                .toList();
    }
}

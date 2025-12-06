package edu.virginia.sde.reviews;

import org.hibernate.Session;

import java.util.*;

/**
 * Handles business logic for Course Search Scene
 */
public class CourseService {

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

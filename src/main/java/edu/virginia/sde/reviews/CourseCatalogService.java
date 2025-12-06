package edu.virginia.sde.reviews;

import java.util.List;

/**
 * Handles business logic for Course Search Scene
 */
public class CourseCatalogService {
    private static List<Course> courses;

    /**
     * Wrapper method for {@link Course#getCourseList()}.
     * @return a List of all courses in the catalog
     */
    public static List<Course> getAllCourses() {
        return Course.getCourseList();
    }
}

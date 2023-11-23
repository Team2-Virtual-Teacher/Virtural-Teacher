package com.alpha53.virtualteacher.repositories.contracts;

import com.alpha53.virtualteacher.models.Course;

import java.util.List;

public interface CourseDao {

    Course get(int id);

    List<Course> getAll();

    List<Course> getCoursesByUser( int userId);
    void create(Course course);

    void update(Course course);

    void delete(int id);
}

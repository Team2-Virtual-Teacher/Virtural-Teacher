package com.alpha53.virtualteacher.repositories.contracts;

import com.alpha53.virtualteacher.models.Course;

import java.util.List;

public interface CourseDao {

    Course get(int id);

    List<Course> getAll();

    void create(Course course);

    void update(Course course);

    void delete(int id);
}

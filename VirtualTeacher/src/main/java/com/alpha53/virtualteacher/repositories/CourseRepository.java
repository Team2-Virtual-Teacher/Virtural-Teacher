package com.alpha53.virtualteacher.repositories;

import com.alpha53.virtualteacher.models.Course;

import java.util.List;

public interface CourseRepository {

    Course get(int id);
    Course getByTitle(String title);

    List<Course> getAll();

    void create(Course course);

    void update(Course course);

    void delete(int id);
}

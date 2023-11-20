package com.alpha53.virtualteacher.repositories;

import com.alpha53.virtualteacher.models.Course;

import java.util.List;

public interface CourseRepository {

    Course get(int id);

    Course get(String email);

    List<Course> getAll();

    void create(Course user);

    void update(Course user);

    void delete(int id);
}

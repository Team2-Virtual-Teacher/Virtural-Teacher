package com.alpha53.virtualteacher.services.contracts;

import com.alpha53.virtualteacher.models.Course;

import java.util.List;

public interface CourseService {
    Course get(int id);

    List<Course> getAll();

}

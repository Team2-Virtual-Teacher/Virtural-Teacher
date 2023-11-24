package com.alpha53.virtualteacher.services.contracts;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.User;

import java.util.List;

public interface CourseService {
    public void create(Course course, User user);


    public void update(Course course, User user);

    public void delete(int id, User user);
    public Course getCourseById(int id);

    public List<Course> getAllCourses();
}

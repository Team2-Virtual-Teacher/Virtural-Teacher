package com.alpha53.virtualteacher.services.contracts;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.*;

import java.util.List;

public interface CourseService {
    public void create(Course course, User user);


    public void update(Course course, User user);

    public void delete(int id, User user);

    public Course getCourseById(int id);


    void transferTeacherCourses(int teacherToTransferFromId, int teacherToTransferToId, User loggedUser);
    public List<Course> get(FilterOptions filterOptions);
    public List<Course> getUsersEnrolledCourses(int userId);
    public void enrollUserForCourse(User user, int courseId);
    public void rateCourse(RatingDto rating, int courseId, int raterId);
}

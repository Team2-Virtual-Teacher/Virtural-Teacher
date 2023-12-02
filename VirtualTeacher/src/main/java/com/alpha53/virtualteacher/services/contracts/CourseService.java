package com.alpha53.virtualteacher.services.contracts;

import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.FilterOptions;
import com.alpha53.virtualteacher.models.RatingDto;
import com.alpha53.virtualteacher.models.User;

import java.util.List;

public interface CourseService {
    public void create(Course course, User user);


    public void update(Course course, User user);

    public void delete(int id, User user);

    public Course getCourseById(int id);
    public Course getCourseByIdAuth(int id, User user);



    void transferTeacherCourses(int teacherToTransferFromId, int teacherToTransferToId, User loggedUser);
    public List<Course> get(FilterOptions filterOptions, User user);
    public List<Course> getPublic(FilterOptions filterOptions);
    public List<Course> getUsersEnrolledCourses(int userId);
    public List<Course> getUsersCompletedCourses(int userId);
    public void enrollUserForCourse(User user, int courseId);
    public void rateCourse(RatingDto rating, int courseId, int raterId);
}

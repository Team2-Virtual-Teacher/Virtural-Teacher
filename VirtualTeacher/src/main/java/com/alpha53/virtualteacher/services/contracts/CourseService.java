package com.alpha53.virtualteacher.services.contracts;

import com.alpha53.virtualteacher.models.*;

import java.util.List;
import java.util.Optional;

public interface CourseService {
     void create(Course course, User user);


     void update(Course course, User user);

     void delete(int id, User user);

     Course getCourseById(int id);
     Course getCourseByIdAuth(int id, User user);



    void transferTeacherCourses(int teacherToTransferFromId, int teacherToTransferToId, User loggedUser);
     List<Course> get(FilterOptions filterOptions, Optional<User> optionalUser);
     List<Course> getPublic(FilterOptions filterOptions);
     List<Course> getUsersEnrolledCourses(int userId);
     List<Course> getUsersCompletedCourses(int userId);
     void enrollUserForCourse(User user, int courseId);
     void rateCourse(RatingDto rating, int courseId, int raterId);
    boolean isUserEnrolled(int userId, int courseId);
    boolean hasUserPassedCourse(int userId, int courseId);
    Integer getCoursesCount();
    List<Rating> getRatingsByCourseId(int courseId);
    public List<User> getStudentsWhichAreEnrolledForCourse(int courseId);
}

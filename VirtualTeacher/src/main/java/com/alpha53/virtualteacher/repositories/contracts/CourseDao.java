package com.alpha53.virtualteacher.repositories.contracts;

import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.FilterOptions;
import com.alpha53.virtualteacher.models.RatingDto;
import com.alpha53.virtualteacher.models.User;

import java.util.Collection;
import java.util.List;

public interface CourseDao {

    Course get(int id);
    Course getByTitle(String title);

    List<Course> get(FilterOptions filterOptions);
    List<Course> getPublicCourses(FilterOptions filterOptions);
    List<Course> getUsersEnrolledCourses(int userId);
    List<Course> getUsersCompletedCourses(int userId);
    List<User> getStudentsWhichAreEnrolledForCourse(int courseId);
    void enrollUserForCourse(int userId, int courseId);

    List<Course> getCoursesByUser( int userId);
    List<Course> getCoursesByCreator(int creatorId);
    void create(Course course);

    void update(Course course);

    void delete(int id);

    void transferTeacherCourses(int teacherToTransferFromId, int teacherToTransferToId);
    //TODO DTO object should not pass to DAO
    void rateCourse(RatingDto rating, int courseId, int raterId);

    boolean isUserEnrolled(int userId, int courseId);
}

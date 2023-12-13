package com.alpha53.virtualteacher.repositories.contracts;

import com.alpha53.virtualteacher.models.*;

import java.util.List;

public interface CourseDao {

    Course get(int id);

    Course getByTitle(String title);

    List<Course> get(FilterOptions filterOptions);

    // List<Course> getUsersEnrolledCourses(int userId);
    List<Course> getUsersCompletedCourses(int userId);

    List<User> getStudentsWhichAreEnrolledForCourse(int courseId);

    void enrollUserForCourse(int userId, int courseId);

    void completeCourse(int userId, int courseId);

    List<Course> getCoursesByUser(int userId);

    List<Course> getCoursesByCreator(int creatorId);

    void create(Course course);

    void update(Course course);

    void delete(int id);

    void transferTeacherCourses(int teacherToTransferFromId, int teacherToTransferToId);

    //TODO DTO object should not pass to DAO
    void rateCourse(RatingDto rating, int courseId, int raterId);

    boolean isUserEnrolled(int userId, int courseId);

    void removeStudent(User user, Course course);

    boolean hasUserPassedCourse(int userId, int courseId);

    List<Integer> getIdOngoingCourses();

    Integer getCoursesCount();

    List<Rating> getRatingsByCourseId(int courseId);
}

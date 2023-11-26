package com.alpha53.virtualteacher.repositories.contracts;

import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.FilterOptions;
import com.alpha53.virtualteacher.models.RatingDto;

import java.util.List;

public interface CourseDao {

    Course get(int id);
    Course getByTitle(String title);

    List<Course> get(FilterOptions filterOptions);
    List<Course> getUsersEnrolledCourses(int userId);
    void enrollUserForCourse(int userId, int courseId);

    List<Course> getCoursesByUser( int userId);
    void create(Course course);

    void update(Course course);

    void delete(int id);

    void transferTeacherCourses(int teacherToTransferFromId, int teacherToTransferToId);


    void rateCourse(RatingDto rating, int courseId, int raterId);
}

package com.alpha53.virtualteacher.repositories.contracts;

import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.User;

import java.util.List;

public interface CourseDao {

    Course get(int id);
    Course getByTitle(String title);

    List<Course> getAll();

    List<Course> getCoursesByUser( int userId);
    void create(Course course);

    void update(Course course);

    void delete(int id);

    void transferTeacherCourses(int teacherToTransferFromId, int teacherToTransferToId);
}

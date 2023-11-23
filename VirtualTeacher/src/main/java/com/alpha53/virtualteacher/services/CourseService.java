package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.models.UserRoles;
import com.alpha53.virtualteacher.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private static final String ONLY_CREATOR_CAN_MODIFY_A_COURSE = "Only  creator or admin can modify a course.";
    private static final String ONLY_TEACHER_OR_ADMIN_CAN_CREATE_COURSE = "Only teacher or admin can create course.";
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void createCourse(Course course, User user){
        boolean duplicateExists = true;
        try {
            courseRepository.getByTitle(course.getTitle());

        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Course", "title", course.getTitle());
        }

        if ( !user.getRole().getRole().equals(UserRoles.ADMIN) && !user.getRole().getRole().equals(UserRoles.TEACHER)){
            throw new AuthorizationException(ONLY_TEACHER_OR_ADMIN_CAN_CREATE_COURSE);
        }
        course.setCreator(user);
        courseRepository.create(course);
    }

    public void updateCourse(Course course, User user){
        boolean duplicateExists = true;
        try {
            courseRepository.getByTitle(course.getTitle());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Course", "title", course.getTitle());
        }
        checkModifyPermissions(course.getId(), user);
        courseRepository.update(course);
    }

    public void deleteCourse(Course course, User user){

        checkModifyPermissions(course.getId(), user);
        courseRepository.delete(course.getId());
    }

    public Course getCourseById(int id){

        return courseRepository.get(id);
    }

    public List<Course> getAllCourses(){
        return courseRepository.getAll();
    }


    private void checkModifyPermissions(int courseId, User user) {
        Course course = courseRepository.get(courseId);
        if (course.getCreator().getId() != user.getId() && !user.getRole().getRole().equals(UserRoles.ADMIN)){
            throw new AuthorizationException(ONLY_CREATOR_CAN_MODIFY_A_COURSE);
        }


    }
//    create
//    update
//    delete
//    get(id)
//    getAll


}

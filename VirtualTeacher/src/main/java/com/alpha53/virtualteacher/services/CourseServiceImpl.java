package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.User;
import com.alpha53.virtualteacher.repositories.contracts.CourseDao;
import com.alpha53.virtualteacher.services.contracts.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private static final String ONLY_CREATOR_CAN_MODIFY_A_COURSE = "Only  creator or admin can modify a course.";
    private static final String ONLY_TEACHER_OR_ADMIN_CAN_CREATE_COURSE = "Only teacher or admin can create course.";
    private final CourseDao courseRepository;

    @Autowired
    public CourseServiceImpl(CourseDao courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void create(Course course, User user){
        boolean duplicateExists = true;
        try {
            courseRepository.getByTitle(course.getTitle());

        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Course", "title", course.getTitle());
        }

        if (!user.getRole().getRoleType().equalsIgnoreCase("teacher") && !user.getRole().getRoleType().equalsIgnoreCase("admin")){
            throw new AuthorizationException(ONLY_TEACHER_OR_ADMIN_CAN_CREATE_COURSE);
        }
        course.setCreator(user);
        courseRepository.create(course);
    }

    public void update(Course course, User user){
        boolean duplicateExists = true;
        try {
            Course existingCourse = courseRepository.getByTitle(course.getTitle());
            if (existingCourse.getCourseId() == course.getCourseId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Course", "title", course.getTitle());
        }
        checkModifyPermissions(course.getCourseId(), user);
        courseRepository.update(course);
    }

    public void delete(int id, User user){

        checkModifyPermissions(id, user);
        courseRepository.delete(id);
    }

    public Course getCourseById(int id){

        return courseRepository.get(id);
    }

    public List<Course> getAllCourses(){
        return courseRepository.getAll();
    }


    private void checkModifyPermissions(int courseId, User user) {
        Course course = courseRepository.get(courseId);
        System.out.println(course.getCreator().getUserId());
        System.out.println(user.getUserId());
        if (course.getCreator().getUserId() != user.getUserId() && !user.getRole().getRoleType().equalsIgnoreCase("admin")){
            throw new AuthorizationException(ONLY_CREATOR_CAN_MODIFY_A_COURSE);
        }


    }
//    create
//    update
//    delete
//    get(id)
//    getAll


}

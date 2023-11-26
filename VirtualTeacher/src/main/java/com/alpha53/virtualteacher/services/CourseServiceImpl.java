package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.exceptions.EntityDuplicateException;
import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.*;
import com.alpha53.virtualteacher.repositories.contracts.CourseDao;
import com.alpha53.virtualteacher.services.contracts.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
    private static final String ONLY_CREATOR_CAN_MODIFY_A_COURSE = "Only  creator or admin can modify a course.";
    private static final String ONLY_TEACHER_OR_ADMIN_CAN_CREATE_COURSE = "Only teacher or admin can create course.";
    public static final String COURSE_TRANSFER_EXCEPTION = "Courses can only be transferred by an Admin.";
    private final CourseDao courseRepository;

    @Autowired
    public CourseServiceImpl(CourseDao courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void create(Course course, User user) {
        boolean duplicateExists = true;
        try {
            courseRepository.getByTitle(course.getTitle());

        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Course", "title", course.getTitle());
        }

        if (!user.getRole().getRoleType().equalsIgnoreCase("teacher") && !user.getRole().getRoleType().equalsIgnoreCase("admin")) {
            throw new AuthorizationException(ONLY_TEACHER_OR_ADMIN_CAN_CREATE_COURSE);
        }
        course.setCreator(user);
        courseRepository.create(course);
    }

    public void update(Course course, User user) {
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

    public void delete(int id, User user) {

        checkModifyPermissions(id, user);
        courseRepository.delete(id);
    }

    public Course getCourseById(int id) {

        return courseRepository.get(id);
    }

    public List<Course> get(FilterOptions filterOptions) {


        return courseRepository.get(filterOptions);
    }

    @Override
    public List<Course> getUsersEnrolledCourses(int userId) {
        return courseRepository.getUsersEnrolledCourses(userId);
    }

    public void transferTeacherCourses(int teacherToTransferFromId, int teacherToTransferToId, User loggedUser){
        if (!loggedUser.getRole().getRoleType().equalsIgnoreCase("Admin")){
            throw new AuthorizationException(COURSE_TRANSFER_EXCEPTION);
        }
        if (teacherToTransferToId == teacherToTransferFromId){
            return;
        }
        courseRepository.transferTeacherCourses(teacherToTransferFromId,teacherToTransferToId);
    }
    @Override
    public void enrollUserForCourse(User user, int courseId) {
     List<Course> enrolledCourses = courseRepository.getUsersEnrolledCourses(user.getUserId());
     if (containsId(enrolledCourses, courseId)){
         throw new EntityDuplicateException("Record", "id", Integer.toString(courseId));

     } else {
         courseRepository.enrollUserForCourse(user.getUserId(), courseId);
     }
    }

    @Override
    public void rateCourse(RatingDto rating, int courseId, int raterId) {
        courseRepository.rateCourse(rating, courseId, raterId);
    }

    public boolean containsId(final List<Course> list, final int id){
        return list.stream().filter(o -> o.getCourseId()==id).findFirst().isPresent();
    }

    private void checkModifyPermissions(int courseId, User user) {
        Course course = courseRepository.get(courseId);
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

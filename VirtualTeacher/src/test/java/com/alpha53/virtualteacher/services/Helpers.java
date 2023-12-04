package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.models.*;

import java.time.LocalDate;
import java.util.HashSet;

public class Helpers {

    //    ===================================================USER HELPERS===================================================
    public static User createMockStudent() {
        return createMockUser(createMockStudentRole());
    }

    public static User createMockTeacher() {
        return createMockUser(createMockTeacherRole());
    }

    public static User createMockAdmin() {
        return createMockUser(createMockAdminRole());
    }

    public static User createMockPendingTeacher() {
        return createMockUser(createMockPendingTeacherRole());
    }

    public static User createMockUser(Role role) {
        return new User(1,
                "mockemail@abv.bg",
                "BilalB@ri123",
                "MockName",
                "MockSurname",
                role,
                "/resources/fileStorage/user-avatar.png",
                true,
                new HashSet<>());
    }

    public static Role createMockStudentRole() {
        return createMockRole(1, "Student");
    }

    public static Role createMockTeacherRole() {
        return createMockRole(2, "Teacher");
    }

    public static Role createMockPendingTeacherRole() {
        return createMockRole(4, "PendingTeacher");
    }

    public static Role createMockAdminRole() {
        return createMockRole(3, "Admin");
    }

    public static Role createMockRole(int id, String roleType) {
        Role role = new Role();
        role.setRoleId(id);
        role.setRoleType(roleType);
        return role;
    }

//  ===================================================Course HELPERS===================================================

    public static Course createMockCourse() {
        Course course = new Course();
        course.setCourseId(1);
        course.setTitle("How to fina a job before going broke?");
        course.setTopic(createMockTopic());
        course.setStartingDate(LocalDate.now());
        course.setCreator(createMockTeacher());
        course.setPublished(false);
        course.setPassingGrade(3);
        course.setAvgRating(0);
        course.setDescription(createMockCourseDescription());
        course.setLectures(new HashSet<>());
        return course;
    }

    public static CourseDescription createMockCourseDescription() {
        return  new CourseDescription(1,
                "If you want to finds yourself a job ASAP after finishing your programming course - seek no more!");
    }

    public static Topic createMockTopic() {
        return new Topic(9,
                "Psychology");
    }

//  ===================================================Lecture HELPERS===================================================

    public static Lecture createMockLecture(){
        return new Lecture(1,
                "Java Basics",
                "https://www.youtube.com/watch?v=p3l7fgvrEKM",
                "/resources/fileStorage/assignment1",
                1,
                createMockLectureDescription());
    }

    public static LectureDescription createMockLectureDescription(){
        return  new LectureDescription(1,
                "How to find a job as a developer 101");
    }

//  ===================================================Rating HELPERS===================================================

    public static Rating createMockRating(){
        return new Rating(1,
                "This course is the best!",
                4.3,
                createMockStudent(),
                createMockCourse());
    }

//  ===================================================Solution HELPERS===================================================

    public static Solution createMockSolution(){
        return new Solution(1,
                1,
                6,
                "/resources/fileStorage/solution1",
                5.5,
                1);
    }

}


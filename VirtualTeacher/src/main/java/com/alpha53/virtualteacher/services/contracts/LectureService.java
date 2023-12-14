package com.alpha53.virtualteacher.services.contracts;

import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LectureService {

    Lecture get(int courseId, int lectureId, User user);

    List<Lecture> getAllByCourseId(int courseId, User user);

    void create(Lecture lecture, User user, MultipartFile assignment);


    void update(Lecture lecture, User user, MultipartFile assignment);

    void delete(int courseId, int lectureId, User user);

    void uploadSolution(int courseId, int lectureId, User user, MultipartFile assignmentSolution);
}


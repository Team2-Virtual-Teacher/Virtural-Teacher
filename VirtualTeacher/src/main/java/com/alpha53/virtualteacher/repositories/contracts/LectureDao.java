package com.alpha53.virtualteacher.repositories.contracts;

import com.alpha53.virtualteacher.models.Assignment;
import com.alpha53.virtualteacher.models.Lecture;

import java.util.List;
import java.util.Optional;

public interface LectureDao {

    Lecture get(int id);

    List<Lecture> getAllByCourseId(int courseId);

    int create(Lecture lecture);

    void update(Lecture lecture);

    int delete(int lectureId);

    Integer getCourseCreatorId(int lectureId);

    void addSolution(int userId, int lectureId, String fileUrl);

    void updateSolution(int userId, int lectureId, String fileUrl);

    Optional<String> getSolutionUrl(int lectureId);

    List<Assignment> getAllByLectureId(int lectureId);
}

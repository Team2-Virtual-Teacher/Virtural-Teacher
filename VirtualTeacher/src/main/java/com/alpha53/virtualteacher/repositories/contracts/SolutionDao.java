package com.alpha53.virtualteacher.repositories.contracts;

import com.alpha53.virtualteacher.models.Solution;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SolutionDao {
    List<Solution> getAllByLectureId(int lectureId);

    List<Solution> getAllByUserId(int userId);

    /*Optional<String> getSolutionUrl(int lectureId);*/

    Optional<String> getSolutionUrl(int lectureId, int userId);

    void addSolution(int userId, int lectureId, String fileUrl);

    void updateSolutionUrl(int userId, int lectureId, String fileUrl);

    Solution getSolution(int userId, int lectureId);

    void addGrade(Solution solution);

    Map<Integer, Double> getSolutionCountAndAVGPerStudent(int studentId, int courseId);
}

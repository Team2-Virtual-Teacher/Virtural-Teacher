package com.alpha53.virtualteacher.repositories.contracts;

import com.alpha53.virtualteacher.models.Solution;

import java.util.List;
import java.util.Optional;

public interface SolutionDao {
    List<Solution> getAllByLectureId(int lectureId);

    List<Solution> getAllByUserId(int userId);

    Optional<String> getSolutionUrl(int lectureId);

    void addSolution(int userId, int lectureId, String fileUrl);

    void updateSolution(int userId, int lectureId, String fileUrl);

}

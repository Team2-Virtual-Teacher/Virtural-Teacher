package com.alpha53.virtualteacher.services.contracts;


import com.alpha53.virtualteacher.models.Solution;
import com.alpha53.virtualteacher.models.User;

public interface SolutionService {

    void addSolutionGrade(Solution solution, User user, int courseId);
}

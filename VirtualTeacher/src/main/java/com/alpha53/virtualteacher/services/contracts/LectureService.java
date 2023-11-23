package com.alpha53.virtualteacher.services.contracts;

import com.alpha53.virtualteacher.models.Lecture;

import java.util.List;

public interface LectureService {
    List<Lecture> getAllByCourseId(int id);
}

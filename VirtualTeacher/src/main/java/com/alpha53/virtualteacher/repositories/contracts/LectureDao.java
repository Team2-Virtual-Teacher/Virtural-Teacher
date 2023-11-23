package com.alpha53.virtualteacher.repositories.contracts;

import com.alpha53.virtualteacher.models.Lecture;

import java.util.List;

public interface LectureDao {

    Lecture get(int id);

    List<Lecture> getAllByCourseId(int id);

    void create(Lecture lecture);

    void update(Lecture lecture);

    void delete(int id);
}

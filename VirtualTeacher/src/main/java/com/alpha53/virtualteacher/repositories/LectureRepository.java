package com.alpha53.virtualteacher.repositories;

import com.alpha53.virtualteacher.models.Lecture;

import java.util.List;

public interface LectureRepository {

    Lecture get(int id);

    List<Lecture> getAllByCourse(int id);

    void create(Lecture lecture);

    void update(Lecture lecture);

    void delete(int id);
}

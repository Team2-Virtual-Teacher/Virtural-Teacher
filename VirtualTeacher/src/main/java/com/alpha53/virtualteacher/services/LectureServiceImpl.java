package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.repositories.contracts.LectureDao;
import com.alpha53.virtualteacher.services.contracts.LectureService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LectureServiceImpl implements LectureService {


    private final LectureDao lectureDao;

    public LectureServiceImpl(LectureDao lectureDao) {
        this.lectureDao = lectureDao;
    }

    @Override
    public List<Lecture> getAllByCourseId(int id) {
        return lectureDao.getAllByCourseId(id);
    }
}

package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.repositories.contracts.CourseDao;
import com.alpha53.virtualteacher.services.contracts.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseDao courseDao;

    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    public Course get(int id){

        return courseDao.get(id);
    }

    public List<Course> getAll() {
        return courseDao.getAll();
    }


}

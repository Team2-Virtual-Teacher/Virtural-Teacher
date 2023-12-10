package com.alpha53.virtualteacher.utilities.mappers.dtoMappers;

import com.alpha53.virtualteacher.models.Course;
import com.alpha53.virtualteacher.models.FilterOptions;
import com.alpha53.virtualteacher.models.dtos.CourseDto;
import com.alpha53.virtualteacher.models.dtos.FilterOptionDto;
import com.alpha53.virtualteacher.repositories.contracts.CourseDao;
import com.alpha53.virtualteacher.repositories.contracts.TopicDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class CourseDtoMapper {

    private final TopicDao topicDao;
    private final CourseDao courseDao;

    @Autowired
    public CourseDtoMapper(TopicDao topicDao, CourseDao courseDao) {
        this.topicDao = topicDao;
        this.courseDao = courseDao;
    }


    public Course fromDto(CourseDto dto){
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setStartingDate(LocalDate.parse(dto.getStartingDate()));
        course.setPassingGrade(dto.getPassingGrade());
        course.setTopic(topicDao.getById(dto.getTopicId()));
        course.setPublished(dto.isPublished());
        course.setPassingGrade(dto.getPassingGrade());
        course.setDescription(dto.getDescription());


        return  course;
    }
    public Course fromDto(int id, CourseDto dto){
        Course course = courseDao.get(id);
        course.setTitle(dto.getTitle());
        course.setStartingDate(LocalDate.parse(dto.getStartingDate()));
        course.setPassingGrade(dto.getPassingGrade());
        course.setTopic(topicDao.getById(dto.getTopicId()));
        course.setPublished(dto.isPublished());
        course.setPassingGrade(dto.getPassingGrade());
        course.setDescription(dto.getDescription());


        return  course;
    }


    public CourseDto toDto(Course course){
        CourseDto courseDto = new CourseDto();
        courseDto.setTitle(course.getTitle());
        courseDto.setPassingGrade(course.getPassingGrade());
        courseDto.setStartingDate(course.getStartingDate().toString());
        courseDto.setTopicId(course.getTopic().getTopicId());
        courseDto.setDescription(course.getDescription());
        courseDto.setPublished(course.isPublished());
        return courseDto;
    }
}


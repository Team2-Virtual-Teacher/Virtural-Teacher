package com.alpha53.virtualteacher.utilities.mappers.dtoMappers;

import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.models.dtos.LectureDto;
import org.springframework.stereotype.Component;

@Component
public class LectureDtoMapper {
    public Lecture dtoToObject(LectureDto lectureDto){
        Lecture lecture = new Lecture();
        lecture.setId(lectureDto.getId());
        lecture.setDescription(lectureDto.getDescription());
        lecture.setAssignment(lectureDto.getAssignment());
        lecture.setTitle(lectureDto.getTitle());
        lecture.setVideoUrl(lectureDto.getVideoUrl());
       // lecture.setCourseId(lectureDto.getCourseId());
        return lecture;
    }
}

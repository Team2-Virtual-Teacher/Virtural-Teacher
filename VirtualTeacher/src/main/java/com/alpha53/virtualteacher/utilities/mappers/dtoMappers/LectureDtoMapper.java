package com.alpha53.virtualteacher.utilities.mappers.dtoMappers;

import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.models.dtos.LectureDto;
import org.springframework.stereotype.Component;

@Component
public class LectureDtoMapper {
    public Lecture dtoToObject(LectureDto lectureDto){
        Lecture lecture = new Lecture();
        lecture.setDescription(lectureDto.getDescription());
        lecture.setTitle(lectureDto.getTitle());
        lecture.setVideoUrl(lectureDto.getVideoUrl());

        return lecture;
    }
}

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
        lecture.setVideoUrl(lectureDto.getVideoUrl().replace("watch?v=","embed/"));
        return lecture;
    }
    public LectureDto objectToDto(Lecture lecture){
        LectureDto lectureDto =  new LectureDto();
        lectureDto.setTitle(lecture.getTitle());
        lectureDto.setDescription(lecture.getDescription());
        lectureDto.setVideoUrl(lecture.getVideoUrl());
        return lectureDto;
    }
}

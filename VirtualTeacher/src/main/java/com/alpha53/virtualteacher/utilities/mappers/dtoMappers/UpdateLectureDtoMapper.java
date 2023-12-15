package com.alpha53.virtualteacher.utilities.mappers.dtoMappers;

import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.models.dtos.UpdateLectureDto;
import org.springframework.stereotype.Component;

@Component
public class UpdateLectureDtoMapper {
    public Lecture dtoToObject(UpdateLectureDto updateLectureDto){
        Lecture lecture = new Lecture();
        lecture.setDescription(updateLectureDto.getDescription());
        lecture.setTitle(updateLectureDto.getTitle());
        lecture.setVideoUrl(updateLectureDto.getVideoUrl().replace("watch?v=","embed/"));
       return lecture;
    }
    public UpdateLectureDto objectToDto(Lecture lecture){
        UpdateLectureDto updateLectureDto = new UpdateLectureDto();
        updateLectureDto.setTitle(lecture.getTitle());
        updateLectureDto.setVideoUrl(lecture.getVideoUrl());
        updateLectureDto.setDescription(lecture.getDescription());
        updateLectureDto.setAssignment(null);
        return updateLectureDto;
    }

}

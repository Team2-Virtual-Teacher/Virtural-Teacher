package com.alpha53.virtualteacher.utilities;

import com.alpha53.virtualteacher.models.Lecture;
import com.alpha53.virtualteacher.models.LectureDescription;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LectureMapper implements RowMapper<Lecture> {

    @Override
    public Lecture mapRow(ResultSet rs, int rowNum) throws SQLException {
        Lecture lecture = new Lecture();
        LectureDescription description = new LectureDescription();
        lecture.setId(rs.getInt("id"));
        lecture.setTitle(rs.getString("title"));
        lecture.setVideoUrl(rs.getString("video_url"));
        lecture.setAssignment(rs.getString("assignment_task"));
        lecture.setCourseId(rs.getInt("course_id"));
        description.setDescription(rs.getString("description"));
        description.setDescriptionId(rs.getInt("lecture_id"));

        /*if (description.getDescription() != null && !description.getDescription().isBlank()) {
            lecture.setDescription(description);
        }*/

        return lecture;
    }
}

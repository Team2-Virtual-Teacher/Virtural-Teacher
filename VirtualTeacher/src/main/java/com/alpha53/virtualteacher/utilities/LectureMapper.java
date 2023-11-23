package com.alpha53.virtualteacher.utilities;

import com.alpha53.virtualteacher.models.Lecture;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LectureMapper implements RowMapper<Lecture> {

    @Override
    public Lecture mapRow(ResultSet rs, int rowNum) throws SQLException {
        Lecture lecture = new Lecture();

        lecture.setId(rs.getInt("id"));
        lecture.setTitle(rs.getString("title"));
        lecture.setVideoUrl(rs.getString("video_url"));
        lecture.setAssignment(rs.getString("assignment_task"));
        lecture.setCourse(rs.getInt("course_id"));

        return lecture;
    }
}

package com.alpha53.virtualteacher.utilities;

import com.alpha53.virtualteacher.models.LectureDescription;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LectureDescriptionMapper implements RowMapper<LectureDescription> {

    @Override
    public LectureDescription mapRow(ResultSet rs, int rowNum) throws SQLException {
        LectureDescription lectureDescription = new LectureDescription();
        lectureDescription.setDescriptionId(rs.getInt("lecture_id"));
        lectureDescription.setDescription(rs.getString("description"));
        return lectureDescription;
    }
}

package com.gpstracker.server.db.map;

import com.gpstracker.server.db.entities.TrackPoint;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TrackPointMapper implements ResultSetMapper<TrackPoint> {
    @Override
    public TrackPoint map(int index, ResultSet rs, StatementContext ctx) throws SQLException {
        return new TrackPoint(
                rs.getInt("id"),
                rs.getInt("track_id"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude"),
                rs.getLong("gps_time"));
    }
}

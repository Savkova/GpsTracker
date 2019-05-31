package com.gpstracker.server.db.map;

import com.gpstracker.server.db.entities.Track;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TrackMapper implements ResultSetMapper<Track> {
    @Override
    public Track map(int index, ResultSet rs, StatementContext ctx) throws SQLException {
        return new Track(rs.getInt("id"),
                rs.getString("name"),
                rs.getLong("starttime_server"),
                rs.getLong("endtime_server"),
                rs.getInt("user_id"));
    }
}

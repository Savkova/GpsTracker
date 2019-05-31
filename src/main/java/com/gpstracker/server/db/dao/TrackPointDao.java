package com.gpstracker.server.db.dao;

import com.gpstracker.server.db.entities.TrackPoint;
import com.gpstracker.server.db.map.TrackPointMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import com.gpstracker.server.util.Constants.DBTable;

import java.util.List;

@RegisterMapper(TrackPointMapper.class)
public interface TrackPointDao {

    @SqlQuery("SELECT * FROM " + DBTable.TRACK_POINTS + " WHERE id = :id")
    public List<TrackPoint> getById(@Bind("id") int id);

    @SqlQuery("SELECT * FROM " + DBTable.TRACK_POINTS + " WHERE track_id = :trackId")
    public List<TrackPoint> getByTrackId(@Bind("trackId") int trackId);

    @SqlQuery("SELECT * FROM " + DBTable.TRACK_POINTS + " WHERE gps_time = :gpsTime")
    public TrackPoint getByGpsTime(@Bind("gpsTime") long gpsTime);

    @SqlUpdate("INSERT INTO " + DBTable.TRACK_POINTS + " (track_id, latitude, longitude, gps_time) VALUES (:trackId, :latitude, :longitude, :gpsTime)")
    public void insert(@BindBean TrackPoint trackPoint);

    @SqlUpdate("DELETE FROM " + DBTable.TRACK_POINTS + " WHERE track_id = :trackId")
    public void deleteByTrackId(@Bind("trackId") int trackId);

}

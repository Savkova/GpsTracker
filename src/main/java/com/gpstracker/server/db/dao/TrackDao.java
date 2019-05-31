package com.gpstracker.server.db.dao;

import com.gpstracker.server.db.entities.Track;
import com.gpstracker.server.db.map.TrackMapper;
import com.gpstracker.server.util.Constants.DBTable;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(TrackMapper.class)
public interface TrackDao {

    @SqlQuery("SELECT * FROM " + DBTable.TRACKS + " WHERE id = :id")
    public Track getById(@Bind("id") int id);

    @SqlQuery("SELECT * FROM " + DBTable.TRACKS + " WHERE user_id = :userId")
    public Track getByUserId(@Bind("userId") int userId);

    @SqlQuery("SELECT * FROM " + DBTable.TRACKS + " WHERE user_id = :userId and name = :name")
    public Track getByUseridAndTrackname(@Bind("userId") int userId, @Bind("name") String name);

    @SqlUpdate("INSERT INTO " + DBTable.TRACKS + " (name, user_id, starttime_server, endtime_server) VALUES (:name, :userId, :starttimeServer, :endtimeServer)")
    public void insert(@BindBean Track track);

    @SqlUpdate("UPDATE " + DBTable.TRACKS + " SET endtime_server = :endtimeServer WHERE id = :id")
    public void updateEndtimeOfTrack(@Bind("id") int track_id, @Bind("endtimeServer") long endtimeServer);

    @SqlUpdate("DELETE FROM " + DBTable.TRACKS + " WHERE id = :id")
    void delete(@Bind("id") int id);
}

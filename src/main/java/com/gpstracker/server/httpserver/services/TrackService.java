package com.gpstracker.server.httpserver.services;

import com.gpstracker.server.db.DBInitUtil;
import com.gpstracker.server.db.dao.TrackDao;
import com.gpstracker.server.db.entities.Track;
import com.gpstracker.server.util.Constants.Loggers;

import java.time.LocalDateTime;

public class TrackService {

    private TrackService() {
    }

    public static Track createTrack(int userId, String trackName) {
        TrackDao trackDao = DBInitUtil.getDbi().onDemand(TrackDao.class);
        trackDao.insert(new Track(trackName, System.currentTimeMillis(), 0, userId));
        Loggers.DB_LOGGER.info(LocalDateTime.now() + " Created new track '" + trackName + "'");

        return trackDao.getByUseridAndTrackname(userId, trackName);
    }

    public static void stopTrack(int trackId) {
        TrackDao trackDao = DBInitUtil.getDbi().onDemand(TrackDao.class);
        trackDao.updateEndtimeOfTrack(trackId, System.currentTimeMillis());
    }

    public static int getTrackId(int userId, String trackName) {
        TrackDao trackDao = DBInitUtil.getDbi().onDemand(TrackDao.class);
        Track track = trackDao.getByUseridAndTrackname(userId, trackName);

        if (track == null) {
            track = createTrack(userId, trackName);
        }
        return track.getId();
    }


}

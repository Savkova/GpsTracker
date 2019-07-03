package com.gpstracker.server.httpserver.services;

import com.gpstracker.server.db.DBInitUtil;
import com.gpstracker.server.db.dao.TrackDao;
import com.gpstracker.server.db.dao.TrackPointDao;
import com.gpstracker.server.db.entities.Track;
import com.gpstracker.server.db.entities.TrackPoint;
import com.gpstracker.server.exceptions.InvalidRequestException;
import com.gpstracker.server.exceptions.InvalidTokenException;
import com.gpstracker.server.exceptions.NotFoundException;
import com.gpstracker.server.util.Constants;
import com.gpstracker.server.util.Constants.Loggers;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

public class TrackService {

    public static final TrackService instance = new TrackService();

    private final UserService userService;

    private TrackService() {
        this.userService = UserService.instance;
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

    /* Example:
   {
       "track1": [
           {"t": "2020-01-17T08:29:59.770", "lon": 36, "lat": 51},
           {"t": "2020-01-17T08:29:59.770", "lon": 36, "lat": 51}
       ]
   }*/
    public JSONObject getTrackPoints(String trackName, Map<String, String> headers)
            throws InvalidTokenException, InvalidRequestException, NotFoundException {

        if (!headers.containsKey(Constants.RequestHeaders.TOKEN)) {
            throw new InvalidRequestException("Missing required header. ");
        }

        String tokenValue = headers.get(Constants.RequestHeaders.TOKEN);
        int userId = userService.getUserId(tokenValue);
        int trackId = TrackService.getTrackId(userId, trackName);

        TrackPointDao trackPointDao = DBInitUtil.getDbi().onDemand(TrackPointDao.class);
        List<TrackPoint> trackPoints = trackPointDao.getByTrackId(trackId);

        if (trackPoints.isEmpty()) {
            Loggers.SERVER_LOGGER.info(LocalDateTime.now() + " Report sent: no records for the track " + trackName);
            return null;
        }

        JSONObject jsonTrack = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        for (TrackPoint trackPoint : trackPoints) {

            LocalDateTime dateTime = Instant.ofEpochMilli(trackPoint.getGpsTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();

            JSONObject record = new JSONObject();
            record.put(Constants.QueryParameters.GPS_TIME, dateTime);
            record.put(Constants.QueryParameters.GPS_LATITUDE, trackPoint.getLatitude());
            record.put(Constants.QueryParameters.GPS_LONGITUDE, trackPoint.getLongitude());
            jsonArray.put(record);
        }

        jsonTrack.put(trackName, jsonArray);

        Loggers.SERVER_LOGGER.info(LocalDateTime.now() + " Report sent to the client (userId = " + userId + ", trackName = '" + trackName + "')");
        return jsonTrack;

    }


}

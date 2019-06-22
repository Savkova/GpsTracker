package com.gpstracker.server.httpserver.services;

import com.gpstracker.server.db.DBInitUtil;
import com.gpstracker.server.db.dao.TrackPointDao;
import com.gpstracker.server.db.entities.TrackPoint;
import com.gpstracker.server.exceptions.InvalidTokenException;
import com.gpstracker.server.util.Constants.QueryParameters;
import com.gpstracker.server.util.Constants.RequestHeaders;
import com.gpstracker.server.util.Constants.Loggers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;


public class ReportService {

    public static final ReportService instance = new ReportService();

    private final UserService userService;

    public ReportService() {
        this.userService = UserService.instance;
    }


    /*
    Example:
    {
        "track1": [
            {"t": "2020-01-17T08:29:59.770", "lon": 36, "lat": 51},
            {"t": "2020-01-17T08:29:59.770", "lon": 36, "lat": 51}
        ]
    }
    */
    public JSONObject getTrackPointReport(Map<String, String> headers) throws InvalidTokenException {

        String tokenValue = headers.get(RequestHeaders.TOKEN);
        String trackName = headers.get(RequestHeaders.TRACK_NAME);
        int userId = userService.getUserId(tokenValue);
        int trackId = TrackService.getTrackId(userId, trackName);

        TrackPointDao trackPointDao = DBInitUtil.getDbi().onDemand(TrackPointDao.class);
        List<TrackPoint> trackPoints = trackPointDao.getByTrackId(trackId);

        if (trackPoints.isEmpty()) {
            Loggers.SERVER_LOGGER.info("Report sent: no records for the track " + trackName);
            return null;
        }

        JSONObject jsonTrack = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        for (TrackPoint trackPoint : trackPoints) {

            LocalDateTime dateTime = Instant.ofEpochMilli(trackPoint.getGpsTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();

            JSONObject record = new JSONObject();
            record.put(QueryParameters.GPS_TIME, dateTime);
            record.put(QueryParameters.GPS_LATITUDE, trackPoint.getLatitude());
            record.put(QueryParameters.GPS_LONGITUDE, trackPoint.getLongitude());
            jsonArray.put(record);
        }

        jsonTrack.put(trackName, jsonArray);

        Loggers.SERVER_LOGGER.info("Report sent to the client (userId = " + userId + ", trackId = '" + trackName + "')");
        return jsonTrack;

    }
}

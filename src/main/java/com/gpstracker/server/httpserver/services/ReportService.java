package com.gpstracker.server.httpserver.services;

import com.gpstracker.server.db.DBInitUtil;
import com.gpstracker.server.db.dao.TrackPointDao;
import com.gpstracker.server.db.entities.TrackPoint;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import com.gpstracker.server.util.Constants.QueryParameters;
import com.gpstracker.server.util.Constants.RequestHeaders;
import com.gpstracker.server.util.Constants.Loggers;

public class ReportService
{

    public static JSONArray getTrackPointReport(Map<String, List<String>> requestParameters) {

        String tokenValue = requestParameters.get(RequestHeaders.TOKEN).get(0);
        String trackName = requestParameters.get(RequestHeaders.TRACK_NAME).get(0);
        int userId = UserService.getUserId(tokenValue);
        int trackId = TrackService.getTrackId(userId, trackName);

        TrackPointDao trackPointDao = DBInitUtil.getDbi().onDemand(TrackPointDao.class);
        List<TrackPoint> trackPoints = trackPointDao.getByTrackId(trackId);
        Loggers.SERVER_LOGGER.info("size=" + trackPoints.size());

        if (trackPoints.isEmpty())
            Loggers.SERVER_LOGGER.info("No records for the track " + trackName);

        JSONArray jsonArray = new JSONArray();
        for (TrackPoint trackPoint : trackPoints) {

            LocalDateTime dateTime = Instant.ofEpochMilli(trackPoint.getGpsTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(RequestHeaders.TRACK_NAME, trackName);
            jsonObject.put(QueryParameters.GPS_TIME, dateTime);
            jsonObject.put(QueryParameters.GPS_LATITUDE, trackPoint.getLatitude());
            jsonObject.put(QueryParameters.GPS_LONGITUDE, trackPoint.getLongitude());
            jsonArray.put(jsonObject);
        }

        return jsonArray;

    }
}

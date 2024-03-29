package com.gpstracker.server.httpserver.services;

import com.gpstracker.server.db.DBInitUtil;
import com.gpstracker.server.db.dao.TrackDao;
import com.gpstracker.server.db.dao.TrackPointDao;
import com.gpstracker.server.db.entities.TrackPoint;
import com.gpstracker.server.exceptions.*;
import com.gpstracker.server.util.Constants.QueryParameters;
import com.gpstracker.server.util.Constants.RequestHeaders;
import com.gpstracker.server.util.Constants.Loggers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class RecordpointService {

    public static final RecordpointService instance = new RecordpointService();

    private final UserService userService;

    private RecordpointService() {
        userService = UserService.instance;
    }

    public boolean makeRecord(Map<String, List<String>> params, Map<String, String> headers)
            throws InvalidRequestException, InvalidTokenException {


        if (!headers.containsKey(RequestHeaders.TOKEN) || !headers.containsKey(RequestHeaders.TRACK_NAME)) {
            throw new InvalidRequestException("Missing required header. ");
        }

        String tokenValue = headers.get(RequestHeaders.TOKEN);
        int userId = userService.getUserId(tokenValue);

        String trackName = headers.get(RequestHeaders.TRACK_NAME);
        int trackId = -1;
        try {
            trackId = TrackService.getTrackId(userId, trackName);
        } catch (NotFoundException e) {
            TrackService.createTrack(userId, trackName);
        }

        if (params.containsKey(QueryParameters.TRACKER_STATUS) && params.get(QueryParameters.TRACKER_STATUS).contains("stop")) {
            TrackService.stopTrack(trackId);
            Loggers.DB_LOGGER.info(LocalDateTime.now() + " Track '" + trackName + "' stoped (userId = " + userId + ")");
            return false;
        }

        if (!params.containsKey(QueryParameters.GPS_LATITUDE)
                || !params.containsKey(QueryParameters.GPS_LONGITUDE)
                || !params.containsKey(QueryParameters.GPS_TIME)) {
            throw new InvalidRequestException("Missing required query parameter. ");
        }

        double latitude = Double.valueOf(params.get(QueryParameters.GPS_LATITUDE).get(0));
        double longitude = Double.valueOf(params.get(QueryParameters.GPS_LONGITUDE).get(0));
        long gpsTime = Long.valueOf(params.get(QueryParameters.GPS_TIME).get(0));

        TrackPointDao trackPointDao = DBInitUtil.getDbi().onDemand(TrackPointDao.class);
        trackPointDao.insert(new TrackPoint(trackId, latitude, longitude, gpsTime));
        Loggers.DB_LOGGER.info(LocalDateTime.now() + " Added new record (userId = " + userId + ", trackId = '" + trackName + "')");

        return true;
    }


}


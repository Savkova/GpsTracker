package com.gpstracker.server.httpserver.services;

import com.gpstracker.server.db.DBInitUtil;
import com.gpstracker.server.db.dao.TrackDao;
import com.gpstracker.server.db.dao.TrackPointDao;
import com.gpstracker.server.db.entities.TrackPoint;
import com.gpstracker.server.exceptions.InvalidRequestException;
import com.gpstracker.server.exceptions.InvalidTokenException;
import com.gpstracker.server.util.Constants.QueryParameters;
import com.gpstracker.server.util.Constants.RequestHeaders;
import com.gpstracker.server.util.Constants.Loggers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class RecordpointService {

    private RecordpointService() {
    }

    public static boolean makeRecord(Map<String, List<String>> params, Map<String, String> headers)
            throws InvalidRequestException, InvalidTokenException {

        String tokenValue = headers.get(RequestHeaders.TOKEN);
        int userId = UserService.getUserId(tokenValue);

        if (userId < 0)
            throw new InvalidTokenException("Invalid token. ");

        String trackName = headers.get(RequestHeaders.TRACK_NAME);
        int trackId = TrackService.getTrackId(userId, trackName);

        if (params.containsKey(QueryParameters.TRACKER_STATUS)
                && params.get(QueryParameters.TRACKER_STATUS).contains("stop")) {

            TrackService.stopTrack(trackId);
            Loggers.DB_LOGGER.info(LocalDateTime.now() + " Track '" + trackName + "' stoped");

            return false;
        }

        try {
            double latitude = Double.valueOf(params.get(QueryParameters.GPS_LATITUDE).get(0));
            double longitude = Double.valueOf(params.get(QueryParameters.GPS_LONGITUDE).get(0));
            long gpsTime = Long.valueOf(params.get(QueryParameters.GPS_TIME).get(0));

            TrackPointDao trackPointDao = DBInitUtil.getDbi().onDemand(TrackPointDao.class);
            trackPointDao.insert(new TrackPoint(trackId, latitude, longitude, gpsTime));
            Loggers.DB_LOGGER.info(java.time.LocalDateTime.now() + " Added new record to '" + trackName + "'");

        } catch (NullPointerException | IndexOutOfBoundsException e) {
            throw new InvalidRequestException("Missing required query parameter. ");
        }

        return true;
    }

    static void deleteTrack(int trackId) {
        //TODO make transaction with trackrecords
        TrackPointDao trackPointDao = DBInitUtil.getDbi().onDemand(TrackPointDao.class);
        trackPointDao.deleteByTrackId(trackId);

        TrackDao trackDao = DBInitUtil.getDbi().onDemand(TrackDao.class);
        trackDao.delete(trackId);

        Loggers.DB_LOGGER.info("Track id=" + trackId + " deleted.");
    }
}


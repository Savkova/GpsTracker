package com.gpstracker.server.util;

import com.gpstracker.server.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final public class Constants {
    public static final class MainSettings {
        public static final Configuration configuration = new Configuration("config.properties");
    }

    public static final class Filters {
        public static final String QUERY_PATH_FILTER = "query_path_filter";
    }

    public static final class RequestHandlers {
        public static final String RECORDPOINT_HANDLER = "/recordpoint";
        public static final String REPORT_HANDLER = "/report";
        public static final String USER_HANDLER = "/user/.*";
    }

    public static final class ContextAttributes {
        public static final String QUERY_PARAMS = "query_params";
        public static final String PATH_PARTS = "path_parts";
        public static final String HEADERS = "headers";
    }

    public static final class QueryParameters {
        public static final String GPS_TIME = "t";
        public static final String GPS_LATITUDE = "lat";
        public static final String GPS_LONGITUDE = "lon";
        public static final String TRACKER_STATUS = "tracker";
    }

    public static final class RequestHeaders {
        public static final String TOKEN = "Token";
        public static final String TRACK_NAME = "Trackid";
        public static final String LOGIN = "Login";
        public static final String PASSWORD = "Password";
        public static final String EMAIL = "Email";
        public static final String NAME = "Name";
    }

    public static final class Loggers {
        public static final Logger SERVER_LOGGER = LoggerFactory.getLogger("MAIN");
        public static final Logger DB_LOGGER = LoggerFactory.getLogger("REPO");
    }

    public static final class DBTable {
        public static final String USERS = "Users";
        public static final String TRACKS = "Tracks";
        public static final String TRACK_POINTS = "TrackPoints";
    }


}

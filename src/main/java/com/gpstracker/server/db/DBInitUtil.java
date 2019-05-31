package com.gpstracker.server.db;

import com.gpstracker.server.db.dao.UserDao;
import com.gpstracker.server.db.entities.User;
import com.gpstracker.server.util.TokenGenerator;
import org.h2.jdbcx.JdbcConnectionPool;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import com.gpstracker.server.util.Constants.Loggers;
import com.gpstracker.server.util.Constants.MainSettings;
import com.gpstracker.server.util.Constants.DBTable;

public class DBInitUtil {
    private static DBI dbi;

    public static void initDbConnection() {
        Loggers.DB_LOGGER.info("Preparing database ...");
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            Loggers.DB_LOGGER.error("H2 Driver not found in classpath.", e);
//      TODO: HttpServer.freeResourses();
            System.exit(-1);
        }

        dbi = new DBI(JdbcConnectionPool.create(
                MainSettings.configuration.getDatabase_url(),
                MainSettings.configuration.getDatabase_user(),
                MainSettings.configuration.getDatabase_password()
        ));

        dbi.useHandle(handle ->
        {
            handle.getConnection();
            Loggers.DB_LOGGER.info("Connection to the database is established successfully");
        });

    }

    public static void initDb() {
//      TODO: sha256 - 64 char or 32 bytes
        dbi.useHandle(handle -> {
            handle.execute("CREATE TABLE IF NOT EXISTS " + DBTable.USERS + " (" +
                    "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "login CHAR(20) NOT NULL, " +
                    "password BINARY(32) NOT NULL, " +
                    "email CHAR(100) NOT NULL, " +
                    "name CHAR(50) NOT NULL, " +
                    "timezone INT2 NOT NULL, " +
                    "token BINARY(16) NOT NULL, " +
                    "CONSTRAINT UC_" + DBTable.USERS + " UNIQUE (login, password))");
            Loggers.DB_LOGGER.debug("Table '" + DBTable.USERS + "' is created");

            handle.execute("CREATE TABLE IF NOT EXISTS " + DBTable.TRACKS + " (" +
                    "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "name CHAR(50) NOT NULL, " +
                    "user_id INTEGER NOT NULL, " +
                    "starttime_server BIGINT NOT NULL, " +
                    "endtime_server BIGINT, " +
                    "FOREIGN KEY(user_id) REFERENCES " + DBTable.USERS + "(id), " +
                    "CONSTRAINT UC_" + DBTable.TRACKS + " UNIQUE (user_id, name))");
            Loggers.DB_LOGGER.debug("Table '" + DBTable.TRACKS + "' is created");

            handle.execute("CREATE TABLE IF NOT EXISTS " + DBTable.TRACK_POINTS + "(" +
                    "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "track_id INTEGER, " +
                    "latitude DOUBLE, " +
                    "longitude DOUBLE, " +
                    "gps_time BIGINT, " +
                    "FOREIGN KEY (track_id) REFERENCES " + DBTable.TRACKS + "(id))");
            Loggers.DB_LOGGER.debug("Table '" + DBTable.TRACK_POINTS + "' is created");

            initDefaultUser();
        });
    }

    public static void deleteTables(Handle handle) {
        handle.execute("DROP TABLE " + DBTable.TRACK_POINTS);
        handle.execute("DROP TABLE " + DBTable.TRACKS);
//        handle.execute("DROP TABLE " + USERS);
    }

    public static int addNewUser(String login, byte[] password, String email, String name, short timezone) {
        byte[] token = TokenGenerator.generateToken();
        UserDao userDao = getDbi().onDemand(UserDao.class);
        int nextId = userDao.getCount() + 1;

        User user = new User(nextId, login, password, email, name, timezone, token);
        if (userDao.getByToken(token) == null) {
            userDao.insert(user);
            user = userDao.getByToken(token);
            Loggers.DB_LOGGER.info("Registered new user: id=" + user.getId() + ", login=" + user.getLogin());
            return user.getId();
        }
        return -1;
    }

    public static DBI getDbi() {
        return dbi;

    }

    private static void initDefaultUser() {
//      TODO delete when sign-up-service will created
        int id = 1;
        String login = "default";
        byte[] password = "qwerty123".getBytes();
        String email = "123@my.mail";
        String name = "username";
        short timezone = 2;
        byte[] token = "1020304050607089".getBytes();

        User user = new User(id, login, password, email, name, timezone, token);
        UserDao userDao = getDbi().onDemand(UserDao.class);
        if (userDao.getByToken(token) == null) {
            userDao.insert(user);
            Loggers.DB_LOGGER.debug("Default user initialized");
        }

    }

}

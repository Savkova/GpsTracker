package com.gpstracker.server.httpserver.services;

import com.gpstracker.server.db.DBInitUtil;
import com.gpstracker.server.db.dao.UserDao;
import com.gpstracker.server.db.entities.User;
import com.gpstracker.server.util.Constants.Loggers;

public class UserService {
    public static final UserService instance = new UserService();

    private UserService() {
    }

    public void registerUser() {
// TODO: not implemented yet
    }

    public static int getUserId(String tokenValue) {
        UserDao userDao = DBInitUtil.getDbi().onDemand(UserDao.class);
        User user = userDao.getByToken(tokenValue.getBytes());

        if (user != null)
            return user.getId();

        Loggers.DB_LOGGER.info("Invalid token");
        return -1;
    }

}

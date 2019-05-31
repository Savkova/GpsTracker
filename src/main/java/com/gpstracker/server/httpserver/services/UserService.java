package com.gpstracker.server.httpserver.services;

import com.gpstracker.server.db.DBInitUtil;
import com.gpstracker.server.db.dao.UserDao;
import com.gpstracker.server.db.entities.User;
import com.gpstracker.server.exceptions.AlreadyExistException;
import com.gpstracker.server.exceptions.InvalidRequestException;
import com.gpstracker.server.util.Constants.RequestHeaders;
import com.gpstracker.server.util.Constants.Loggers;
import com.gpstracker.server.util.TokenGenerator;

import java.util.Map;

public class UserService {
    public static final UserService instance = new UserService();

    private UserService() {
    }

    public String registerUser(Map<String, String> headers) throws InvalidRequestException, AlreadyExistException {

        String login;
        byte[] password;
        String email;
        String name;
        try {
            login = headers.get(RequestHeaders.LOGIN);
            password = headers.get(RequestHeaders.PASSWORD).getBytes();
            email = headers.get(RequestHeaders.EMAIL);
            name = headers.get(RequestHeaders.NAME);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            throw new InvalidRequestException("Missing required header. ");
        }

        User user = saveUser(login, password, email, name, (short) 2);
        return new String(user.getToken());

    }

    public int getUserId(String tokenValue) {
        UserDao userDao = DBInitUtil.getDbi().onDemand(UserDao.class);
        User user = userDao.getByToken(tokenValue.getBytes());

        if (user != null)
            return user.getId();

        Loggers.DB_LOGGER.info("Invalid token");
        return -1;
    }

    private static User saveUser(String login, byte[] password, String email, String name, short timezone)
            throws AlreadyExistException {

        byte[] token = TokenGenerator.generateToken().toString().getBytes();
        UserDao userDao = DBInitUtil.getDbi().onDemand(UserDao.class);
        int nextId = userDao.getCount() + 1;

        //TODO: add check for unique

        User user = new User(nextId, login, password, email, name, timezone, token);
        if (userDao.getByToken(token) == null) {
            userDao.insert(user);
            user = userDao.getByToken(token);
            Loggers.DB_LOGGER.info("Registered user: login=" + user.getLogin() + ", email=" + user.getEmail());
            return user;
        } else {
            throw new AlreadyExistException("Token already exist. Try again, please. ");
        }
    }


}

package com.gpstracker.server.httpserver.services;

import com.gpstracker.server.db.DBInitUtil;
import com.gpstracker.server.db.dao.UserDao;
import com.gpstracker.server.db.entities.User;
import com.gpstracker.server.exceptions.AlreadyExistException;
import com.gpstracker.server.exceptions.FailedLoginException;
import com.gpstracker.server.exceptions.InvalidRequestException;
import com.gpstracker.server.util.Constants.RequestHeaders;
import com.gpstracker.server.util.Constants.Loggers;
import com.gpstracker.server.util.TokenCash;
import com.gpstracker.server.util.TokenGenerator;

import java.util.Map;
import java.util.UUID;

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

        return saveUser(login, password, email, name, (short) 2).getLogin();

    }

    public int getUserId(String tokenValue) {
       /* UserDao userDao = DBInitUtil.getDbi().onDemand(UserDao.class);
        User user = userDao.getByToken(tokenValue.getBytes());

        if (user != null)
            return user.getId();
*/
        // TODO not implemented
        Loggers.DB_LOGGER.info("Invalid token");
        return -1;
    }

    private static User saveUser(String login, byte[] password, String email, String name, short timezone)
            throws AlreadyExistException {

        UserDao userDao = DBInitUtil.getDbi().onDemand(UserDao.class);

        if (userDao.getByLogin(login) != null) {
            throw new AlreadyExistException("The login is taken. Try another. ");
        }

        if (userDao.getByEmail(email) != null) {
            throw new AlreadyExistException("User with that email already exists. ");
        }

        User user = new User(0, login, password, email, name, timezone);

        userDao.insert(user);

        user = userDao.getByLogin(login);
        Loggers.DB_LOGGER.info("Registered user: login=" + user.getLogin() + ", email=" + user.getEmail());
        return user;

    }


    public String login(Map<String, String> headers) throws InvalidRequestException, FailedLoginException {

        String login;
        byte[] password;
        try {
            login = headers.get(RequestHeaders.LOGIN);
            password = headers.get(RequestHeaders.PASSWORD).getBytes();
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            throw new InvalidRequestException("Missing required header. ");
        }

        UserDao userDao = DBInitUtil.getDbi().onDemand(UserDao.class);

        if (userDao.getByLogin(login) == null) {
            throw new FailedLoginException("User does not exist. ");
        }

        User user = userDao.getByLoginAndPassword(login, password);

        if (user == null) {
            throw new FailedLoginException("Login failed. Invalid username or password. ");
        }

        UUID token = TokenGenerator.generateToken();
        TokenCash.getTokens().put(token, user.getId());

        Loggers.SERVER_LOGGER.info("User '" + user.getLogin() + "' logged in");

        return token.toString();

    }

    public String logout(Map<String, String> headers) {
        return null;
        // TODO not implemented
    }
}

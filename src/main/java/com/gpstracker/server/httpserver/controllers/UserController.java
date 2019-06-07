package com.gpstracker.server.httpserver.controllers;

import com.gpstracker.server.exceptions.*;
import com.gpstracker.server.util.Constants.ContextAttributes;
import com.gpstracker.server.httpserver.services.UserService;

import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.nio.*;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.http.message.BasicHttpResponse;
import org.apache.hc.core5.http.nio.entity.StringAsyncEntityConsumer;
import org.apache.hc.core5.http.impl.EnglishReasonPhraseCatalog;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import static com.gpstracker.server.util.Constants.Actions.*;

public class UserController implements AsyncServerRequestHandler<Message<HttpRequest, String>> {


    private final UserService userService;

    public UserController() {
        this.userService = UserService.instance;
    }

    @Override
    public AsyncRequestConsumer<Message<HttpRequest, String>> prepare(HttpRequest request, HttpContext context) {
        return new BasicRequestConsumer<>(new StringAsyncEntityConsumer());
    }

    @Override
    public void handle(Message<HttpRequest, String> requestMessage,
                       ResponseTrigger responseTrigger,
                       HttpContext context) throws IOException, HttpException {

        String[] pathParts = (String[]) context.getAttribute(ContextAttributes.PATH_PARTS);
        Map<String, String> headers = (Map<String, String>) context.getAttribute(ContextAttributes.HEADERS);

        String action = pathParts[2];

        HttpResponse response;
        int status;
        String message;
        try {
            String login;
            switch (action) {
                case REGISTER_USER:
                    login = userService.registerUser(headers);
                    status = HttpStatus.SC_OK;
                    message = "User ' " + login + "' registered. ";
                    break;
                case LOGIN_USER:
                    String token = userService.login(headers);
                    message = "token: " + token;
                    status = HttpStatus.SC_OK;
                    break;
                case LOGOUT_USER:
                    login = userService.logout(headers);
                    message = "User " + login + " logged out. ";
                    status = HttpStatus.SC_OK;
                    break;
                default:
                    throw new NotFoundException("Resource not found.");
            }

        } catch (FailedLoginException | AlreadyExistException e) {
            message = e.getMessage();
            status = HttpStatus.SC_OK;

        } catch (InvalidRequestException | IllegalArgumentException e) {
            message = e.getMessage();
            status = HttpStatus.SC_BAD_REQUEST;

        } catch (NotFoundException e) {
            message = e.getMessage();
            status = HttpStatus.SC_NOT_FOUND;
        }

        response = new BasicHttpResponse(status, EnglishReasonPhraseCatalog.INSTANCE.getReason(status, Locale.US));
        responseTrigger.submitResponse(new BasicResponseProducer(response, message));

    }


}

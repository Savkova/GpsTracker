package com.gpstracker.server.httpserver.controllers;

import com.gpstracker.server.util.Constants.ContextAttributes;
import com.gpstracker.server.httpserver.services.UserService;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.Message;
import org.apache.hc.core5.http.nio.AsyncRequestConsumer;
import org.apache.hc.core5.http.nio.AsyncServerRequestHandler;
import org.apache.hc.core5.http.protocol.HttpContext;

public class UserController implements AsyncServerRequestHandler<Message<HttpRequest, String>> {
    private static final String REGISTER_USER = "register";

    private final UserService userService;

    public UserController() {
        this.userService = UserService.instance;
    }

    @Override
    public AsyncRequestConsumer<Message<HttpRequest, String>> prepare(HttpRequest request, HttpContext context) {
        return null;
    }

    @Override
    public void handle(Message<HttpRequest, String> requestMessage,
                       ResponseTrigger responseTrigger,
                       HttpContext context) {

        String[] pathParts = (String[]) context.getAttribute(ContextAttributes.PATH_PARTS);

        String action = pathParts[2];

        switch (action) {
            case REGISTER_USER:
                userService.registerUser();
                break;

            default:
                throw new IllegalArgumentException("'" + action + "' is not found!");
        }

    }
}

package com.gpstracker.server.httpserver.controllers;

import com.gpstracker.server.util.Constants.ContextAttributes;
import com.gpstracker.server.httpserver.services.UserService;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.Message;
import org.apache.hc.core5.http.nio.AsyncRequestConsumer;
import org.apache.hc.core5.http.nio.AsyncServerRequestHandler;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.IOException;

public class UserController implements AsyncServerRequestHandler<Message<HttpRequest, String>>
{
    private static final String REGISTER_USER = "register";

    private final UserService userService;

    public UserController()
    {
        userService = UserService.instance;
    }

    @Override
    public AsyncRequestConsumer<Message<HttpRequest, String>> prepare(HttpRequest request, HttpContext context) throws HttpException
    {
        return null;
    }

    @Override
    public void handle(Message<HttpRequest, String> requestMessage, ResponseTrigger responseTrigger, HttpContext context) throws HttpException, IOException
    {
        String[] pathParts = (String[]) context.getAttribute(ContextAttributes.PATH_PARTS);

        String action = pathParts[1];

        switch (action)
        {
            case REGISTER_USER:
                userService.registerUser();
                break;

            default:
                throw new IllegalArgumentException("'" + action + "' is not found!");
        }

    }
}

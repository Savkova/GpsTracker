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

public class UserController implements AsyncServerRequestHandler<Message<HttpRequest, String>> {

    private static final String REGISTER_USER = "register";

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

        String path = pathParts[2];

        HttpResponse response;
        int status;
        try {

            if (path.equals(REGISTER_USER)) {
                String token = userService.registerUser(headers);
                status = HttpStatus.SC_OK;
                response = new BasicHttpResponse(status, EnglishReasonPhraseCatalog.INSTANCE.getReason(status, Locale.US));
                responseTrigger.submitResponse(new BasicResponseProducer(response, "token: " + token));
            }

        } catch (InvalidRequestException | AlreadyExistException | IllegalArgumentException e) {
            status = HttpStatus.SC_BAD_REQUEST;
            response = new BasicHttpResponse(status, EnglishReasonPhraseCatalog.INSTANCE.getReason(status, Locale.US));
            responseTrigger.submitResponse(new BasicResponseProducer(response, e.getMessage()));
        }

    }
}

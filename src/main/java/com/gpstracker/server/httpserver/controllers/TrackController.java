package com.gpstracker.server.httpserver.controllers;

import com.gpstracker.server.exceptions.*;
import com.gpstracker.server.httpserver.services.TrackService;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.impl.EnglishReasonPhraseCatalog;
import org.apache.hc.core5.http.message.BasicHttpResponse;
import org.apache.hc.core5.http.nio.*;
import org.apache.hc.core5.http.nio.entity.StringAsyncEntityConsumer;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import com.gpstracker.server.util.Constants.ContextAttributes;

import static com.gpstracker.server.util.Constants.Actions.*;

public class TrackController implements AsyncServerRequestHandler<Message<HttpRequest, String>> {

    private final TrackService trackService;

    public TrackController() {
        this.trackService = TrackService.instance;
    }

    @Override
    public AsyncRequestConsumer<Message<HttpRequest, String>> prepare(final HttpRequest request, final HttpContext context) {
        return new BasicRequestConsumer<>(new StringAsyncEntityConsumer());
    }

    @Override
    public void handle(final Message<HttpRequest, String> requestMessage,
                       final ResponseTrigger responseTrigger,
                       final HttpContext context) throws HttpException, IOException {

        String[] pathParts = (String[]) context.getAttribute(ContextAttributes.PATH_PARTS);
        Map<String, String> headers = (Map<String, String>) context.getAttribute(ContextAttributes.HEADERS);

        String action = "";
        String trackName = "";
        if (pathParts.length > 3) {
            action = pathParts[3];
            trackName = pathParts[2];
        }

        int status;
        String message;
        HttpResponse response;
        try {
            switch (action) {
                case TRACK_REPORT:
                    message = "" + trackService.getTrackPoints(trackName, headers);
                    status = HttpStatus.SC_OK;
                    break;
                case DELETE_TRACK:
                    message = "Track deleted (id = " + trackService.deleteTrack(trackName, headers) + "). ";
                    status = HttpStatus.SC_OK;
                    break;
                default:
                    throw new NotFoundException("Resource not found.");
            }

        } catch (InvalidTokenException e) {
            message = e.getMessage();
            status = HttpStatus.SC_FORBIDDEN;

        } catch (InvalidRequestException e) {
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

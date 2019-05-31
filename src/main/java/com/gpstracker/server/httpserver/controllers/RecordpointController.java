package com.gpstracker.server.httpserver.controllers;

import com.gpstracker.server.exceptions.InvalidRequestException;
import com.gpstracker.server.exceptions.InvalidTokenException;
import com.gpstracker.server.httpserver.services.RecordpointService;
import com.gpstracker.server.util.Constants.ContextAttributes;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.impl.EnglishReasonPhraseCatalog;
import org.apache.hc.core5.http.message.BasicHttpResponse;
import org.apache.hc.core5.http.nio.*;
import org.apache.hc.core5.http.nio.entity.StringAsyncEntityConsumer;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecordpointController implements AsyncServerRequestHandler<Message<HttpRequest, String>> {

    private final RecordpointService recordpointService;

    public RecordpointController() {
        this.recordpointService = RecordpointService.instance;
    }

    @Override
    public AsyncRequestConsumer<Message<HttpRequest, String>> prepare(HttpRequest request, HttpContext context) {
        return new BasicRequestConsumer<>(new StringAsyncEntityConsumer());
    }

    @Override
    public void handle(Message<HttpRequest, String> requestMessage,
                       ResponseTrigger responseTrigger,
                       HttpContext context) throws HttpException, IOException {

        Map<String, List<String>> params = (Map<String, List<String>>) context.getAttribute(ContextAttributes.QUERY_PARAMS);
        Map<String, String> headers = (Map<String, String>) context.getAttribute(ContextAttributes.HEADERS);

        HttpResponse response;
        int status;
        try {
            String message = recordpointService.makeRecord(params, headers) ? "Created new record" : "Track stoped";

            status = HttpStatus.SC_OK;
            response = new BasicHttpResponse(status, EnglishReasonPhraseCatalog.INSTANCE.getReason(status, Locale.US));

            responseTrigger.submitResponse(new BasicResponseProducer(response, message));

        } catch (InvalidRequestException | InvalidTokenException e) {
            status = HttpStatus.SC_BAD_REQUEST;
            response = new BasicHttpResponse(status, EnglishReasonPhraseCatalog.INSTANCE.getReason(status, Locale.US));
            responseTrigger.submitResponse(new BasicResponseProducer(response, e.getMessage()));
        }
    }
}

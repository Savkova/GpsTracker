package com.gpstracker.server.httpserver.controllers;

import com.gpstracker.server.exceptions.InvalidRequestException;
import com.gpstracker.server.exceptions.InvalidTokenException;
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
import com.gpstracker.server.httpserver.services.ReportService;

public class ReportController implements AsyncServerRequestHandler<Message<HttpRequest, String>> {

    private final ReportService reportService;

    public ReportController() {
        this.reportService = ReportService.instance;
    }

    @Override
    public AsyncRequestConsumer<Message<HttpRequest, String>> prepare(final HttpRequest request, final HttpContext context) {
        return new BasicRequestConsumer<>(new StringAsyncEntityConsumer());
    }

    @Override
    public void handle(final Message<HttpRequest, String> requestMessage,
                       final ResponseTrigger responseTrigger,
                       final HttpContext context) throws HttpException, IOException {

        Map<String, String> headers = (Map<String, String>) context.getAttribute(ContextAttributes.HEADERS);

        int status;
        String message;
        HttpResponse response;
        try {
            message = "" + reportService.getTrackPointReport(headers);
            status = HttpStatus.SC_OK;
        } catch (InvalidTokenException e) {
            message = e.getMessage();
            status = HttpStatus.SC_FORBIDDEN;
        } catch (InvalidRequestException e) {
            message = e.getMessage();
            status = HttpStatus.SC_BAD_REQUEST;
        }

        response = new BasicHttpResponse(status, EnglishReasonPhraseCatalog.INSTANCE.getReason(status, Locale.US));
        responseTrigger.submitResponse(new BasicResponseProducer(response, message));

    }


}

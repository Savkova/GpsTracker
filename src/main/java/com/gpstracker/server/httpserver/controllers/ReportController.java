package com.gpstracker.server.httpserver.controllers;

import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.nio.*;
import org.apache.hc.core5.http.nio.entity.BasicAsyncEntityProducer;
import org.apache.hc.core5.http.nio.entity.StringAsyncEntityConsumer;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.IOException;
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

        responseTrigger.submitResponse(new BasicResponseProducer(HttpStatus.SC_OK, new BasicAsyncEntityProducer(
                "" + reportService.getTrackPointReport(headers),
                ContentType.APPLICATION_JSON)));
    }

}

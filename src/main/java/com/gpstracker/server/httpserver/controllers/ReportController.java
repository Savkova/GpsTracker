package com.gpstracker.server.httpserver.controllers;

import com.gpstracker.server.util.Constants.ContextAttributes;
import com.gpstracker.server.httpserver.services.ReportService;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.nio.AsyncRequestConsumer;
import org.apache.hc.core5.http.nio.AsyncServerRequestHandler;
import org.apache.hc.core5.http.nio.BasicRequestConsumer;
import org.apache.hc.core5.http.nio.BasicResponseProducer;
import org.apache.hc.core5.http.nio.entity.BasicAsyncEntityProducer;
import org.apache.hc.core5.http.nio.entity.StringAsyncEntityConsumer;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ReportController implements AsyncServerRequestHandler<Message<HttpRequest, String>> {
    @Override
    public AsyncRequestConsumer<Message<HttpRequest, String>> prepare(final HttpRequest request, final HttpContext context) {
        return new BasicRequestConsumer<>(new StringAsyncEntityConsumer());
    }

    @Override
    public void handle(final Message<HttpRequest, String> requestMessage,
                       final ResponseTrigger responseTrigger,
                       final HttpContext context) throws HttpException, IOException {

        Map<String, List<String>> params = (Map<String, List<String>>) context.getAttribute(ContextAttributes.QUERY_PARAMS);

        responseTrigger.submitResponse(new BasicResponseProducer(HttpStatus.SC_OK, new BasicAsyncEntityProducer(
                "" + ReportService.getTrackPointReport(params),
                ContentType.APPLICATION_JSON)));
    }

}

package com.gpstracker.server.httpserver.filters;

import com.gpstracker.server.util.Constants.Loggers;
import com.gpstracker.server.util.Constants.ContextAttributes;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.message.BasicHttpResponse;
import org.apache.hc.core5.http.nio.AsyncDataConsumer;
import org.apache.hc.core5.http.nio.AsyncFilterChain;
import org.apache.hc.core5.http.nio.AsyncFilterHandler;
import org.apache.hc.core5.http.nio.entity.BasicAsyncEntityProducer;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.net.URLEncodedUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.*;

public class AnalyzeRequestFilter implements AsyncFilterHandler {

    @Override
    public AsyncDataConsumer handle(
            final HttpRequest request,
            final EntityDetails entityDetails,
            final HttpContext context,
            final AsyncFilterChain.ResponseTrigger responseTrigger,
            final AsyncFilterChain chain) throws HttpException, IOException {

        String decodedQueryString;
        String[] pathParts;
        Header[] headers;
        try {
            decodedQueryString = new URI(request.getRequestUri()).getQuery();
            pathParts = new URI(request.getPath()).getPath().split("\\/");
            headers = request.getAllHeaders();
        } catch (URISyntaxException e) {
            responseTrigger.submitResponse(
                    new BasicHttpResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR),
                    new BasicAsyncEntityProducer("{\"code\":10, \"message\":\"Error on query parsing.\"}",
                            ContentType.APPLICATION_JSON));
            Loggers.SERVER_LOGGER.warn("Error on query parsing: " + request.getRequestUri());
            return null;
        }

        List<NameValuePair> queryParameters = URLEncodedUtils.parse(decodedQueryString, Charset.defaultCharset());

        Map<String, List<String>> mapParameters = queryParameters.stream()
                .collect(HashMap::new, (map, valuePair) -> map.computeIfAbsent(valuePair.getName(),
                        key -> new ArrayList<>()).add(valuePair.getValue()), HashMap::putAll);

        context.setAttribute(ContextAttributes.QUERY_PARAMS, mapParameters);

        Map<String, String> mapHeaders = new HashMap<>();
        for (Header header : headers) {
            mapHeaders.put(header.getName(), header.getValue());
        }

        context.setAttribute(ContextAttributes.HEADERS, mapHeaders);

        context.setAttribute(ContextAttributes.PATH_PARTS, pathParts);

        return chain.proceed(request, entityDetails, context, responseTrigger);
    }
}

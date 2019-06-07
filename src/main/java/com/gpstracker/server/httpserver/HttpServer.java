package com.gpstracker.server.httpserver;

import com.gpstracker.server.util.Constants;
import com.gpstracker.server.db.DBInitUtil;
import com.gpstracker.server.httpserver.controllers.RecordpointController;
import com.gpstracker.server.httpserver.controllers.ReportController;
import com.gpstracker.server.httpserver.controllers.UserController;
import com.gpstracker.server.httpserver.filters.AnalyzeRequestFilter;
import org.apache.hc.core5.http.impl.bootstrap.AsyncServerBootstrap;
import org.apache.hc.core5.http.impl.bootstrap.HttpAsyncServer;
import org.apache.hc.core5.http.protocol.UriPatternType;
import org.apache.hc.core5.io.ShutdownType;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.reactor.ListenerEndpoint;
import org.apache.hc.core5.util.TimeValue;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import com.gpstracker.server.util.Constants.MainSettings;
import com.gpstracker.server.util.Constants.Loggers;


public class HttpServer {

    public static void main(String[] args) throws Exception {

        Loggers.SERVER_LOGGER.info("Welcome to the " + MainSettings.configuration.getServer_name() + "!");

        IOReactorConfig config = IOReactorConfig.custom()
                .setSoTimeout(15, TimeUnit.SECONDS)
                .setSoReuseAddress(true)
                .setTcpNoDelay(true)
                .build();

        final HttpAsyncServer server = AsyncServerBootstrap.bootstrap()
                .setUriPatternType(UriPatternType.REGEX)
                .setIOReactorConfig(config)
                .setCanonicalHostName(MainSettings.configuration.getServer_ip())

                .addFilterFirst(Constants.Filters.QUERY_REQUEST_FILTER, new AnalyzeRequestFilter())

                .register(Constants.RequestHandlers.RECORDPOINT_HANDLER, new RecordpointController())
                .register(Constants.RequestHandlers.REPORT_HANDLER, new ReportController())
                .register(Constants.RequestHandlers.USER_HANDLER, new UserController())

                .create();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("HTTP server shutting down");
            server.shutdown(ShutdownType.GRACEFUL);
        }));

        DBInitUtil.initDbConnection();
        DBInitUtil.initDb();

        server.start();

        ListenerEndpoint listenerEndpoint = server.listen(new InetSocketAddress(
                MainSettings.configuration.getServer_ip(),
                MainSettings.configuration.getServer_port()
        )).get();

        Loggers.SERVER_LOGGER.info("Server started: listening on " + listenerEndpoint.getAddress());

        server.awaitShutdown(TimeValue.ofDays(Long.MAX_VALUE));
    }

}


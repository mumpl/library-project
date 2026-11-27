
package monitoring;

import com.sun.net.httpserver.HttpServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class MetricsServer {

    private static final Logger log =
            LoggerFactory.getLogger(MetricsServer.class);

    private final HttpServer server;

    public MetricsServer(LibraryMetrics metrics, int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/metrics", exchange -> {
            String response = metrics.getRegistry().scrape();

            byte[] responseBytes =
                    response.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().set(
                    "Content-Type",
                    "text/plain; version=0.0.4; charset=utf-8"
            );

            exchange.sendResponseHeaders(200, responseBytes.length);

            try (OutputStream output = exchange.getResponseBody()) {
                output.write(responseBytes);
            }
        });
    }

    public void start() {
        server.start();
        log.info(
                "Metrics endpoint: http://localhost:8080/metrics"
        );
    }

    public void stop() {
        server.stop(0);
    }
}

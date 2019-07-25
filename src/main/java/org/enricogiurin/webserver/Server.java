package org.enricogiurin.webserver;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is the Server class based on {@link ServerSocket}.
 */
@Slf4j
public class Server implements Runnable {
    private static final int NUM_THREADS = 100;
    String root;
    int port;
    private ServerSocket server;
    private ExecutorService executorService = null;

    Server(String root, String port) {
        this.root = root;
        this.port = Integer.parseInt(port);
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            log.info("Listening for connections on port: " + port);
            executorService = Executors.newFixedThreadPool(NUM_THREADS);
            while (true) {
                Socket socket = server.accept();
                executorService.execute(new Handler(root, socket));
            }
        } catch (SocketException e) {
            log.error("ServerSocket closed!");

        } catch (IOException e) {
            log.error("Error in the server class", e);
        }
    }

    public void shutdown() throws IOException {
        log.warn("shutdown the server");
        server.close();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}

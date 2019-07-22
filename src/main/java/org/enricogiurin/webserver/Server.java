package org.enricogiurin.webserver;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Server {
    String root;
    int port;
    private ServerSocket server;
    private ExecutorService executorService = null;

    Server(String root, String port) {
        this.root = root;
        this.port = Integer.parseInt(port);
    }


    public void execute() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    log.warn("shutdown the server");
                    if (executorService != null) executorService.shutdown();
                })
        );
        try {
            server = new ServerSocket(port);
            log.info("Listening for connections on port: " + port);
            executorService = Executors.newCachedThreadPool();
            while (!executorService.isShutdown() && !executorService.isTerminated()) {
                Socket socket = server.accept();
                executorService.execute(new Handler(root, socket));
            }
        } catch (IOException e) {
            log.error("Error in the server class", e);
        }
    }

}
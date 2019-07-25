package org.enricogiurin.webserver;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

/**
 * This class is responsible for the client - server communication. *
 */
@Slf4j
public class Handler implements Runnable {
    private static final String STATUS_OK = "HTTP/1.1 200 OK";
    private static final String STATUS_405 = "HTTP/1.1 405 Method Not Allowed";
    private static final String STATUS_404 = "HTTP/1.1 404 Not Found";
    private static final String STATUS_403 = "HTTP/1.1 403 Forbidden";

    private Socket socket;
    private String root;

    public Handler(String root, Socket socket) {
        this.root = root;
        this.socket = socket;
    }

    @Override
    public void run() {
        log.info("Executing socket: " + socket);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream());
             BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream())) {
            String header = in.readLine();
            log.debug("header: " + header);
            HttpRequest httpRequest = new HttpRequest(header);
            log.info(httpRequest.toString());
            if (!httpRequest.isValid()) {
                out.println(STATUS_405);
            } else {
                Path path = Paths.get(root, httpRequest.getUri());
                log.debug("Absolute Path: " + path.toAbsolutePath().toString());
                if (!Files.exists(path)) {
                    out.println(STATUS_404);
                } else if (Files.isDirectory(path)) {
                    out.println(STATUS_403);
                } else {
                    out.println(STATUS_OK);
                    out.println("Date: " + LocalDateTime.now());
                    out.println("Content-Type: " + getContentType(path));
                    out.println("Server: Multithreaded_web-server");
                    out.println("Content-Length: " + Files.size(path));
                    out.println(); // blank line between headers and content, very important !
                    Files.copy(path, bos);
                }
            }
            out.flush(); // flush character output stream buffer
            // file
            bos.flush();

        } catch (IOException e) {
            log.error(e.toString(), e);
        }
    }

    private static String getContentType(Path path) {
        String fileName = path.toString();
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            return "text/html";
        }
        if (fileName.endsWith(".json")) {
            return "application/json";
        }
        return "text/plain";
    }

}

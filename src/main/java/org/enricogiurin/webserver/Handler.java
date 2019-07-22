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

@Slf4j
public class Handler implements Runnable {
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
            HttpRequest httpRequest = new HttpRequest(in.readLine());
            log.debug(httpRequest.toString());
            if (!httpRequest.isValid()) {
                out.println("HTTP/1.1 501 Not Implemented");
            } else {
                Path path = Paths.get(root, httpRequest.getUri());
                log.info("Absolute Path: " + path.toAbsolutePath().toString());
                if (!Files.exists(path)) {
                    out.println("HTTP/1.1 404 File Not Found");
                } else if (Files.isDirectory(path)) {
                    out.println("HTTP/1.1 403 Forbidden");
                } else {
                    out.println("HTTP/1.1 200 OK");
                    out.println(); // blank line between headers and content, very important !
                    Files.copy(path, bos);
                }
            }
            out.flush(); // flush character output stream buffer
            // file
            bos.flush();

        } catch (IOException e) {
            log.error(e.toString(), e);
        } finally {
            try {
                socket.close(); // we close socket connection
            } catch (IOException e) {
                log.error(e.toString(), e);
            }
        }
    }

}

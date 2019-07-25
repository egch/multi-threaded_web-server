package org.enricogiurin.webserver;

public class Main {
    public static void main(String[] args) {
        if (args.length == 2) {
            Server server = new Server(args[0], args[1]);
            Thread serverThread = new Thread(server);
            serverThread.start();
        } else {
            System.out.println("Usage: \njava -jar web-server-<version> <root> <port>");
        }
    }
}

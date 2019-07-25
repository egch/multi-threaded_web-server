package org.enricogiurin.webserver;

import lombok.Getter;
import lombok.ToString;

import static org.enricogiurin.webserver.RequestMethod.GET;

@Getter
@ToString
public class HttpRequest {
    HttpRequest(String header) {
        String[] split = header.split(" ", 3);
        this.method = RequestMethod.valueOf(split[0]);
        this.uri = split[1];
        this.version = split[2];

    }

    private RequestMethod method;
    private String uri;
    private String version;

    /**
     * This version only supports GET requests
     *
     * @return
     */
    public boolean isValid() {
        return this.method == GET;
    }
}

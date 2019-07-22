package org.enricogiurin.webserver;

import lombok.Getter;
import lombok.ToString;

import static org.enricogiurin.webserver.RequestMethod.GET;
import static org.enricogiurin.webserver.RequestMethod.HEAD;

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

    public boolean isValid() {
        if (this.method == GET || this.method == HEAD)
            return true;
        return false;
    }
}

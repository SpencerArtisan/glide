package com.bigcustard.planet.code;

public class InaccessibleUrlException extends RuntimeException {
    public InaccessibleUrlException(String url, Exception cause) {
        super("Could not connect to url " + url, cause);
    }
}

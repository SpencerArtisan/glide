package com.mygdx.game.code;

public class InaccessibleUrlException extends RuntimeException {
    public InaccessibleUrlException(String url, Exception cause) {
        super("Could not connect to url " + url, cause);
    }
}

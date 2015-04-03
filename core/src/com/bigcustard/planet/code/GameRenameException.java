package com.bigcustard.planet.code;

public class GameRenameException extends RuntimeException {
    public GameRenameException(String newName) {
        super("Cannot name planet '" + newName + "'. That name is taken!");
    }
}

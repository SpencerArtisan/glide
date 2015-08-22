package com.bigcustard.glide.code;

public class GameRenameException extends RuntimeException {
    public GameRenameException(String newName) {
        super("Cannot name game '" + newName + "'. That name is taken!");
    }
}

package com.bigcustard.scene2dplus.command;

public interface Command {
    void execute();
    void undo();
    boolean canExecute();
}

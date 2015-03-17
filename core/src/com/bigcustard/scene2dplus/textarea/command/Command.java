package com.bigcustard.scene2dplus.textarea.command;

public interface Command {
    void execute();
    void undo();
    boolean canExecute();
}

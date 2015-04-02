package com.bigcustard.scene2dplus.command;

public abstract class AbstractCommand implements Command {
    @Override
    public void undo() {
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}

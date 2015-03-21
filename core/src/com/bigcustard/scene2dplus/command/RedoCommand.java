package com.bigcustard.scene2dplus.command;

import com.bigcustard.scene2dplus.command.Command;
import com.bigcustard.scene2dplus.command.CommandHistory;

public class RedoCommand implements Command {
    private CommandHistory commandHistory;

    public RedoCommand(CommandHistory commandHistory) {
        this.commandHistory = commandHistory;
    }

    @Override
    public void execute() {
        commandHistory.redo();
    }

    @Override
    public void undo() {
        commandHistory.undo();
    }

    @Override
    public boolean canExecute() {
        return commandHistory.canRedo();
    }
}

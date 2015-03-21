package com.bigcustard.scene2dplus.command;

import com.bigcustard.scene2dplus.command.Command;
import com.bigcustard.scene2dplus.command.CommandHistory;

public class UndoCommand implements Command {
    private CommandHistory commandHistory;

    public UndoCommand(CommandHistory commandHistory) {
        this.commandHistory = commandHistory;
    }

    @Override
    public void execute() {
        commandHistory.undo();
    }

    @Override
    public void undo() {
        commandHistory.redo();
    }

    @Override
    public boolean canExecute() {
        return commandHistory.canUndo();
    }
}

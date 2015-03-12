package com.mygdx.game.textarea.command;

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

package com.mygdx.game.textarea.command;

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

package com.bigcustard.scene2dplus.command;

import java.util.LinkedList;
import java.util.List;

public class CommandHistory {
    private List<Command> executedCommands = new LinkedList<Command>();
    private int lastCommandIndex = -1;

    public boolean canRedo() {
        return lastCommandIndex < executedCommands.size() - 1;
    }

    public void redo() {
        if (canRedo()) {
            Command nextCommand = executedCommands.get(lastCommandIndex + 1);
            lastCommandIndex++;
            nextCommand.execute();
        }
    }

    public boolean canUndo() {
        return lastCommandIndex >= 0;
    }

    public void undo() {
        if (canUndo()) {
            Command lastCommand = executedCommands.get(lastCommandIndex);
            lastCommandIndex--;
            lastCommand.undo();
        }
    }

    public void execute(Command command) {
        if (command != null) {
            clearRedoChain();
            executedCommands.add(command);
            lastCommandIndex++;
            command.execute();
        }
    }

    public void execute(Runnable command, Runnable undo) {
        execute(new Command() {
            @Override
            public void execute() {
                command.run();
            }

            @Override
            public void undo() {
                undo.run();
            }

            @Override
            public boolean canExecute() {
                return true;
            }
        });
    }

    private void clearRedoChain() {
        executedCommands = lastCommandIndex == -1 ? new LinkedList<>() : executedCommands.subList(0, lastCommandIndex + 1);
    }
}

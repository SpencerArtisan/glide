package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.CodeRunner;
import com.bigcustard.planet.code.Game;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.bigcustard.scene2dplus.textarea.command.AbstractCommand;

public class RunCommand extends AbstractCommand {
    private Game game;

    public RunCommand(TextAreaModel model, Game game) {
        super(model);
        this.game = game;
    }

    @Override
    public void execute() {
        game.run();
    }

    @Override
    public boolean canExecute() {
        return game.isValid();
    }
}

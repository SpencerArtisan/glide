package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;

public abstract class AbstractCommand implements Command {
    protected TextAreaModel model;

    public AbstractCommand(TextAreaModel model) {
        this.model = model;
    }
}

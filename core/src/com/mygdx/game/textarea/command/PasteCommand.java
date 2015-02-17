package com.mygdx.game.textarea.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import com.mygdx.game.textarea.TextAreaModel;

public class PasteCommand extends AbstractCommand {
    public PasteCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public void execute() {
        model.insert(getClipboard().getContents());
    }

    public Clipboard getClipboard() {
        return Gdx.app.getClipboard();
    }
}

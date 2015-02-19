package com.mygdx.game.textarea.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import com.mygdx.game.textarea.TextAreaModel;

public class CopyCommand extends AbstractCommand {
    public CopyCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public void execute() {
        if (model.caret().isAreaSelected()) {
            getClipboard().setContents(model.getSelection());
        } else {
            getClipboard().setContents(model.getCurrentLine() + "\n");
        }
    }

    public Clipboard getClipboard() {
        return Gdx.app.getClipboard();
    }
}

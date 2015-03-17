package com.bigcustard.scene2dplus.textarea.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;

public class CopyCommand extends AbstractCommand {
    public CopyCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public boolean canExecute() {
        return model.caret().isAreaSelected();
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

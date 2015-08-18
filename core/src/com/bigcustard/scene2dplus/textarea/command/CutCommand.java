package com.bigcustard.scene2dplus.textarea.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import com.bigcustard.scene2dplus.XY;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.google.common.annotations.VisibleForTesting;

public class CutCommand extends AbstractTextAreaCommand {
    public CutCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public boolean canExecute() {
        return model.caret().isAreaSelected();
    }

    @Override
    public void execute() {
        TextAreaModel.Caret caret = model.caret();
        if (!caret.isAreaSelected()) {
            int y = caret.location().y;
            caret.setSelection(new XY(0, y), new XY(0, y + 1));
        }

        getClipboard().setContents(model.getSelection());
        model.deleteCharacter();
    }

    @VisibleForTesting
    protected Clipboard getClipboard() {
        return Gdx.app.getClipboard();
    }
}

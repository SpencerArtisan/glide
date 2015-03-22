package com.bigcustard.scene2dplus.textarea;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bigcustard.scene2dplus.XY;
import com.bigcustard.scene2dplus.command.CommandHistory;

public class ScrollableTextArea extends ScrollPane {
    public ScrollableTextArea(TextAreaModel model, Skin skin, CommandHistory commandHistory) {
        super(new TextArea(model, skin), skin);

        TextAreaController controller = new TextAreaController(model, this, commandHistory);
        addListener(controller);
        pack();
        setFadeScrollBars(false);
        setFlickScroll(false);
    }

    public TextArea textArea() {
        return (TextArea) getWidget();
    }

    public void onModelChange(TextAreaModel model) {
        scrollPaneToShowCurrentCaret(model);
    }

    public XY<Integer> worldPositionToCaretLocation(XY<Integer> worldXY) {
        return textArea().worldPositionToCaretLocation(worldXY);
    }

    private boolean isOffBottom(XY<Integer> location) {
        return location.y < textArea().getRowHeight();
    }

    private boolean isOffTop(XY<Integer> location) {
        return location.y + textArea().getRowHeight() > getHeight();
    }

    private void scrollPaneToShowCurrentCaret(TextAreaModel model) {
        XY<Integer> caretPosition = textArea().caretLocationToPosition(model.caret().location());
        if (isOffBottom(caretPosition)) {
            float newScrollY = (model.caret().location().y + 2) * textArea().getRowHeight() - getHeight();
            setScrollY(newScrollY);
        } else if (isOffTop(caretPosition)) {
            float newScrollY = (model.caret().location().y - 2) * textArea().getRowHeight();
            setScrollY(newScrollY);
        }
    }
}

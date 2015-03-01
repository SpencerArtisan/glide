package com.mygdx.game.textarea;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.XY;
import com.mygdx.game.textarea.command.CommandHistory;

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

    private boolean isOffBottom(XY<Integer> location) {
        return location.y < textArea().getRowHeight();
    }

    private boolean isOffTop(XY<Integer> location) {
        return location.y + textArea().getRowHeight() > getHeight();
    }

    public XY<Integer> worldPositionToCaretLocation(XY<Integer> worldXY) {
        return textArea().worldPositionToCaretLocation(worldXY);
    }

    private void scrollPaneToShowCurrentCaret(TextAreaModel model) {
        XY<Integer> caretPosition = textArea().caretLocationToPosition(model.caret().location());
        if (isOffBottom(caretPosition)) {
            float newScrollY = (model.caret().location().y + 2) * textArea().getRowHeight() - getHeight();
            setScrollY(newScrollY);
        } else if (isOffTop(caretPosition)) {
            System.out.println("OFF TOP");
            float newScrollY = (model.caret().location().y - 2) * textArea().getRowHeight();
            setScrollY(newScrollY);
        }
    }
}

package com.bigcustard.scene2dplus.textarea;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bigcustard.scene2dplus.XY;
import com.bigcustard.scene2dplus.command.CommandHistory;

public class ScrollableTextArea extends ScrollPane {
    public ScrollableTextArea(TextAreaModel model, Skin skin, CommandHistory commandHistory, String style) {
        super(new TextArea(model, skin, style), skin);

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

    public XY worldPositionToCaretLocation(XY worldXY) {
        XY deltaXY = new XY(-getScrollX(),
                            getHeight() - textArea().getHeight() + getScrollY());
        return textArea().worldPositionToCaretLocation(worldXY.subtract(deltaXY));
    }

    private void scrollPaneToShowCurrentCaret(TextAreaModel model) {
        XY caretPosition = textArea().caretLocationToPosition(model.caret().location());
        scrollVerticallyToShowCaret(model, caretPosition);
        scrollHorizontallyToShowCaret(model, caretPosition);
    }

    private void scrollVerticallyToShowCaret(TextAreaModel model, XY caretPosition) {
        if (isOffBottom(caretPosition)) {
            float newScrollY = (model.caret().location().y + 2) * textArea().getRowHeight() - getHeight();
            setScrollY(newScrollY);
        } else if (isOffTop(caretPosition)) {
            float newScrollY = (model.caret().location().y - 2) * textArea().getRowHeight();
            setScrollY(newScrollY);
        }
    }

    private void scrollHorizontallyToShowCaret(TextAreaModel model, XY caretPosition) {
        if (isOffRight(caretPosition)) {
            textArea().setWidth(textArea().getWidth() + textArea().getColumnWidth() * 2);
            float newScrollX = (model.caret().location().x + 4) * textArea().getColumnWidth() - getWidth();
            setScrollX(newScrollX);
        } else if (isOffLeft(caretPosition)) {
            float newScrollX = (model.caret().location().x - 2) * textArea().getColumnWidth();
            setScrollX(newScrollX);
        }
    }

    private boolean isOffRight(XY location) {
        return location.x + textArea().getColumnWidth() * 3 > getWidth();
    }

    private boolean isOffLeft(XY location) {
        return location.x < textArea().getColumnWidth();
    }

    private boolean isOffBottom(XY location) {
        return location.y < textArea().getRowHeight();
    }

    private boolean isOffTop(XY location) {
        return location.y + textArea().getRowHeight() > getHeight();
    }
}

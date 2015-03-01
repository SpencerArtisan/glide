package com.mygdx.game.textarea;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.XY;

public class ScrollableTextArea extends ScrollPane {
    public ScrollableTextArea(TextAreaModel model, Skin skin, Viewport viewport) {
        super(createTextArea(model, skin, viewport), skin);
        pack();
        setFadeScrollBars(false);
        setFlickScroll(false);
        TextAreaController controller = new TextAreaController(model, this);
        addListener(controller);
    }

    private static Actor createTextArea(TextAreaModel model, Skin skin, Viewport viewport) {
        TextArea textArea = new TextArea(model, skin);
        textArea.setWidth(viewport.getWorldWidth());
        textArea.setHeight(viewport.getWorldHeight());
        return textArea;
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

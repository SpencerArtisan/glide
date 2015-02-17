package com.mygdx.game.textarea;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.mygdx.game.XY;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Set;

public class TextArea extends Actor {
    private static final int TOP_MARGIN = 30;
    private static final int LEFT_MARGIN = 8;
    private TextAreaModel model;
    private TextAreaController controller;
    private TextAreaStyle style;

    public TextArea(TextAreaModel model, Skin skin) {
        this.model = model;
        controller = new TextAreaController(model, this);
        style = skin.get(TextAreaStyle.class);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawBackground(batch);
        drawCurrentLineBackground(batch);
        drawErrorLineBackgrounds(batch);
        drawSelectionBackground(batch);
        drawText(batch);
        drawCaret(batch);
    }

    private void drawBackground(Batch batch) {
        style.background.draw(batch, 0, 0, getWidth(), getHeight());
    }

    private void drawCurrentLineBackground(Batch batch) {
        if (model.caret().selection() == null) {
            XY<Integer> topLeftCurrent = caretLocationToPosition(new XY<Integer>(0, model.caret().location().y));
            style.focusedBackground.draw(batch, 0, topLeftCurrent.y, getWidth(), getRowHeight());
        }
    }

    private void drawErrorLineBackgrounds(Batch batch) {
        Set<Integer> errorLines = model.getErrorLines();
        for (Integer errorLine : errorLines) {
            XY<Integer> topLeftCurrent = caretLocationToPosition(new XY<Integer>(0, errorLine));
            style.errorBackground.draw(batch, 0, topLeftCurrent.y, getWidth(), getRowHeight());
        }
    }

    private void drawSelectionBackground(Batch batch) {
        Pair<XY<Integer>, XY<Integer>> selection = model.caret().selection();
        if (selection != null) {
            XY<Integer> topLeftSelection = caretLocationToPosition(selection.getLeft());
            XY<Integer> bottomRightSelection = caretLocationToPosition(selection.getRight());
            if (selection.getLeft().y != selection.getRight().y) {
                style.selection.draw(batch, topLeftSelection.x, topLeftSelection.y, getWidth() - topLeftSelection.x, getRowHeight());
                style.selection.draw(batch, 0, bottomRightSelection.y + getRowHeight(), getWidth(), topLeftSelection.y - bottomRightSelection.y - getRowHeight());
                style.selection.draw(batch, 0, bottomRightSelection.y, bottomRightSelection.x, getRowHeight());
            } else {
                style.selection.draw(batch, topLeftSelection.x, topLeftSelection.y, bottomRightSelection.x - topLeftSelection.x, getRowHeight());
            }
        }
    }

    private void drawText(Batch batch) {
        style.font.setMarkupEnabled(true);
        style.font.drawMultiLine(batch, model.getColoredText(), 10, 740);
    }

    private void drawCaret(Batch batch) {
        Drawable caretImage = style.cursor;
        XY<Integer> caretPosition = caretLocationToPosition(model.caret().location());
        caretImage.draw(batch, caretPosition.x, caretPosition.y, caretImage.getMinWidth(), getRowHeight());
    }

    private float getRowHeight() {
        return style.font.getLineHeight();
    }

    private float getColumnWidth() {
        return 14;
    }

    public InputProcessor getController() {
        return controller;
    }

    public XY<Integer> caretLocationToPosition(XY<Integer> caret) {
        float x = LEFT_MARGIN + caret.x * getColumnWidth();
        float y = this.getHeight() - TOP_MARGIN - caret.y * getRowHeight();
        return new XY<Integer>((int) x, (int) y);
    }

    public XY<Integer> screenPositionToCaretLocation(XY<Integer> screenPosition) {
        Vector3 pos = new Vector3(screenPosition.x, screenPosition.y, 0);
        Vector3 worldPosition = this.getStage().getCamera().unproject(pos);

        float caretX = (worldPosition.x - LEFT_MARGIN) / getColumnWidth();
        float caretY = (this.getHeight() - TOP_MARGIN + 22 - worldPosition.y) / getRowHeight();
        return new XY<Integer>((int) caretX, (int) caretY);
    }

    static public class TextAreaStyle extends TextField.TextFieldStyle {
        public Drawable errorBackground;
    }
}

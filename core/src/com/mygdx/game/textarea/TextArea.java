package com.mygdx.game.textarea;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.XY;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

public class TextArea extends Actor {
    private static final int TOP_MARGIN = 29;
    private static final int LEFT_MARGIN = 8;
    private final TextureRegionDrawable white;
    private TextAreaModel model;
    private TextAreaController controller;
    private TextField.TextFieldStyle style;

    public TextArea(TextAreaModel model, Skin skin) {
        this.model = model;
        controller = new TextAreaController(model, this);
        addListener(controller);
        style = skin.get(TextField.TextFieldStyle.class);
        white = (TextureRegionDrawable) skin.getDrawable("white");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawBackground(batch);
        drawCurrentLineBackground(batch);
        drawOtherLineBackgrounds(batch);
        drawSelectionBackground(batch);
        drawText(batch);
        drawCaret(batch);
    }

    private void drawBackground(Batch batch) {
        style.background.draw(batch, getX(), getY(), getWidth(), getHeight());
    }

    private void drawCurrentLineBackground(Batch batch) {
        if (model.caret().selection() == null) {
            XY<Integer> topLeftCurrent = caretLocationToPosition(new XY<Integer>(0, model.caret().location().y));
            style.focusedBackground.draw(batch, 0, topLeftCurrent.y, getWidth(), getRowHeight());
        }
    }

    private void drawOtherLineBackgrounds(Batch batch) {
        Map<Integer, Color> coloredLines = model.getColoredLines();
        for (Map.Entry<Integer, Color> colorLine : coloredLines.entrySet()) {
            SpriteDrawable background = white.tint(colorLine.getValue());
            XY<Integer> topLeftCurrent = caretLocationToPosition(new XY<Integer>(0, colorLine.getKey()));
            background.draw(batch, 0, topLeftCurrent.y, getWidth(), getRowHeight());
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
        XY<Integer> textStart = caretLocationToPosition(new XY<Integer>(0, 0));
        style.font.drawMultiLine(batch, model.getColoredText(), textStart.x, textStart.y + 21);
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

    public XY<Integer> caretLocationToPosition(XY<Integer> caret) {
        float x = LEFT_MARGIN + getX() + caret.x * getColumnWidth();
        float y = -TOP_MARGIN + getHeight() + getY() - caret.y * getRowHeight();
        return new XY<Integer>((int) x, (int) y);
    }

    public XY<Integer> worldPositionToCaretLocation(XY<Integer> worldXY) {
        float caretX = (worldXY.x  - LEFT_MARGIN) / getColumnWidth();
        float caretY = (this.getHeight()  - TOP_MARGIN + 16 - worldXY.y) / getRowHeight();
        return new XY<Integer>((int) caretX, (int) caretY);
    }
}

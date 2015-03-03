package com.mygdx.game;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;

public class CellAccessor implements TweenAccessor<Cell> {
    public static final int PAD_LEFT = 0;

    @Override
    public int getValues(Cell cell, int tweenType, float[] returnValues) {
        returnValues[0] = cell.getPadLeft();
        return 1;
    }

    @Override
    public void setValues(Cell cell, int tweenType, float[] newValues) {
        cell.padLeft(newValues[0]);
    }
}

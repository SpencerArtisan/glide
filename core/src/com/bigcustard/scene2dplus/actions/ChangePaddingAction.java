package com.bigcustard.scene2dplus.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class ChangePaddingAction extends TemporalAction {
    private Cell<Label> cell;
    private float startX;
    private final float end;

    public ChangePaddingAction(Cell<Label> cell, float end, float duration, Interpolation interpolation) {
        this.cell = cell;
        this.end = end;
        setDuration(duration);
        setInterpolation(interpolation);
    }

    protected void begin () {
        startX = cell.getPadLeft();
    }

    protected void update (float percent) {
        cell.padLeft(startX + (end - startX) * percent);
        cell.getTable().invalidate();
    }
}

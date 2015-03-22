package com.bigcustard.scene2dplus;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Spacer extends Actor {
    private float pixels;

    public Spacer(float pixels) {
        this.pixels = pixels;
    }

    public float getWidth() {
        return pixels;
    }

    public float getHeight() {
        return pixels;
    }
}

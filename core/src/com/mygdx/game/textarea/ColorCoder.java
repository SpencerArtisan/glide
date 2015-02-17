package com.mygdx.game.textarea;

import com.badlogic.gdx.graphics.Color;

import java.util.Map;

public interface ColorCoder {
	String encode(String text);
    Map<Integer, Color> colorLines(String text);
}

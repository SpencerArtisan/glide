package com.bigcustard.scene2dplus.textarea;

import com.badlogic.gdx.graphics.Color;

import java.util.HashMap;
import java.util.Map;

public class NullColorCoder implements ColorCoder {
    @Override
    public String encode(String text) {
        return text;
    }

    @Override
    public Map<Integer, Color> colorLines(String text) {
        return new HashMap<>();
    }
}

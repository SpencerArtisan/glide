package com.bigcustard.planet.language;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class FrameworkKeywords implements Keywords {
    @Override
    public String[] get() {
        return new String[] {
                "keyboard", "isKeyPressed", "Key", "blurp", "blurpify", "createTextSprite",
                "createImageSprite", "utils", "random", "scale", "Key_Left", "Key_Down", "Key_Up", "Key_Right",
                "Key_Space", "rotateBy", "colour", "setPosition", "moveTowards", "scale", "flipX", "flipY",
                "position", "x", "y", "add", "Colours", "text"};
    }
}

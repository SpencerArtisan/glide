package com.bigcustard.glide.desktop;

import com.bigcustard.blurp.ui.MouseWindowChecker;
import org.lwjgl.input.Mouse;

public class LwjglMouseWindowChecker implements MouseWindowChecker {

    @Override
    public boolean isInsideWindow() {

        return Mouse.isInsideWindow();
    }
}

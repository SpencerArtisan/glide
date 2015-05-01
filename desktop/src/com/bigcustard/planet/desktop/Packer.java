package com.bigcustard.planet.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class Packer {
    public static void main(String[] args) {
        TexturePacker.process("android/assets/images", "android/assets/theme", "uiskin");
    }
}

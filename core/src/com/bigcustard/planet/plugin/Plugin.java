package com.bigcustard.planet.plugin;

import com.bigcustard.planet.code.Syntax;
import com.bigcustard.scene2dplus.textarea.ColorCoder;

/**
 * Created by spencerward on 02/04/2015.
 */
public interface Plugin {
    Syntax syntax();
    ColorCoder colorCoder();
}

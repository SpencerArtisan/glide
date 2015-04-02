package com.bigcustard.planet.plugin.groovy;

import com.bigcustard.planet.code.Syntax;
import com.bigcustard.planet.plugin.Plugin;
import com.bigcustard.scene2dplus.textarea.ColorCoder;

/**
 * Created by spencerward on 02/04/2015.
 */
public class GroovyPlugin implements Plugin {
    @Override
    public Syntax syntax() {
        return new GroovySyntax();
    }

    @Override
    public ColorCoder colorCoder() {
        return new GroovyColorCoder();
    }
}

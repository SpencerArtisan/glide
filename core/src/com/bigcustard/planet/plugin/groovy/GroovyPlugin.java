package com.bigcustard.planet.plugin.groovy;

import com.bigcustard.planet.code.Runner;
import com.bigcustard.planet.code.Syntax;
import com.bigcustard.planet.plugin.Plugin;

public class GroovyPlugin implements Plugin {
    @Override
    public Syntax syntax() {
        return new GroovySyntax();
    }

    @Override
    public String language() {
        return "groovy";
    }
}

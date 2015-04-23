package com.bigcustard.planet.plugin.jruby;

import com.bigcustard.planet.code.Syntax;
import com.bigcustard.planet.plugin.Plugin;

public class JRubyPlugin implements Plugin {
    @Override
    public Syntax syntax() {
        return new JRubySyntax();
    }

    @Override
    public String language() {
        return "jruby";
    }
}

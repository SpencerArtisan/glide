package com.bigcustard.planet.plugin;

import com.bigcustard.planet.code.Runner;
import com.bigcustard.planet.code.Syntax;

public interface Plugin {
    Syntax syntax();
    Runner runner();
}

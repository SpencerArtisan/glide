package com.bigcustard.planet.code;

import java.util.Set;

public interface Syntax {
    boolean isValid(String code);
    Set<Integer> errorLines(String program);
}

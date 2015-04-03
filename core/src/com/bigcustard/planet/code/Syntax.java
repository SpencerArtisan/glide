package com.bigcustard.planet.code;

import java.util.List;
import java.util.Set;

public interface Syntax {
    boolean isValid(String program);
    Set<Integer> errorLines(String program);
    List<SyntaxPart> parse(String program);
}

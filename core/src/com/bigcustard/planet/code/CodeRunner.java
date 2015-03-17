package com.bigcustard.planet.code;

import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;

public class CodeRunner {
    public void run(String code) {
    }

    public boolean isValid(String code) {
        try {
            new GroovyClassLoader().parseClass(code);
            return true;
        } catch (MultipleCompilationErrorsException e) {
            return false;
        }
    }
}

package com.bigcustard.glide.code.language;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.python.core.PyBaseCode;
import org.python.core.PyBaseException;
import org.python.core.PyException;
import org.python.core.PyFrame;
import org.python.core.PyObject;
import org.python.core.PySyntaxError;
import org.python.core.PyTraceback;
import org.python.core.ThreadState;

import static org.assertj.core.api.Assertions.assertThat;

public class PythonTest {
    private Python subject = new Python();

    @Test
    public void errorLine() {
        PyException error = complexError();

        Pair<Integer, String> details = subject.locateError(error);
        assertThat(details.getRight()).isEqualTo("bad thing");
        assertThat(details.getLeft()).isEqualTo(42);
    }

    @Test
    public void errorLineInCause() {
        RuntimeException error = new RuntimeException("", complexError());

        Pair<Integer, String> details = subject.locateError(error);
        assertThat(details.getRight()).isEqualTo("bad thing");
        assertThat(details.getLeft()).isEqualTo(42);
    }

    @Test
    public void syntaxError() {
        PySyntaxError error = new PySyntaxError("a", 42, 10, "line text", "file");
        Pair<Integer, String> details = subject.locateError(error);
        assertThat(details.getRight()).isEqualTo("Syntax error");
        assertThat(details.getLeft()).isEqualTo(42);
    }

    private PyException complexError() {
        PyBaseException pyObject = new PyBaseException();
        pyObject.setMessage(new PyObject() {
            public String toString() {
                return "bad thing";
            }
        });
        PyFrame frame = new PyFrame(new PyBaseCode(){
            protected PyObject interpret(PyFrame pyFrame, ThreadState threadState) {
                return null;
            }
            protected int getline(PyFrame f) {
                return 42;
            }
        }, null);
        PyTraceback traceback = new PyTraceback(null, frame);
        return new PyException(null, pyObject, traceback);
    }

}

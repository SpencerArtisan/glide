package com.bigcustard.glide.code.language;

import com.bigcustard.glide.language.PythonKeywords;
import org.apache.commons.lang3.tuple.Pair;
import org.python.core.PyBaseException;
import org.python.core.PyBaseExceptionDerived;
import org.python.core.PyException;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PySyntaxError;
import org.python.core.PyTraceback;

public class Python extends Language {
    public static final String TEMPLATE = "##  My Game written by me!  2016";

    public Python() {
        super(new PythonKeywords(), "py", "python-button", TEMPLATE);
    }

    @Override
    public Pair<Integer, String> errorChecker(String code) {
        return null;
    }

    public Pair<Integer, String> locateError(Throwable throwable) {
        throwable = getOriginalCause(throwable);
        if (throwable instanceof PySyntaxError) {
            return handlePySyntaxError((PySyntaxError) throwable);
        } else if (throwable instanceof PyException) {
            return handlePyException((PyException) throwable);
        }
        return null;
    }

    private Pair<Integer, String> handlePySyntaxError(PySyntaxError throwable) {
        PyBaseExceptionDerived value = (PyBaseExceptionDerived) throwable.value;
        PyInteger line = (PyInteger) value.getSlot(2);

//        PyTuple embedded = (PyTuple) value.get(1);
        return Pair.of(line.asInt(), "Syntax error");
    }

    private Pair<Integer, String> handlePyException(PyException throwable) {
        PyException pyError = throwable;
        String message = "";
        PyObject value = pyError.value;
        if (value instanceof PyBaseException) {
            message = ((PyBaseException) value).getMessage().toString();
        } else {
            message = value.toString().substring(1 + value.toString().indexOf(":"));
        }
        PyTraceback traceback = pyError.traceback;
        int line = -99;
        if (traceback != null) {
            line = traceback.tb_lineno;
            while (traceback.tb_next instanceof PyTraceback) {
                traceback = (PyTraceback) traceback.tb_next;
                line = traceback.tb_lineno;
            }
        }
        return Pair.of(line, message);
    }

    private Throwable getOriginalCause(Throwable throwable) {
        return throwable == null ? null :
                throwable.getCause() == null ? throwable :
                        getOriginalCause(throwable.getCause());
    }

}

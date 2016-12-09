package com.bigcustard.glide.code.language;

import com.bigcustard.blurp.core.BlurpException;
import com.bigcustard.glide.language.PythonKeywords;
import org.apache.commons.lang3.tuple.Pair;
import org.python.core.PyBaseException;
import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.core.PyTraceback;
import org.python.core.PyTuple;

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
        if (throwable != null) {
            try {
                Throwable cause = getOriginalCause(throwable);
                if (cause instanceof PyException) {
                    PyTraceback traceback = ((PyException) cause).traceback;
                    int line = -99;
                    if (traceback != null) {
                        line = traceback.tb_lineno;
                        while (traceback.tb_next instanceof PyTraceback) {
                            traceback = (PyTraceback) traceback.tb_next;
                            line = traceback.tb_lineno;
                        }
                    }
                    String message = "Error";
                    PyObject value = ((PyException) cause).value;
                    if (value instanceof PyBaseException) {
                        PyTuple args = (PyTuple) ((PyBaseException) value).args;
                        message = args.get(0).toString();
                        if (line == -99 && args.size() > 0) line = Integer.parseInt(((PyTuple) args.get(1)).get(1).toString());
                    } else {
                        message = value.toString().substring(1 + value.toString().indexOf(":"));
                    }
                    return Pair.of(line, message);
                } else if (cause instanceof BlurpException) {
                    return Pair.of(-99, cause.getMessage());
                }
            } catch (Exception e) {
            }
            return Pair.of(-99, throwable.getMessage());
        }
        return null;
    }


    private Throwable getOriginalCause(Throwable throwable) {
        return throwable == null ? null :
                throwable.getCause() == null ? throwable :
                        getOriginalCause(throwable.getCause());
    }

}

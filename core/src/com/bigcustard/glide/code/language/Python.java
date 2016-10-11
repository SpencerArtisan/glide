package com.bigcustard.glide.code.language;

import com.bigcustard.glide.language.JavascriptKeywords;
import com.bigcustard.glide.language.PythonKeywords;
import com.google.common.base.Strings;
import org.apache.commons.lang3.tuple.Pair;
import org.python.core.PyBaseException;
import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.core.PyObjectDerived;
import org.python.core.PyTraceback;

public class Python extends Language {
    public static final String TEMPLATE =
            "##################################### \n"
          + "##       Welcome to GLIDE!         ## \n"
          + "##  Start writing your game below  ## \n"
          + "## Look at Samples for inspiration ## \n"
          + "##################################### \n\n";

    public Python() {
        super(new PythonKeywords(), "py", "python-button", TEMPLATE);
    }

    @Override
    public Pair<Integer, String> errorChecker(String code) {
        return null;
    }

    public Pair<Integer, String> locateError(Throwable throwable) {
        if (throwable instanceof PyException) {
            PyException pyError = (PyException) throwable;
            String message = "";
            PyObject value = pyError.value;
            if (value instanceof PyBaseException) {
                message = ((PyBaseException) value).getMessage().toString();
            } else {
                message = value.toString().substring(1 + value.toString().indexOf(":"));
            }
            PyTraceback traceback = pyError.traceback;
            int line = traceback.tb_lineno;
            while (traceback.tb_next instanceof PyTraceback) {
                traceback = (PyTraceback) traceback.tb_next;
                line = traceback.tb_lineno;
            }
            return Pair.of(line, message);
        }
        return null;
    }

}

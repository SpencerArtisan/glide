package com.bigcustard.glide.code.language;

import com.bigcustard.scene2dplus.textarea.NullColorCoder;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GroovyTest {
    private TextAreaModel model = new TextAreaModel(new NullColorCoder());
    private Groovy subject = new Groovy();

    @Test
    public void enterOnNormalLine() {
        model.insert("hello");
        String veto = subject.vetoPreInsert("\n", model);
        assertThat(veto).isEqualTo("\n");
    }

    @Test
    public void enterOnLineEndingInBraceAddsClosingBracesAutomatically() {
        model.insert("hello {");
        String veto = subject.vetoPreInsert("\n", model);
        assertThat(veto).isEqualTo("\n    $END$");
    }

    @Test
    public void enterOnLineEndingInBraceAddsClosingBracesInCorrectColumn() {
        model.insert("  hello {");
        String veto = subject.vetoPreInsert("\n  ", model);
        assertThat(veto).isEqualTo("\n      $END$");
    }

    @Test
    public void itShould_NotHaveErrorsInValidGroovy() {
        Pair<Integer, String> errors = subject.errorChecker("if (true) { System.out.println(42); }");
        assertThat(errors).isNull();
    }

    @Test
    public void itShould_HaveErrorsInInvalidGroovy() {
        Pair<Integer, String> errors = subject.errorChecker("}");
        assertThat(errors).isEqualTo(Pair.of(0, "unexpected token: }"));
    }
}

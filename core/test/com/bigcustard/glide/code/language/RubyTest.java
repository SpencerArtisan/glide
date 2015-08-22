package com.bigcustard.glide.code.language;

import com.bigcustard.scene2dplus.textarea.NullColorCoder;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RubyTest {
    private TextAreaModel model = new TextAreaModel(new NullColorCoder());
    private Ruby subject = new Ruby();

    @Test
    public void enterOnNormalLine() {
        model.insert("hello");
        String veto = subject.vetoPreInsert("\n", model);
        assertThat(veto).isEqualTo("\n");
    }

    @Test
    public void enterOnLineEndingInDoAddsClosingEndAutomatically() {
        model.insert("hello do");
        String veto = subject.vetoPreInsert("\n", model);
        assertThat(veto).isEqualTo("\n    $END$\nend");
    }

    @Test
    public void enterOnLineEndingInBraceAddsClosingBracesInCorrectColumn() {
        model.insert("  hello do");
        String veto = subject.vetoPreInsert("\n  ", model);
        assertThat(veto).isEqualTo("\n      $END$\n  end");
    }

    @Test
    public void itShould_NotHaveErrorsInValidGroovy() {
        Pair<Integer, String> errors = subject.errorChecker("if True\nputs 42\nend");
        assertThat(errors).isNull();
    }

    @Test
    public void itShould_HaveErrorsInInvalidGroovy() {
        Pair<Integer, String> errors = subject.errorChecker("puts \"hello");
        assertThat(errors).isEqualTo(Pair.of(0, "unterminated string meets end of file"));
    }
}

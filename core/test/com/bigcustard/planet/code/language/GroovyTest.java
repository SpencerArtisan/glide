package com.bigcustard.planet.code.language;

import com.bigcustard.scene2dplus.textarea.NullColorCoder;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
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
        assertThat(veto).isEqualTo("\n    $END$\n}");
    }

    @Test
    public void enterOnLineEndingInBraceAddsClosingBracesInCorrectColumn() {
        model.insert("  hello {");
        String veto = subject.vetoPreInsert("\n  ", model);
        assertThat(veto).isEqualTo("\n      $END$\n  }");
    }
}

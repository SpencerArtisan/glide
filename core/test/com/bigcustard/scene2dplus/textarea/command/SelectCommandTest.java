package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.XY;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.bigcustard.scene2dplus.textarea.XYAssert;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SelectCommandTest {
    private TextAreaModel model;
    private SelectCommand command;

    @Before
    public void before() {
        model = new TextAreaModel("hello\nthere", null);
        command = new SelectCommand(model, new XY(3, 0), new XY(2, 1));
    }

    @Test
    public void execute() {
        command.execute();
        assertThat(model.caret().isAreaSelected()).isTrue();
        XYAssert.assertThat(model.caret().selection().getLeft()).at(3, 0);
        XYAssert.assertThat(model.caret().selection().getRight()).at(2, 1);
    }
}

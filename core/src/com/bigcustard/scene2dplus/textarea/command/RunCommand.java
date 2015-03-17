package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.planet.code.CodeRunner;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;

public class RunCommand extends AbstractCommand {
    private CodeRunner codeRunner;

    public RunCommand(TextAreaModel model, CodeRunner codeRunner) {
        super(model);
        this.codeRunner = codeRunner;
    }

    @Override
    public void execute() {
        codeRunner.run(model.getText());
    }

    @Override
    public boolean canExecute() {
        return codeRunner.isValid(model.getText());
    }
}

package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.CodeRunner;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.bigcustard.scene2dplus.textarea.command.AbstractCommand;

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

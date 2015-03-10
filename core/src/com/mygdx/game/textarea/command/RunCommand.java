package com.mygdx.game.textarea.command;

import com.mygdx.game.code.CodeRunner;
import com.mygdx.game.textarea.TextAreaModel;
import org.apache.commons.lang3.StringUtils;

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

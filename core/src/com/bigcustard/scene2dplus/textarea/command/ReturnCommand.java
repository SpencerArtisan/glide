package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.textarea.TextAreaModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReturnCommand extends AbstractTextAreaCommand {
    public ReturnCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public void execute() {
        Matcher matcher = Pattern.compile("(\\s*)\\S.*").matcher(model.getCurrentLine());
        model.insert("\n");
        if (matcher.matches()) {
            model.insert(matcher.group(1));
        }
    }
}

package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import org.apache.commons.lang3.StringUtils;

public class TabCommand extends AbstractCommand {
    private String spaces;

    public TabCommand(TextAreaModel model) {
        super(model);
        int column = model.caret().location().x;
        int toColumn = (column / 4) * 4 + 4;
        spaces = StringUtils.repeat(" ", toColumn - column);
    }

    @Override
    public void execute() {
        model.insert(spaces);
    }
}

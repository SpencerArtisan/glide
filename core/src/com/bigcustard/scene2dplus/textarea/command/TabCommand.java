package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import org.apache.commons.lang3.StringUtils;

public class TabCommand extends AbstractTextAreaCommand {
    public static final int TAB_WIDTH = 4;
    private String spaces;

    public TabCommand(TextAreaModel model) {
        super(model);
        int column = model.caret().location().x;
        int toColumn = (column / TAB_WIDTH) * TAB_WIDTH + TAB_WIDTH;
        spaces = StringUtils.repeat(" ", toColumn - column);
    }

    @Override
    public void execute() {
        model.insert(spaces);
    }
}

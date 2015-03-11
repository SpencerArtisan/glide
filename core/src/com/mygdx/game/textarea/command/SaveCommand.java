package com.mygdx.game.textarea.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.code.Program;
import com.mygdx.game.textarea.TextAreaModel;
import org.apache.commons.lang3.StringUtils;

public class SaveCommand extends AbstractCommand {
    private static String CODE_FILE = "code.groovy";
    private Program program;

    public SaveCommand(TextAreaModel model, Program program) {
        super(model);
        this.program = program;
    }

    @Override
    public void execute() {
        String pathname = program.getFileFolder() + CODE_FILE;
        FileHandle game = Gdx.files.local(pathname);
        game.writeString(model.getText(), false);
    }
}

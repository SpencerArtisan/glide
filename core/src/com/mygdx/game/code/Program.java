package com.mygdx.game.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.textarea.TextAreaModel;

public class Program {
    private static String FOLDER = "games/%s/";
    private static String CODE_FILE = "code.groovy";
    private static String TEMPLATE =
		       "////////////////////////////////// \n"
			 + "// Welcome to Planet Burpl! \n"
			 + "// Start writing your game below. \n"
			 + "// Click here if you need help \n"
			 + "////////////////////////////////// \n\n";

    private String name;
    private String code;

    public Program() {
        this.name = "Unnamed Game";
        code = TEMPLATE;
	}

	public String code() {
		return code;
	}

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileFolder() {
        return String.format(FOLDER, name);
    }

    public void save(TextAreaModel model) {
        String pathname = getFileFolder() + CODE_FILE;
        FileHandle game = Gdx.files.local(pathname);
        game.writeString(model.getText(), false);
    }
}

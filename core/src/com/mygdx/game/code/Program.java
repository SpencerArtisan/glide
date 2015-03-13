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
        FileHandle source = getDirectory();
        this.name = name;
        FileHandle target = getDirectory();
        source.moveTo(target);
    }

    public String getDirectoryName() {
        return String.format(FOLDER, name);
    }

    public FileHandle getDirectory() {
        return Gdx.files.local(getDirectoryName());
    }

    public void save(TextAreaModel model) {
        String pathname = getDirectoryName() + CODE_FILE;
        FileHandle game = Gdx.files.local(pathname);
        game.writeString(model.getText(), false);
    }
}

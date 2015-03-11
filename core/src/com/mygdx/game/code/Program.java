package com.mygdx.game.code;

public class Program {
    private static String FOLDER = "games/%s/";

    private String name;
	private String code;

    private static String TEMPLATE =
		       "////////////////////////////////// \n"
			 + "// Welcome to Planet Burpl! \n"
			 + "// Start writing your game below. \n"
			 + "// Click here if you need help \n"
			 + "////////////////////////////////// \n\n";

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

    public String getFileFolder() {
        return String.format(FOLDER, name);
    }
}

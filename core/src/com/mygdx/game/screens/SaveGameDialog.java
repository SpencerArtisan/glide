package com.mygdx.game.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.google.common.util.concurrent.SettableFuture;
import com.mygdx.game.code.Program;

public class SaveGameDialog extends Dialog {
    private SettableFuture<String> futureGameName = SettableFuture.create();
    private TextField nameTextField;

    public SaveGameDialog(Program program, Skin skin) {
        super("", skin);
        createNameField(program, skin);
        layoutControls();
    }

    private void layoutControls() {
        Table contentTable = getContentTable();
        contentTable.padTop(20).padLeft(60).padRight(60);
        text("Save your game");
        contentTable.row();
        contentTable.add(nameTextField).expandX().fillX().padTop(20).padBottom(25);
        contentTable.row();
        button("Save").padBottom(25);
    }

    public TextField getNameTextField() {
        return nameTextField;
    }

    public SettableFuture<String> getFutureGameName() {
        return futureGameName;
    }

    @Override
    protected void result(Object object) {
        futureGameName.set(nameTextField.getText());
    }

    private TextField createNameField(Program program, Skin skin) {
        nameTextField = new TextField(program.name(), skin);
        nameTextField.setAlignment(Align.center);
        nameTextField.setMaxLength(16);
        nameTextField.setCursorPosition(nameTextField.getText().length());
        return nameTextField;
    }
}

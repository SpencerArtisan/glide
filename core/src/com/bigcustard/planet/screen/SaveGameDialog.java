package com.bigcustard.planet.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.google.common.util.concurrent.SettableFuture;
import com.bigcustard.planet.code.Game;

public class SaveGameDialog extends Dialog {
    private SettableFuture<String> futureGameName = SettableFuture.create();
    private TextField nameTextField;

    public SaveGameDialog(Game game, Skin skin) {
        super("", skin);
        createNameField(game, skin);
        layoutControls();
    }

    private void layoutControls() {
        Table contentTable = getContentTable();
        contentTable.padTop(20).padLeft(60).padRight(60);
        text("Save your planet");
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

    private TextField createNameField(Game game, Skin skin) {
        nameTextField = new TextField(game.name(), skin);
        nameTextField.setAlignment(Align.center);
        nameTextField.setMaxLength(16);
        nameTextField.setCursorPosition(nameTextField.getText().length());
        return nameTextField;
    }
}

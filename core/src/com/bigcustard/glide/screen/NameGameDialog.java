package com.bigcustard.glide.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.bigcustard.glide.code.Game;
import com.google.common.util.concurrent.SettableFuture;

public class NameGameDialog extends Dialog {
    private SettableFuture<String> futureGameName = SettableFuture.create();
    private TextField nameTextField;

    public NameGameDialog(Game game, Skin skin) {
        super("", skin);
        layoutControls(game, skin);
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

    @Override
    public float getPrefWidth() {
        return 440;
    }

    private void layoutControls(Game game, Skin skin) {
        Table contentTable = getContentTable();
        contentTable.padTop(20).padLeft(40).padRight(40);
        text("Name your game");
        contentTable.row();
        createNameField(game, skin);
        contentTable.add(nameTextField).expandX().fillX().padTop(20).padBottom(25);
        contentTable.row();
        button("Save").padBottom(25);
    }

    private void createNameField(Game game, Skin skin) {
        nameTextField = new TextField(game.isNamed() ? game.name() : "", skin);
        nameTextField.setAlignment(Align.center);
        nameTextField.setMaxLength(24);
        nameTextField.setCursorPosition(nameTextField.getText().length());
    }
}

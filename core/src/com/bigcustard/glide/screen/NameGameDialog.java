package com.bigcustard.glide.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.bigcustard.glide.code.Game;
import com.bigcustard.scene2dplus.button.ErrorHandler;
import com.bigcustard.scene2dplus.button.TextButtonPlus;
import com.bigcustard.scene2dplus.textfield.TextFieldPlus;
import com.google.common.util.concurrent.SettableFuture;

public class NameGameDialog extends Dialog {
    private SettableFuture<String> futureGameName = SettableFuture.create();
    private TextFieldPlus nameTextField;
    private TextButtonPlus saveButton;

    public NameGameDialog(Game.Token game, Skin skin) {
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

    private void layoutControls(Game.Token game, Skin skin) {
        Table contentTable = getContentTable();
        contentTable.padTop(20).padLeft(40).padRight(40);
        text("Name your game");
        contentTable.row();
        createNameField(game, skin);
        contentTable.add(nameTextField).expandX().fillX().padTop(20).padBottom(25);
        contentTable.row();
        saveButton = new TextButtonPlus("Save", skin);
        if (!game.isNamed()) saveButton.setDisabled(true);
        button(saveButton).padBottom(25);
    }

    private void createNameField(Game.Token game, Skin skin) {
        nameTextField = new TextFieldPlus(game.isNamed() ? game.name() : "", skin, "entry");
        nameTextField.setAlignment(Align.center);
        nameTextField.setMaxLength(24);
        nameTextField.setCursorPosition(nameTextField.getText().length());
        ErrorHandler.onType(nameTextField, field -> saveButton.setDisabled(field.getText().isEmpty()));
    }
}

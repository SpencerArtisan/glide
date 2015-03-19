package com.bigcustard.planet.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.bigcustard.planet.code.Game;
import com.google.common.util.concurrent.SettableFuture;

public class SaveChoiceDialog extends Dialog {
    private SettableFuture<Boolean> futureSaveChoice = SettableFuture.create();

    public SaveChoiceDialog(Skin skin) {
        super("", skin);
        layoutControls();
    }

    private void layoutControls() {
        Table contentTable = getContentTable();
        contentTable.padTop(20).padLeft(40).padRight(40);
        text("Shall I save your game?");
        contentTable.row();
        button("Save", true);
        getButtonTable().getCells().get(0).padRight(40);
        button("Delete", false);
        getButtonTable().pad(25);
    }

    public SettableFuture<Boolean> getFutureSaveChoice() {
        return futureSaveChoice;
    }

    @Override
    protected void result(Object save) {
        futureSaveChoice.set((boolean) save);
    }
}

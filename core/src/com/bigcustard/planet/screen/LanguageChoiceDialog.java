package com.bigcustard.planet.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.bigcustard.planet.code.Language;
import com.google.common.util.concurrent.SettableFuture;

public class LanguageChoiceDialog extends Dialog {
    private SettableFuture<Language> futureLanguageChoice = SettableFuture.create();

    public LanguageChoiceDialog(Skin skin) {
        super("", skin);
        layoutControls();
    }

    public SettableFuture<Language> getFutureLanguageChoice() {
        return futureLanguageChoice;
    }

    @Override
    protected void result(Object save) {
        futureLanguageChoice.set((Language) save);
    }

    private void layoutControls() {
        Table contentTable = getContentTable();
        contentTable.padTop(20).padLeft(40).padRight(40);
        text("Which Language?");
        contentTable.row();
        button("Ruby", Language.JRuby);
        contentTable.row();
        button("Groovy", Language.Groovy);
        contentTable.row();
        button("Cancel", null).padTop(40);
        getButtonTable().pad(25);
    }
}

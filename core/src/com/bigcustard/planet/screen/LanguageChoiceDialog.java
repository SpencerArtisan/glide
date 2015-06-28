package com.bigcustard.planet.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.bigcustard.planet.code.Language;
import com.bigcustard.scene2dplus.Spacer;
import com.google.common.util.concurrent.SettableFuture;

public class LanguageChoiceDialog extends Dialog {
    private SettableFuture<Language> futureLanguageChoice = SettableFuture.create();

    public LanguageChoiceDialog(Skin skin) {
        super("", skin);
        layoutControls(skin);
    }

    public SettableFuture<Language> getFutureLanguageChoice() {
        return futureLanguageChoice;
    }

    @Override
    protected void result(Object save) {
        futureLanguageChoice.set((Language) save);
        remove();
    }

    private void layoutControls(Skin skin) {
        padTop(20).padLeft(40).padRight(40);
        text("Which Language?");
        getButtonTable().padTop(20).padBottom(20);

        ImageTextButton javascriptButton = new ImageTextButton(" Javascript ", skin, "javascript-button");
        addButton(javascriptButton, Language.Javascript);

        ImageTextButton groovyButton = new ImageTextButton(" Groovy ", skin, "groovy-button");
        addButton(groovyButton, Language.Groovy);

        getButtonTable().row();
        TextButton cancelButton = new TextButton("  Cancel  ", skin);
        setObject(cancelButton, null);
        getButtonTable().add(cancelButton).padTop(20).padBottom(0).colspan(2);
    }

    private void addButton(ImageTextButton button, Language language) {
        button.clearChildren();
        button.add(new Spacer(3));
        button.add(button.getImage());
        button.add(button.getLabel());
        getButtonTable().add(button).width(280).padLeft(10).padRight(10);
        setObject(button, language);
    }
}

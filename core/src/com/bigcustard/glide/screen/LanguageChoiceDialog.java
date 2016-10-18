package com.bigcustard.glide.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.bigcustard.glide.code.language.Language;
import com.bigcustard.scene2dplus.Spacer;
import com.bigcustard.scene2dplus.button.ImageTextButtonPlus;
import com.bigcustard.scene2dplus.button.TextButtonPlus;
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

        ImageTextButton pythonButton = new ImageTextButtonPlus(" Python ", skin, "python-button");
        addButton(pythonButton, Language.Python);

        ImageTextButton groovyButton = new ImageTextButtonPlus(" Groovy ", skin, "groovy-button");
        addButton(groovyButton, Language.Groovy);

        getButtonTable().row();

        ImageTextButton rubyButton = new ImageTextButtonPlus(" Ruby ", skin, "ruby-button");
        addButton(rubyButton, Language.Ruby);

        ImageTextButton javascriptButton = new ImageTextButtonPlus(" Javascript ", skin, "javascript-button");
        addButton(javascriptButton, Language.Javascript);

        getButtonTable().row();

        TextButton cancelButton = new TextButtonPlus("  Cancel  ", skin);
        setObject(cancelButton, null);
        getButtonTable().add(cancelButton).padTop(20).padBottom(0).colspan(2);
    }

    private void addButton(ImageTextButton button, Language language) {
        button.clearChildren();
        button.add(new Spacer(3));
        button.add(button.getImage());
        button.add(button.getLabel());
        getButtonTable().add(button).width(190).height(60).pad(10);
        setObject(button, language);
    }
}

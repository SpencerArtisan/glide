package com.bigcustard.scene2dplus.sound;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.bigcustard.scene2dplus.button.ImageButtonPlus;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.scene2dplus.resource.Resource;
import com.bigcustard.scene2dplus.textfield.TextFieldPlus;

public class SoundEditor implements Resource<SoundModel> {
    private static final int MAX_NAME_LENGTH = 18;
    private SoundModel model;
    private Editor editor;
    private Editor.Controller controller;

    public SoundEditor(SoundModel model, Skin skin, CommandHistory commandHistory) {
        this.model = model;
        editor = new Editor(skin);
        controller = editor.new Controller(commandHistory);
    }

    @Override
    public Actor editor() {
        return editor;
    }

    @Override
    public Controller controller() {
        return controller;
    }

    @Override
    public SoundModel model() {
        return model;
    }

    @Override
    public void dispose() {
        controller.dispose();
    }

    private class Editor extends Table {
        private final TextFieldPlus nameField;
        private final ImageButtonPlus deleteButton;
        private final Skin skin;
        private Image soundGlyph;

        public Editor(Skin skin) {
            this.skin = skin;
            this.nameField = createNameField();
            this.deleteButton = createDeleteButton();
            layoutControls();
        }

        private TextFieldPlus createNameField() {
            TextFieldPlus textField = new TextFieldPlus("", skin);
            textField.setAlignment(Align.center);
            textField.setMaxLength(MAX_NAME_LENGTH);
            return textField;
        }

        private ImageButtonPlus createDeleteButton() {
            return new ImageButtonPlus(skin, "trash-button");
        }

        void layoutControls() {
            row();
            add(getSoundControl()).fill().padBottom(10);
            row();
            add(nameField).fillX();
        }

        private Actor getSoundControl() {
            Table buttonTable = new Table();
            buttonTable.setFillParent(true);
            buttonTable.add(deleteButton).expand().top().right();

            soundGlyph = new Image(skin, "sound");
            soundGlyph.setScaling(Scaling.fit);
            Stack group = new Stack();
            group.addActor(soundGlyph);
            group.addActor(buttonTable);
            return group;
        }

        private class Controller implements Resource.Controller, Disposable {
            private final CommandHistory commandHistory;

            public Controller(CommandHistory commandHistory) {
                this.commandHistory = commandHistory;
                viewToModel();
                modelToView();
                playSoundOnClick();
            }

            private void playSoundOnClick() {
                soundGlyph.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        model.sound().play();
                    }
                });
            }

            private void viewToModel() {
                nameField.setTextFieldListener((text, ignored) -> {
                    String oldName = model.name().get();
                    String newName = text.getText();
                    commandHistory.execute(() -> model.name(newName), () -> model.name(oldName));
                });
            }

            private void modelToView() {
                model.name().watch(nameField::setText).broadcast();
            }

            @Override
            public void watchRemoveButton(Runnable onRemove) {
                deleteButton.onClick(onRemove::run);
            }

            @Override
            public void unwatchRemoveButton() {
                deleteButton.clearListeners();
            }

            @Override
            public void dispose() {
                unwatchRemoveButton();
            }
        }
    }
}

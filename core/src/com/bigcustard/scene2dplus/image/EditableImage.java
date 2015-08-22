package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.scene2dplus.resource.Resource;
import com.bigcustard.scene2dplus.textfield.TextFieldPlus;
import com.google.common.base.Strings;

public class EditableImage implements Resource<ImageModel> {
    private static final int MAX_NAME_LENGTH = 18;
    private DisposableImage image;
    private ImageModel model;
    private Editor editor;
    private Editor.Controller controller;

    public EditableImage(ImageModel model, Skin skin, CommandHistory commandHistory) {
        this.image = ImageUtils.asImage(model.file());
        this.model = model;
        editor = new Editor(skin);
        controller = editor.new Controller(commandHistory);
    }

    public Image getImage() {
        return image;
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
    public ImageModel model() {
        return model;
    }

    @Override
    public void dispose() {
        image.dispose();
    }

    private class Editor extends Table {
        private final TextFieldPlus nameField;
        private final TextFieldPlus widthField;
        private final TextFieldPlus heightField;
        private final Button deleteButton;
        private final Skin skin;

        public Editor(Skin skin) {
            this.skin = skin;
            this.nameField = createNameField();
            this.widthField = createSizeField();
            this.heightField = createSizeField();
            this.deleteButton = createDeleteButton();
            layoutControls();
        }

        private TextFieldPlus createNameField() {
            TextFieldPlus textField = new TextFieldPlus("", skin);
            textField.setAlignment(Align.center);
            textField.setMaxLength(MAX_NAME_LENGTH);
            return textField;
        }

        private TextFieldPlus createSizeField() {
            TextFieldPlus textField = new TextFieldPlus("", skin);
            textField.setAlignment(Align.center);
            textField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
            return textField;
        }

        private Button createDeleteButton() {
            return new ImageButton(skin, "trash-button");
        }

        void layoutControls() {
            row();
            add(getImageControl()).minSize(100).maxHeight(200).fill().colspan(3).padBottom(10);
            row();
            add(nameField).colspan(3).fillX();
            row();
            add(widthField).minWidth(10);
            add(new Label(" x ", skin));
            add(heightField).minWidth(10);
        }

        private Actor getImageControl() {
            Table buttonTable = new Table();
            buttonTable.setFillParent(true);
            buttonTable.add(deleteButton).expand().top().right();

            image.setScaling(Scaling.fit);
            Stack group = new Stack();
            group.addActor(image);
            group.addActor(buttonTable);
            return group;
        }

        private class Controller implements Resource.Controller {
            private final CommandHistory commandHistory;

            public Controller(CommandHistory commandHistory) {
                this.commandHistory = commandHistory;
                viewToModel();
                modelToView();
            }

            private void viewToModel() {
                widthField.setTextFieldListener((text, ignored) -> {
                    Integer oldWidth = model.width().get();
                    Integer newWidth = toInt(text);
                    commandHistory.execute(() -> model.width(newWidth), () -> model.width(oldWidth));
                });
                heightField.setTextFieldListener((text, ignored) -> {
                    Integer oldHeight = model.height().get();
                    Integer newHeight = toInt(text);
                    commandHistory.execute(() -> model.height(newHeight), () -> model.height(oldHeight));
                });
                nameField.setTextFieldListener((text, ignored) -> {
                    String oldName = model.name().get();
                    String newName = text.getText();
                    commandHistory.execute(() -> model.name(newName), () -> model.name(oldName));
                });
            }

            private void modelToView() {
                model.width().watch((value) -> widthField.setText(String.valueOf(value)));
                model.height().watch((value) -> heightField.setText(String.valueOf(value)));
                model.name().watch(nameField::setText);
            }

            @Override
            public void watchRemoveButton(Runnable onRemove) {
                deleteButton.addListener(new ChangeListener() {
                    public void changed(ChangeEvent event, Actor actor) {
                        onRemove.run();
                    }
                });
            }

            private Integer toInt(TextField field) {
                return Strings.isNullOrEmpty(field.getText()) ? null : Integer.parseInt(field.getText());
            }
        }
    }
}

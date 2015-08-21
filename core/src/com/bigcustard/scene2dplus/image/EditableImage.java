package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.scene2dplus.resource.Resource;
import com.bigcustard.scene2dplus.textfield.TextFieldPlus;
import com.bigcustard.util.Watchable;
import com.google.common.base.Strings;

public class EditableImage implements Resource {
    private static final int MAX_NAME_LENGTH = 18;
    private Image image;
    private Watchable<String> name;
    private Watchable<Integer> width;
    private Watchable<Integer> height;
    private Integer originalHeight;
    private Integer originalWidth;

    public EditableImage(Image image, String name, int width, int height) {
        this.image = image;
        this.name = new Watchable<>(name);
        this.width = new Watchable<>(width);
        this.height = new Watchable<>(height);
        originalHeight = height;
        originalWidth = width;
    }

    public EditableImage(ImageModel model) {
        this(ImageUtils.asImage(model.file()), model.name(), model.width(), model.height());
    }

    private void width(Integer newWidth) {
        width.set(newWidth);
        if (newWidth != null) {
            height.set(newWidth * originalHeight / originalWidth);
        } else {
            height.set(null);
        }
    }

    private void height(Integer newHeight) {
        height.set(newHeight);
        if (newHeight != null) {
            width.set(newHeight * originalWidth / originalHeight);
        } else {
            width.set(null);
        }
    }

    private void name(String newName) {
        this.name.set(newName);
    }

    public Image getImage() {
        return image;
    }

    @Override
    public Actor editor(Skin skin, CommandHistory commandHistory) {
        Editor editor = new Editor(skin);
        editor.new Controller(commandHistory);
        return editor;
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

        private class Controller {
            private final CommandHistory commandHistory;

            public Controller(CommandHistory commandHistory) {
                this.commandHistory = commandHistory;
                viewToModel();
                modelToView();
            }

            private void viewToModel() {
                widthField.setTextFieldListener((text, ignored) -> {
                    Integer oldWidth = width.get();
                    Integer newWidth = toInt(text);
                    commandHistory.execute(() -> width(newWidth), () -> width(oldWidth));
                });
                heightField.setTextFieldListener((text, ignored) -> {
                    Integer oldHeight = height.get();
                    Integer newHeight = toInt(text);
                    commandHistory.execute(() -> height(newHeight), () -> height(oldHeight));
                });
                nameField.setTextFieldListener((text, ignored) -> {
                    String oldName = name.get();
                    String newName = text.getText();
                    commandHistory.execute(() -> name(newName), () -> name(oldName));
                });
            }

            private void modelToView() {
                width.add((value) -> widthField.setText(String.valueOf(value)));
                height.add((value) -> heightField.setText(String.valueOf(value)));
                name.add(nameField::setText);
            }

            private Integer toInt(TextField field) {
                return Strings.isNullOrEmpty(field.getText()) ? null : Integer.parseInt(field.getText());
            }
        }
    }
}

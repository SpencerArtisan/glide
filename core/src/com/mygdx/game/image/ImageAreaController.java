package com.mygdx.game.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Clipboard;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;

public class ImageAreaController {
    private final ImageGrabber grabber;
    private final ImageArea view;
    private ImageAreaModel model;

    public ImageAreaController(final ImageGrabber grabber, ImageArea view, ImageAreaModel model) {
        this.grabber = grabber;
        this.view = view;
        this.model = model;
    }

    public void init() {
        addImportBehaviour();
        addImageAdjustmentBehaviour();
    }

    private void addImportBehaviour() {
        view.importButton().addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                onImageUrlProvided(getClipboard().getContents());
            }
        });
    }

    private void addImageAdjustmentBehaviour() {
        for (GameImage gameImage : model.getImages()) {
            addImageAdjustmentBehaviour(gameImage);
        }
    }

    private void addImageAdjustmentBehaviour(GameImage gameImage) {
        ImageControls imageControls = view.getImageControls(gameImage);
        addRenameBehaviour(imageControls);
        addWidthChangeBehaviour(imageControls);
        addHeightChangeBehaviour(imageControls);
    }

    private void addRenameBehaviour(ImageControls imageControls) {
        imageControls.getNameField().setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                imageControls.getGameImage().setName(textField.getText());
            }
        });
    }

    private void addWidthChangeBehaviour(ImageControls imageControls) {
        imageControls.getWidthField().setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                imageControls.getGameImage().setWidth(parseInt(textField));
            }

        });
    }

    private void addHeightChangeBehaviour(ImageControls imageControls) {
        imageControls.getHeightField().setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                imageControls.getGameImage().setHeight(parseInt(textField));
            }
        });
    }

    private void onImageUrlProvided(String url) {
        try {
            FileHandle imageFile = grabber.grab(url);
            GameImage gameImage = model.add(imageFile);
            gameImage.addListener(() -> onModelChange(gameImage));
            view.onImageAdded(gameImage);
            addImageAdjustmentBehaviour(gameImage);
        } catch (IOException e) {
            view.showFailure();
        }
    }

    void onModelChange(GameImage image) {
        ImageControls imageControls = view.getImageControls(image);
        imageControls.getWidthField().setText(ObjectUtils.toString(image.width()));
        imageControls.getHeightField().setText(ObjectUtils.toString(image.height()));
        imageControls.getNameField().setText(image.name());
    }

    private Integer parseInt(TextField textField) {
        String text = textField.getText();
        return text.isEmpty() ? null : Integer.parseInt(text);
    }

    protected Clipboard getClipboard() {
        return Gdx.app.getClipboard();
    }
}

package com.mygdx.game.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Clipboard;

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
        for (ImageArea.ImageControls imageControls : view.getImageControlList()) {
            addRenameBehaviour(imageControls);
            addWidthChangeBehaviour(imageControls);
            addHeightChangeBehaviour(imageControls);
        }
    }

    private void addRenameBehaviour(ImageArea.ImageControls imageControls) {
        imageControls.getNameField().setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                imageControls.getGameImage().setName(textField.getText());
            }
        });
    }

    private void addWidthChangeBehaviour(ImageArea.ImageControls imageControls) {
        imageControls.getWidthField().setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                imageControls.getGameImage().setWidth(Integer.parseInt(textField.getText()));
                view.refresh();
            }
        });
    }

    private void addHeightChangeBehaviour(ImageArea.ImageControls imageControls) {
        imageControls.getHeightField().setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                imageControls.getGameImage().setHeight(Integer.parseInt(textField.getText()));
                view.refresh();
            }
        });
    }

    private void onImageUrlProvided(String url) {
        try {
            FileHandle imageFile = grabber.grab(url);
            model.add(imageFile);
            view.refresh();
            addImageAdjustmentBehaviour();
        } catch (IOException e) {
            view.showFailure();
        }
    }

    protected Clipboard getClipboard() {
        return Gdx.app.getClipboard();
    }
}

package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Clipboard;
import org.apache.commons.lang3.ObjectUtils;

public class ImageAreaController {
    private final ImageArea view;
    private final ImageAreaModel model;

    public ImageAreaController(ImageArea view, ImageAreaModel model) {
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
        for (ImagePlus gameImage : model.getImages()) {
            addImageAdjustmentBehaviour(gameImage);
        }
    }

    private void addImageAdjustmentBehaviour(ImagePlus gameImage) {
        addImageListener(gameImage);
        ImageControls imageControls = view.getImageControls(gameImage);
        addRenameBehaviour(imageControls);
        addWidthChangeBehaviour(imageControls);
        addHeightChangeBehaviour(imageControls);
    }

    private void addImageListener(ImagePlus gameImage) {
        gameImage.addListener(() -> onModelChange(gameImage));
    }

    void onModelChange(ImagePlus image) {
        ImageControls imageControls = view.getImageControls(image);
        imageControls.getWidthField().setText(ObjectUtils.toString(image.width()));
        imageControls.getHeightField().setText(ObjectUtils.toString(image.height()));
        imageControls.getNameField().setText(image.name());
    }

    private void addRenameBehaviour(ImageControls imageControls) {
        imageControls.getNameField().setTextFieldListener((field, c) -> imageControls.getGameImage().setName(field.getText()));
    }

    private void addWidthChangeBehaviour(ImageControls imageControls) {
        imageControls.getWidthField().setTextFieldListener((field, c) -> imageControls.getGameImage().setWidth(parseInt(field)));
    }

    private void addHeightChangeBehaviour(ImageControls imageControls) {
        imageControls.getHeightField().setTextFieldListener((field, c) -> imageControls.getGameImage().setHeight(parseInt(field)));
    }

    private void onImageUrlProvided(String url) {
        try {
            ImagePlus gameImage = model.addImage(url);
            view.onImageAdded(gameImage);
            addImageAdjustmentBehaviour(gameImage);
        } catch (Exception e) {
            view.showFailure();
        }
    }

    private Integer parseInt(TextField textField) {
        String text = textField.getText();
        return text.isEmpty() ? null : Integer.parseInt(text);
    }

    protected Clipboard getClipboard() {
        return Gdx.app.getClipboard();
    }
}

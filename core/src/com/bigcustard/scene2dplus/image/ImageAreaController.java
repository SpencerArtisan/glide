package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Clipboard;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

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
        validate();
    }

    private void addImageAdjustmentBehaviour(ImagePlus gameImage) {
        addImageListener(gameImage);
        ImageControls imageControls = view.getImageControls(gameImage);
        addRenameBehaviour(imageControls);
        addWidthChangeBehaviour(imageControls);
        addHeightChangeBehaviour(imageControls);
        addDeleteBehaviour(imageControls);
    }

    private void addImageListener(ImagePlus gameImage) {
        gameImage.addListener(() -> onModelChange(gameImage));
    }

    void onModelChange(ImagePlus image) {
        ImageControls imageControls = view.getImageControls(image);
        imageControls.getWidthField().setText(ObjectUtils.toString(image.width()));
        imageControls.getHeightField().setText(ObjectUtils.toString(image.height()));
        imageControls.getNameField().setText(image.name());
        validate();
    }

    private void validate() {
        List<ImageValidator.Result> results = model.validateImages();
        for (ImageValidator.Result result : results) {
            ImageControls imageControls = view.getImageControls(result.image());
            imageControls.getNameField().setValid(result.isNameValid());
            imageControls.getWidthField().setValid(result.isWidthValid());
            imageControls.getHeightField().setValid(result.isHeightValid());
        }
    }

    private void addRenameBehaviour(ImageControls imageControls) {
        imageControls.getNameField().setTextFieldListener((field, c) -> imageControls.getImage().setName(field.getText()));
    }

    private void addWidthChangeBehaviour(ImageControls imageControls) {
        imageControls.getWidthField().setTextFieldListener((field, c) -> imageControls.getImage().setWidth(parseInt(field)));
    }

    private void addHeightChangeBehaviour(ImageControls imageControls) {
        imageControls.getHeightField().setTextFieldListener((field, c) -> imageControls.getImage().setHeight(parseInt(field)));
    }

    private void addDeleteBehaviour(ImageControls imageControls) {
        imageControls.getDeleteButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                model.deleteImage(imageControls.getImage());
            }
        });
    }

    private void onImageUrlProvided(String url) {
        try {
            ImagePlus gameImage = model.addImage(url);
            view.onImageAdded(gameImage);
            addImageAdjustmentBehaviour(gameImage);
            validate();
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

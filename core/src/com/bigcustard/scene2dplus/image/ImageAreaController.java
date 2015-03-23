package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.scene2dplus.image.command.*;

public class ImageAreaController {
    private final ImageArea view;
    private final ImageAreaModel model;
    private final CommandHistory commandHistory;

    public ImageAreaController(ImageArea view, ImageAreaModel model, CommandHistory commandHistory) {
        this.view = view;
        this.model = model;
        this.commandHistory = commandHistory;
    }

    public void init() {
        addImportBehaviour();
        addAllImageAdjustmentBehaviour();
        addImageListChangeBehaviour();
    }

    private void addImageListChangeBehaviour() {
        view.registerAddImageControlsListener(this::addImageAdjustmentBehaviour);
    }

    protected Clipboard getClipboard() {
        return Gdx.app.getClipboard();
    }

    private void addImportBehaviour() {
        view.registerImportButtonListener(this::addImageFromClipboardUrl);
    }

    private void addAllImageAdjustmentBehaviour() {
        for (ImageControls imageControls : view.getAllImageControls()) {
            addImageAdjustmentBehaviour(imageControls);
        }
    }

    private void addImageAdjustmentBehaviour(ImageControls imageControls) {
        addRenameBehaviour(imageControls);
        addWidthChangeBehaviour(imageControls);
        addHeightChangeBehaviour(imageControls);
        addDeleteBehaviour(imageControls);
    }

    private void addRenameBehaviour(ImageControls imageControls) {
        imageControls.registerNameFieldListener((text) ->
                commandHistory.execute(new ChangeNameCommand(imageControls.getImage(), text)));
    }

    private void addWidthChangeBehaviour(ImageControls imageControls) {
        imageControls.registerWidthFieldListener((value) ->
                commandHistory.execute(new ChangeWidthCommand(imageControls.getImage(), value)));
    }

    private void addHeightChangeBehaviour(ImageControls imageControls) {
        imageControls.registerHeightFieldListener((value) ->
                commandHistory.execute(new ChangeHeightCommand(imageControls.getImage(), value)));
    }

    private void addDeleteBehaviour(ImageControls imageControls) {
        imageControls.registerDeleteButtonListener(() ->
                commandHistory.execute(new RemoveImageCommand(model, imageControls.getImage())));
    }

    private void addImageFromClipboardUrl() {
        try {
            commandHistory.execute(new AddImageCommand(model, getClipboard().getContents()));
        } catch (Exception e) {
            view.onImageImportFailure();
        }
    }
}

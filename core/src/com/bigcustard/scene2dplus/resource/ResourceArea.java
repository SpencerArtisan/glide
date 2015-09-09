package com.bigcustard.scene2dplus.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Clipboard;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.scene2dplus.button.ButtonUtil;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.scene2dplus.dialog.FileDialog;
import com.google.common.annotations.VisibleForTesting;

import java.io.InputStream;
import java.net.URL;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ResourceArea<TModel> extends ScrollPane implements Disposable {
    private Skin skin;
    private final ResourceSet<TModel> resources;
    private final CommandHistory commandHistory;
    private final BiFunction<InputStream, String, Resource<TModel>> resourceImporter;
    private TextButton clipboardButton;
    private TextButton fileButton;

    public ResourceArea(Skin skin,
                        ResourceSet<TModel> resources,
                        CommandHistory commandHistory,
                        BiFunction<InputStream, String, Resource<TModel>> resourceImporter) {
        super(new Table(), skin);
        this.skin = skin;
        this.resources = resources;
        this.commandHistory = commandHistory;
        this.resourceImporter = resourceImporter;
        this.setScrollingDisabled(true, false);
        createClipboardButton(skin);
        createFileButton(skin);
        layoutControls();
        pack();
        resources.resources().watchAdd((list) -> layoutControls());
        resources.resources().watchRemove((list) -> layoutControls());
    }

    public void chooseFile(Consumer<FileHandle> fileConsumer) {
        FileDialog files = FileDialog.createLoadDialog("Pick your image", skin, Gdx.files.external("."));
        files.setResultListener((success, result) -> {
            if (success) fileConsumer.accept(result);
            files.dispose();
            return success;
        });
        files.show(getStage());
    }

    private void createClipboardButton(Skin skin) {
        clipboardButton = new TextButton("Add from clipboard", skin);
        ButtonUtil.onClick(clipboardButton, this::addFromClipboardUrl);
    }

    private void addFromClipboardUrl() {
        try {
            String url = getClipboard().getContents();
            Resource<TModel> resource = resourceImporter.apply(new URL(url).openStream(), url);
            commandHistory.execute(() -> resources.resources().add(resource), () -> resources.resources().remove(resource));
        } catch (Exception e) {
            System.err.println("Error adding resource from clipboard: " + e);
            dodgyWiggle(clipboardButton);
        }
    }

    private void createFileButton(Skin skin) {
        fileButton = new TextButton("Add from file", skin);
        ButtonUtil.onClick(fileButton, this::addFromFile);
    }

    private void addFromFile() {
        chooseFile((fileHandle -> {
            try {
                Resource<TModel> resource = resourceImporter.apply(fileHandle.read(), fileHandle.path());
                commandHistory.execute(() -> resources.resources().add(resource), () -> resources.resources().remove(resource));
            } catch (Exception e) {
                System.err.println("Error adding resource from file: " + e);
                dodgyWiggle(fileButton);
            }
        }));
    }

    private void layoutControls() {
        Table table = (Table) getWidget();
        table.defaults().pad(12).padBottom(0);
        table.background(skin.getDrawable("solarizedNew"));
        table.clearChildren();
        table.top();
        table.add(clipboardButton).fillX().expandX();
        table.row();
        table.add(fileButton).fillX().expandX();
        table.row();
        for (Resource resource : resources.resources()) {
            Actor editor = resource.editor();
            table.add(editor);
            table.row();
        }
        table.pack();
    }

    @Override
    public void dispose() {
        resources.dispose();
        clipboardButton.clearListeners();
        fileButton.clearListeners();
    }

    @VisibleForTesting
    protected Clipboard getClipboard() {
        return Gdx.app.getClipboard();
    }

    private void dodgyWiggle(TextButton button) {
        String originalText = button.getText().toString();
        button.setText("Dodgy resource!");
        button.addAction(
                Actions.sequence(
                        Actions.repeat(10,
                                Actions.sequence(Actions.moveBy(-3, 0, 0.02f, Interpolation.sineOut),
                                        Actions.moveBy(6, 0, 0.04f, Interpolation.sine),
                                        Actions.moveBy(-3, 0, 0.02f, Interpolation.sineIn))),
                        Actions.run(() -> button.setText(originalText))));
    }
}

package com.bigcustard.scene2dplus.resource;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.google.common.collect.Lists;
import javafx.collections.ListChangeListener;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceArea<TModel> extends ScrollPane implements Disposable {
    private Skin skin;
    private final ResourceSet<TModel> resources;
    private TextButton clipboardButton;
    private TextButton fileButton;

    public ResourceArea(Skin skin, ResourceSet<TModel> resources) {
        super(new Table(), skin);
        this.skin = skin;
        this.resources = resources;
        this.setScrollingDisabled(true, false);
        createClipboardButton(skin);
        createFileButton(skin);
        layoutControls();
        pack();
        resources.resources().watch((list) -> layoutControls());
    }

    private void createClipboardButton(Skin skin) {
        clipboardButton = new TextButton("Add from clipboard", skin);
    }

    private void createFileButton(Skin skin) {
        fileButton = new TextButton("Add from file", skin);
    }

    private void layoutControls() {
        Table table = (Table) getWidget();
        table.defaults().pad(12).padBottom(0);
        table.background(skin.getDrawable("solarizedNew"));
        table.clearChildren();
        table.top();
        table.add(clipboardButton).fillX();
        table.row();
        table.add(fileButton).fillX();
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

    }
}

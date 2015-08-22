package com.bigcustard.scene2dplus.resource;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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

    public ResourceArea(Skin skin, ResourceSet<TModel> resources) {
        super(new Table(), skin);
        this.skin = skin;
        this.resources = resources;
        this.setScrollingDisabled(true, false);
        layoutControls();
        pack();
        resources.resources().watch((list) -> layoutControls());
    }

    private void layoutControls() {
        Table layoutTable = (Table) getWidget();
        layoutTable.defaults().pad(12);
        layoutTable.background(skin.getDrawable("solarizedNew"));
        layoutTable.clearChildren();
        layoutTable.top();
        for (Resource resource : resources.resources()) {
            Actor editor = resource.editor();
            layoutTable.add(editor);
            layoutTable.row();
        }
        layoutTable.pack();
    }

    @Override
    public void dispose() {

    }
}

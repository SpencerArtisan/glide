package com.bigcustard.scene2dplus.resource;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceArea extends ScrollPane implements Disposable {
    private Skin skin;
    private List<Resource> resources = new ArrayList<>();
    private final CommandHistory commandHistory;

    public ResourceArea(Skin skin, Resource[] resources, CommandHistory commandHistory) {
        super(new Table(), skin);
        this.skin = skin;
        this.resources = Lists.newArrayList(resources);
        this.commandHistory = commandHistory;
        this.setScrollingDisabled(true, false);
        layoutControls();
        pack();
    }

    private void layoutControls() {
        Table layoutTable = (Table) getWidget();
        layoutTable.defaults().pad(12);
        layoutTable.background(skin.getDrawable("solarizedNew"));
        layoutTable.clearChildren();
        layoutTable.top();
        for (Resource resource : resources) {
            Pair<Actor, Resource.Controller> editorAndController = resource.editor(skin, commandHistory);
            editorAndController.getRight().registerRemoveListener(() -> {
                commandHistory.execute(() -> resources.remove(resource), () -> resources.add(resource));
                layoutControls();
            });
            layoutTable.add(editorAndController.getLeft());
            layoutTable.row();
        }
        layoutTable.pack();
    }

    @Override
    public void dispose() {

    }
}

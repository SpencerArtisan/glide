package com.bigcustard.scene2dplus.resource;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.scene2dplus.command.CommandHistory;

public class ResourceArea extends ScrollPane implements Disposable {
    private Skin skin;
    private Resource[] resources;
    private final CommandHistory commandHistory;

    public ResourceArea(Skin skin, Resource[] resources, CommandHistory commandHistory) {
        super(new Table(), skin);
        this.skin = skin;
        this.resources = resources;
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
            layoutTable.add(resource.editor(skin, commandHistory));
            layoutTable.row();
        }
        layoutTable.pack();
    }

    @Override
    public void dispose() {

    }
}

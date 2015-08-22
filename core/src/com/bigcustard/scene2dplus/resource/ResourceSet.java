package com.bigcustard.scene2dplus.resource;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.util.WatchableList;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;

import java.util.List;

public class ResourceSet {
    private WatchableList<Resource> resources;

    public ResourceSet(List<Resource> resources, CommandHistory commandHistory) {
        this.resources = new WatchableList<>(resources);
        watchRemoveEvents(commandHistory);
    }

    private void watchRemoveEvents(CommandHistory commandHistory) {
        for (Resource resource : resources) {
            resource.controller().registerRemoveListener(() -> {
                commandHistory.execute(() -> resources.remove(resource), () -> resources.add(resource));
            });
        }
    }

    public WatchableList<Resource> resources() {
        return resources;
    }
}

package com.bigcustard.scene2dplus.resource;

import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.util.WatchableList;

import java.util.List;

public class ResourceSet<TModel> {
    private WatchableList<Resource<TModel>> resources;

    public ResourceSet(List<Resource<TModel>> resources, CommandHistory commandHistory) {
        this.resources = new WatchableList<>(resources);
        watchRemoveEvents(commandHistory);
    }

    private void watchRemoveEvents(CommandHistory commandHistory) {
        for (Resource<TModel> resource : resources) {
            resource.controller().registerRemoveListener(() -> {
                commandHistory.execute(() -> resources.remove(resource), () -> resources.add(resource));
            });
        }
    }

    public WatchableList<Resource<TModel>> resources() {
        return resources;
    }
}

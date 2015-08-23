package com.bigcustard.scene2dplus.resource;

import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.util.WatchableList;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

public class ResourceSet<TModel> implements Disposable {
    private final CommandHistory commandHistory;
    private WatchableList<Resource<TModel>> resources;

    public ResourceSet(List<Resource<TModel>> resources, CommandHistory commandHistory) {
        this.commandHistory = commandHistory;
        this.resources = new WatchableList<>(resources);
        watchRemoveButton();
        watchModelChanges();
    }

    private void watchModelChanges() {
        resources.watchAdd(this::watchRemoveButton);
    }

    private void watchRemoveButton() {
        resources.forEach(this::watchRemoveButton);
    }

    private void watchRemoveButton(Resource<TModel> resource) {
        resource.controller().watchRemoveButton(() -> {
            commandHistory.execute(() -> resources.remove(resource), () -> resources.add(resource));
        });
    }

    public WatchableList<Resource<TModel>> resources() {
        return resources;
    }

    @Override
    public void dispose() {
        resources.forEach(Resource::dispose);
        resources.dispose();
    }
}

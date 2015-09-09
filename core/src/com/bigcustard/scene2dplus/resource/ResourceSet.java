package com.bigcustard.scene2dplus.resource;

import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.util.WatchableList;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class ResourceSet<TModel> implements Disposable {
    private final CommandHistory commandHistory;
    private WatchableList<Resource<TModel>> resources;

    public ResourceSet(List<Resource<TModel>> resources, CommandHistory commandHistory) {
        this.commandHistory = commandHistory;
        this.resources = new WatchableList<>(resources);
        watchRemoveButtons();
        watchModelChanges();
    }

    public WatchableList<Resource<TModel>> resources() {
        return resources;
    }

    public Stream<Resource<TModel>> stream() {
        return resources.stream();
    }

    private void watchModelChanges() {
        resources.watchAdd(this::watchRemoveButton);
        resources.watchRemove(this::unwatchRemoveButton);
    }

    private void watchRemoveButtons() {
        resources.forEach(this::watchRemoveButton);
    }

    private void watchRemoveButton(Resource<TModel> resource) {
        resource.controller().watchRemoveButton(() -> executeRemoveCommand(resource));
    }

    private void unwatchRemoveButton(Resource<TModel> resource) {
        Executors.newSingleThreadScheduledExecutor().submit(() -> resource.controller().unwatchRemoveButton());
    }

    private void executeRemoveCommand(Resource<TModel> resource) {
        commandHistory.execute(() -> resources.remove(resource), () -> resources.add(resource));
    }

    @Override
    public void dispose() {
        resources.forEach(Resource::dispose);
        resources.dispose();
    }
}

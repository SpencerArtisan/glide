package com.bigcustard.scene2dplus.tab;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.scene2dplus.button.ButtonUtil;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class TabControl extends Table {
    private java.util.List<Actor> tabs = new ArrayList<>();
    private java.util.List<Button> tabButtons = new ArrayList<>();

    public void addTab(Actor tab, Button tabButton) {
        tabs.add(tab);
        tabButtons.add(tabButton);
    }

    public void init() {
        AtomicReference<Cell<Actor>> tabCell = new AtomicReference<>();

        for (int i = 0; i < tabs.size(); i++) {
            final Button tabButton = tabButtons.get(i);
            final Actor tab = tabs.get(i);
            add(tabButton).padTop(10);
            ButtonUtil.onClick(tabButton, () -> deactivateOtherTabs(tabCell, tabButton, tab));
        }
        row();
        tabCell.set(add(tabs.get(0)).colspan(2).fill().expand());
        tabButtons.get(0).setChecked(true);
    }

    private void deactivateOtherTabs(AtomicReference<Cell<Actor>> tabCell, Button activeButton, Actor tab) {
        if (activeButton.isChecked()) {
            tabButtons.stream().filter((button) -> button != activeButton).forEach((button) -> button.setChecked(false));
            tabCell.get().setActor(tab);
        }
    }

    public void dispose() {
        tabs.forEach((tab) -> { if (tab instanceof Disposable) ((Disposable) tab).dispose(); });
    }
}

package com.bigcustard.scene2dplus.tab;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

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
            tabButton.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    if (tabButton.isChecked()) {
                        tabButtons.stream().filter((button) -> button != tabButton).forEach((button) -> button.setChecked(false));
                        tabCell.get().setActor(tab);
                    }
                }
            });
        }
        row();
        tabCell.set(add(tabs.get(0)).colspan(2).fill().expand());
        tabButtons.get(0).setChecked(true);
    }
}

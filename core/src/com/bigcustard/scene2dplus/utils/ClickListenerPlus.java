package com.bigcustard.scene2dplus.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bigcustard.scene2dplus.button.ErrorHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public class ClickListenerPlus extends ClickListener {
    private Actor view;
    private Skin skin;

    public ClickListenerPlus(Actor view, Skin skin) {
        this.view = view;
        this.skin = skin;
    }

    public boolean handle (Event e) {
        AtomicBoolean result = new AtomicBoolean();
        ErrorHandler.tryAndRecover(view, skin, () -> result.set(super.handle(e)));
        return result.get();
    }
}

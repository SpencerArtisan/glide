package com.bigcustard.scene2dplus.resource;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bigcustard.scene2dplus.command.CommandHistory;
import org.apache.commons.lang3.tuple.Pair;

public interface Resource {
    Pair<Actor, Controller> editor(Skin skin, CommandHistory commandHistory);

    interface Controller {
        void registerRemoveListener(Runnable onRemove);
    }
}

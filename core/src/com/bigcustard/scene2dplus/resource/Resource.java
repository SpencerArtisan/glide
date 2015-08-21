package com.bigcustard.scene2dplus.resource;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bigcustard.scene2dplus.command.CommandHistory;

public interface Resource {
    Actor editor(Skin skin, CommandHistory commandHistory);
}

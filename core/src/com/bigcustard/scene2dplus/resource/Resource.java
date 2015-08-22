package com.bigcustard.scene2dplus.resource;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface Resource<TModel> {
    Actor editor();
    Controller controller();
    TModel model();

    interface Controller {
        void registerRemoveListener(Runnable onRemove);
    }
}

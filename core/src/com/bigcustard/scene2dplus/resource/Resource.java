package com.bigcustard.scene2dplus.resource;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public interface Resource<TModel> extends Disposable {
    Actor editor();
    Controller controller();
    TModel model();

    interface Controller {
        void watchRemoveButton(Runnable onRemove);
    }
}

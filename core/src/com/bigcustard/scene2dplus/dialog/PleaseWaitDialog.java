package com.bigcustard.scene2dplus.dialog;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.bigcustard.scene2dplus.Spacer;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class PleaseWaitDialog extends Dialog {
    public PleaseWaitDialog(Skin skin) {
        super("", skin);
        layoutControls();
    }

    @Override
    public Dialog show(Stage stage) {
        show(stage, null);
        setPosition(Math.round((stage.getWidth() - getWidth()) / 2),
                Math.round((stage.getHeight() - getHeight()) / 2));
        return this;
    }

    @Override
    public void hide() {
        hide(sequence(Actions.removeListener(ignoreTouchDown, true), Actions.removeActor()));
    }

    private void layoutControls() {
        Table contentTable = getContentTable();
        contentTable.padTop(20).padLeft(40).padRight(40);
        Label label = new Label("Please Wait", getSkin());
        label.setAlignment(Align.center);
        text(label);
        contentTable.row();
        add(new Spacer(10));
    }
}

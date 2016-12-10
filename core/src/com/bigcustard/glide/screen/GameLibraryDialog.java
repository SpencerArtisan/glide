package com.bigcustard.glide.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.glide.code.Game;
import com.bigcustard.glide.code.GameStore;
import com.bigcustard.scene2dplus.button.ImageButtonPlus;

import java.util.List;

public class GameLibraryDialog extends BaseLibraryDialog implements Disposable {
    private static int COLUMNS = 2;
    private List<Game.Token> games;

    public GameLibraryDialog(Skin skin) {
        super(skin);
        games = new GameStore().allUserGames();
    }

    protected void layoutGameButtons(Skin skin) {
        int i = 0;
        for (Game.Token game : games) {
            ImageTextButton button = createButton(skin, game);
            getButtonTable().add(button).fillX().spaceLeft(10).spaceRight(10).padLeft(10).padRight(6).padTop(6);
            setObject(button, game);
            getButtonTable().add(createDeleteButton(skin, game)).padTop(2);
            if (++i%COLUMNS == 0) getButtonTable().row();
        }
    }

    private Button createDeleteButton(Skin skin, Game.Token game) {
        ImageButtonPlus button = new ImageButtonPlus(skin, "trash-button");
        button.onClick(() -> deleteGame(skin, game));
        return button;
    }

    private void deleteGame(Skin skin, Game.Token game) {
        new GameStore().delete(game);
        games.remove(game);
        layoutControls(skin);
    }


    @Override
    public void dispose() {
        getButtonTable().getChildren().forEach(Actor::clearListeners);
    }
}

package com.bigcustard.glide.screen;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.glide.code.Game;
import com.bigcustard.glide.code.GameStore;
import com.bigcustard.scene2dplus.button.ErrorHandler;
import com.bigcustard.scene2dplus.button.ImageButtonPlus;
import com.bigcustard.util.FutureSuppliers;
import com.google.common.util.concurrent.SettableFuture;

import java.util.List;

public class GameLibraryDialog extends BaseLibraryDialog implements Disposable {
    private static int COLUMNS = 2;
    private List<Game.Token> games;
    private FileHandle folder;

    public GameLibraryDialog(Skin skin, FileHandle folder) {
        super(skin);
        this.folder = folder;
    }

    @Override
    protected void layoutControls() {
        games = new GameStore().allGames(folder);
        super.layoutControls();
    }

    protected void layoutGameButtons() {
        int i = 0;
        for (Game.Token game : games) {
            ImageTextButton button = createButton(game);
            getButtonTable().add(button).fillX().spaceLeft(10).spaceRight(10).padLeft(10).padRight(6).padTop(6);

            setObject(button, game);
            ErrorHandler.onRightClick(button, () -> renameGame(game), false);
            getButtonTable().add(createDeleteButton(game)).padTop(2);
            if (++i%COLUMNS == 0) getButtonTable().row();
        }
    }

    private void renameGame(Game.Token game) {
        NameGameDialog nameGameDialog = new NameGameDialog(game, getSkin());
        nameGameDialog.show(getStage());
        getStage().setKeyboardFocus(nameGameDialog.getNameTextField());
        SettableFuture<String> gameNameSupplier = nameGameDialog.getFutureGameName();
        FutureSuppliers.onGet(() -> gameNameSupplier, newName -> {
            new GameStore().rename(game, newName);
            layoutControls();
        });
    }

    private Button createDeleteButton(Game.Token game) {
        ImageButtonPlus button = new ImageButtonPlus(getSkin(), "trash-button");
        ErrorHandler.onClick(button, () -> deleteGame(game), false);
        return button;
    }

    private void deleteGame(Game.Token game) {
        new GameStore().delete(game);
        games.remove(game);
        layoutControls();
    }


    @Override
    public void dispose() {
        getButtonTable().getChildren().forEach(Actor::clearListeners);
    }
}

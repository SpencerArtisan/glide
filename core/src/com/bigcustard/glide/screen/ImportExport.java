package com.bigcustard.glide.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bigcustard.glide.code.Game;
import com.bigcustard.glide.code.GameStore;
import com.bigcustard.scene2dplus.dialog.FileDialog;

import java.util.function.Consumer;

public class ImportExport {
    private GameStore gameStore;
    private Skin skin;
    private Stage stage;

    public ImportExport(GameStore gameStore, Skin skin, Stage stage) {
        this.gameStore = gameStore;
        this.skin = skin;
        this.stage = stage;
    }

    public void withSelectedFolder(Runnable onDone, Consumer<FileHandle> fileConsumer, String okButtonText) {
        String folder = ".";
        FileDialog files = FileDialog.createDirectoryDialog("Choose game folder", skin, Gdx.files.external(folder), okButtonText);
        files.setResultListener((success, result) -> {
            onDone.run();
            if (success && result != null) {
                fileConsumer.accept(result);
            }
            files.dispose();
            return success;
        });
        files.show(stage);
    }

    void importGame(Runnable onDone) {
        withSelectedFolder(onDone, (source) -> copy(source, gameStore.userFolder()), "Import");
    }

    void exportGame(Game.Token game, Runnable onDone) {
        withSelectedFolder(onDone, (target) -> copy(game.gameFolder(), target), "Export");
    }

    private void copy(FileHandle source, FileHandle target) {
        source.copyTo(target.child(source.name()));
    }
}

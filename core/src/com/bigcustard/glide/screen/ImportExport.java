package com.bigcustard.glide.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bigcustard.glide.code.Game;
import com.bigcustard.scene2dplus.dialog.FileDialog;

import java.util.function.Consumer;

public class ImportExport {
    private Skin skin;
    private Stage stage;

    public ImportExport(Skin skin, Stage stage) {
        this.skin = skin;
        this.stage = stage;
    }

    public void export(Game game) {
        withSelectedFolder((target) -> game.token().gameFolder().copyTo(target));
    }

    public void withSelectedFolder(Consumer<FileHandle> fileConsumer) {
        String folder = ".";
        FileDialog files = FileDialog.createLoadDialog("Choose game folder", skin, Gdx.files.external(folder));
        files.setResultListener((success, result) -> {
            if (success && result != null) {
                fileConsumer.accept(result);
            }
            files.dispose();
            return success;
        });
        files.show(stage);
    }

}

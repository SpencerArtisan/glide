package com.bigcustard.glide.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.glide.code.Game;
import com.bigcustard.glide.code.GameStore;
import com.bigcustard.scene2dplus.Spacer;
import com.google.common.util.concurrent.SettableFuture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class SampleLibraryDialog extends BaseLibraryDialog implements Disposable {
    private Map<Integer, List<Game.Token>> games = new HashMap<>();
    private SettableFuture<Game.Token> futureGame = SettableFuture.create();

    public SampleLibraryDialog(Skin skin) {
        super(skin);
        GameStore gameStore = new GameStore();
        games.put(0, gameStore.allSimpleSampleGames());
        games.put(1, gameStore.allMediumSampleGames());
        games.put(2, gameStore.allHardSampleGames());
    }

    protected void layoutGameButtons() {
        Stream.of(1, 2, 3).forEach(level ->
                getButtonTable().add(new Image(getSkin(), "level" + level)).padTop(10)
        );
        getButtonTable().row();
        Stream.of("Harmless Hacker", "Competent Hacker", "Elite Hacker").forEach(level -> {
                    Label small = new Label(level, getSkin(), "small");
                    small.setColor(Color.YELLOW);
                    small.setAlignment(Align.center);
                    getButtonTable().add(small).center().fillX().padTop(-8).padBottom(6);
                }
        );
        getButtonTable().row();

        for (int row = 0; ; row++) {
            boolean emptyRow = true;
            for (int level = 0; level < 3; level++) {
                if (games.get(level).size() > row) {
                    Game.Token game = games.get(level).get(row);
                    emptyRow = false;
                    ImageTextButton button = createButton(game);
                    getButtonTable().add(button).fillX().spaceLeft(10).spaceRight(15).padLeft(15).padRight(6).padTop(6);
                    setObject(button, game);
                } else {
                    getButtonTable().add(new Spacer(16));
                }
            }
            getButtonTable().row();
            if (emptyRow) return;
        }
    }

    @Override
    protected void result(Object object) {
        Game.Token selected = (Game.Token) object;
        if (object != null) {
            GameStore gameStore = new GameStore();
            selected = gameStore.rename(selected, gameStore.findUniqueName().name());
        }
        super.result(selected);
    }
}

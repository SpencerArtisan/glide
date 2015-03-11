package com.mygdx.game.image;

import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.code.Program;

import java.util.*;

public class ImageAreaModel {
    private Map<String, GameImage> images = new LinkedHashMap<>();
    private Program program;

    public ImageAreaModel(Program program) {
        this.program = program;
    }

    public GameImage add(FileHandle file) {
        GameImage gameImage = new GameImage(file);
        ensureNameUnique(gameImage);
        images.put(gameImage.name(), gameImage);
        return gameImage;
    }

    private void ensureNameUnique(GameImage gameImage) {
        int suffix = 2;
        String candidateName = gameImage.name();
        while (images.containsKey(candidateName)) {
            candidateName = gameImage.name() + suffix;
            if (candidateName.length() > gameImage.maxNameLength()) {
                candidateName = candidateName.substring(0, gameImage.maxNameLength() - 1) + suffix;
            }
            suffix++;
        }
        gameImage.setName(candidateName);
    }

    public List<GameImage> getImages() {
        return new ArrayList<>(images.values());
    }
}

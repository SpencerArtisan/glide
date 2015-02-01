package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ResourceManager {
	private static final String SKIN_NAME = "theme/uiskin.json";
	private static final String ATLAS_NAME = "theme/uiskin.atlas";
	private AssetManager assetManager;

	public ResourceManager() {
		assetManager = new AssetManager();
		assetManager.load(SKIN_NAME, Skin.class);
		assetManager.load(ATLAS_NAME, TextureAtlas.class);
		assetManager.finishLoading();
	}
	
	public Skin getSkin() {
		return assetManager.get(SKIN_NAME);
	}
}

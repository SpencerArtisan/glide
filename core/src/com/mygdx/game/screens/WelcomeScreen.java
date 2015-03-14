package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ResourceManager;

public class WelcomeScreen extends ScreenAdapter {
	private Table table;
	private Stage stage;
	private TextButton newGameButton;
	private TextButton continueGameButton;

	public WelcomeScreen(Viewport viewport, ResourceManager resourceManager) {
		super();
		this.stage = new Stage(viewport);

        Skin skin = resourceManager.getSkin();

	    Label title = createTitle(skin);
	    createNewGameButton(skin);
	    createContinueGameButton(skin);
		TextureRegionDrawable backgroundRegion = createBackground();
		layoutScreen(backgroundRegion);		
		animate(title);

		stage.addActor(table);
		stage.addActor(title);
		
		Gdx.input.setInputProcessor(stage);
	}
	
	public TextButton getNewGameButton() {
		return newGameButton;
	}

	public TextButton getContinueGameButton() {
		return continueGameButton;
	}

	private void createNewGameButton(Skin skin) {
		newGameButton = new TextButton("    New Game    ", skin, "big");
	}

	private void createContinueGameButton(Skin skin) {
		continueGameButton = new TextButton("  Continue Game  ", skin, "big");
	}

	private void animate(Label title) {
		table.getColor().a = 0f;
		table.addAction(Actions.fadeIn(2f));
		title.setPosition(-400, 660);
		title.addAction(Actions.moveTo(50, 660, 0.6f, Interpolation.pow2));
	}

	private void layoutScreen(TextureRegionDrawable backgroundRegion) {
		table = new Table();
		table.row();
		table.add(newGameButton).colspan(2).fillX();
		table.row();
		table.add(continueGameButton).padTop(20f).colspan(2);
		table.background(backgroundRegion);
		table.setFillParent(true);
		table.pack();
	}

	private TextureRegionDrawable createBackground() {
		Texture backgroundTexture = new Texture(Gdx.files.internal("images/WelcomeBackground.png"));
		backgroundTexture.setFilter( TextureFilter.Linear, TextureFilter.Linear );
		TextureRegionDrawable backgroundRegion = new TextureRegionDrawable(new TextureRegion(backgroundTexture));
		return backgroundRegion;
	}

	private Label createTitle(Skin skin) {
	    return new Label("Welcome to Planet Burpl", skin);
	}

    @Override
    public void render(float delta) { 
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                                                                                
        stage.act(Math.min(delta, 1 / 60f));
        stage.draw();
    }  	  
}

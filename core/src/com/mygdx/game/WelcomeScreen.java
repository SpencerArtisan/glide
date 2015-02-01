package com.mygdx.game;

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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class WelcomeScreen extends ScreenAdapter {
	private Table table;
	private Stage stage;
	private BitmapFont font;
	private TextButton.TextButtonStyle tbStyle;
	private TextButton newGameButton;
	private TextButton continueGameButton;
	private TextButton exploreGamesButton;
	
	public WelcomeScreen(Viewport viewport) {
		super();
		this.stage = new Stage(viewport);

		createFont();
		createStyles();
	    Label title = createTitle();
	    createNewGameButton();
	    createContinueGameButton();
	    createExploreGamesButton();
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

	public TextButton getExploreGamesButton() {
		return exploreGamesButton;
	}

	private void createStyles() {
		Texture buttonUp = new Texture(Gdx.files.internal("images/myactor.png"));
		Texture buttonOver = new Texture(Gdx.files.internal("images/myactorOver.png"));
		Texture buttonDown = new Texture(Gdx.files.internal("images/myactorDown.png"));
		tbStyle = new TextButton.TextButtonStyle();
		tbStyle.font = font;
		tbStyle.up = new TextureRegionDrawable(new TextureRegion(buttonUp));
		tbStyle.over = new TextureRegionDrawable(new TextureRegion(buttonOver));
		tbStyle.down = new TextureRegionDrawable(new TextureRegion(buttonDown));
	}

	private void createNewGameButton() {
		newGameButton = new TextButton("    New Game    ", tbStyle);
	}

	private void createContinueGameButton() {
		continueGameButton = new TextButton("  Continue Game  ", tbStyle);
	}

	private void createExploreGamesButton() {
		exploreGamesButton = new TextButton("  Explore Games  ", tbStyle);
	}

	private void animate(Label title) {
		table.getColor().a = 0f;
		table.addAction(Actions.fadeIn(2f));
		title.setPosition(-400, 600);
		title.addAction(Actions.moveTo(50, 600, 0.6f, Interpolation.pow2));
	}

	private void layoutScreen(TextureRegionDrawable backgroundRegion) {
		table = new Table();
		table.row();
		table.add(newGameButton).padTop(150f).colspan(2).fillX();
		table.row();
		table.add(continueGameButton).padTop(20f).colspan(2);
		table.row();
		table.add(exploreGamesButton).padTop(20f).colspan(2);
		table.background(backgroundRegion);
		table.setFillParent(true);
		table.pack();
	}

	private TextureRegionDrawable createBackground() {
		Texture backgroundTexture = new Texture(Gdx.files.internal("images/WelcomeBackground.jpg"));
		backgroundTexture.setFilter( TextureFilter.Linear, TextureFilter.Linear );
		TextureRegionDrawable backgroundRegion = new TextureRegionDrawable(new TextureRegion(backgroundTexture));
		return backgroundRegion;
	}

	private Label createTitle() {
		LabelStyle style = new Label.LabelStyle();
	    style.font = font;
	    return new Label("Welcome to Groovy Planet", style);
	}

	private void createFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/LiberationMono.ttf"));
	    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
	    parameter.size = 28;
	    font = generator.generateFont(parameter);
	}

    @Override
    public void render(float delta) { 
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                                                                                
        stage.act(Math.min(delta, 1 / 60f));
        stage.draw();
    }  	  
}

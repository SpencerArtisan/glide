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
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane.SplitPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.textarea.TextArea;
import com.mygdx.game.textarea.TextAreaModel;

public class CodingScreen extends ScreenAdapter {
	private Stage stage;
	private BitmapFont font;
	private TextButton.TextButtonStyle tbStyle;
	
	public CodingScreen(Viewport viewport) {
		super();
		this.stage = new Stage(viewport);

		createFont();
		createStyles();
		
		TextArea textArea = new TextArea(new TextAreaModel());
//		ImageArea imageArea = new ImageArea();
//		
//		SplitPaneStyle spStyle = new SplitPane.SplitPaneStyle();
//		spStyle.handle = new TextureRegionDrawable(new TextureRegion(
//				new Texture(Gdx.files.internal("images/divider.png"))));
//		SplitPane sp = new SplitPane(textArea, imageArea, false, spStyle);
		stage.addActor(textArea);
		
		Gdx.input.setInputProcessor(textArea.getController());
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

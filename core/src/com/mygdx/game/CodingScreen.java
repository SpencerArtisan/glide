package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.code.Program;
import com.mygdx.game.textarea.TextArea;
import com.mygdx.game.textarea.TextAreaModel;

public class CodingScreen extends ScreenAdapter {
	private Stage stage;
	private BitmapFont font;
	private ScrollPane scrollPane;
	
	public CodingScreen(Program program, Viewport viewport, ResourceManager resourceManager) {
		this.stage = new Stage(viewport);

//		ImageArea imageArea = new ImageArea();
//		SplitPaneStyle spStyle = new SplitPane.SplitPaneStyle();
//		spStyle.handle = new TextureRegionDrawable(new TextureRegion(
//				new Texture(Gdx.files.internal("images/divider.png"))));
//		SplitPane sp = new SplitPane(textArea, imageArea, false, spStyle);

		Skin skin = resourceManager.getSkin();
		TextAreaModel model = new TextAreaModel(program.code());
		model.caret().moveToBottom();
		TextArea textArea = new TextArea(model, skin);

	    scrollPane = new ScrollPane(textArea, skin);
		scrollPane.setWidth(viewport.getWorldWidth());
		scrollPane.setHeight(viewport.getWorldHeight());
		scrollPane.setFadeScrollBars(false);
		stage.addActor(scrollPane);
		stage.setKeyboardFocus(textArea);

		Gdx.input.setInputProcessor(textArea.getController());
	}

    @Override
    public void render(float delta) { 
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                                                                                
        stage.act(Math.min(delta, 1 / 60f));
        stage.draw();
    }

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		scrollPane.setSize(width, height);
	}  	  
}

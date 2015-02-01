package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.code.Code;
import com.mygdx.game.textarea.TextAreaExt;

public class CodingScreen extends ScreenAdapter {
	private Stage stage;
	private BitmapFont font;
	private ScrollPane scrollPane;
	
	public CodingScreen(Viewport viewport, ResourceManager resourceManager) {
		this.stage = new Stage(viewport);

//		ImageArea imageArea = new ImageArea();
//		SplitPaneStyle spStyle = new SplitPane.SplitPaneStyle();
//		spStyle.handle = new TextureRegionDrawable(new TextureRegion(
//				new Texture(Gdx.files.internal("images/divider.png"))));
//		SplitPane sp = new SplitPane(textArea, imageArea, false, spStyle);

		Skin skin = resourceManager.getSkin();
		TextArea textArea = createTextArea(viewport, skin);

	    scrollPane = new ScrollPane(textArea, skin);
		scrollPane.setWidth(viewport.getWorldWidth());
		scrollPane.setHeight(viewport.getWorldHeight());
		scrollPane.setFadeScrollBars(false);
		stage.addActor(scrollPane);
		stage.setKeyboardFocus(textArea);

		Gdx.input.setInputProcessor(stage);
	}

	private TextArea createTextArea(Viewport viewport, Skin skin) {
		TextArea textArea = new TextAreaExt(Code.groovyTemplate(), skin);
		textArea.setPrefRows(44);
		textArea.setCursorPosition(textArea.getText().length());
//		textArea.getStyle().font.setMarkupEnabled(true);
		return textArea;
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

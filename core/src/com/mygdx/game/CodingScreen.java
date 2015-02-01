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
//	private TextButton.TextButtonStyle tbStyle;
//	private TextFieldStyle textFieldStyle;
	
	public CodingScreen(Viewport viewport, ResourceManager resourceManager) {
		this.stage = new Stage(viewport);

//		font = new BitmapFont(Gdx.files.internal("fonts/white-rabbit.fnt"));
		
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
//		scrollPane.setFadeScrollBars(false);
		stage.addActor(scrollPane);
		stage.setKeyboardFocus(textArea);

		Gdx.input.setInputProcessor(stage);
	}

	private TextArea createTextArea(Viewport viewport, Skin skin) {
		TextArea textArea = new TextAreaExt(Code.groovyTemplate(), skin);
		textArea.setPrefRows(44);
		textArea.setCursorPosition(textArea.getText().length());
		textArea.getStyle().font.setMarkupEnabled(true);
		return textArea;
	}
	
//	private void createStyles() {
//		textFieldStyle = new TextField.TextFieldStyle();
//		textFieldStyle.font = font;
//		font.setMarkupEnabled(true);
//		textFieldStyle.fontColor = Color.RED;
//
//		Texture tfSelection = new Texture(Gdx.files.internal("images/tfSelection.png"));
//        Texture tfCursor = new Texture(Gdx.files.internal("images/cursor.png"));
//        textFieldStyle.selection = new TextureRegionDrawable(new TextureRegion(tfSelection));
//        textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(tfCursor)).tint(Color.GREEN);
//	}

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

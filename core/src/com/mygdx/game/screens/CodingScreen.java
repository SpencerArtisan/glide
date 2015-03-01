package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ResourceManager;
import com.mygdx.game.code.Program;
import com.mygdx.game.groovy.GroovyColorCoder;
import com.mygdx.game.textarea.ScrollableTextArea;
import com.mygdx.game.textarea.TextAreaModel;

public class CodingScreen extends ScreenAdapter {
	private Stage stage;
	private ScrollableTextArea textArea;
    private Table table;
    private Table buttonBar;
	
	public CodingScreen(Program program, Viewport viewport, ResourceManager resourceManager) {
		this.stage = new Stage(viewport);

//		ImageArea imageArea = new ImageArea();
//		SplitPaneStyle spStyle = new SplitPane.SplitPaneStyle();
//		spStyle.handle = new TextureRegionDrawable(new TextureRegion(
//				new Texture(Gdx.files.internal("images/divider.png"))));
//		SplitPane sp = new SplitPane(textArea, imageArea, false, spStyle);

		Skin skin = resourceManager.getSkin();

        createButtonBar(viewport, skin);
        createTextArea(program, viewport, skin);
        layoutScreen(viewport, skin);

		stage.addActor(table);
		stage.setKeyboardFocus(textArea.textArea());

		Gdx.input.setInputProcessor(stage);
	}

    private void layoutScreen(Viewport viewport, Skin skin) {
        table = new Table();
//        table.debug();
        table.row();
        table.add(buttonBar);
        table.row();
        table.add(textArea).expand().fill();
        table.setFillParent(true);
        table.pack();
    }

    private void createButtonBar(Viewport viewport, Skin skin) {
//        TextureAtlas.AtlasRegion undoRegion = ResourceManager.shared().getAtlas("editor").findRegion("Tardis");
//        Image undoImage = new Image(undoRegion);
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle();
//        style.imageUp = undoImage;
        ImageTextButton undoButton = new ImageTextButton("Undo", skin, "undo-button");




//        TextButton button = new TextButton("Undo", skin);
        buttonBar = new Table();
        buttonBar.row();
        buttonBar.add(undoButton).left();
//        buttonBar.setFillParent(true);
        buttonBar.pack();
    }

    private void createTextArea(Program program, Viewport viewport, Skin skin) {
        TextAreaModel model = new TextAreaModel(program.code(), new GroovyColorCoder());
        textArea = new ScrollableTextArea(model, skin, viewport);
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
		textArea.setSize(width, height);
	}  	  
}

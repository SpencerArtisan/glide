package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
    private HorizontalGroup buttonBar;
	
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
        table.background(skin.getDrawable("solarizedBackground"));
        table.row();
        table.add(textArea).expand().fill();
        table.row();
        table.add(buttonBar).expandX().fillX();
        table.setFillParent(true);
        table.pack();
    }

    private void createButtonBar(Viewport viewport, Skin skin) {
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle();
        ImageTextButton undoButton = new ImageTextButton("Undo", skin, "undo-button");
        ImageTextButton redoButton = new ImageTextButton("Redo", skin, "redo-button");
        ImageTextButton runButton = new ImageTextButton("Run", skin, "run-button");
        ImageTextButton saveButton = new ImageTextButton("Save", skin, "save-button");

        buttonBar = new HorizontalGroup();
        buttonBar.pad(4);
        buttonBar.space(6);
//        buttonBar.debug();
//        com.badlogic.gdx.scenes.scene2d.ui.Skin$TintedDrawable: {
//            dialogDim: { name: white, color: { r: 0, g: 0, b: 0, a: 0.45 } },
//            solarizedBackground: { name: white, color: { r: 0, g: 0.16862, b: 0.21176, a: 1 } },
//            solarizedLine: { name: white, color: { r: 0.02745, g: 0.21176, b: 0.25882, a: 1 } },
//            solarizedSelection: { name: white, color: { r: 0.34510, g: 0.43137, b: 0.45882, a: 0.5 } }
//            solarizedError: { name: white, color: { r: 0.86275, g: 0.19608, b: 0.18431, a: 0.6 } }
//        },
//
//        TextureRegionDrawable white = (TextureRegionDrawable) skin.getDrawable("white");
//        SpriteDrawable background = white.tint(Color.GREEN);
//        buttonBar.columnDefaults(0).left();
//        buttonBar.background(skin.getDrawable("solarizedBackground"));
//        buttonBar.row().expandX().left();
        buttonBar.addActor(undoButton);
        buttonBar.addActor(redoButton);
        buttonBar.addActor(saveButton);
        buttonBar.addActor(runButton);
//        buttonBar.add(redoButton).left().pad(4);
//        buttonBar.add(runButton).left().pad(4);
//        buttonBar.add(saveButton).left().pad(4);
//        buttonBar.pack();
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

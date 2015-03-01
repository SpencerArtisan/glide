package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ResourceManager;
import com.mygdx.game.code.Program;
import com.mygdx.game.groovy.GroovyColorCoder;
import com.mygdx.game.textarea.ScrollableTextArea;
import com.mygdx.game.textarea.TextAreaModel;
import com.mygdx.game.textarea.command.CommandHistory;

public class CodingScreen extends ScreenAdapter {
	private Stage stage;
	private ScrollableTextArea textArea;
    private Table table;
    private HorizontalGroup buttonBar;
    private CommandHistory commandHistory = new CommandHistory();
	
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
        table.background(skin.getDrawable("solarizedBackground"));
        table.row();
        table.add(textArea).expand().fill();
        table.row();
        table.add(buttonBar).expandX().fillX();
        table.setFillParent(true);
        table.pack();
    }

    private void createButtonBar(Viewport viewport, Skin skin) {
        TextButton undoButton = new TextButton("Past <", skin, "undo-button");
        TextButton redoButton = new TextButton("> Future", skin, "redo-button");
        ImageTextButton runButton = new ImageTextButton(" Run", skin, "run-button");
        ImageTextButton saveButton = new ImageTextButton("Save", skin, "save-button");
        undoButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                commandHistory.undo();
            }
        });
        redoButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                commandHistory.redo();
            }
        });

        buttonBar = new HorizontalGroup();
        buttonBar.pad(8);
        buttonBar.space(10);

        runButton.padLeft(20);
        buttonBar.addActor(undoButton);
        buttonBar.addActor(new Image(skin, "tardis2"));
        buttonBar.addActor(redoButton);
        buttonBar.addActor(spacer(20));
        buttonBar.addActor(saveButton);
        buttonBar.addActor(runButton);
    }

    private Actor spacer(final int width) {
        return new Actor() {
            public float getWidth() {
                return width;
            }
        };
    }

    private void createTextArea(Program program, Viewport viewport, Skin skin) {
        TextAreaModel model = new TextAreaModel(program.code(), new GroovyColorCoder());
        textArea = new ScrollableTextArea(model, skin, viewport, commandHistory);
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

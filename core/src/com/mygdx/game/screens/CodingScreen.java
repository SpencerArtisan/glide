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
import com.mygdx.game.image.ImageArea;
import com.mygdx.game.textarea.ScrollableTextArea;
import com.mygdx.game.textarea.TextAreaModel;
import com.mygdx.game.textarea.command.Command;
import com.mygdx.game.textarea.command.CommandHistory;
import com.mygdx.game.textarea.command.CopyCommand;
import com.mygdx.game.textarea.command.PasteCommand;

import java.util.function.Consumer;

public class CodingScreen extends ScreenAdapter {
    private final Skin skin;
    private Stage stage;
    private Table table;
    private TextAreaModel model;
    private ScrollableTextArea textArea;
    private ImageArea imageArea;
    private HorizontalGroup buttonBar;
    private CommandHistory commandHistory = new CommandHistory();

    public CodingScreen(Program program, Viewport viewport, ResourceManager resourceManager) {
		this.stage = new Stage(viewport);

		skin = resourceManager.getSkin();

        createTextArea(program);
        createButtonBar();
        createImageArea();
        layoutScreen();

		stage.addActor(table);
		stage.setKeyboardFocus(textArea.textArea());

		Gdx.input.setInputProcessor(stage);
	}

    private void layoutScreen() {
        table = new Table();
        table.background(skin.getDrawable("solarizedLine"));
        table.row();
        table.add(textArea).expand().fill();
        table.add(imageArea).width(280).expandY().fillY();
        table.row();
        table.add(buttonBar).colspan(2).expandX().fillX();
        table.setFillParent(true);
        table.pack();
    }

    private void createButtonBar() {
        ImageTextButton runButton = createImageButton(" Run", "run-button");
        ImageTextButton saveButton = createImageButton(" Save", "save-button");
        TextButton undoButton = createTextButton("Past <", commandHistory::undo);
        TextButton redoButton = createTextButton("> Future", commandHistory::redo);
        TextButton copyButton = createTextButton("Copy", () -> commandHistory.execute(new CopyCommand(model)));
        TextButton pasteButton = createTextButton("Paste", () -> commandHistory.execute(new PasteCommand(model)));

        buttonBar = new HorizontalGroup();
        buttonBar.pad(18);
        buttonBar.space(7);

        buttonBar.addActor(undoButton);
        buttonBar.addActor(new Image(skin, "tardis2"));
        buttonBar.addActor(redoButton);
        buttonBar.addActor(spacer(14));
        buttonBar.addActor(copyButton);
        buttonBar.addActor(new Image(skin, "copy"));
        buttonBar.addActor(pasteButton);
        buttonBar.addActor(spacer(14));
        buttonBar.addActor(saveButton);
        buttonBar.addActor(spacer(8));
        buttonBar.addActor(runButton);
    }

    private TextButton createTextButton(String text, Runnable action) {
        TextButton button = new TextButton(text, skin);
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                action.run();
            }
        });
        return button;
    }

    private ImageTextButton createImageButton(String text, String styleName) {
        ImageTextButton button = new ImageTextButton(text, skin, styleName);
        return button;
    }

    private Actor spacer(final int width) {
        return new Actor() {
            public float getWidth() {
                return width;
            }
        };
    }

    private void createImageArea() {
        imageArea = new ImageArea(skin);
    }

    private void createTextArea(Program program) {
        model = new TextAreaModel(program.code(), new GroovyColorCoder());
        textArea = new ScrollableTextArea(model, skin, commandHistory);
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
//		textArea.setSize(width, height);
	}  	  
}

package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.util.concurrent.ListenableFuture;
import com.mygdx.game.App;
import com.mygdx.game.ResourceManager;
import com.mygdx.game.button.ButtonBar;
import com.mygdx.game.code.CodeRunner;
import com.mygdx.game.code.Program;
import com.mygdx.game.groovy.GroovyColorCoder;
import com.mygdx.game.image.ImageArea;
import com.mygdx.game.image.ImageAreaModel;
import com.mygdx.game.image.ImageGrabber;
import com.mygdx.game.textarea.ScrollableTextArea;
import com.mygdx.game.textarea.TextAreaModel;
import com.mygdx.game.textarea.command.*;

public class CodingScreen extends ScreenAdapter {
    private final Skin skin;
    private Stage stage;
    private Table table;
    private TextAreaModel model;
    private ScrollableTextArea textArea;
    private ImageArea imageArea;
    private ButtonBar buttonBar;
    private CommandHistory commandHistory = new CommandHistory();
    private Program program;

    public CodingScreen(Program program, Viewport viewport, ResourceManager resourceManager) {
        this.program = program;
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
        buttonBar = new ButtonBar(skin);
        buttonBar.addTextButton("Past <", () -> new UndoCommand(commandHistory));
        buttonBar.addImage("tardis2");
        buttonBar.addTextButton("> Future", () -> new RedoCommand(commandHistory));
        buttonBar.addSpacer(14);
        buttonBar.addTextButton("Copy", () -> new CopyCommand(model));
        buttonBar.addImage("copy");
        buttonBar.addTextButton("Paste", () -> new PasteCommand(model));
        buttonBar.addSpacer(14);
        buttonBar.addImageButton(" Run", "run-button", () -> new SaveCommand(model, program, this::getGameName));
        buttonBar.addSpacer(14);
        buttonBar.addImageButton(" Exit", "exit-button", () -> new ExitCommand(model, program, this::getGameName));
        model.addListener(buttonBar::refreshEnabledStatuses);
    }

    private void createImageArea() {
        ImageAreaModel imageAreaModel = new ImageAreaModel(program);
        ImageGrabber imageGrabber = new ImageGrabber(program);
        imageArea = new ImageArea(imageAreaModel, imageGrabber, skin);
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

    private ListenableFuture<String> getGameName() {
        SaveGameDialog saveGameDialog = new SaveGameDialog(program, skin);
        saveGameDialog.setPosition(stage.getWidth() / 2, stage.getHeight() / 2);
        saveGameDialog.show(stage);
        stage.setKeyboardFocus(saveGameDialog.getNameTextField());
        return saveGameDialog.getFutureGameName();
    }
}

package com.bigcustard.planet.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigcustard.planet.code.command.ExitCommand;
import com.bigcustard.planet.code.command.SaveCommand;
import com.google.common.util.concurrent.ListenableFuture;
import com.bigcustard.scene2dplus.button.ButtonBar;
import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.GroovyColorCoder;
import com.bigcustard.scene2dplus.image.ImageArea;
import com.bigcustard.scene2dplus.image.ImageAreaModel;
import com.bigcustard.scene2dplus.textarea.ScrollableTextArea;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.bigcustard.scene2dplus.textarea.command.*;

public class CodingScreen extends ScreenAdapter {
    private final Skin skin;
    private Stage stage;
    private Table table;
    private TextAreaModel model;
    private ScrollableTextArea textArea;
    private ImageArea imageArea;
    private ButtonBar buttonBar;
    private CommandHistory commandHistory = new CommandHistory();
    private Game game;
    private Runnable exitListener;

    public CodingScreen(Game game, Viewport viewport, ResourceManager resourceManager, Runnable exitListener) {
        this.game = game;
        this.exitListener = exitListener;
        this.stage = new Stage(viewport);

		skin = resourceManager.getSkin();

        createTextArea(game);
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
        buttonBar.addSpacer(1);
        buttonBar.addTextButton("Past <", () -> new UndoCommand(commandHistory));
        buttonBar.addImage("tardis2");
        buttonBar.addTextButton("> Future", () -> new RedoCommand(commandHistory));
        buttonBar.addSpacer(16);
        buttonBar.addTextButton("Copy", () -> new CopyCommand(model));
        buttonBar.addImage("copy");
        buttonBar.addTextButton("Paste", () -> new PasteCommand(model));
        buttonBar.addSpacer(16);
        buttonBar.addImageButton(" Run", "run-button", () -> new SaveCommand(model, game, this::getGameName));
        buttonBar.addSpacer(16);
        buttonBar.addImageButton(" Exit", "exit-button", () -> new ExitCommand(model, game, this::saveGameChoice, this::getGameName, exitListener));
        model.addListener(buttonBar::refreshEnabledStatuses);
    }

    private void createImageArea() {
        ImageAreaModel imageAreaModel = game;
        imageArea = new ImageArea(imageAreaModel, skin);
    }

    private void createTextArea(Game game) {
        model = new TextAreaModel(game.code(), new GroovyColorCoder());
        model.addListener(() -> game.setCode(model.getText()));
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
        NameGameDialog nameGameDialog = new NameGameDialog(game, skin);
        nameGameDialog.setPosition(stage.getWidth() / 2, stage.getHeight() / 2);
        nameGameDialog.show(stage);
        stage.setKeyboardFocus(nameGameDialog.getNameTextField());
        return nameGameDialog.getFutureGameName();
    }

    private ListenableFuture<Boolean> saveGameChoice() {
        SaveChoiceDialog saveGameDialog = new SaveChoiceDialog(skin);
        saveGameDialog.setPosition(stage.getWidth() / 2, stage.getHeight() / 2);
        saveGameDialog.show(stage);
        return saveGameDialog.getFutureSaveChoice();
    }
}

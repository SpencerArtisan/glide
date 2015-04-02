package com.bigcustard.planet.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.command.ExitCommand;
import com.bigcustard.planet.code.command.RunCommand;
import com.bigcustard.planet.plugin.Plugin;
import com.bigcustard.scene2dplus.button.ButtonBar;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.scene2dplus.command.RedoCommand;
import com.bigcustard.scene2dplus.command.UndoCommand;
import com.bigcustard.scene2dplus.image.ImageArea;
import com.bigcustard.scene2dplus.image.ImageAreaController;
import com.bigcustard.scene2dplus.image.ImageAreaModel;
import com.bigcustard.scene2dplus.textarea.ScrollableTextArea;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.bigcustard.scene2dplus.textarea.command.CopyCommand;
import com.bigcustard.scene2dplus.textarea.command.PasteCommand;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.function.Consumer;

public class CodingScreen extends ScreenAdapter {
    private Skin skin;
    private Stage stage;
    private Table layoutTable;
    private TextAreaModel model;
    private ScrollableTextArea textArea;
    private ImageArea imageArea;
    private ButtonBar buttonBar;
    private CommandHistory commandHistory = new CommandHistory();
    private Game game;
    private Runnable exitToMainMenu;
    private Consumer<Game> runGame;
    private Plugin plugin;

    public CodingScreen(Game game, Viewport viewport, ResourceManager resourceManager, Runnable exitToMainMenu, Consumer<Game> runGame, Plugin plugin) {
        this.game = game;
        this.exitToMainMenu = exitToMainMenu;
        this.runGame = runGame;
        this.plugin = plugin;
        this.stage = new Stage(viewport);

		skin = resourceManager.getSkin();

        createTextArea(game);
        createButtonBar();
        createImageArea();
        layoutScreen();

		stage.addActor(layoutTable);
		stage.setKeyboardFocus(textArea.textArea());

		Gdx.input.setInputProcessor(stage);
	}

    private void layoutScreen() {
        layoutTable = new Table();
        layoutTable.background(skin.getDrawable("solarizedLine"));
        layoutTable.row();
        layoutTable.add(textArea).expand().fill();
        layoutTable.add(imageArea).width(280).expandY().fillY();
        layoutTable.row();
        layoutTable.add(buttonBar).colspan(2).expandX().fillX();
        layoutTable.setFillParent(true);
        layoutTable.pack();
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
        buttonBar.addImageButton(" Run", "run-button", () -> new RunCommand(game, this::getGameName, runGame, plugin.syntax()));
        buttonBar.addSpacer(16);
        buttonBar.addImageButton(" Exit", "exit-button", () -> new ExitCommand(game, this::saveGameChoice, this::getGameName, exitToMainMenu));
        game.registerChangeListener((game) -> buttonBar.refreshEnabledStatuses());
    }

    private void createImageArea() {
        ImageAreaModel imageAreaModel = game.images();
        imageArea = new ImageArea(imageAreaModel, skin);
        new ImageAreaController(imageArea, imageAreaModel, commandHistory).init();
    }

    private void createTextArea(Game game) {
        model = new TextAreaModel(game.code(), plugin.colorCoder());
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
        nameGameDialog.show(stage);
        stage.setKeyboardFocus(nameGameDialog.getNameTextField());
        return nameGameDialog.getFutureGameName();
    }

    private ListenableFuture<Boolean> saveGameChoice() {
        SaveChoiceDialog saveGameDialog = new SaveChoiceDialog(skin);
        saveGameDialog.show(stage);
        return saveGameDialog.getFutureSaveChoice();
    }
}

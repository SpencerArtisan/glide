package com.bigcustard.planet.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.command.ExitCommand;
import com.bigcustard.planet.code.command.RunCommand;
import com.bigcustard.scene2dplus.button.ButtonBar;
import com.bigcustard.scene2dplus.command.RedoCommand;
import com.bigcustard.scene2dplus.command.UndoCommand;
import com.bigcustard.scene2dplus.dialog.ErrorDialog;
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
    private Game game;
    private Runnable exitToMainMenu;
    private Consumer<Screen> setScreen;
    private Label errorLabel;

    public CodingScreen(Game game, Viewport viewport, Skin skin, Runnable exitToMainMenu, Consumer<Screen> setScreen) {
        this.game = game;
        this.exitToMainMenu = exitToMainMenu;
        this.setScreen = setScreen;
        this.stage = new Stage(viewport);
		this.skin = skin;

        createTextArea(game);
        createErrorLabel(game);
        createButtonBar();
        createImageArea();
        layoutScreen();

		stage.addActor(layoutTable);
		stage.setKeyboardFocus(textArea.textArea());
        Gdx.input.setInputProcessor(stage);
	}

    private void layoutScreen() {
        Table textAreaTable = new Table();
        textAreaTable.add(textArea).fill().expand();
        textAreaTable.row();
        textAreaTable.add(errorLabel).fillX();

        layoutTable = new Table();
        layoutTable.background(skin.getDrawable("solarizedLine"));
        layoutTable.row();
        layoutTable.add(textAreaTable).expand().fill();
        layoutTable.add(imageArea).width(280).expandY().fillY();
        layoutTable.row();
        layoutTable.add(buttonBar).colspan(2).expandX().fillX();
        layoutTable.setFillParent(true);
        layoutTable.pack();
    }

    private void createButtonBar() {
        buttonBar = new ButtonBar(skin);
        buttonBar.addSpacer(1);
        buttonBar.addTextButton("Past <", () -> new UndoCommand(game.getCommandHistory()));
        buttonBar.addImage("tardis2");
        buttonBar.addTextButton("> Future", () -> new RedoCommand(game.getCommandHistory()));
        buttonBar.addSpacer(16);
        buttonBar.addTextButton("Copy", () -> new CopyCommand(model));
        buttonBar.addImage("copy");
        buttonBar.addTextButton("Paste", () -> new PasteCommand(model));
        buttonBar.addSpacer(16);
        buttonBar.addImageButton(" Run", "run-button", () -> new RunCommand(game, this::showRunScreen));
        buttonBar.addSpacer(16);
        buttonBar.addImageButton(" Exit", "exit-button", () -> new ExitCommand(game, this::saveGameChoice, this::getGameName, this::errorReporter, exitToMainMenu));
        game.registerChangeListener((game) -> buttonBar.refreshEnabledStatuses());
    }

    private void showRunScreen(Game game) {
        new RunScreen(skin, game, setScreen, () -> {
            setScreen.accept(this);
            Gdx.input.setInputProcessor(stage);
            stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        }).showScreen();
    }

    private void createImageArea() {
        ImageAreaModel imageAreaModel = game.imageModel();
        imageArea = new ImageArea(imageAreaModel, skin);
        new ImageAreaController(imageArea, imageAreaModel, game.getCommandHistory()).init();
    }

    private void createTextArea(Game game) {
        model = new TextAreaModel(game.code(), game.language().codeColorCoder());
        model.addChangeListener((m) -> game.setCode(model.text()));
        textArea = new ScrollableTextArea(model, skin, game.getCommandHistory());
    }

    private void createErrorLabel(Game game) {
        errorLabel = new Label("", skin, "error");
        errorLabel.setVisible(false);
        errorLabel.setWrap(true);
        game.registerChangeListener((g) -> {
            errorLabel.setVisible(game.runtimeError() != null);
            errorLabel.setText("Runtime error: " + game.runtimeError());
        });
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
	}

    private void errorReporter(Exception e, Runnable onClosed) {
        ErrorDialog errorDialog = new ErrorDialog(skin, e, onClosed);
        errorDialog.show(stage);
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

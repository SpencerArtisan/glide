package com.bigcustard.planet.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.GameStore;
import com.bigcustard.planet.code.command.ExitCommand;
import com.bigcustard.planet.code.command.RunCommand;
import com.bigcustard.scene2dplus.button.ButtonBar;
import com.bigcustard.scene2dplus.command.RedoCommand;
import com.bigcustard.scene2dplus.command.UndoCommand;
import com.bigcustard.scene2dplus.dialog.ErrorDialog;
import com.bigcustard.scene2dplus.image.EditableImage;
import com.bigcustard.scene2dplus.image.ImageModel;
import com.bigcustard.scene2dplus.image.ImageUtils;
import com.bigcustard.scene2dplus.resource.Resource;
import com.bigcustard.scene2dplus.resource.ResourceArea;
import com.bigcustard.scene2dplus.resource.ResourceSet;
import com.bigcustard.scene2dplus.sound.SoundArea;
import com.bigcustard.scene2dplus.sound.SoundAreaController;
import com.bigcustard.scene2dplus.sound.SoundAreaModel;
import com.bigcustard.scene2dplus.tab.TabControl;
import com.bigcustard.scene2dplus.textarea.ScrollableTextArea;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.bigcustard.scene2dplus.textarea.command.CopyCommand;
import com.bigcustard.scene2dplus.textarea.command.PasteCommand;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CodingScreen extends ScreenAdapter {
    private Skin skin;
    private Stage stage;
    private Table layoutTable;
    private TextAreaModel model;
    private ScrollableTextArea textArea;
    private ResourceArea imageArea;
    private SoundArea soundArea;
    private TabControl resourceArea;
    private ButtonBar buttonBar;
    private Game game;
    private GameStore gameStore;
    private Consumer<Screen> setScreen;
    private ScreenFactory screenFactory;
    private Label errorLabel;
    private ScheduledFuture<?> gameSavingProcess;

    public CodingScreen(Game game, GameStore gameStore, Viewport viewport, Consumer<Screen> setScreen, ScreenFactory screenFactory, Skin skin) {
        this.game = game;
        this.gameStore = gameStore;
        this.setScreen = setScreen;
        this.screenFactory = screenFactory;
        this.stage = new Stage(viewport);
        this.skin = skin;

        createTextArea(game);
        createErrorLabel(game);
        createButtonBar();
        createResourceArea();
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
        layoutTable.background(skin.getDrawable("solarizedNew"));
        layoutTable.row();
        layoutTable.add(textAreaTable).expand().fill();
        layoutTable.add(resourceArea).width(280).expandY().fillY();
        layoutTable.row();
        layoutTable.add(buttonBar).colspan(2).expandX().fillX();
        layoutTable.setFillParent(true);
        layoutTable.pack();
    }

    private void createButtonBar() {
        buttonBar = new ButtonBar(skin);
        buttonBar.addSpacer(1);
        buttonBar.addTextButton("Past <", () -> new UndoCommand(game.commandHistory()));
        buttonBar.addImage("tardis2");
        buttonBar.addTextButton("> Future", () -> new RedoCommand(game.commandHistory()));
        buttonBar.addSpacer(16);
        buttonBar.addTextButton("Copy", () -> new CopyCommand(model));
        buttonBar.addImage("copy");
        buttonBar.addTextButton("Paste", () -> new PasteCommand(model));
        buttonBar.addSpacer(16);
        buttonBar.addImageButton(" Run", "run-button", () -> new RunCommand(game, gameStore, this::showRunScreen));
        buttonBar.addSpacer(16);
        buttonBar.addImageButton(" Exit", "exit-button", () -> new ExitCommand(game, gameStore, this::saveGameChoice, this::getGameName, this::errorReporter, this::exitToMainMenu));

        gameSavingProcess = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            System.out.print(".");
            buttonBar.refreshEnabledStatuses();
            gameStore.save(game);
        }, 0, 4, TimeUnit.SECONDS);
    }

    private void exitToMainMenu() {
        gameSavingProcess.cancel(true);
        setScreen.accept(screenFactory.createWelcomeScreen());
        dispose();
    }

    private void showRunScreen(Game game) {
        RuntimeFacade.INSTANCE.run(game, () -> {
            setScreen.accept(this);
            Gdx.input.setInputProcessor(stage);
        });
    }

    private void createResourceArea() {
        createImageArea();
        createSoundArea();
        resourceArea = new TabControl();
        resourceArea.addTab(imageArea, new TextButton("Images  ", skin, "tab"));
        resourceArea.addTab(soundArea, new TextButton("Sounds  ", skin, "tab"));
        resourceArea.background(skin.getDrawable("solarizedBackground"));
        resourceArea.init();
    }

    private void createImageArea() {
        List<ImageModel> imageModels = game.imageModel().images();
        List<Resource<ImageModel>> editableImages = imageModels
                .stream()
                .map((model) -> new EditableImage(model, skin, game.commandHistory()))
                .collect(Collectors.toList());
        ResourceSet<ImageModel> resourceSet = new ResourceSet<>(editableImages, game.commandHistory());
        resourceSet.resources().watch((images) -> {
            List<ImageModel> models = images.stream().map(Resource::model).collect(Collectors.toList());
            game.imageModel().images(models);
        });
        imageArea = new ResourceArea<>(skin, resourceSet, game.commandHistory(), (stream, url) -> {
            ImageModel model = ImageUtils.importImage(stream, url, game.imageModel().folder());
            return new EditableImage(model, skin, game.commandHistory());
        });
    }

    private void createSoundArea() {
        SoundAreaModel soundAreaModel = game.soundModel();
        soundArea = new SoundArea(soundAreaModel, skin);
        new SoundAreaController(soundArea, soundAreaModel, game.commandHistory()).init();
    }

    private void createTextArea(Game game) {
        model = new TextAreaModel(game.code(), game.language().codeColorCoder());
        model.preInsertVetoer(game.language()::vetoPreInsert);
        model.addChangeListener((m) -> game.code(model.text()));
        textArea = new ScrollableTextArea(model, skin, game.commandHistory());
    }

    private void createErrorLabel(Game game) {
        errorLabel = new Label("", skin, "error");
        errorLabel.setVisible(false);
        errorLabel.setWrap(true);
        game.registerChangeListener((g) -> {
            String error = game.runtimeError();
            errorLabel.setVisible(error != null);
            errorLabel.setText(error == null ? null : "Runtime error: " + error);
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
        stage.getViewport().update(width, height, true);
    }

    private void errorReporter(Exception e, Runnable onClosed) {
        ErrorDialog errorDialog = new ErrorDialog(e, onClosed, skin);
        errorDialog.show(stage);
    }

    private ListenableFuture<String> getGameName() {
        NameGameDialog nameGameDialog = new NameGameDialog(game, skin);
        nameGameDialog.show(stage);
        stage.setKeyboardFocus(nameGameDialog.getNameTextField());
        return nameGameDialog.getFutureGameName();
    }

    private ListenableFuture<Boolean> saveGameChoice() {
        gameSavingProcess.cancel(true);
        SaveChoiceDialog saveGameDialog = new SaveChoiceDialog(skin);
        saveGameDialog.show(stage);
        return saveGameDialog.getFutureSaveChoice();
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        model.dispose();
        imageArea.dispose();
        soundArea.dispose();
        game.dispose();
    }
}

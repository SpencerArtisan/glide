package com.bigcustard.glide.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigcustard.glide.code.Game;
import com.bigcustard.glide.code.GameStore;
import com.bigcustard.glide.code.command.NewCommand;
import com.bigcustard.glide.code.language.Language;
import com.bigcustard.scene2dplus.actions.ChangePaddingAction;
import com.bigcustard.scene2dplus.button.ErrorHandler;
import com.bigcustard.scene2dplus.button.ImageButtonPlus;
import com.bigcustard.scene2dplus.button.TextButtonPlus;
import com.bigcustard.scene2dplus.dialog.ErrorDialog;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WelcomeScreen extends ScreenAdapter {
    private static boolean welcomed = false;
    private final Skin skin;
    private Table table;
    private Table outerTable;
    private Stage stage;
    private TextButtonPlus newGameButton;
    private TextButtonPlus exportGameButton;
    private TextButtonPlus importGameButton;
    private TextButtonPlus samplesButton;
    private TextButtonPlus myGamesButton;
    private TextButtonPlus quitButton;
    private Consumer<Screen> setScreen;
    private ScreenFactory screenFactory;
    private GameStore gameStore;
    private Image title;
    private Cell<Image> titleCell;
    private Image blurpLogo;
    private Image poweredBy;
    private Label version;
    private ScheduledExecutorService executorService;
    private ImportExport importExport;
    private ImageButtonPlus trash;

    WelcomeScreen(GameStore gameStore, Viewport viewport, Consumer<Screen> setScreen, ScreenFactory screenFactory, Skin skin) {
        this.setScreen = setScreen;
        this.screenFactory = screenFactory;
        this.gameStore = gameStore;
        this.stage = new Stage(viewport);
        this.skin = skin;
        this.importExport = new ImportExport(gameStore, skin, stage);

        createTitle();
        createBlurpLogo();
        createVersion();
        createTrash();
        createNewGameButton();
        createExportGameButton();
        createImportGameButton();
        createSamplesButton();
        createMyGamesButton();
        createQuitButton();
        refreshButtonEnabledStatuses();
        layoutScreen(createBackground());

        refreshButtonEnabledStatuses();
        animateTitle();
        animateBlurp();

        setScreen.accept(this);
        Gdx.input.setInputProcessor(stage);
    }

    public Table getTable() {
        return table;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(delta, 1 / 60f));
        stage.draw();
    }

    private void refreshButtonEnabledStatuses() {
        samplesButton.setTouchable(Touchable.enabled);

        boolean areGames = gameStore.allUserGames().size() > 0;
        myGamesButton.setDisabled(!areGames);
        exportGameButton.setDisabled(!areGames);
    }

    private void createVersion() {
        version = new Label("v1.3", skin, "small");
        version.setColor(Color.LIGHT_GRAY);
    }

    private void createTitle() {
        title = new Image(skin, "glide");
    }

    private void createBlurpLogo() {
        blurpLogo = new Image(skin, "Blurp");
    }

    private ListenableFuture<Language> saveGameChoice() {
        LanguageChoiceDialog languageChoiceDialog = new LanguageChoiceDialog(skin);
        languageChoiceDialog.show(stage);
        return languageChoiceDialog.getFutureLanguageChoice();
    }

    private void createImportGameButton() {
        importGameButton = new TextButtonPlus("  Import Game  ", skin, "big");
        menuHidingButton(importGameButton, (onDone) -> importExport.importGame(onDone));
    }

    private void createExportGameButton() {
        exportGameButton = new TextButtonPlus("  Export Game  ", skin, "big");
        createGamesButton(exportGameButton, () -> new GameLibraryDialog(skin, gameStore.userFolder()), (game) -> importExport.exportGame(game, this::showMainMenu));
    }

    private void createSamplesButton() {
        samplesButton = new TextButtonPlus("     Hack a Game     ", skin, "big");
        createGamesButton(samplesButton, () -> new SampleLibraryDialog(skin), this::showCodingScreen);
    }

    private void createMyGamesButton() {
        myGamesButton = new TextButtonPlus("    My Games    ", skin, "big");
        createGamesButton(myGamesButton, () -> new GameLibraryDialog(skin, gameStore.userFolder()), this::showCodingScreen);
    }

    private void createNewGameButton() {
        newGameButton = new TextButtonPlus("    Write a Game    ", skin, "big");
        menuHidingButton(newGameButton, (afterDone) ->
                new NewCommand(this::saveGameChoice, this::showCodingScreen, this::showMainMenu).execute()
        );
    }

    private void createGamesButton(Button button, Supplier<BaseLibraryDialog> dialogSupplier, Consumer<Game.Token> handleChoice) {
        menuHidingButton(button, (afterDone) -> dialogSupplier.get().display(stage, afterDone, handleChoice));
    }

    private void menuHidingButton(Button button, Consumer<Runnable> buttonHandler) {
        ErrorHandler.onClick(button, () -> {
            hideMainMenu();
            buttonHandler.accept(this::showMainMenu);
        }, false);
    }

    private void createTrash() {
        trash = new ImageButtonPlus(skin, "trash-button");
        createGamesButton(trash, () -> new GameLibraryDialog(skin, gameStore.trashFolder()), this::showCodingScreen);
    }

    private void showCodingScreen(Language language) {
        showCodingScreen(() -> gameStore.create(language));
    }

    private void showCodingScreen(Game.Token game) {
        showCodingScreen(() -> gameStore.load(game));
    }

    private void showCodingScreen(Supplier<Game> programSupplier) {
        ErrorHandler.tryAndRecover(stage, skin, () -> {
            try {
                showMainMenu();
                CodingScreen codingScreen = screenFactory.createCodingScreen(programSupplier.get());
                setScreen.accept(codingScreen);
                dispose();
            } catch (Exception e) {
                showError(e);
            }
        });
    }

    private void createQuitButton() {
        quitButton = new TextButtonPlus("       Quit       ", skin, "big");
        quitButton.onClick(Gdx.app::exit);
    }

    private void animateBlurp() {
        blurpLogo.setScale(0.4f);
        blurpLogo.setOrigin(blurpLogo.getWidth() / 2, blurpLogo.getHeight() / 2);
        blurpLogo.setPosition(stage.getWidth() - 450, -50);
        stage.addActor(blurpLogo);

        poweredBy = new Image(skin, "powered_by");
        poweredBy.setOrigin(poweredBy.getWidth() / 2, poweredBy.getHeight() / 2);
        stage.addActor(poweredBy);

        if (!welcomed) {
            poweredBy.setPosition(-999, 0);
            poweredBy.addAction(Actions.sequence(
                    Actions.delay(0.6f),
                    Actions.scaleBy(20f, 20f),
                    Actions.moveTo(stage.getWidth() - 330, 10),
                    Actions.scaleTo(1f, 1f, 0.4f, Interpolation.pow2Out)
            ));

            stage.addAction(Actions.sequence(
                    Actions.scaleTo(1.03f, 1.03f),
                    Actions.delay(0.9f),
                    Actions.scaleTo(1f, 1f, 0.5f, Interpolation.bounceOut)
            ));
            executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(
                    () -> Gdx.audio.newSound(Gdx.files.internal("sound/TireBlow.wav")).play(),
                    980,
                    TimeUnit.MILLISECONDS);
        }
        welcomed = true;
    }

    private void animateTitle() {
        table.getColor().a = 0f;
        outerTable.getColor().a = 0f;
        outerTable.addAction(Actions.fadeIn(1.9f));
        titleCell.padLeft(1800);
        outerTable.addAction(Actions.sequence(
                Actions.delay(0.5f),
                new ChangePaddingAction(this.titleCell, 50, 0.6f, Interpolation.pow2Out)));
        this.title.setScaleX(1.3f);
        this.title.addAction(Actions.sequence(
                Actions.delay(0.6f),
                Actions.scaleTo(1, 1, 0.6f, Interpolation.pow2)));
        this.table.addAction(Actions.sequence(Actions.delay(0.4f), Actions.fadeIn(0.2f)));
    }

    private void layoutScreen(TextureRegionDrawable backgroundRegion) {
        outerTable = new Table();
        table = new Table();
        table.add(newGameButton).colspan(2).fillX();
        table.row();
        table.add(samplesButton).padTop(20f).colspan(2).fillX();
        table.row();
        table.add(myGamesButton).padTop(20f).colspan(2).fillX();
        table.row();
        table.add(importGameButton).padTop(20f).colspan(2).fillX();
        table.row();
        table.add(exportGameButton).padTop(20f).colspan(2).fillX();
        table.row();
        table.add(quitButton).padTop(20f).colspan(2).fillX();
        outerTable.background(backgroundRegion);
        title.setX(-55);
        titleCell = outerTable.add(title).expand().padTop(10).padBottom(-100).padLeft(50).top().left();
        outerTable.row();
        outerTable.add(table).expandY().top();
        outerTable.row();
        outerTable.add(version).left().bottom();
        outerTable.add(trash).right().bottom();
        outerTable.setFillParent(true);
        outerTable.pack();
        stage.addActor(outerTable);
    }

    private TextureRegionDrawable createBackground() {
        int backgroundSketch = 1 + Math.abs(new Random().nextInt()) % 5;
        Texture backgroundTexture = new Texture(Gdx.files.internal("bigimages/sketch" + backgroundSketch + ".png"));
        backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        return new TextureRegionDrawable(new TextureRegion(backgroundTexture));
    }

    private void showError(Throwable e) {
        new ErrorDialog(e, this::showMainMenu, skin).show(stage);
    }

    private void showMainMenu() {
        getTable().setVisible(true);
        refreshButtonEnabledStatuses();
    }

    private void hideMainMenu() {
        getTable().setVisible(false);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (poweredBy.getX() > -900) poweredBy.setX(width - 330);
        blurpLogo.setX(width - 450);
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        myGamesButton.clearListeners();
        newGameButton.clearListeners();
        quitButton.clearListeners();
        samplesButton.clearListeners();
        executorService.shutdown();
    }
}

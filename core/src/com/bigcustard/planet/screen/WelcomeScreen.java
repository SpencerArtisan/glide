package com.bigcustard.planet.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.GameStore;
import com.bigcustard.planet.code.Language;
import com.bigcustard.planet.code.command.NewCommand;
import com.bigcustard.scene2dplus.actions.ChangePaddingAction;
import com.bigcustard.scene2dplus.dialog.ErrorDialog;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WelcomeScreen extends ScreenAdapter {
	private final Skin skin;
	private Table table;
	private Table outerTable;
	private Stage stage;
	private TextButton newGameButton;
	private TextButton continueGameButton;
	private TextButton samplesButton;
	private TextButton myGamesButton;
	private TextButton quitButton;
	private Consumer<Screen> setScreen;
	private ScreenFactory screenFactory;
	private GameStore gameStore;
	private Image title;
	private Cell<Image> titleCell;

	WelcomeScreen(GameStore gameStore, Viewport viewport, Consumer<Screen> setScreen, ScreenFactory screenFactory, Skin skin) {
		super();
		this.setScreen = setScreen;
		this.screenFactory = screenFactory;
		this.gameStore = gameStore;
		this.stage = new Stage(viewport);
		this.skin = skin;

		createTitle();
	    createNewGameButton();
		createContinueGameButton();
		createSamplesButton();
		createMyGamesButton();
		createQuitButton();
		refreshButtonEnabledStatuses();
		layoutScreen(createBackground());

		refreshButtonEnabledStatuses();
		animateTitle();
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
		boolean continueEnabled = gameStore.hasMostRecent();
		continueGameButton.setDisabled(!continueEnabled);
		continueGameButton.setTouchable(continueEnabled ? Touchable.enabled : Touchable.disabled);

		boolean samplesEnabled = gameStore.allSampleGames().size() > 0;
		samplesButton.setDisabled(!samplesEnabled);
		samplesButton.setTouchable(samplesEnabled ? Touchable.enabled : Touchable.disabled);

		boolean myGamesEnabled = gameStore.allUserGames().size() > 0;
		myGamesButton.setDisabled(!myGamesEnabled);
		myGamesButton.setTouchable(myGamesEnabled ? Touchable.enabled : Touchable.disabled);
	}

	private void createTitle() {
		title = new Image(skin, "glide");
	}

	private void createNewGameButton() {
		newGameButton = new TextButton("    New Game    ", skin, "big");
		newGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hideMainMenu();
				NewCommand newCommand = new NewCommand(
						WelcomeScreen.this::saveGameChoice,
						(language) -> showCodingScreen(() -> gameStore.create(language)),
						WelcomeScreen.this::showMainMenu);
				newCommand.execute();
			}
		});
	}

	private ListenableFuture<Language> saveGameChoice() {
		LanguageChoiceDialog languageChoiceDialog = new LanguageChoiceDialog(skin);
		languageChoiceDialog.show(stage);
		return languageChoiceDialog.getFutureLanguageChoice();
	}

	private void createContinueGameButton() {
		continueGameButton = new TextButton("  Continue Game  ", skin, "big");
		continueGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showCodingScreen(gameStore::mostRecent);
			}
		});
	}

	private void createSamplesButton() {
		samplesButton = new TextButton("     Samples     ", skin, "big");
		createGamesButton(samplesButton, () -> GameLibraryDialog.sampleGames(skin));
	}

	private void createMyGamesButton() {
		myGamesButton = new TextButton("    My Games    ", skin, "big");
		createGamesButton(myGamesButton, () -> GameLibraryDialog.userGames(skin));
	}

	private void createGamesButton(TextButton button, Supplier<GameLibraryDialog> dialogSupplier) {
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hideMainMenu();
				final GameLibraryDialog dialog = dialogSupplier.get();
				dialog.show(stage);
				Futures.addCallback(dialog.getFutureGame(), new FutureCallback<Game>() {
					@Override
					public void onSuccess(Game game) {
						showMainMenu();
						refreshButtonEnabledStatuses();
						if (game != null) {
							dialog.remove();
							showCodingScreen(() -> game);
						}
					}

					@Override
					public void onFailure(Throwable t) {
						showError(t);
					}
				});
			}
		});
	}

	private void createQuitButton() {
		quitButton = new TextButton("       Quit       ", skin, "big");
		quitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
	}

	private void animateTitle() {
        table.getColor().a = 0f;
        outerTable.getColor().a = 0f;
		outerTable.addAction(Actions.fadeIn(1.9f));
		titleCell.padLeft(1400);
		outerTable.addAction(Actions.sequence(
				Actions.delay(0.6f),
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
		table.add(continueGameButton).padTop(20f).colspan(2).fillX();
		table.row();
		table.add(samplesButton).padTop(20f).colspan(2).fillX();
		table.row();
		table.add(myGamesButton).padTop(20f).colspan(2).fillX();
		table.row();
		table.add(quitButton).padTop(20f).colspan(2).fillX();
		outerTable.background(backgroundRegion);
		title.setX(-55);
		titleCell = outerTable.add(title).expand().padTop(40).padBottom(-100).padLeft(50).top().left();
		outerTable.row();
		outerTable.add(table).expandY().top();
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

	private void showCodingScreen(Supplier<Game> programSupplier) {
		try {
			showMainMenu();
			CodingScreen codingScreen = screenFactory.createCodingScreen(programSupplier.get());
			setScreen.accept(codingScreen);
		} catch (Exception e) {
			showError(e);
		}
	}

	private void showError(Throwable e) {
		new ErrorDialog(e, this::showMainMenu, skin).show(stage);
	}

	private void showMainMenu() {
		getTable().setVisible(true);
	}

	private void hideMainMenu() {
		getTable().setVisible(false);
	}
}

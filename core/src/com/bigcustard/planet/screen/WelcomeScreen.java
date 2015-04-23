package com.bigcustard.planet.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
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
import com.bigcustard.planet.code.Language;
import com.bigcustard.planet.plugin.Plugin;
import com.bigcustard.planet.plugin.jruby.JRubyPlugin;
import com.bigcustard.scene2dplus.actions.ChangePaddingAction;
import com.bigcustard.scene2dplus.dialog.ErrorDialog;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class WelcomeScreen extends ScreenAdapter {
	private static Plugin PLUGIN = new JRubyPlugin();
	private final Skin skin;
	private Table table;
	private Table outerTable;
	private Stage stage;
	private TextButton newGameButton;
	private TextButton continueGameButton;
	private TextButton gameLibraryButton;
	private TextButton quitButton;
	private Consumer<Screen> setScreen;
	private Viewport viewport;
	private Label title;
	private Cell<Label> titleCell;

	public WelcomeScreen(Viewport viewport, Skin skin, Consumer<Screen> setScreen) {
		super();
		this.setScreen = setScreen;
		this.stage = new Stage(viewport);
		this.viewport = viewport;
		this.skin = skin;

		createTitle();
	    createNewGameButton();
		createContinueGameButton();
		createGameLibraryButton();
		createQuitButton();
		refreshButtonEnabledStatuses();
		layoutScreen(createBackground());
	}

	public void showWelcomeScreen() {
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
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Math.min(delta, 1 / 60f));
		stage.draw();
	}

	private void refreshButtonEnabledStatuses() {
		boolean continueEnabled = Game.hasMostRecent();
		continueGameButton.setDisabled(!continueEnabled);
		continueGameButton.setTouchable(continueEnabled ? Touchable.enabled : Touchable.disabled);

		boolean libraryEnabled = Game.allGameFolders().length > 0;
		gameLibraryButton.setDisabled(!libraryEnabled);
		gameLibraryButton.setTouchable(libraryEnabled ? Touchable.enabled : Touchable.disabled);
	}

	private void createTitle() {
		title = new Label("Welcome to Planet Burpl", skin);
	}

	private void createNewGameButton() {
		newGameButton = new TextButton("    New Game    ", skin, "big");
		newGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showCodingScreen(() -> Game.create(Language.Groovy));
			}
		});
	}

	private void createContinueGameButton() {
		continueGameButton = new TextButton("  Continue Game  ", skin, "big");
		continueGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showCodingScreen(Game::mostRecent);
			}
		});
	}

	private void createGameLibraryButton() {
		gameLibraryButton = new TextButton("   Game Library   ", skin, "big");
		gameLibraryButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hideButtons();
				final GameLibraryDialog dialog = new GameLibraryDialog(skin);
				dialog.show(stage);
				Futures.addCallback(dialog.getFutureGame(), new FutureCallback<FileHandle>() {
					@Override
					public void onSuccess(FileHandle gameFolder) {
						showMainMenu();
						refreshButtonEnabledStatuses();
						if (gameFolder != null) {
							dialog.remove();
							showCodingScreen(() -> Game.from(gameFolder));
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
				System.exit(0);
			}
		});
	}

	private void animateTitle() {
        outerTable.getColor().a = 0f;
		outerTable.addAction(Actions.fadeIn(1.3f));
		titleCell.padLeft(-400);
		outerTable.addAction(new ChangePaddingAction(this.titleCell, 50, 1, Interpolation.pow2));
	}

	private void layoutScreen(TextureRegionDrawable backgroundRegion) {
        outerTable = new Table();
		table = new Table();
		table.row();
		table.add(newGameButton).colspan(2).fillX();
		table.row();
		table.add(continueGameButton).padTop(20f).colspan(2).fillX();
		table.row();
		table.add(gameLibraryButton).padTop(20f).colspan(2).fillX();
		table.row();
		table.add(quitButton).padTop(20f).colspan(2).fillX();
		outerTable.background(backgroundRegion);
		title.setX(-55);
		titleCell = outerTable.add(title).expand().padTop(40).padLeft(50).top().left();
		outerTable.row();
		outerTable.add(table).expandY().top();
		outerTable.setFillParent(true);
		outerTable.pack();
		stage.addActor(outerTable);
	}

	private TextureRegionDrawable createBackground() {
		Texture backgroundTexture = new Texture(Gdx.files.internal("images/WelcomeBackground.png"));
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        return new TextureRegionDrawable(new TextureRegion(backgroundTexture));
	}

	private void showCodingScreen(Supplier<Game> programSupplier) {
		try {
			CodingScreen codingScreen = new CodingScreen(
					programSupplier.get(),
					viewport,
					skin,
					this::showWelcomeScreen,
					setScreen,
					PLUGIN);
			setScreen.accept(codingScreen);
		} catch (Exception e) {
			showError(e);
		}
	}

	private void showError(Throwable e) {
		new ErrorDialog(skin, e, this::showMainMenu).show(stage);
	}

	private void showMainMenu() {
		getTable().setVisible(true);
	}

	private void hideButtons() {
		getTable().setVisible(false);
	}
}

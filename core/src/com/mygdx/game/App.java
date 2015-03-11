package com.mygdx.game;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.code.Program;
import com.mygdx.game.screens.CodingScreen;
import com.mygdx.game.screens.WelcomeScreen;

public class App extends Game {
	private Viewport viewport;
	private ResourceManager resourceManager;
    public static TweenManager tweenManager;

    @Override
	public void create () {
        tweenManager = new TweenManager();
        Tween.registerAccessor(Cell.class, new CellAccessor());

        viewport = new ScreenViewport();
		resourceManager = new ResourceManager();

		WelcomeScreen welcomeScreen = new WelcomeScreen(viewport, resourceManager);
		welcomeScreen.getNewGameButton().addListener(new ClickListener() {			
			@Override
			public void clicked(InputEvent event, float x, float y) {
                final Program program = new Program();
				setScreen(new CodingScreen(program, viewport, resourceManager));
			}
		});
		
		setScreen(welcomeScreen);
	}
	
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height, true);
        getScreen().resize(width, height);
    }
}

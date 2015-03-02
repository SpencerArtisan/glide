package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.code.Program;
import com.mygdx.game.image.GameImage;
import com.mygdx.game.screens.CodingScreen;
import com.mygdx.game.screens.WelcomeScreen;

import java.net.URL;

public class App extends Game {
	private Viewport viewport;
	private ResourceManager resourceManager;
	
	@Override
	public void create () {
        viewport = new ScreenViewport();
		resourceManager = new ResourceManager();
		final Program program = new Program();
		
		WelcomeScreen welcomeScreen = new WelcomeScreen(viewport, resourceManager);
		welcomeScreen.getNewGameButton().addListener(new ClickListener() {			
			@Override
			public void clicked(InputEvent event, float x, float y) {
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

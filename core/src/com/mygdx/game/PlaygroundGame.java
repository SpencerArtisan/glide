package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PlaygroundGame extends Game {
	private Viewport viewport;
	
	@Override
	public void create () {
		viewport = new FitViewport(1150, 750);

		WelcomeScreen welcomeScreen = new WelcomeScreen(viewport);

		welcomeScreen.getNewGameButton().addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				setScreen(new CodingScreen(viewport));
			}
		});
		
		setScreen(welcomeScreen);
	}
}

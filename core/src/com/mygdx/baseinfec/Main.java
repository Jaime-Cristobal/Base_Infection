package com.mygdx.baseinfec;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.baseinfec.animator.AnimatePOD;

public class Main extends Game
{
	public SpriteBatch batch;
	public Asset assetmanager;

	public Skin skin;

	public void create()

	{
		System.out.println("Game is starting....");

		batch = new SpriteBatch();

		assetmanager = new Asset();

		skin = assetmanager.manager.get("skin/flat-earth-ui.json");

		AnimatePOD.create();

		this.setScreen(new StartMenu(this));
	}

	public void render()
	{
		super.render();
	}

	public void dispose()
	{
		batch.dispose();
		assetmanager.Dispose();
		skin.dispose();

		if(this.getScreen() != null)
			this.getScreen().dispose();

		System.out.println("Game is fully disposed....");
	}
}

package com.mygdx.baseinfec;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by FlapJack on 7/29/2017.
 */

public class Asset
{
    public AssetManager manager;

    boolean isExternalAval = Gdx.files.isExternalStorageAvailable();
    boolean isLocalAval = Gdx.files.isLocalStorageAvailable();
    private FileHandle handler;

    public Asset()
    {
        manager = new AssetManager();

        handler = Gdx.files.classpath("assets/");

        Loadfiles();
    }

    public Object getFile(String file)
    {
        return manager.get(file);
    }

    private void Loadfiles()
    {
        manager.load("generic1.png", Texture.class);
        manager.load("map2.png", Texture.class);
        manager.load("map3.png", Texture.class);
        manager.load("player2.png", Texture.class);
        manager.load("player3.png", Texture.class);
        manager.load("box.png", Texture.class);
        manager.load("fort.png", Texture.class);
        manager.load("base.png", Texture.class);
        manager.load("map1.png", Texture.class);
        manager.load("player1.png", Texture.class);

        manager.load("player.atlas", TextureAtlas.class);
        manager.load("fx1.atlas", TextureAtlas.class);
        manager.load("player4.atlas", TextureAtlas.class);

        manager.load("skin/flat-earth-ui.json", Skin.class);

        manager.finishLoading();
    }

    public void Dispose()
    {
        manager.dispose();
    }
}
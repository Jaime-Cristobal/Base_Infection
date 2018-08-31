package com.mygdx.baseinfec;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class StartMenu implements Screen
{
    final private Main main;

    private ExtendViewport viewport;
    private Stage stage;
    private Table menuTable;
    private Array<TextButton> buttons;


    public StartMenu(Main main)
    {
        this.main = main;

        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        menuTable = new Table();
        buttons = new Array<TextButton>();

        Gdx.input.setInputProcessor(stage);
    }

    /** Called when this screen becomes the current screen for a {@link Game}. */
    public void show ()
    {
        main.assetmanager.manager.update();

        menuTable.setPosition(0, -180);

        buttons.add(new TextButton("Play", main.skin));
        buttons.add(new TextButton("Exit", main.skin));
        for(TextButton iter : buttons)
        {
            iter.setTransform(true);
        }

        menuTable.setFillParent(true);
        stage.addActor(menuTable);
        menuTable.add(buttons.get(0)).fillX().uniformX().center().right().pad(0, 20, 0, 20);
        menuTable.add(buttons.get(1)).fillX().uniformX().center().pad(0, 20, 0, 20);

        buttonFunc();
    }

    private void buttonFunc()
    {
        buttons.get(0).addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                main.setScreen(new MapStage(main));
                dispose();
            }
        });

        buttons.get(1).addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Gdx.app.exit();
            }
        });
    }

    /** Called when the screen should render itself.
     * @param delta The time in seconds since the last render. */
    public void render (float delta)
    {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        main.batch.setProjectionMatrix(stage.getCamera().combined);

        main.batch.begin();
        main.batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /** @see ApplicationListener#resize(int, int) */
    public void resize (int width, int height)
    {

    }

    /** @see ApplicationListener#pause() */
    public void pause ()
    {

    }

    /** @see ApplicationListener#resume() */
    public void resume ()
    {

    }

    /** Called when this screen is no longer the current screen for a {@link Game}. */
    public void hide ()
    {

    }

    /** Called when this screen should release all resources. */
    public void dispose ()
    {
        stage.dispose();

        Gdx.app.log("StartMenu.java", "disposed successfully");
    }
}

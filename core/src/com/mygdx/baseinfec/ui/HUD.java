package com.mygdx.baseinfec.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.baseinfec.Main;

/**
 * Created by FlapJack on 8/5/2017.
 */

public class HUD implements GestureListener
{
    private Score distance;

    final private Stage stage;
    private Table scoretable;
    private Table menutable;
    private Table weapons;
    private Label score;
    final public CheckBox shoot;
    final public CheckBox menu;

    private Label amount;   //amount of trash
    private int coinAmount, scoreTotal;

    final public GestureDetector detector;

    public HUD(final Main main, Stage stage_main)
    {
        this.stage = stage_main;

        distance = new Score();

        main.assetmanager.manager.update();

        scoretable = new Table();
        scoretable.setY(280f);

        menutable = new Table();
        menutable.setX(185);
        menutable.setY(280f);

        weapons = new Table();
        weapons.setX(275f);
        weapons.setY(-210f);

        score = new Label("Score: 0", main.skin);
        shoot = new CheckBox(null, main.skin);
        menu = new CheckBox(null, main.skin);

        amount = new Label("Coin: 0", main.skin);
        coinAmount = 0;
        scoreTotal = 0;

        detector = new GestureDetector(this);
    }

    public void Create()
    {
        distance.create();
        scoretable.setTransform(true);
        menutable.setTransform(true);
        weapons.setTransform(true);

        //Creates the buttons
        scoretable.setFillParent(true);
        menutable.setFillParent(true);
        weapons.setFillParent(true);
        //table.setDebug(true);
        stage.addActor(scoretable);
        stage.addActor(menutable);
        stage.addActor(weapons);

        scoretable.add(score).fillX().uniformX().center().right().pad(0, 20, 0, 20);
        scoretable.add(amount).fillX().uniformX();

        menutable.add(menu).fillX().uniformX();

        weapons.add(shoot).fillX().uniformX();
    }

    public void Render()
    {
        distance.Run();

        score.setText("Score: " + scoreTotal);
        amount.setText("Coin: " + coinAmount);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    public int GetScore()
    {
        return distance.GetScore();
    }

    public int getCoin()
    {
        return coinAmount;
    }

    public void addTrash(int val)
    {
        coinAmount += val;
    }

    public void addCoin(int val)
    {
        coinAmount += val;
    }

    public void addTotal(int val)
    {
        if(val >= 0)
            scoreTotal += val;
        else
            System.err.println("Value for function addTotal in HUD.java must be a positive value");
    }

    public void removeTotal(int val)
    {
        if(scoreTotal - val < 0)
        {
            scoreTotal = 0;
        }
        else
        {
            scoreTotal -= val;
        }
    }

    public void removeCoin(int val)
    {
        coinAmount -= val;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button)
    {
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        System.out.println("tap");
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {

        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {

        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {

        return false;
    }

    @Override
    public boolean zoom (float originalDistance, float currentDistance){

        return false;
    }

    @Override
    public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer){

        return false;
    }
    @Override
    public void pinchStop () {
    }
}

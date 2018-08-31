package com.mygdx.baseinfec.ui;

import com.badlogic.gdx.Gdx;

/**
 * Created by FlapJack on 7/22/2017.
 */

public class Score {
    private int score;
    private int temperature;

    public void create()
    {
        score = 0;
        temperature = 0;
    }

    public void Run()
    {
        score += 100 * Gdx.graphics.getDeltaTime();
    }

    public void setGold(int amount)
    {
        temperature += amount * Gdx.graphics.getDeltaTime();
    }

    public int GetScore()
    {
        return score;
    }

    public int getGold()
    {
        return temperature;
    }
}

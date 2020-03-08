package com.example.levelup;

public class Character {
    protected int level = 9;
    protected int exp;
    protected int atk;
    protected int energy;

    protected void levelUp(){
        level++;
    }

    protected int getLevel(){
        return level;
    }
}

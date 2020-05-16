package com.djothi.dotsboxes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ConstrainGame extends AppCompatActivity implements GameFragment.GameListener{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

    }


    @Override
    public void GameClicked(int[] score, int turn) {
        System.out.println("turn :"+turn);
    }
}

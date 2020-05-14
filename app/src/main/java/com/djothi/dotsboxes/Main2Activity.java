package com.djothi.dotsboxes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Main2Activity extends AppCompatActivity implements GameFragment2.GameListener{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    }


    @Override
    public void GameClicked(int score, int turn) {
        System.out.println(score);
    }
}

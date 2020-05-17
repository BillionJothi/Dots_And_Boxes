package com.djothi.dotsboxes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainGame extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_game);
             GameFragment fragmentgame;
            if (findViewById(R.id.container_a) != null) {
                //fragmentgame = new GameFragment();
              //  TopGameFragment topGameFragment = new TopGameFragment();
               // getSupportFragmentManager().beginTransaction()
                       // .add(R.id.container_a, fragmentgame)
                        //.add(R.id.container_b, topGameFragment)
                     //   .commit();
            }
        }
}

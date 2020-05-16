package com.djothi.dotsboxes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Main2Activity2 extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);
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

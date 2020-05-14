package com.djothi.dotsboxes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TableRow;

public class gameAct extends AppCompatActivity {
    View view;
    View game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        game = findViewById(R.id.gamefrag);
        Resources r = getResources();

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(game.getWidth(),
                game.getWidth());
       // game.setLayoutParams(layoutParams);


    }
}

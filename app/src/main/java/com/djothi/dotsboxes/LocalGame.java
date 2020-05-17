package com.djothi.dotsboxes;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LocalGame extends AppCompatActivity implements GameOptionsFragment.GameOptionsListerner{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_game);

        //Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        //Setting up toolbar
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(R.string.vs_computer);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void fragementStartButtonClicked(
            int grid, int human, int AI,
            boolean hint, boolean undo, boolean p1Starts, boolean quickMode, boolean randomTurns) {
        openGameActivity();
    }

    public void openGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}

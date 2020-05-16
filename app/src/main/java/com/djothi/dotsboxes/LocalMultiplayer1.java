package com.djothi.dotsboxes;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LocalMultiplayer1 extends AppCompatActivity implements GameOptionsFragment.GameOptionsListerner {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_multiplayer);

        //Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        //Setting up toolbar
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(R.string.local_multiplayer);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void fragementStartButtonClicked(boolean hints, boolean undos) {
        openGameActivity();
    }

    public void openGameActivity() {
        Intent intent = new Intent(this, ConstrainGame.class);
        startActivity(intent);
    }
}

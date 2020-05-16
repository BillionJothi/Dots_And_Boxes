package com.djothi.dotsboxes;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LocalGame1 extends AppCompatActivity implements GameOptionsFragment.GameOptionsListerner{

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
    public void fragementStartButtonClicked(boolean hints, boolean undos) {
        openGameActivity();
    }

    public void openGameActivity() {
        Intent intent = new Intent(this, Main2Activity2.class);
        startActivity(intent);
    }
}

package com.djothi.dotsboxes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Choreographer;

import java.util.Objects;

public class LocalGame1 extends AppCompatActivity implements GameOptionsFragment.GameOptionsListerner{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_game1);

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
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }
}

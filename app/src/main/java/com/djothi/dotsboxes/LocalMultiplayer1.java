package com.djothi.dotsboxes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class LocalMultiplayer1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_multiplayer1);

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
}

package com.djothi.dotsboxes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.util.Objects;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main_page);
    }

    public void VsComputer(View view){

    }

    public void LocalMultiplayer(View view){

    }

    public void Settings(View view){

    }
}

package com.djothi.dotsboxes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
    }


    //OnClick
    public void VsComputer(View view){
        openLocalGame();
    }
    //OnClick
    public void LocalMultiplayer(View view){  openLocalMultiplayer();  }
    //OnClick
    public void Settings(View view){ openSettings(); }



    //Opens Local GameFragment_static Activity
    public void openLocalGame() {
        Intent intent = new Intent(this, LocalGame1.class);
        startActivity(intent);
    }

    //Opens Local Multiplayer Activity
    public void openLocalMultiplayer() {
        Intent intent = new Intent(this, LocalMultiplayer1.class);
        startActivity(intent);
    }

    //Opens Settings Activity
    public void openSettings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}

package com.djothi.dotsboxes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.Window;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final int delay = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        delay();
    }

    //Flash logo before going to main screen
    public void delay() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                openMainActivity();
            }
        }, delay);
    }


    //Opens Main Menu Screen
    public void openMainActivity() {
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
    }
}

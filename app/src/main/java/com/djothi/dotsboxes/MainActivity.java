package com.djothi.dotsboxes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final int delay = 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        openMainActivity();
        return true;
    }

    //Opens Main Menu Screen
    public void openMainActivity() {
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
    }
}

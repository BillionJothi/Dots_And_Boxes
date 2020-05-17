package com.djothi.dotsboxes;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity implements FragmentBottomGame {
    int grid, human, AI;
    boolean hint, undo, p1Starts, quickMode, randomTurns;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_game);
            Intent intent = getIntent();
            grid = intent.getIntExtra("grid",3);
            human = intent.getIntExtra("human",1);
            AI = intent.getIntExtra("AI",1);
            hint = intent.getBooleanExtra("hint",true);
            undo = intent.getBooleanExtra("undo",true);
            p1Starts = intent.getBooleanExtra("p1Starts",true);
            quickMode = intent.getBooleanExtra("quickMode",false);
            randomTurns = intent.getBooleanExtra("randomTurns",true);

            MainGameFragment fragmentgame;
            FragmentTopGame fragmentTopGame;
            FragmentBottomGame fragmentBottomGame;
            if (findViewById(R.id.game_fragment) != null) {
                fragmentgame =
                    (MainGameFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment);

                fragmentgame.setBoardSize(grid);
                fragmentgame.setLocalPlayers(human);
                fragmentgame.setNoPcPlaying(AI);
                fragmentgame.setPcPlaying(AI==0);
                fragmentgame.setPlayersTurn(p1Starts);
                if(quickMode){fragmentgame.setDelay(0);}
                else {fragmentgame.setDelay(500);}
                fragmentgame.setRandomTurns(randomTurns);
            }

            if (findViewById(R.id.top_game_fragment) != null) {
                fragmentTopGame =
                        (FragmentTopGame) getSupportFragmentManager().findFragmentById(R.id.top_game_fragment);
                //TODO get turn everymove, get actual turns for display, get score for update
            }

            if (findViewById(R.id.bottom_game_fragment) != null) {
                fragmentBottomGame =
                        (FragmentBottomGame) getSupportFragmentManager().findFragmentById(R.id.bottom_game_fragment);

                assert fragmentBottomGame != null;
                fragmentBottomGame.setHintActive(hint);
                fragmentBottomGame.setUndoActive(undo);
            }
        }


    }

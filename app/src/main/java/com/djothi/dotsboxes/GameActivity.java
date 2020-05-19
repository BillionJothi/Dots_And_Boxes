package com.djothi.dotsboxes;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity
        implements FragmentBottomGame.FragmentBottomGameListener,
            MainGameFragment.GameListener {
    int grid, human, AI;
    boolean hint, undo, p1Starts, quickMode, randomTurns;
    MainGameFragment fragmentgame;
    FragmentTopGame fragmentTopGame;
    FragmentBottomGame fragmentBottomGame;

    @Override
    protected void onStart() { super.onStart(); }
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

        Bundle bundle = new Bundle();
        bundle.putInt("grid",grid);
        bundle.putInt("human",human);
        bundle.putInt("AI",AI);
        bundle.putBoolean("hint",hint);
        bundle.putBoolean("undo",undo);
        bundle.putBoolean("p1Starts",p1Starts);
        bundle.putBoolean("quickMode",quickMode);
        bundle.putBoolean("randomTurns",randomTurns);
        MainGameFragment game = new MainGameFragment();

        if (findViewById(R.id.game_fragment) != null) {
            fragmentgame =
                    (MainGameFragment) getSupportFragmentManager().findFragmentById(R.id.game_fragment);
            //Send Data
            fragmentgame.setArguments(bundle);
                assert fragmentgame != null;
                fragmentgame.setBoardSize(grid);
                //  Toast.makeText(this, "grid", Toast.LENGTH_SHORT).show();
                fragmentgame.setNoLocalPlayers(human);
                fragmentgame.setNoPcPlaying(AI);
                fragmentgame.setPcPlaying(AI == 0);
                fragmentgame.setPlayersTurn(p1Starts);
                if (quickMode) {
                    fragmentgame.setDelay(0);
                } else {
                    fragmentgame.setDelay(5000);
                }
                fragmentgame.setRandomTurnsOn(randomTurns);
            }
        System.out.println("**************GameAct shared**************");
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
                fragmentBottomGame.setScore(0);

            }
        }



    @Override
    public void updatePlayerScore(int score) { fragmentBottomGame.setScore(score); }
    @Override
    public void updatePlayerName(String s) { fragmentBottomGame.newPlayerName(s); }

    @Override
    public void updateAIName(String s) {
        fragmentTopGame.newAIName(s);
    }

    @Override
    public void updateAIScore(int score) {
        fragmentTopGame.newAIScore(score);
    }

    @Override
    public void updateOverallScore(String s) {
        fragmentTopGame.setAllScore(s);
    }

    @Override
    public void disableButtons() { fragmentBottomGame.endGameDisableButtons(); }
    @Override
    public void updateTurn(String turn) { fragmentTopGame.setTurn(turn); }
    //@Override
    public void setInitialTurn(String turn) { fragmentTopGame.setInitialTurn(turn); }
    @Override
    public void undo() { fragmentgame.undo(); }
    @Override
    public void hint() {
        fragmentgame.hint();
    }
}

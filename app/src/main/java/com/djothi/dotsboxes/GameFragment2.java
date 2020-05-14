package com.djothi.dotsboxes;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Random;


public class GameFragment2 extends Fragment {

    int boardSize;
    boolean playersTurn;
    int turn;
    int players = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_game2, container, false);
        LinearLayout layout = view.findViewById(R.id.linear2);
        layout.removeAllViews();

        /*Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment4);
        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        fragTransaction.detach(currentFragment);*/

        //Get Settings from resouruces
        boardSize = getResources().getInteger(R.integer.boardSize);
        playersTurn = getResources().getBoolean(R.bool.playerStarts1st);
        turn = getResources().getInteger(R.integer.turnStart);

        //Make GameBoard
        CreateGameBoard.MakeBoard(view, layout, boardSize);

        /*fragTransaction.attach(currentFragment);
        fragTransaction.commit();*/

        // Inflate the layout for this fragment
        return view;
    }

    private void nextTurn(){
        if(turn == players-1){
            this.turn = 0;
        }else{
            this.turn++;
        }
    }

    public void hLineClicked(View view, ImageViewAdded img){
        if(playersTurn){
            img.setSet(true);
            img.setImageResource(R.drawable.bluehorizontal);
            nextTurn();
        }
    }

    public void vLineClicked(View view, ImageViewAdded img){
        if(playersTurn){
            img.setImageResource(R.drawable.bluevertical);
        }
    }

    public void computersTurn(View view){
        setPlayersTurn(false);
        Random random = new Random();
        int totallines = 24;
        int playablelines = 0;
        for(int i=0; i<totallines; i++){
            //if(view.findViewWithTag());
        }


    }



    public void setPlayersTurn(boolean playersTurn) {
        this.playersTurn = playersTurn;
    }

}

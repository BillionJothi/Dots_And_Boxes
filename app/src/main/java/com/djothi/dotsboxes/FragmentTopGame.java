package com.djothi.dotsboxes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentTopGame extends Fragment {

    private TextView turn;
    private TextView allScore;

    public FragmentTopGame() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_top, container, false);

        turn = (TextView) v.findViewById(R.id.turn);
        allScore = (TextView) v.findViewById(R.id.score_all);

        return v;
    }

    public void setInitialTurn(String newturn){
        turn.setText(newturn);
    }

    public void setTurn(String s){
       /* String s = turn.getText().toString().replaceFirst("[0-9]+",
                Integer.toString(newturn));
        System.out.println(s);*/
        turn.setText(s);
    }

    public void setAllScore(int newscore){
        String s = allScore.getText().toString().replaceFirst("[0-9]+",
                Integer.toString(newscore));
        allScore.setText(s);
    }

}

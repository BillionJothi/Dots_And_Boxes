package com.djothi.dotsboxes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentBottomGame extends Fragment {

    private TextView score;
    private Button undo;
    private Button hint;

    public FragmentBottomGame() {
    }

     FragmentBottomGameListener activityCommander;

    public void onStart() {
        super.onStart();
        // rest of the code
    }
    public interface FragmentBottomGameListener{
        public void undo();
        public void hint();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            activityCommander = (FragmentBottomGameListener) getActivity();
        }catch (ClassCastException e ){
            throw new ClassCastException(requireActivity().toString()
            + "Must implement FragmentBottomGameListerner");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_bottom, container, false);

        Button undo = (Button) v.findViewById(R.id.undobutton);
        Button hint = (Button) v.findViewById(R.id.hintbutton);
        score = (TextView) v.findViewById(R.id.score_player);

        undo.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                undoClicked(v);
            }
        });

        hint.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                hintClicked(v);
            }
        });

        return v;
    }

    public void undoClicked(View view){
        activityCommander.undo();
    }

    public void hintClicked(View view){
        activityCommander.hint();
    }

    public void setHintActive(boolean active){
        hint.setEnabled(active);
    }

    public void setUndoActive(boolean active){
        undo.setEnabled(active);
    }

    public void setScore(int newscore){
        String s = score.getText().toString().replaceFirst("[0-9]+",
                Integer.toString(newscore));
        score.setText(s);
    }

}

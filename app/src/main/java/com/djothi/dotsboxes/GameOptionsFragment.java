package com.djothi.dotsboxes;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;


public class GameOptionsFragment extends Fragment {
    private Switch hints;
    private Switch undo;

    GameOptionsListerner activityCommander;

    public interface GameOptionsListerner{
        public void fragementStartButtonClicked(boolean hints, boolean undos);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            activityCommander = (GameOptionsListerner) getActivity();
        }catch (ClassCastException e ){
            throw new ClassCastException(getActivity().toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_options, container,false);

        hints = (Switch) view.findViewById(R.id.hintSwitch);
        undo = (Switch) view.findViewById(R.id.undoSwitch);
        Button start = (Button) view.findViewById(R.id.goButton);

        start.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startClick(v);
                    }
                }
        );

        // Inflate the layout for this fragment
        return view;
    }


    public void startClick(View view){
        activityCommander.fragementStartButtonClicked(hints.isChecked(),undo.isChecked());
    }
}

package com.djothi.dotsboxes;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


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




        View v = view;

        NumberPicker np = (NumberPicker) v.findViewById(R.id.np);

        //Set TextView text color
        final TextView tv = (TextView) v.findViewById(R.id.tv);
        System.out.println(tv);
        tv.setTextColor(Color.parseColor("#FF2C834F"));

        //Initializing a new string array with elements
        final String[] values= {"English","Hindi", "Suomi"};

        //Populate NumberPicker values from String array values
        //Set the minimum value of NumberPicker
        np.setMinValue(0); //from array first value
        //Specify the maximum value/number of NumberPicker
        np.setMaxValue(values.length-1); //to array last value

        //Specify the NumberPicker data source as array elements
        np.setDisplayedValues(values);

        //Gets whether the selector wheel wraps when reaching the min/max value.
        np.setWrapSelectorWheel(true);

        //Set a value change listener for NumberPicker
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected value from picker
                tv.setText("Selected value : " + values[newVal]);
            }
        });









        // Inflate the layout for this fragment
        return view;
    }


    public void startClick(View view){
        activityCommander.fragementStartButtonClicked(hints.isChecked(),undo.isChecked());
    }
}

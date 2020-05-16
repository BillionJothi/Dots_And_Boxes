package com.djothi.dotsboxes;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


public class GameOptionsFragment extends Fragment {
    private Switch hints;
    private Switch undo;
    private TextView textView7;

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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_options, container,false);

        /*textView7 = (TextView) view.findViewById(R.id.setNoAIPlayers);
        hints = (Switch) view.findViewById(R.id.hintSwitch);
        undo = (Switch) view.findViewById(R.id.undoSwitch);
        textView7.setText(hints.getText().toString());
        //textView7.setTextClassifier(hints.getTextClassifier());
        //textView7.setTypeface(hints.getTypeface());
        //textView7.setTextSize(hints.getTextSize());
        hints.setWidth(0);
        hints.setHeight(0);
        //hints.setVisibility(View.INVISIBLE);
        hints.setActivated(false);
        undo.getTrackDrawable();
        undo.getThumbDrawable().setVisible(false,false);
        undo.getTrackDrawable().setVisible(false,false);
        //undo.setActivated(true);
        //undo.setEnabled(false);
        //undo.setThumbDrawable(getResources().getDrawable());*/


        Button start = (Button) view.findViewById(R.id.startGameButton);
        start.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startClick(v);
                    }
                }
        );

        View v = view;

        /*NumberPicker numberPicker = (NumberPicker) v.findViewById(R.id.number_picker);
        //NumberPicker numberPicker2 = (NumberPicker) v.findViewById(R.id.number_picker2);
        // Set divider color
        numberPicker.setDividerColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
        numberPicker.setDividerColorResource(R.color.colorPrimary);
        // Set selected text color
        numberPicker.setSelectedTextColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
        numberPicker.setSelectedTextColorResource(R.color.colorPrimary);
        // Set selected text size

        // Set value
        numberPicker.setMaxValue(59);
        numberPicker.setMinValue(0);
        numberPicker.setValue(3);

        // Using string values
        // IMPORTANT! setMinValue to 1 and call setDisplayedValues after setMinValue and setMaxValue
        String[] data = {"2", "3", "4", "5", "6", "7", "8", "9", "10"};
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(data.length);
        numberPicker.setDisplayedValues(data);
        numberPicker.setValue(7);

        // Set fading edge enabled
        numberPicker.setFadingEdgeEnabled(true);
        // Set scroller enabled
        numberPicker.setScrollerEnabled(true);
        //numberPicker2.setScrollerEnabled(true);
        // Set wrap selector wheel
        numberPicker.setWrapSelectorWheel(true);
        // Set accessibility description enabled
        numberPicker.setAccessibilityDescriptionEnabled(true);
        // OnClickListener
        numberPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "Click on current value");
            }
        });
        // OnValueChangeListener
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // Log.d(TAG, String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
            }
        });
        // OnScrollListener
        numberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker picker, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    // Log.d(TAG, String.format(Locale.US, "newVal: %d", picker.getValue()));
                }
            }
        });*/

        // Inflate the layout for this fragment
        return view;
    }


    public void startClick(View view){
        activityCommander.fragementStartButtonClicked(hints.isChecked(),undo.isChecked());
    }
}

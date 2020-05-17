package com.djothi.dotsboxes;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.shawnlin.numberpicker.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


public class GameOptionsFragment extends Fragment {
    private TextView totalNoPlayersText;
    private TextView turnsPerPlayerText;
    private NumberPicker noAIPlayersNumberPicker;
    private NumberPicker noHumanPlayersNumberPicker;
    private NumberPicker gridSizeNumberPicker;
    private Switch isUndos;
    private Switch isP1Starts;
    private Switch isQuickMode;
    private Switch isRandomizeTurns;
    private Switch isHints;
    private int gridSize;
    private int noPlayersInt;
    private int turnsInt;
    private ColorStateList totalTextColor;
    private ColorStateList turnsTextColor;
    private boolean quickMode;
    private boolean suggestQuick;


    GameOptionsListerner activityCommander;

    public interface GameOptionsListerner{
        public void fragementStartButtonClicked(
                int grid, int human, int AI,
                boolean hint, boolean undo, boolean p1Starts, boolean quickMode,
                boolean randomTurns);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            activityCommander = (GameOptionsListerner) getActivity();
        }catch (ClassCastException e ){
            throw new ClassCastException(requireActivity().toString()
             + "Attached activity does not implement the right interface for GameOptions");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_options, container,false);

        gridSizeNumberPicker =
                (NumberPicker) view.findViewById(R.id.gridSizePickerInput);
       noHumanPlayersNumberPicker =
                (NumberPicker) view.findViewById(R.id.noHumanPlayersPickerInput);
        noAIPlayersNumberPicker =
                (NumberPicker) view.findViewById(R.id.noAIPlayersPickerInput);
        totalNoPlayersText = (TextView) view.findViewById(R.id.showTotalNoPlayersText);
        turnsPerPlayerText = (TextView) view.findViewById(R.id.showTrunsPerPlayerText);
        isHints = (Switch) view.findViewById(R.id.setHintAllowedSwitch);
        isUndos = (Switch) view.findViewById(R.id.setUndoAllowedSwitch);
        isP1Starts = (Switch) view.findViewById(R.id.setP1StartsSwitch);
        isQuickMode = (Switch) view.findViewById(R.id.setQuickModeSwitch);
        isRandomizeTurns = (Switch) view.findViewById(R.id.setRandomTurnsSwitch);
        isRandomizeTurns.setChecked(true);
        Button startGameButton = (Button) view.findViewById(R.id.startGameButton);
        this.gridSize = gridSizeNumberPicker.getValue();
        this.totalTextColor = totalNoPlayersText.getTextColors();
        this.turnsTextColor = turnsPerPlayerText.getTextColors();
        this.noPlayersInt =
                noAIPlayersNumberPicker.getValue() + noHumanPlayersNumberPicker.getValue();
        this.turnsInt = (int) ((gridSize*2*(gridSize+1)) / (double) noPlayersInt);
        this.quickMode = false;

        // OnValueChangeListener
        gridSizeNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                gridSize = newVal;
                int playable = newVal*2*(newVal+1);
                //Note: Rounds down in casting
                if(noPlayersInt != 0){
                    turnsInt = (int) ((playable) / (double) noPlayersInt);
                }
                updateTurnsPerPlayerText();
                noHumanPlayersNumberPicker.setMaxValue(playable);
                noHumanPlayersNumberPicker.setWrapSelectorWheel(true);
                noAIPlayersNumberPicker.setMaxValue(playable);
                noAIPlayersNumberPicker.setWrapSelectorWheel(true);
            }
        });

        noHumanPlayersNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                noPlayersInt = newVal + noAIPlayersNumberPicker.getValue();
                int playable = gridSize*2*(gridSize+1);
                //Note: Rounds down in casting
                if(noPlayersInt != 0){
                    turnsInt = (int) ((playable) / (double) noPlayersInt);
                }
                updateTotalPlayersText();
                updateTurnsPerPlayerText();
                noAIPlayersNumberPicker.setMaxValue(playable-newVal);
                noAIPlayersNumberPicker.setWrapSelectorWheel(true);
            }
        });

        noAIPlayersNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                noPlayersInt = newVal + noHumanPlayersNumberPicker.getValue();
                int playable = gridSize*2*(gridSize+1);
                //Note: Rounds down in casting
                if(noPlayersInt != 0){
                    turnsInt = (int) ((playable) / (double) noPlayersInt);
                }
                updateTotalPlayersText();
                updateTurnsPerPlayerText();
                noHumanPlayersNumberPicker.setMaxValue(playable-newVal);
                noHumanPlayersNumberPicker.setWrapSelectorWheel(true);
            }
        });

        isQuickMode.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                quickMode = isChecked;
                setTotalPlayerTextColor();
                if(!suggestQuick){
                    originalColor();
                }else if(isChecked){
                    originalColor();
                }else {
                    suggestTurnOnQuick();
                }
            }
        });

      /*  // Using string values
        // IMPORTANT! setMinValue to 1 and call setDisplayedValues after setMinValue and setMaxValue
        String[] data = {"2", "3", "4", "5", "6", "7", "8", "9", "10"};
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(data.length);
        numberPicker.setDisplayedValues(data);
        numberPicker.setValue(7);
        numberPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "Click on current value");
            }
        });
        // OnScrollListener
        gridSizeNumberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker picker, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                }
            }
        });
*/

        startGameButton.setOnClickListener(
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

    //Method updates the text
    private void updateTotalPlayersText(){
        String s = totalNoPlayersText.getText().toString().replaceFirst("[0-9]+",
                Integer.toString(this.noPlayersInt));
        totalNoPlayersText.setText(s);
        setTotalPlayerTextColor();
    }
    //Methods Checks if too little turns/player & gives visual warninig(
    private void setTotalPlayerTextColor(){
        if(this.noPlayersInt == 0){ }
        else if(this.noPlayersInt > 15 && !quickMode){
            totalNoPlayersText.setTextColor(Color.RED);
        } else if(((this.gridSize*2*(this.gridSize+1)) % this.noPlayersInt) != 0) {
            totalNoPlayersText.setTextColor(Color.YELLOW);
        } else {
            totalNoPlayersText.setTextColor(totalTextColor);
        };

        if(this.noPlayersInt > 7 && !quickMode) {
            suggestTurnOnQuick();
        }else{originalColor();}
    }
    private void originalColor(){
        isQuickMode.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP);
    }
    private void suggestTurnOnQuick(){
        suggestQuick = true;
        isQuickMode.getBackground().setColorFilter(Color.MAGENTA, PorterDuff.Mode.SRC_ATOP);
    }
    //Method updates the text
    private void updateTurnsPerPlayerText(){
        String s = turnsPerPlayerText.getText().toString().replaceFirst("[0-9]+",
                Integer.toString(this.turnsInt));
        turnsPerPlayerText.setText(s);
        setTurnsPerPlayerTextColor();
    }
    //Methods Checks if too little turns/player & gives visual warninig
    private void setTurnsPerPlayerTextColor(){
        if(this.turnsInt < 8){
            turnsPerPlayerText.setTextColor(Color.RED);
        }else {
            turnsPerPlayerText.setTextColor(turnsTextColor);
        };
    }

    public void startClick(View view){
        activityCommander.fragementStartButtonClicked(
                gridSizeNumberPicker.getValue(), noHumanPlayersNumberPicker.getValue(),
                noAIPlayersNumberPicker.getValue(), isHints.isChecked(), isUndos.isChecked(),
                isP1Starts.isChecked(), isQuickMode.isChecked(), isRandomizeTurns.isChecked());
    }
}

package com.djothi.dotsboxes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.shawnlin.numberpicker.NumberPicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LocalMultiplayer extends AppCompatActivity implements GameOptionsFragment.GameOptionsListerner {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_multiplayer);

        //Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        //Setting up toolbar
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(R.string.local_multiplayer);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        View v = findViewById(R.id.fragment_game_options);
        v.findViewById(R.id.setNoAIPlayersText).setVisibility(View.GONE);
        v.findViewById(R.id.noAIPlayersPickerInput).setVisibility(View.GONE);
        v.findViewById(R.id.setQuickModeSwitch).setVisibility(View.GONE);
        NumberPicker local = v.findViewById(R.id.noHumanPlayersPickerInput);
        local.setValue(2);
        Switch s = v.findViewById(R.id.setP1StartsSwitch);
        ViewGroup.LayoutParams tochangewidth = s.getLayoutParams();
        tochangewidth.width = (int) getResources().getDimension(R.dimen.switchWidth);
    }

    @Override
    public void fragementStartButtonClicked(
            int grid, int human, int AI,
            boolean hint, boolean undo, boolean p1Starts, boolean quickMode, boolean randomTurns) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("grid",grid);
        intent.putExtra("human",human);
        intent.putExtra("AI",0);
        intent.putExtra("hint",hint);
        intent.putExtra("undo",undo);
        intent.putExtra("p1Starts",p1Starts);
        intent.putExtra("quickMode",quickMode);
        intent.putExtra("randomTurns",randomTurns);
        startActivity(intent);
    }

    @Override
    public void longClicked() {

    }

}

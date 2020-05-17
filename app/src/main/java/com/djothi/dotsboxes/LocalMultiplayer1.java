package com.djothi.dotsboxes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LocalMultiplayer1 extends AppCompatActivity implements GameOptionsFragment.GameOptionsListerner {

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
        Switch s = v.findViewById(R.id.setP1StartsSwitch);
        ViewGroup.LayoutParams tochangewidth = s.getLayoutParams();
        tochangewidth.width = (int) getResources().getDimension(R.dimen.switchWidth);
        //v.findViewById(R.id.showTotalNoPlayersText).setTextAlignment(View
        // .TEXT_ALIGNMENT_TEXT_START);
        //v.findViewById(R.id.showTrunsPerPlayerText).setTextAlignment(View
        // .TEXT_ALIGNMENT_TEXT_END);

        //v.findViewById(R.id.setHintAllowedSwitch).getLayoutParams().
    }

    @Override
    public void fragementStartButtonClicked(
            int grid, int human, int AI,
            boolean hint, boolean undo, boolean p1Starts, boolean quickMode, boolean randomTurns) {
        openGameActivity();
    }


    public void openGameActivity() {
        Intent intent = new Intent(this, ConstrainGame.class);
        startActivity(intent);
    }


}

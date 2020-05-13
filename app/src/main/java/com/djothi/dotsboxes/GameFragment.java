package com.djothi.dotsboxes;

import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableRow;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {

    ImageView dot0;
    ImageView line0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        float dip = 50f;
        Resources r = getResources();
        int px = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
                r.getDisplayMetrics()));
        View view = inflater.inflate(R.layout.fragment_game, container,false);

        LinearLayout layout = (LinearLayout)view.findViewById(R.id.layout);
        //boolean success = formIsValid(layout);

        //withd & height
        TableRow.LayoutParams dotLayout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                px);
        TableRow.LayoutParams linearLineLayout = new TableRow.LayoutParams( px,
                (int)px);

        /*dot0 = view.findViewById(R.id.dot0);
        dot0.setLayoutParams(dotLayout);
        dot0.requestLayout();
        line0 = view.findViewById(R.id.line0);
        line0.setLayoutParams(linearLineLayout);
        line0.requestLayout();*/

        // Inflate the layout for this fragment
        return view;
    }


    public boolean formIsValid(LinearLayout layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v instanceof ImageView) {
                v.getForegroundGravity();
                v.setForegroundGravity(Gravity.FILL);
                //validate your EditText here
            } else if (v instanceof RadioButton) {
                //validate RadioButton
            } //etc. If it fails anywhere, just return false.
        }
        return true;
    }
}

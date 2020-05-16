package com.djothi.dotsboxes;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;


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

        /*float dip = 50f;
        Resources r = getResources();

        int px = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
                r.getDisplayMetrics()));*/
        View view = inflater.inflate(R.layout.fragment_game, container,false);


        set(view,3);

        //LinearLayout layout = (LinearLayout)view.findViewById(R.id.layout);
        //boolean success = formIsValid(layout);

        //withd & height
      /*  TableRow.LayoutParams dotLayout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                px);
        TableRow.LayoutParams linearLineLayout = new TableRow.LayoutParams( px,
                (int)px);*/

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

    public void set(View view, int x){
        int actual = (x*2)+1;
        int dots = (x+1)^2;
        int totallines = (x*2*(x+1));
        int lines = totallines/2;
        int boxes = x^2;

        float dotLinearLayoutWeight = (float) 1.25;
        float linearLayoutWeight = (float) 1.0;
        float dotWeight = (float) 1.05;
        float verticalLineWeight = (float) 0.86;
        float horizontalLineWeight = (float) 1.0;
        float boxesWeight = (float) 0.86;

        LinearLayout[] linearLayouts = new LinearLayout[actual];
        ImageView[] imageViews_dot = new ImageView[dots];
        ImageView[] imageViews_verticalLines = new ImageView[lines];
        ImageView[] imageViews_horziontalLines = new ImageView[lines];
        ImageView[] imageViews_boxes = new ImageView[boxes];

        //Layout for linearlayout
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
        //Layout for assets
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(layoutParams);

        //Dots
        layoutParams2.weight = dotWeight;
        layoutParams2.gravity = Gravity.CENTER;
        for(ImageView i : imageViews_dot ){
            i = new ImageView(view.getContext());
            i.setLayoutParams(layoutParams2);
            i.setScaleType(ImageView.ScaleType.FIT_CENTER);
            i.setImageResource(R.drawable.dotDrawable);
        }

        //HorizontaLines
        layoutParams2.weight = horizontalLineWeight;
        layoutParams2.gravity = Gravity.CENTER;
        for(ImageView i : imageViews_horziontalLines ){
            i = new ImageView(view.getContext());
            i.setLayoutParams(layoutParams2);
            i.setScaleType(ImageView.ScaleType.FIT_CENTER);
            i.setImageResource(R.drawable.blankHorizontalDrawable);
        }

        //Vertical Lines
        layoutParams2.weight = verticalLineWeight;
        layoutParams2.gravity = Gravity.CENTER;
        for(ImageView i : imageViews_verticalLines ){
            i = new ImageView(view.getContext());
            i.setLayoutParams(layoutParams2);
            i.setScaleType(ImageView.ScaleType.FIT_CENTER);
            i.setImageResource(R.drawable.blankVerticalDrawable);
        }

        //boxes
        layoutParams2.weight = boxesWeight;
        layoutParams2.gravity = Gravity.CENTER;
        for(ImageView i : imageViews_verticalLines ){
            i = new ImageView(view.getContext());
            i.setLayoutParams(layoutParams2);
            i.setScaleType(ImageView.ScaleType.FIT_XY);
            i.setImageResource(R.drawable.blankBoxDrawable);
        }

        //Layout
        for(int i=0; i<actual; i++){
            linearLayouts[i] = new LinearLayout(view.getContext());
            linearLayouts[i].setOrientation(LinearLayout.HORIZONTAL);
            if(i%2 == 0){
                layoutParams.weight = dotLinearLayoutWeight;
//                linearLayouts[i].addView(imageViews_boxes[i]);
            }else{
                layoutParams.weight = linearLayoutWeight;
            }
            linearLayouts[i].setLayoutParams(layoutParams);
        }


    }
}

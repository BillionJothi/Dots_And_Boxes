package com.djothi.dotsboxes;

import android.content.Context;
import android.content.res.Resources;
import android.text.Layout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CreateGameBoard {

    private static float getFloatResourcesValues(View view, int resources){
        TypedValue outValue = new TypedValue();
        view.getResources().getValue(resources, outValue, true);
        return (outValue.getFloat());
    }

    public static View MakeBoard(View view, LinearLayout layout, int boardSize) {
        final int gridSize = (boardSize*2)+1;
        final int dots = (int) Math.pow((boardSize+1),2);
        final int totallines = (boardSize*2*(boardSize+1));
        final int lines = totallines/2;
        final int boxes = (int) Math.pow(boardSize,2);
        //No.of LayoutParems is dependent on the number of object types.
        final int noLayoutParems = 6;

        //get Resouces Values for each object
        final float dotLinearLayoutWeight = getFloatResourcesValues(view, R.dimen.dotLinearLayoutWeight);
        final float linearLayoutWeight =  getFloatResourcesValues(view, R.dimen.linearLayoutWeight);
        final float dotWeight =  getFloatResourcesValues(view, R.dimen.dotWeight);
        final float verticalLineWeight =  getFloatResourcesValues(view, R.dimen.verticalLineWeight);
        final float horizontalLineWeight = getFloatResourcesValues(view, R.dimen.horizontalLineWeight);
        final float boxesWeight =  getFloatResourcesValues(view, R.dimen.boxesWeight);

        //Create array for each object
        LinearLayout[] linearLayouts = new LinearLayout[gridSize];
        ImageView[] imageViews_dot = new ImageView[dots];
        ImageViewAdded[] imageViews_verticalLines = new ImageViewAdded[lines];
        ImageViewAdded[] imageViews_horziontalLines = new ImageViewAdded[lines];
        ImageViewAdded[] imageViews_boxes = new ImageViewAdded[boxes];

        //CreateLayout for each object
        LinearLayout.LayoutParams[] layoutParams = new LinearLayout.LayoutParams[noLayoutParems];
        for(int i=0; i<noLayoutParems; i++){
            layoutParams[i] = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }

        //Create Layouts
        for(int i=0; i<gridSize; i++){
            linearLayouts[i] = new LinearLayout(view.getContext());
            linearLayouts[i].setTag("horizontalGameLayout"+i);
            linearLayouts[i].setOrientation(LinearLayout.HORIZONTAL);
            if(i%2 == 0){
                layoutParams[0].weight = dotLinearLayoutWeight;
                linearLayouts[i].setLayoutParams(layoutParams[0]);
            }else{
                layoutParams[1].weight = linearLayoutWeight;
                linearLayouts[i].setLayoutParams(layoutParams[1]);
            }
            layout.addView(linearLayouts[i]);
        }

        //Create Dots
        layoutParams[2].weight = dotWeight;
        layoutParams[2].gravity = R.integer.dotGravity;
        for( int i=0; i<imageViews_dot.length; i++){
            imageViews_dot[i] = new ImageView(view.getContext());
            imageViews_dot[i].setTag(R.string.dotTag+i);
            imageViews_dot[i].setLayoutParams(layoutParams[2]);
            imageViews_dot[i].setScaleType(ImageView.ScaleType.valueOf(
                    view.getResources().getString(R.string.dotScalling)));
            imageViews_dot[i].setImageResource(R.drawable.dotDrawable);
        }

        //Create HorizontaLines
        layoutParams[3].weight = horizontalLineWeight;
        layoutParams[3].gravity = R.integer.horizontalLineGravity;
        for( int i=0; i<imageViews_horziontalLines.length; i++){
            imageViews_horziontalLines[i] = new ImageViewAdded(view.getContext());
            imageViews_horziontalLines[i].setTag(
                    R.string.horizontalLineTag+Integer.toString(i));
            imageViews_horziontalLines[i].setLayoutParams(layoutParams[3]);
            imageViews_horziontalLines[i].setScaleType(ImageView.ScaleType.valueOf(
                    view.getResources().getString(R.string.horizontalLineScalling)));
            imageViews_horziontalLines[i].setImageResource(R.drawable.blankHorizontalLineDrawable);
            //setPlayed to false
            imageViews_horziontalLines[i].setSet(false);
            setHorizontalLineClicker(imageViews_horziontalLines[i]);
        }

        //Create Vertical Lines
        layoutParams[4].weight = verticalLineWeight;
        layoutParams[4].gravity = R.integer.verticalLineGravity;
        for( int i=0; i<imageViews_verticalLines.length; i++){
            imageViews_verticalLines[i] = new ImageViewAdded(view.getContext());
            imageViews_verticalLines[i].setTag(R.string.verticalLineTag+i);
            imageViews_verticalLines[i].setLayoutParams(layoutParams[4]);
            imageViews_verticalLines[i].setScaleType(ImageView.ScaleType.valueOf(
                    view.getResources().getString(R.string.verticalLineScalling)));
            imageViews_verticalLines[i].setImageResource(R.drawable.blankVerticalLineDrawable);
            //Set played to false
            imageViews_verticalLines[i].setSet(false);
            setVerticalLineClicker(imageViews_verticalLines[i]);
        }


        //Create Boxes
        layoutParams[5].weight = boxesWeight;
        layoutParams[5].gravity = R.integer.boxGravity;
        for( int i=0; i<imageViews_boxes.length; i++){
            imageViews_boxes[i] = new ImageViewAdded(view.getContext());
            imageViews_boxes[i].setTag(R.string.boxTag+i);
            imageViews_boxes[i].setLayoutParams(layoutParams[5]);
            imageViews_boxes[i].setScaleType(ImageView.ScaleType.valueOf(
                    view.getResources().getString(R.string.boxScalling)));
            imageViews_boxes[i].setImageResource(R.drawable.blankBoxDrawable);
            //Set played to false
            imageViews_boxes[i].setSet(false);
        }

        int dotCount = 0;
        int horizontalLineCount = 0;
        int verticalLineCount = 0;
        int boxesCount = 0;
        for(int i=0; i<gridSize; i++){
            //If Even line, add dots & horziontal lines
            if(i%2 == 0){
                int count = 0;
                for(int j=0; j<gridSize;j++){
                    /*Checks if max horziontal grid size reached
                    * If not add a dot & increament then check again, if not add a line*/
                    if(count<gridSize){
                        linearLayouts[i].addView(imageViews_dot[dotCount]);
                        count++;
                        dotCount++;
                    }
                    if(count<gridSize){
                        linearLayouts[i].addView(imageViews_horziontalLines[horizontalLineCount]);
                        count++;
                        horizontalLineCount++;
                    }
                }
            }else {
                int count = 0;
                for(int j=0; j<gridSize;j++){
                    /*Checks if max vertical grid size reached
                     * If not add a line & increament then check again, if not add a box*/
                    if(count<gridSize){
                        linearLayouts[i].addView(imageViews_verticalLines[verticalLineCount]);
                        count++;
                        verticalLineCount++;
                    }
                    if(count<gridSize){
                        linearLayouts[i].addView(imageViews_boxes[boxesCount]);
                        count++;
                        boxesCount++;
                    }
                }
            }
        }

        return view;
    }


    //OnClickLister for line
    private static void setHorizontalLineClicker(ImageView img){
        img.setOnClickListener(
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //hLineClicked(v);
                }
            }
        );
    }

    //OnClickLister for line
    private static void setVerticalLineClicker(ImageView img){
        img.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // vLineClicked(v,img);
                    }
                }
        );
    }


    private static void print(){
        /*System.out.println(actual +" \n"+
                dots +" \n"+
                totallines +" \n"+
                lines +" \n"+
                boxes + "\n"+"\n");*/

        /*System.out.println(" \n"+
                imageViews_boxes[1].getForegroundGravity() +" \n"+
                imageViews_dot[1].getForegroundGravity() +" \n"+
                imageViews_horziontalLines[1].getForegroundGravity() +" \n"+
                imageViews_verticalLines[1].getForegroundGravity() + "\n"+ linearLayouts[1].getForegroundGravity()+"\n");
        System.out.println(" \n"+
                ((LinearLayout.LayoutParams)imageViews_boxes[1].getLayoutParams()).weight +" \n"+
                ((LinearLayout.LayoutParams)imageViews_dot[1].getLayoutParams()).weight +" \n"+
                ((LinearLayout.LayoutParams)imageViews_horziontalLines[1].getLayoutParams()).weight +" \n"+
                ((LinearLayout.LayoutParams)imageViews_verticalLines[1].getLayoutParams()).weight + "\n" +
                ((LinearLayout.LayoutParams)linearLayouts[1].getLayoutParams()).weight+"\n");*/
        /*//setConte*//*
        layout.removeAllViews();
        linearLayouts[0].addView(imageViews_boxes[0]);
        layout.addView(linearLayouts[0]);*/
    }






}

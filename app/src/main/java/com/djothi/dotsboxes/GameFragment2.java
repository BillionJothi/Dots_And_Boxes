package com.djothi.dotsboxes;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class GameFragment2 extends Fragment {

    //For Setup
    private int gridSize;
    private int noTotalDots;
    private int noTotalInitialXlines;
    private int noTotalBoxes;
    //For Gameplay
    int boardSize;
    int noTotalPlayableSides;
    private int noPlayableLinesLeft;
    private int noHorizontalPlayableLinesLeft;
    private int noVerticalPlayableLinesLeft;
    private boolean playersTurn;
    private final boolean pcPlaying = true;
    private final int noPcPlaying = 1;
    private int turn;
    private int players;
    private int score;
    private long time;
    private String hLineTag;
    private String vLineTag;
    private String playerColor;
    private String pcColor;
    private List<ImageViewAdded> verticalLinesLeft;
    private List<ImageViewAdded> horziontalLinesLeft;
    private GameListener activityCommander;
    View view;

    //private int .;

    public interface GameListener{
        public void GameClicked(int score, int turn);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            activityCommander = (GameListener) getActivity();
        }catch (ClassCastException e ){
            throw new ClassCastException(getActivity().toString());
        }
      /*  Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment4);
        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        fragTransaction.detach(currentFragment);
        //GameFragment2 g = new GameFragment2();
        CreateGameBoard g2 = (CreateGameBoard) new CreateGameBoard();
        g2.makeBoard(layout);
        GameFragment2 g =(GameFragment2) g2;
        fragTransaction.attach(currentFragment);
        fragTransaction.commit();*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        this.view =inflater.inflate(R.layout.fragment_game2, container, false);
        LinearLayout layout = view.findViewById(R.id.linear2);
        layout.removeAllViews();

        //Get Settings from resouruces
        this.playersTurn = getResources().getBoolean(R.bool.playerStarts1st);
        this.turn = getResources().getInteger(R.integer.turnStart);
        this.players = getResources().getInteger(R.integer.initialPlayers);
        this.score = getResources().getInteger(R.integer.initialScore);
        this.boardSize = getResources().getInteger(R.integer.boardSize);
        this.noTotalPlayableSides = (boardSize*2*(boardSize+1));
        this.noPlayableLinesLeft = noTotalPlayableSides;
        this.noHorizontalPlayableLinesLeft = noTotalPlayableSides /2;
        this.noVerticalPlayableLinesLeft = noTotalPlayableSides - noHorizontalPlayableLinesLeft;
        this.hLineTag = getResources().getString(R.string.horizontalLineTagChecker);
        this.vLineTag = getResources().getString(R.string.verticalLineTagChecker);
        this.playerColor = getResources().getString(R.string.P0Colour);
        this.pcColor = getResources().getString(R.string.P1Colour);

        try {
            checkCubeBoard();
        } catch (Exception e) {
            e.printStackTrace();
            showToast("BoardLayout Errors");
        }
        makeBoard(layout);
        // Inflate the layout for this fragment
        return view;
    }

    private void checkCubeBoard() throws Exception {
        if(noHorizontalPlayableLinesLeft != noVerticalPlayableLinesLeft){
            throw new Exception("Horizontal & Vertical playables not equal");
        }else if((noHorizontalPlayableLinesLeft + noVerticalPlayableLinesLeft) != noPlayableLinesLeft){
            throw new Exception("Total playables not equal to remaining");
        }
    }

    private void makeBoard(LinearLayout layout) {
        this.gridSize = (boardSize*2)+1;
        this.noTotalDots = (int) Math.pow((boardSize+1),2);
        this.noTotalInitialXlines = noTotalPlayableSides/2;
        this.noTotalBoxes = (int) Math.pow(boardSize,2);
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
        ImageView[] imageViews_dot = new ImageView[noTotalDots];
        ImageViewAdded[] imageViews_verticalLines = new ImageViewAdded[noTotalInitialXlines];
        ImageViewAdded[] imageViews_horziontalLines = new ImageViewAdded[noTotalInitialXlines];
        ImageViewAdded[] imageViews_boxes = new ImageViewAdded[noTotalBoxes];

        //CreateLayout for each object
        LinearLayout.LayoutParams[] layoutParams = new LinearLayout.LayoutParams[noLayoutParems];
        for(int i=0; i<noLayoutParems; i++){
            layoutParams[i] = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }

        //Create Layouts
        layoutParams[0].weight = dotLinearLayoutWeight;
        layoutParams[1].weight = linearLayoutWeight;
        for(int i=0; i<gridSize; i++){
            linearLayouts[i] = new LinearLayout(view.getContext());
            linearLayouts[i].setTag(
                    view.getResources().getString(R.string.horizontalGameLayoutTag)+ i);
            linearLayouts[i].setOrientation(LinearLayout.HORIZONTAL);
            if(i%2 == 0){ linearLayouts[i].setLayoutParams(layoutParams[0]); }
            else{ linearLayouts[i].setLayoutParams(layoutParams[1]); }
            layout.addView(linearLayouts[i]);
        }

        //Create Dots
        layoutParams[2].weight = dotWeight;
        layoutParams[2].gravity = R.integer.dotGravity;
        for( int i=0; i<imageViews_dot.length; i++){
            imageViews_dot[i] = new ImageView(view.getContext());
            imageViews_dot[i].setTag(
                    view.getResources().getString(R.string.dotTag)+ i);
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
                    view.getResources().getString(R.string.horizontalLineTag)+ i);
            imageViews_horziontalLines[i].setLayoutParams(layoutParams[3]);
            imageViews_horziontalLines[i].setScaleType(ImageView.ScaleType.valueOf(
                    view.getResources().getString(R.string.horizontalLineScalling)));
            imageViews_horziontalLines[i].setImageResource(R.drawable.blankHorizontalDrawable);
            //setPlayed to false
            imageViews_horziontalLines[i].setSet(false);
            final ImageViewAdded himg = imageViews_horziontalLines[i];
            imageViews_horziontalLines[i].setOnClickListener(setLineClicker(himg,true));
        }

        //Create Vertical Lines
        layoutParams[4].weight = verticalLineWeight;
        layoutParams[4].gravity = R.integer.verticalLineGravity;
        for(int i = 0; i<imageViews_verticalLines.length; i++){
            imageViews_verticalLines[i] = new ImageViewAdded(view.getContext());
            imageViews_verticalLines[i].setTag(
                    view.getResources().getString(R.string.verticalLineTag)+ i);
            imageViews_verticalLines[i].setLayoutParams(layoutParams[4]);
            imageViews_verticalLines[i].setScaleType(ImageView.ScaleType.valueOf(
                    view.getResources().getString(R.string.verticalLineScalling)));
            imageViews_verticalLines[i].setImageResource(R.drawable.blankVerticalDrawable);
            //Set played to false
            imageViews_verticalLines[i].setSet(false);
            final ImageViewAdded vimg = imageViews_verticalLines[i];
            imageViews_verticalLines[i].setOnClickListener(setLineClicker(vimg,false));
        }

        //Create Boxes
        layoutParams[5].weight = boxesWeight;
        layoutParams[5].gravity = R.integer.boxGravity;
        for( int i=0; i<imageViews_boxes.length; i++){
            imageViews_boxes[i] = new ImageViewAdded(view.getContext());
            imageViews_boxes[i].setTag(
                    view.getResources().getString(R.string.boxTag)+ i);
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

        //Copy array for finding specific lines later
        this.horziontalLinesLeft = new ArrayList<>(Arrays.asList(Arrays.copyOf(imageViews_horziontalLines,
                imageViews_horziontalLines.length)));
        this.verticalLinesLeft = new ArrayList<>(Arrays.asList(Arrays.copyOf(imageViews_verticalLines,
                imageViews_verticalLines.length)));
    }


    /*************************** END - Methods Used in MakeBoard****************************/
    private static float getFloatResourcesValues(View view, Integer resources){
        TypedValue outValue = new TypedValue();
        view.getResources().getValue(resources, outValue, true);
        return (outValue.getFloat());
    }
    //OnClickLister for line
    private  Button.OnClickListener
        setLineClicker(final ImageViewAdded img, final Boolean isHorizontalLine){
        return new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineClicked(img,isHorizontalLine);
            }
        };
    }
    private void lineClicked(ImageViewAdded img, Boolean horizontalLine) {
        isBox();
        if(playersTurn && !img.isSet()){
            time = System.currentTimeMillis();
            upPlayerScore();
            removeLine(img,playersTurn,horizontalLine);
            activityCommander.GameClicked(score,turn);
            nextTurn();
        }else{showWaitToast();}
    }


    /*********Clicker Extensions/Gameplay**************/
    private void upPlayerScore()  {
        int linescore = getResources().getInteger(R.integer.lineScore);
        this.score+=linescore;
    }
    private void removeLine(ImageViewAdded img,Boolean player, Boolean isHorizontal) {
        img.setSet(true);
        img.setClickable(false);
        noPlayableLinesLeft--;
        if(isHorizontal){
            noHorizontalPlayableLinesLeft--;
            horziontalLinesLeft.remove(img);
            if(player){switchLineColor(playerColor,img,true);}
            else {switchLineColor(pcColor,img,true);}
        }else{
            noVerticalPlayableLinesLeft--;
            verticalLinesLeft.remove(img);
            if(player){switchLineColor(playerColor,img,false);}
            else {switchLineColor(pcColor,img,false);}
        }
    }
    private void switchLineColor(String r, ImageViewAdded img, Boolean isHorizontal){
        int line;
        if(isHorizontal){
            switch (r){
                case "blue": line = R.drawable.blueHorizontalDrawable; break;
                case "red": line = R.drawable.redHorizontalDrawable; break;
                case "blank": line = R.drawable.blankHorizontalDrawable; break;
                default: line = R.drawable.blueHorizontalDrawable;
            }
        }else{
            switch (r){
                case "blue": line = R.drawable.blueVerticalDrawable; break;
                case "red": line = R.drawable.redVerticalDrawable; break;
                case "blank": line = R.drawable.blankVerticalDrawable; break;
                default: line = R.drawable.blueVerticalDrawable;
            }
        }
        img.setImageResource(line);
    }

    private void nextTurn(){
        if(haveTurns()){
            turn++;
            if(turn > players){ this.turn = 0; }
            nextPlayer();
        }else{ showToast("GAME OVER!"); }
    }
    private boolean haveTurns(){
        return (noPlayableLinesLeft !=0);
    }
    private void nextPlayer(){
        playersTurn = (turn != players);
        if(!playersTurn){
            if(pcPlaying){
                computersTurn();
                nextTurn();
            }
            else{
                long elapsed = System.currentTimeMillis()-time;
                if(elapsed > (long) getResources().getInteger(R.integer.waitForNextPlayer)){
                    showWaitToast();
                }
                if(elapsed > (long) getResources().getInteger(R.integer.timeToSkipNextPlayer)){
                    nextTurn();
                }
            }
        }
    }

    /****************PC Gameplay Methods****************/
    private void computersTurn() {
        //TODO As of now it will play all of PC turns at 1 go - possibly to change/expand
        //TODO upPC scoare
        for(int i=0; i<noPcPlaying; i++){
            try {
                randomLine();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(),"PC failed to find a line",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void randomLine() {
        ImageViewAdded choosenLine;
        final Random random = new Random();
        final int min = 0;
        boolean horizontal = (Math.random()<0.5);
        if(noHorizontalPlayableLinesLeft == 0){ horizontal=false; }
        else if(noVerticalPlayableLinesLeft == 0){ horizontal=true; }
        if(horizontal){
            choosenLine = horziontalLinesLeft.get(random.nextInt(horziontalLinesLeft.size()-min)+min);
        }else {
            choosenLine = verticalLinesLeft.get(random.nextInt(verticalLinesLeft.size()-min)+min);
        }
        removeLine(choosenLine,false,horizontal);
    }


    private void isBox(){
       for(int i=0; i<noTotalBoxes; i++){
           ImageViewAdded b =
                   view.findViewWithTag(getResources().getString(R.string.boxTagChecker)+i);
          System.out.println( "*************************"+b.getNextFocusUpId()+
                  "******************");
       }
    }

    /********************Aux Meathods**********/
    private void showWaitToast(){ String s = "Waiting for next player's decision"; showToast(s); }
    private void showToast(String s){ Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show(); }

    /***********Getters & Setters **********8*/
    public void setPlayersTurn(boolean playersTurn) {
        this.playersTurn = playersTurn;
    }
    public int getBoardSize() { return boardSize; }
    public void setBoardSize(int boardSize) { this.boardSize = boardSize;}
    public boolean isPlayersTurn() { return playersTurn; }
    public int getTurn() { return turn; }
    public void setTurn(int turn) { this.turn = turn;}
    public int getPlayers() { return players; }
    public void setPlayers(int players) { this.players = players;}





    /************************To Remove**************/
    private ImageViewAdded tryFindLine(View view, boolean h) throws Exception {
        boolean p = false;
        ImageViewAdded img;
        for(int i=0; i<100; i++) {
            if (h) {
                img = view.findViewWithTag(hLineTag);
                if (!img.isSet()) {
                    p = true;
                    break;
                }
            } else {
                img = view.findViewWithTag(vLineTag);
                if (!img.isSet()) {
                    p = true;
                    break;
                }
            }
            return img;
        }
        if(!p){
            throw new Exception("Unable to find Line");
        }return null;
    }
    private boolean isHorizontalLine(ImageViewAdded img) throws Exception {
        if(img.getTag().toString().contains(hLineTag)){
            return true;
        }else if(img.getTag().toString().contains(vLineTag)){
            return false;
        }else{
            throw new Exception("Unknown Line");
        }
    }
}



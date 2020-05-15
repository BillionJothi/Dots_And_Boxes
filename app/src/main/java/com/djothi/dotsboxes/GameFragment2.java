package com.djothi.dotsboxes;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
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
    private int linesPerDotGrid;
    private int noDotGrids;
    //For Gameplay
    private int boardSize;
    private int noTotalPlayableSides;
    private int noPlayableLinesLeft;
    private int noHorizontalPlayableLinesLeft;
    private int noVerticalPlayableLinesLeft;
    //Players (Set in Drawable)
    private int turn;
    private boolean playersTurn;
    private boolean pcPlaying;
    private int noPcPlaying;
    private int PCTurns[];
    private int onlinePlayers;
    private int localPlayers;
    private int totalPlayers;
    //Gameplay
    private int score;
    private long time;
    private String hLineTag;
    private String vLineTag;
    private String playerColor;
    private String pcColor;
    private List<ImageViewAdded> verticalLinesLeft;
    private List<ImageViewAdded> horziontalLinesLeft;
    private GameListener activityCommander;
    private ImageViewAdded box;
    private int noSqaures = 0;
    private View view;

    private ImageViewAdded[][] layoutInArray;

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
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        this.view =inflater.inflate(R.layout.fragment_game2, container, false);
        LinearLayout layout = view.findViewById(R.id.linear2);
        layout.removeAllViews();

        try {
            calPlayers();
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Error getting rnad no. for PC");
        }
        //Get Settings from resouruces
        this.turn = getResources().getInteger(R.integer.turnStart);
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
        layoutInArray =  getLayoutArray();

        System.out.println(layoutInArray[0][0].getTag()+","+layoutInArray[0][1].getTag()+","+layoutInArray[0][2].getTag()+ ","+layoutInArray[0][3].getTag()+","+layoutInArray[0][4].getTag()+","+layoutInArray[0][5].getTag()+","+layoutInArray[0][6].getTag());
        System.out.println(layoutInArray[1][0].getTag()+","+layoutInArray[1][1].getTag()+","+layoutInArray[1][2].getTag()+","+layoutInArray[1][3].getTag()+","+layoutInArray[1][4].getTag()+","+layoutInArray[1][5].getTag()+","+layoutInArray[1][6].getTag());
        System.out.println(layoutInArray[2][0].getTag()+","+layoutInArray[2][1].getTag()+","+layoutInArray[2][2].getTag()+","+layoutInArray[2][3].getTag()+","+layoutInArray[2][4].getTag()+","+layoutInArray[2][5].getTag()+","+layoutInArray[2][6].getTag());
        System.out.println(layoutInArray[3][0].getTag()+","+layoutInArray[3][1].getTag()+","+layoutInArray[3][2].getTag()+","+layoutInArray[3][3].getTag()+","+layoutInArray[3][4].getTag()+","+layoutInArray[3][5].getTag()+","+layoutInArray[3][6].getTag());
        System.out.println(layoutInArray[4][0].getTag()+","+layoutInArray[4][1].getTag()+","+layoutInArray[4][2].getTag()+","+layoutInArray[4][3].getTag()+","+layoutInArray[4][4].getTag()+","+layoutInArray[4][5].getTag()+","+layoutInArray[4][6].getTag());
        System.out.println(layoutInArray[5][0].getTag()+","+layoutInArray[5][1].getTag()+","+layoutInArray[5][2].getTag()+","+layoutInArray[5][3].getTag()+","+layoutInArray[5][4].getTag()+","+layoutInArray[5][5].getTag()+","+layoutInArray[5][6].getTag());
        System.out.println(layoutInArray[6][0].getTag()+","+layoutInArray[6][1].getTag()+","+layoutInArray[6][2].getTag()+","+layoutInArray[6][3].getTag()+","+layoutInArray[6][4].getTag()+","+layoutInArray[6][5].getTag()+","+layoutInArray[6][6].getTag());


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
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void makeBoard(LinearLayout layout) {
        this.gridSize = (boardSize*2)+1;
        this.noTotalDots = (int) Math.pow((boardSize+1),2);
        this.noTotalInitialXlines = noTotalPlayableSides/2;
        //for above,also formaula: Math.pow(x,2)+x;
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
        ImageViewAdded[] imageViews_dot = new ImageViewAdded[noTotalDots];
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
            linearLayouts[i].getAutofillId();
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
            imageViews_dot[i] = new ImageViewAdded(view.getContext());
            imageViews_dot[i].getAutofillId();
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
            imageViews_horziontalLines[i].getAutofillId();
            imageViews_horziontalLines[i].setTag(
                    view.getResources().getString(R.string.horizontalLineTag)+ i);
            imageViews_horziontalLines[i].setLayoutParams(layoutParams[3]);
            imageViews_horziontalLines[i].setScaleType(ImageView.ScaleType.valueOf(
                    view.getResources().getString(R.string.horizontalLineScalling)));
            imageViews_horziontalLines[i].setImageResource(R.drawable.blankHorizontalDrawable);
            //setPlayed to false
            imageViews_horziontalLines[i].setSet(false);
            ImageViewAdded himg = imageViews_horziontalLines[i];
            imageViews_horziontalLines[i].setOnClickListener(setLineClicker());
        }

        //Create Vertical Lines
        layoutParams[4].weight = verticalLineWeight;
        layoutParams[4].gravity = R.integer.verticalLineGravity;
        for(int i = 0; i<imageViews_verticalLines.length; i++){
            imageViews_verticalLines[i] = new ImageViewAdded(view.getContext());
            imageViews_verticalLines[i].getAutofillId();
            imageViews_verticalLines[i].setTag(
                    view.getResources().getString(R.string.verticalLineTag)+ i);
            imageViews_verticalLines[i].setLayoutParams(layoutParams[4]);
            imageViews_verticalLines[i].setScaleType(ImageView.ScaleType.valueOf(
                    view.getResources().getString(R.string.verticalLineScalling)));
            imageViews_verticalLines[i].setImageResource(R.drawable.blankVerticalDrawable);
            //Set played to false
            imageViews_verticalLines[i].setSet(false);
            ImageViewAdded vimg = imageViews_verticalLines[i];
            imageViews_verticalLines[i].setOnClickListener(setLineClicker());
        }

        //Create Boxes
        layoutParams[5].weight = boxesWeight;
        layoutParams[5].gravity = R.integer.boxGravity;
        for( int i=0; i<imageViews_boxes.length; i++){
            imageViews_boxes[i] = new ImageViewAdded(view.getContext());
            imageViews_boxes[i].getAutofillId();
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
    private void calPlayers() throws Exception {
        this.localPlayers = getResources().getInteger(R.integer.localPlayers);
        this.onlinePlayers =  getResources().getInteger(R.integer.onlinePlayers);
        this.pcPlaying = getResources().getBoolean(R.bool.pcPlaying);
        this.noPcPlaying = getResources().getInteger(R.integer.noPCPlayers);
        this.playersTurn = getResources().getBoolean(R.bool.playerStarts1st);
        totalPlayers = localPlayers + onlinePlayers;
        if(pcPlaying) {
            totalPlayers+=noPcPlaying;
            this.PCTurns = new int[noPcPlaying];
        }
        boolean done =false;
        //TODO add player don't start 1st
        int pcTurn, count = 0, min = 0, notp =0;
        if(!playersTurn){notp=1;};
        if(pcPlaying) {
            for (int i = 0; i < PCTurns.length; i++) {
                Random r = new Random();
                do {
                    count++;
                    pcTurn = r.nextInt(totalPlayers - min) + min - notp;
                    //Setting PC turn
                    done = true;
                    if(playersTurn && pcTurn == 0){pcTurn=1;};
                    if(i>0){
                        int j=0;
                        do{
                            if(pcTurn == PCTurns[j]){
                                done=false;
                            }
                            j++;
                        }while (j<i);
                    }
                    if (count == 100) {
                        throw new Exception("Cant find new Rand No. for PC");
                    }
                } while (!done);
                PCTurns[i] = pcTurn;
            }
        }

       /* System.out.println("localPlayers " + localPlayers+ "\n"+
                " onlinePlayers " + onlinePlayers+"\n"+
                " pcPlaying " + pcPlaying+"\n"+
                " noPcPlaying " + noPcPlaying+"\n"+
                " playersTurn " + playersTurn+"\n"+
                " totalPlayers " + totalPlayers+"\n"+
                " PCPlayers "+ Arrays.toString(PCTurns));*/
    }

    /*************************** END - Methods Used in MakeBoard****************************/
    private static float getFloatResourcesValues(View view, Integer resources){
        TypedValue outValue = new TypedValue();
        view.getResources().getValue(resources, outValue, true);
        return (outValue.getFloat());
    }


    //OnClickLister for line
    private Button.OnClickListener
        setLineClicker(){
        return new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineClicked(v);
            }
        };
    }
    private void lineClicked(View v) {
        ImageViewAdded img = (ImageViewAdded) v;
        boolean isHorizontalLine = false;
        try {
            isHorizontalLine = isHorizontalLine(img);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(playersTurn && !img.isSet()){
            time = System.currentTimeMillis();
            upPlayerScore();
            removeLine(img,playersTurn,isHorizontalLine);
            if(boxExist(v)){
                box.setImageResource(R.drawable.redBoxDrawable);
            }
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
            if(turn >= totalPlayers){ this.turn = 0;}
            //System.out.println(" NewTurn: = "+turn);
            nextPlayer();
        }else{ showToast("GAME OVER!"); }
    }
    private boolean haveTurns(){
        return (noPlayableLinesLeft !=0);
    }
    private void nextPlayer(){
        playersTurn = !isPCTurn();
        if(!playersTurn){
            if(pcPlaying && isPCTurn()){
                computersTurn();
                nextTurn();
            }
            else{
                //TODO set as service so can update when time is over - now is static (not
                // functional)
                long elapsed = System.currentTimeMillis()-time;
                if(elapsed > (long) getResources().getInteger(R.integer.waitForNextPlayer)){
                    showWaitToast();
                }
                if(elapsed > (long) getResources().getInteger(R.integer.timeToSkipNextPlayer)){
                    //nextTurn();
                }
            }
        }
    }

    /****************PC Gameplay Methods****************/
    private void computersTurn() {
        //TODO As of now it will play all of PC turns at 1 go - possibly to change/expand
        //TODO upPC scoare
        //for(int i=0; i<noPcPlaying; i++){
        try {
            randomLine();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"PC failed to find a line",Toast.LENGTH_SHORT).show();
        }
        //}
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

    //method to check if line sets a box
    private boolean isBox(ImageViewAdded line){

        List<Boolean[][]> r = getLinesSetValues();  //hLinesChecked & then vLinesChecked
        List<Integer> i; //rowNo, then actNumber
        boolean box;
        boolean check;
        this.noSqaures = 0;
        try {
            boolean horizontal = isHorizontalLine(line);
            //System.out.println("LINE IS XXXXXX"+horizontal);
            String s = getLineTagNo(line, horizontal);
            int v = Integer.parseInt(s);
            int linesPerGrid;

            if(horizontal){
                linesPerGrid = linesPerDotGrid;
                i = calLineRowColums(v,linesPerGrid);
                //System.out.println("LINE CORD isHorizontal"+horizontal+"," + i.get(0)+"," +i
                // .get(1));
                check = r.get(0)[i.get(0)][i.get(1)];
                if(i.get(0)==0){
                    if(checkHBelow(r,i)){
                        box = true; noSqaures++;
                        this.box = view.findViewWithTag(
                                getResources().getString(R.string.boxTagChecker)+ i.get(1));
                        return box;
                    }
                }  //Only check below if topmost
                else if(i.get(0)== noDotGrids -1) {
                    if(checkHAbove(r,i)){
                        box = true; noSqaures++;
                        this.box = view.findViewWithTag(
                                getResources().getString(R.string.boxTagChecker)+ i.get(1));
                        return box;
                    }
                }
                //only check above if bottom most (noHLinesGrid) is if last line
                else{
                    if(checkHBelow(r,i)){
                        box = true; noSqaures++;
                        this.box = view.findViewWithTag(
                                getResources().getString(R.string.boxTagChecker)+ i.get(1));
                        return box;
                    }else if(checkHAbove(r,i)){
                        box = true; noSqaures++;
                        this.box = view.findViewWithTag(
                                getResources().getString(R.string.boxTagChecker)+ i.get(1));
                        return box;
                    }
                } //check abocve & below

            }else {
                //Vertical
                linesPerGrid = linesPerDotGrid;
                i = calLineRowColums(v,linesPerGrid);
                //System.out.println("LINE CORD isHorizontal"+horizontal+"," + i.get(0)+"," +i
                // .get(1));
                check =  r.get(1)[i.get(0)][i.get(1)];
                if(i.get(0)==0){
                    if(checkVforward(r,i)){
                        box = true; noSqaures++;
                        this.box = view.findViewWithTag(
                                getResources().getString(R.string.boxTagChecker)+ i.get(1));
                        return box;
                    }

                } //Only check forward if 1st
                else if(i.get(0)== noDotGrids) {
                    if(checkVBackward(r,i)){
                        box = true; noSqaures++;
                        this.box = view.findViewWithTag(
                                getResources().getString(R.string.boxTagChecker)+ i.get(1));
                        return box;
                    }
                }
                //only check backward if last , (noHLinesGrid) is if last line
                else{
                    if(checkVBackward(r,i)){
                        box = true; noSqaures++;
                        this.box = view.findViewWithTag(
                                getResources().getString(R.string.boxTagChecker)+ i.get(1));
                        return box;
                    }else if(checkVforward(r,i)){
                        box = true; noSqaures++;
                        this.box = view.findViewWithTag(
                                getResources().getString(R.string.boxTagChecker)+ i.get(1));
                        return box;
                    }
                } //check forward & backward
            }
            //Check to ensure right line
            if(line.isSet() != check){
                throw new Exception("Line settings not equal");
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Unknown Line or error");
        }
        return false;
    }
    private List<Boolean[][]> getLinesSetValues(){
        this.linesPerDotGrid = (noTotalInitialXlines-boardSize)/boardSize;
        this.noDotGrids = noTotalInitialXlines/ linesPerDotGrid;
        int totalPerGrid = linesPerDotGrid+noDotGrids;
        int dotCounter=0,hLineCounter = 0, vLineCounter =0, boxCounter =0;

        Boolean[][] hLinesChecked = new Boolean[noDotGrids][linesPerDotGrid];
        Boolean[][] vLinesChecked = new Boolean[noDotGrids][linesPerDotGrid];
        int counter = 0;
        //For every horizontal line check if checked
        //outputs fomr [0][0],[0][1],[0][2],[1][0]...
        //Do also for veritcal lines
        for(int i = 0; i< noDotGrids; i++){
            for (int j = 0; j< linesPerDotGrid; j++){
                ImageViewAdded side =
                        view.findViewWithTag(getResources().getString(R.string.horizontalLineTagChecker)+ counter);
                hLinesChecked[i][j] = side.isSet();
                ImageViewAdded Vside =
                        view.findViewWithTag(getResources().getString(R.string.verticalLineTagChecker)+counter);
                vLinesChecked[i][j] = Vside.isSet();
                counter++;
            }
        }
        List r = new ArrayList();
        r.add(hLinesChecked);
        r.add(vLinesChecked);
        return r;
    }
    private ArrayList<Integer> calLineRowColums(Integer v, Integer linesPerGrid){
        int actnumber;
        int rowno;
        if(v==0){
            actnumber = 0; rowno = 0;
        }else if(v> linesPerGrid){
            actnumber= v%(linesPerGrid);
            rowno = v/(linesPerGrid);
        }else{
            actnumber = v; rowno = 0;
        }
        ArrayList<Integer> r = new ArrayList<Integer>();
        r.add(rowno);
        r.add(actnumber);
        return r;
    }

    private ImageViewAdded[][] getLayoutArray(){
        this.linesPerDotGrid = (noTotalInitialXlines-boardSize)/boardSize;
        this.noDotGrids = noTotalInitialXlines/ linesPerDotGrid;
        int totalPerGrid = linesPerDotGrid+noDotGrids;
        int dotCounter=0,hLineCounter = 0, vLineCounter =0, boxCounter =0;

        ImageViewAdded[][] finalLayout = new ImageViewAdded[totalPerGrid][totalPerGrid];
        for(int i=0; i<totalPerGrid; i++){
            for(int j=0; j<totalPerGrid; j++){
                if(i%2==0){
                    if(j%2 ==0){
                        ImageView v =
                                view.findViewWithTag(getResources().getString(R.string.dotTagChecker)+ dotCounter);
                        finalLayout[i][j] = (ImageViewAdded) v;
                        dotCounter++;
                    }else{
                        ImageViewAdded v =
                                view.findViewWithTag(getResources().getString(R.string.horizontalLineTagChecker)+ hLineCounter);
                        finalLayout[i][j] = v;
                        hLineCounter++;
                    }
                }else{
                    if(j%2==0){
                        ImageViewAdded v =
                                view.findViewWithTag(getResources().getString(R.string.verticalLineTagChecker)+ vLineCounter);
                        finalLayout[i][j] = v;
                        vLineCounter++;
                    }else{
                        ImageViewAdded v =
                                view.findViewWithTag(getResources().getString(R.string.boxTagChecker)+ boxCounter);
                        finalLayout[i][j] = v;
                        boxCounter++;
                    }
                }
            }
        }
        return finalLayout;
    }
    private boolean boxExist(View v) {
        ImageViewAdded line = (ImageViewAdded) v;
        ImageViewAdded[][] lay = layoutInArray;
        int row = 0, column = 0;
        boolean hori = true;
        //Note: ROWS goes left to right on screen & columsns top to down
        //Also layour [row][column]
        //Get row/colum values
        for (int i = 0; i < lay.length; i++) {
            for (int j = 0; j < lay.length; j++) {
                if (lay[i][j].getTag() == line.getTag()) {
                    column = i;
                    row = j;
                    break;
                }
            }
        }
        showToast(line.getTag().toString() + " "+line.getId() +"\n" + "r:"+row+"c"+column);
        try {
            // System.out.println("row: "+row+" column "+column+ " isHOri" +hori);
            hori = isHorizontalLine(line);
            System.out.println("row: "+ row + " column " + column + " isHOri" + hori);
            if (hori) {
                if (column == 0) {
                    return topLines(lay, column, row);
                }
                if (column == lay.length-1) {
                    return bottomLines(lay, column, row);
                } else {
                    boolean t = topLines(lay, column, row);
                    boolean b = bottomLines(lay, column, row);
                    if (t || b) {
                        return true;
                    } else {
                        return false;
                    }
                }

            } else {
                //Vertical Lines only
                if (row == 0) {
                    return beforeLines(lay, column, row);
                }
                if (row == lay.length-1) {
                    return afterLines(lay, column, row);
                } else { //TODO add dual boxes function
                    boolean b = beforeLines(lay, column, row);
                    boolean a = afterLines(lay, column, row);
                    if (b || a) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }return false;

    }


    private void animate(ImageViewAdded i){
        final AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.blueHorizontalDrawable,null), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.redHorizontalDrawable,null), 200);
        animation.addFrame(getResources().getDrawable(R.drawable.blueHorizontalDrawable,null), 300);
        animation.setOneShot(false);
        i.setImageDrawable(animation);
                // Drawable[] drawables = new Drawable[i.length];
        /*for(int k=0; k<i.length; k++){
            drawables[k] = i[k].getDrawable();
            i[k].setImageDrawable(animation);
        }*/
        animation.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animation.stop();
                //refreshing.clearAnimation();
            }
        }, 5000);
        /*for(int k=0; k<i.length; k++){
            i[k].setImageDrawable(drawables[k]);
        }*/
    }



    //Colum addd goes up the table, column minus goes down
    //row add goes into table, minums out
    private boolean bottomLines(ImageViewAdded[][] lay, int column, int row) {
        ImageViewAdded[] i = {lay[column-1][row+1],lay[column-1][row-1],lay[column-2][row]};
        animate(lay[column-1][row+1]);
        Boolean y = false;
        for (ImageViewAdded j: i) {
            y = j.isSet();
        }
        boolean r = y;
        //animate(i);
        if(r){box = lay[column-1][row];}
        return r;
    }
    private boolean topLines(ImageViewAdded[][] lay, int column, int row) {
        System.out.println("******************c"+column+"********"+row);
        boolean r = (lay[column+1][row+1].isSet() &&
                      lay[column+1][row-1].isSet() &&
                       lay[column+2][row].isSet());
        if(r){box = lay[column+1][row];}
        return r;
    }
    private boolean beforeLines(ImageViewAdded[][] lay, int column, int row) {
        boolean r = (lay[column+1][row+1].isSet() &&
                     lay[column-1][row+1].isSet() &&
                      lay[column][row+2].isSet());
        if(r){box = lay[column][row+1];}
        return r;
    }
    private boolean afterLines(ImageViewAdded[][] lay, int column, int row) {
        boolean r = (lay[column-1][row-1].isSet() &&
                      lay[column-1][row+1].isSet() &&
                      lay[column][row-2].isSet());
        if(r){box = lay[column][row-1];}
        return r;
    }


    /*************Meathods used in checking if lines near box************8*/
    /*HorizontalLineas are in lineBools(0), VerticalLines: LineBools(1), Location of the current
    * line is Loc(0) for row(downwards on table) & loc(1) for colums (sidewards on table).
    * Currently all LineBools are stored in lineBools in
    * format:: HORIZONTAL/VERTICAL [row] [colume] with row & column being relative to it's type
    * of line only (so same amount of rows & colums of Horizontal & Vertical Lines)*/
    private boolean checkHBelow( List<Boolean[][]> lineBools, List<Integer> loc){
        return (lineBools.get(1)[loc.get(0)][loc.get(1)] //Vertical BOTOOM Left
                && lineBools.get(1)[loc.get(0)][loc.get(1) +1] //Vertical BOTOOM Right
                && lineBools.get(0)[loc.get(0)+1][loc.get(1)]);//Horizontal BOTOOM (directly opp)
    }
    private boolean checkHAbove(List<Boolean[][]> lineBools,List<Integer> loc){
        return(lineBools.get(1)[loc.get(0)-1][loc.get(1)] //Vertical TOP Left
                && lineBools.get(1)[loc.get(0)-1][loc.get(1)+1] //Vertical TOP Right
                && lineBools.get(0)[loc.get(0)-1][loc.get(1)]); //Horizontal TOP (direnctly opp)
    }
    private boolean checkVforward(List<Boolean[][]> lineBools,List<Integer> loc){
        return(lineBools.get(0)[loc.get(0)][loc.get(1)] //Horizontal FORWARD top
                && lineBools.get(0)[loc.get(0)+1][loc.get(1)] //Horizontal FORWARD bottom
                && lineBools.get(1)[loc.get(0)][loc.get(1)+1]); //Vertical FORWARD (direnctly opp)
    }
    private boolean checkVBackward( List<Boolean[][]> lineBools,List<Integer> loc){
        return(lineBools.get(0)[loc.get(0)][loc.get(1)-1] //Horizontal BACKWARD top
                && lineBools.get(0)[loc.get(0)+1][loc.get(1)-1] //Horizontal BACKWARD bottom
                && lineBools.get(1)[loc.get(0)][loc.get(1)-1]); //Vertical BACKWARD (direnctly opp)
    }


    /********************Aux Meathods**********/
    private void showWaitToast(){ String s = "Waiting for next player's decision"; showToast(s); }
    private void showToast(String s){ Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show(); }
    private boolean isHorizontalLine(ImageViewAdded img) throws Exception {
        if(img.getTag().toString().contains(hLineTag)){
            return true;
        }else if(img.getTag().toString().contains(vLineTag)){
            return false;
        }else{
            throw new Exception("Unknown Line");
        }
    }
    private String getLineTagNo(ImageViewAdded line,Boolean isHorizontal){
        if(isHorizontal){
            return line.getTag().toString().replace(hLineTag,"");
        }else{
            return line.getTag().toString().replace(vLineTag,"");
        }
    }
    private boolean isPCTurn(){
        for (int pcPlayer : PCTurns) {
            if (turn == pcPlayer) {
                return true;
            }
        }
        return false;
    }

    /***********Getters & Setters **********8*/
    public void setPlayersTurn(boolean playersTurn) {
        this.playersTurn = playersTurn;
    }
    public int getBoardSize() { return boardSize; }
    public void setBoardSize(int boardSize) { this.boardSize = boardSize;}
    public boolean isPlayersTurn() { return playersTurn; }
    public int getTurn() { return turn; }
    public void setTurn(int turn) { this.turn = turn;}
    public int getLocalPlayers() { return localPlayers; }
    public void setLocalPlayers(int localPlayers) { this.localPlayers = localPlayers;}





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

}



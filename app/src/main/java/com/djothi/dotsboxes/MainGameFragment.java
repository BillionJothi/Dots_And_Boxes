package com.djothi.dotsboxes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


public class MainGameFragment extends Fragment {

    private int noTotalDots;
    private int noTotalInitialXlines;
    private int noTotalBoxes;
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
    private boolean randomTurns;
    private int noPcPlaying;
    private int PCTurns[];
    private int playerTurns[];
    private int onlinePlayers;
    private int localPlayers;
    private int totalPlayers;
    private int delay;
    //Gameplay
    private int score[];
    private long time;
    private String hLineTag;
    private String vLineTag;
    private String playerColors[];
    //Background
    private ImageViewAdded[][] layoutInArray;
    private List<ImageViewAdded> verticalLinesLeft;
    private List<ImageViewAdded> horziontalLinesLeft;
    private List<ImageViewAdded> moves = new ArrayList<ImageViewAdded>();
    private View view;
    private LinearLayout layout;
    GameListener activityCommander;




    public MainGameFragment() {
    }

    public interface GameListener{
        public void GameClicked(int[] score, int turn);
        public void updateTurn(String turn);
        public void setInitialTurn(String turn);
        public void updatePlayerScore(int score);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
             activityCommander = (GameListener) getActivity();
        }catch (ClassCastException e ){
            throw new ClassCastException(getActivity().toString() +
                    "    Must implement Game fragment interface");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_game, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*Getting internal data*/
        System.out.println("**************Setting internal data!***************");
        this.boardSize = getArguments().getInt("grid");
        this.localPlayers = getArguments().getInt("human");
        this.noPcPlaying = getArguments().getInt("AI");
        this.playersTurn = getArguments().getBoolean("p1Starts");
        if(localPlayers == 0) {playersTurn = false;}
        if(getArguments().getBoolean("quickMode")){this.delay = 0;} else {this.delay = 1000;}
        this.randomTurns = getArguments().getBoolean("randomTurns");
        if(noPcPlaying == 0) {this.pcPlaying = false; } else {this.pcPlaying = true;}
        System.out.println("Board Size:" + this.boardSize);
        Setup();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public View Setup(){
        this.layout = view.findViewById(R.id.linear2);
        layout.removeAllViews();

        try {
            calPlayers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Get Settings from resouruces
        this.turn = getResources().getInteger(R.integer.turnStart);

        this.score = new int[totalPlayers];
        for(int i: score){ i = getResources().getInteger(R.integer.initialScore); }
        //this.boardSize = getResources().getInteger(R.integer.boardSize);
        this.noTotalPlayableSides = (boardSize*2*(boardSize+1));
        this.noPlayableLinesLeft = noTotalPlayableSides;
        this.noHorizontalPlayableLinesLeft = noTotalPlayableSides /2;
        this.noVerticalPlayableLinesLeft = noTotalPlayableSides - noHorizontalPlayableLinesLeft;
        this.hLineTag = getResources().getString(R.string.horizontalLineTagChecker);
        this.vLineTag = getResources().getString(R.string.verticalLineTagChecker);
        this.playerColors = new String[totalPlayers];
        setPlayerColors();

        try {
            checkCubeBoard();
        } catch (Exception e) {
            e.printStackTrace();
            showToast("BoardLayout Errors - Board not made");
        }
        makeBoard();
        layoutInArray =  getLayoutArray();
        if(!playersTurn){
            nextPlayer();
        }
        return view;
    }

    /*********Create Environment***************************/
    // Method makes the board - Build O for auto-setting element IDs
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void makeBoard() {
        //For Setup
        int gridSize = (boardSize * 2) + 1;
        this.noTotalDots = (int) Math.pow((boardSize+1),2);
        //or for below, can use formaula: Math.pow(x,2)+x where x is boardSize instead;
        this.noTotalInitialXlines = noTotalPlayableSides/2;
        this.noTotalBoxes = (int) Math.pow(boardSize,2);
        //No.of LayoutParams is dependent on the number of object types. Currently fixes no of types
        final int noLayoutParams = 6;


        //get Resources Values for each object
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
        LinearLayout.LayoutParams[] layoutParams = new LinearLayout.LayoutParams[noLayoutParams];
        for(int i=0; i<noLayoutParams ; i++){
            layoutParams[i] = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }

        //Create Layouts
        layoutParams[0].weight = dotLinearLayoutWeight;
        layoutParams[1].weight = linearLayoutWeight;
        for(int i = 0; i< gridSize; i++){
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

        //Adding items to view in right layout. Note: This allows for variable grids
        int dotCount = 0;
        int horizontalLineCount = 0;
        int verticalLineCount = 0;
        int boxesCount = 0;
        for(int i = 0; i< gridSize; i++){
            //If Even line, add dots & horziontal lines
            if(i%2 == 0){
                int count = 0;
                for(int j = 0; j< gridSize; j++){
                    /*Checks if max horziontal grid size reached
                     * If not add a dot & increament then check again, if not add a line*/
                    if(count< gridSize){
                        linearLayouts[i].addView(imageViews_dot[dotCount]);
                        count++;
                        dotCount++;
                    }
                    if(count< gridSize){
                        linearLayouts[i].addView(imageViews_horziontalLines[horizontalLineCount]);
                        count++;
                        horizontalLineCount++;
                    }
                }
            }else {
                int count = 0;
                for(int j = 0; j< gridSize; j++){
                    /*Checks if max vertical grid size reached
                     * If not add a line & increament then check again, if not add a box*/
                    if(count< gridSize){
                        linearLayouts[i].addView(imageViews_verticalLines[verticalLineCount]);
                        count++;
                        verticalLineCount++;
                    }
                    if(count< gridSize){
                        linearLayouts[i].addView(imageViews_boxes[boxesCount]);
                        count++;
                        boxesCount++;
                    }
                }
            }
        }

        //TODO Note: Copying of Arrays of Assets happens here
        //Copy array for finding specific lines later
        this.horziontalLinesLeft = new ArrayList<>(Arrays.asList(Arrays.copyOf(imageViews_horziontalLines,
                imageViews_horziontalLines.length)));
        this.verticalLinesLeft = new ArrayList<>(Arrays.asList(Arrays.copyOf(imageViews_verticalLines,
                imageViews_verticalLines.length)));
    }
    //Method calculates the number of players & PCs & set accordingly. Sets random PC turns too
    // Players & PC may have overlapping turns if > 1 player or > 1 PC!
    private void calPlayers() throws Exception {
        this.onlinePlayers =  getResources().getInteger(R.integer.onlinePlayers);
        totalPlayers = localPlayers + onlinePlayers;
        if(pcPlaying) {
            totalPlayers+=noPcPlaying;
            this.PCTurns = new int[noPcPlaying];
        }
        List<Integer> a = new ArrayList<Integer>(noPcPlaying);
        boolean done = true;
        int pcTurn = 0, count = 0, min = 0;
        if(pcPlaying) {
            if(randomTurns ){
                for (int i = 0; i < PCTurns.length; i++) {
                    Random r = new Random();
                    do {
                        count++;
                        if(playersTurn) {min=1;}
                        pcTurn = r.nextInt(totalPlayers - min) + min;
                        done = true;
                        System.out.println("Total Player s =  "+totalPlayers
                        + "PCTURNS Siz =" + PCTurns.length);
                        if(playersTurn && totalPlayers == 2){pcTurn=1;}
                        if(!playersTurn && totalPlayers ==2){pcTurn=0;}
                        if(i>0){
                            //check that new turn is not equal to an older one.
                            for(int j=0; j<i; j++){
                                if(pcTurn == PCTurns[j]){
                                    done=false;
                                    break;
                                }
                            }
                        }
                    } while (!done);
                    if(done){
                        PCTurns[i] = pcTurn;
                    }
                }
            }else{
                int j=0;
                if(!playersTurn){
                    for(int i=0; i<noPcPlaying; i++){ PCTurns[i] = i; }
                }else{
                    j = localPlayers;
                    for(int i=0; i<noPcPlaying; i++){ PCTurns[i]=j; }
                }
            }
        }
        System.out.println("TotalNo.Players = "+totalPlayers + ", PC Players = " + noPcPlaying +
                ", PCTurns = "+ Arrays.toString(PCTurns) );
        //Setting Player turns no.
        playerTurns = new int[localPlayers];
        int playercount = 0;
        boolean isAPlyaerTurn = false;
        for (int i=0; i<totalPlayers; i++){
            for(int j=0; j<PCTurns.length; j++){
                if (PCTurns[j] == i) {
                    isAPlyaerTurn = false;
                }
            }
            if(isAPlyaerTurn){
                System.out.println("placing "+ i);
                playerTurns[playercount] = i;
                playercount++;
            }
        }
    }
    //Gets an Array of the exact board layout in table format
    private ImageViewAdded[][] getLayoutArray(){
        int linesPerDotGrid = (noTotalInitialXlines - boardSize) / boardSize;
        int noDotGrids = noTotalInitialXlines / linesPerDotGrid;
        int totalPerGrid = linesPerDotGrid + noDotGrids;
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
        //Checks if layout gotten matches actual game
        try {
            if(dotCounter != noTotalDots){throw new Exception("Dots layout not matching Game");}
            if(hLineCounter != noTotalInitialXlines){throw new Exception("Boxes layout not matching Game");}
            if(vLineCounter != noTotalInitialXlines){throw new Exception("Boxes layout not matching Game");}
            if(boxCounter != noTotalBoxes){throw new Exception("Boxes layout not matching Game");}
        }catch (Exception e){
            showToast(e.getMessage());
        }
        return finalLayout;
    }
    private void setPlayerColors(){
        if(totalPlayers == 2){
            //Sets 1st human player color to always equal to blue
            if(playersTurn){
                playerColors[0]=getResources().getString(R.string.P0Colour);
                playerColors[1]=getResources().getString(R.string.P1Colour);
            }
            else {
                playerColors[0] = getResources().getString(R.string.P1Colour);
                playerColors[1] = getResources().getString(R.string.P0Colour);
            }
        }else{
            for(int i=0; i<playerColors.length; i++) {
                //if >players than available colors, random them
                Random rnd = new Random();
                int color = Color.argb(200, rnd.nextInt(256), rnd.nextInt(256),
                        rnd.nextInt(256));
                playerColors[i] = Integer.toString(color);
            }
            //sets 1st human to p0 & 1st pc to p1 always

            if(localPlayers > 0){
                Arrays.sort(playerTurns);
                playerColors[playerTurns[0]] = getResources().getString(R.string.P0Colour);
            }if (noPcPlaying > 0){
                Arrays.sort(PCTurns);
                playerColors[PCTurns[0]] = getResources().getString(R.string.P1Colour);
            }
        }
    }

    /*************************** Aux methods Used in MakeBoard****************************/
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
        view = v;
        ImageViewAdded img = (ImageViewAdded) v;
        //On line clicked, upsocre, remove/update the line & change it's color then change turn
        if(playersTurn && !img.isSet()){
            time = System.currentTimeMillis();
            upScore(true, turn);
            removeLine(img);
            //TODO caller
            //activityCommander.GameClicked(score,turn);
            //System.out.println("Curren Turn = " + turn + ", isPCPlaying = " + pcPlaying + " " +
              //      ", isPCTurn" + isPCTurn() + ", PCTUrns " + PCTurns.toString());
            nextTurn();
        }else{showWaitToast();}
    }
    //Method just checks if cubes board
    private void checkCubeBoard() throws Exception {
        if(noHorizontalPlayableLinesLeft != noVerticalPlayableLinesLeft){
            throw new Exception(getString(R.string.exception_playable_horizontal_equal_vertical));
        }else if((noHorizontalPlayableLinesLeft + noVerticalPlayableLinesLeft) != noPlayableLinesLeft){
            throw new Exception(getString(R.string.exception_playable_equal_remianing));
        }
    }

    /*********Clicker Extensions/Gameplay**************/
    private void upScore(Boolean line, int player)  {
        int lineScore = getResources().getInteger(R.integer.lineScore);
        int boxScore = getResources().getInteger(R.integer.boxScore);
        if(line){ this.score[player]+=lineScore; }
        else { this.score[player]+= boxScore; }
        if(localPlayers > 0){
            if(player == playerTurns[0]) {
                activityCommander.updatePlayerScore(this.score[player]);
            }
        }
    }
    private void downScore(Boolean line, int player)  {
        int lineScore = getResources().getInteger(R.integer.lineScore);
        int boxScore = getResources().getInteger(R.integer.boxScore);
        if(line){ this.score[player]-=lineScore; }
        else { this.score[player]-= boxScore; }
    }
    //Meathod Removes the line & updates it based off players colors, also calls to check if box set
    private void removeLine(ImageViewAdded img) {
        try {
            boolean isHorizontalLine = isHorizontalLine(img);
            img.setSet(true);
            img.setClickable(false);
            noPlayableLinesLeft--;
            if(isHorizontalLine){
                noHorizontalPlayableLinesLeft--;
                horziontalLinesLeft.remove(img);
                moves.add(img);
                switchLineColor(playerColors[turn],img,true);
            }else{
                noVerticalPlayableLinesLeft--;
                verticalLinesLeft.remove(img);
                moves.add(img);
                switchLineColor(playerColors[turn],img,false);
            }
            setFilledBox(img);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), R.string.exception_toast_pc_unable_line,Toast.LENGTH_SHORT).show();
        }
    }
    //Auto getting line colors based off inputs, allows for easy upgrades
    private void switchLineColor(String r, ImageViewAdded img, Boolean isHorizontal){
        int line;
        if(isHorizontal){
            switch (r){
                case "blue": line = R.drawable.blueHorizontalDrawable; img.setImageResource(line); break;
                case "red": line = R.drawable.redHorizontalDrawable; img.setImageResource(line); break;
                case "blank": line = R.drawable.blankHorizontalDrawable; img.setImageResource(line); break;
                case "lightBlue": line = R.drawable.lightblueHorizontalDrawable; img.setImageResource(line); break;
                case "yellow": line = R.drawable.yellowHorizontalDrawable; img.setImageResource(line); break;
                case "pink": line = R.drawable.pinkHorizontalDrawable; img.setImageResource(line); break;
                case "orange": line = R.drawable.orangeHorizontalDrawable; img.setImageResource(line); break;
                default: line = R.drawable.redHorizontalDrawable;
                    img.setImageResource(line);
                    img.setColorFilter(Integer.parseInt(r),PorterDuff.Mode.SRC_ATOP);
                    //System.out.println("color in int = "+ line);
            }
        }else{
            switch (r){
                case "blue": line = R.drawable.blueVerticalDrawable; img.setImageResource(line); break;
                case "red": line = R.drawable.redVerticalDrawable;img.setImageResource(line); break;
                case "blank": line = R.drawable.blankVerticalDrawable; img.setImageResource(line); break;
                case "lightBlue": line = R.drawable.lightblueVerticalDrawable; img.setImageResource(line); break;
                case "yellow": line = R.drawable.yellowVerticalDrawable; img.setImageResource(line); break;
                case "pink": line = R.drawable.pinkVerticalDrawable; img.setImageResource(line); break;
                case "orange": line = R.drawable.orangeVerticalDrawable; img.setImageResource(line); break;
                default: line = R.drawable.redVerticalDrawable;
                    img.setImageResource(line);
                    img.setColorFilter(Integer.parseInt(r), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }
    //Method increments the turn
    private void nextTurn(){
        if(haveTurns()){
            this.turn++;
            if(turn >= totalPlayers){ this.turn = 0;}
            activityCommander.updateTurn(whoiseTurn(turn));
            nextPlayer();
        }else{ showToast("GAME OVER!"); }
    }
    //Method Changes Players
    private void nextPlayer(){
        System.out.println("****Players turn =  " + playersTurn + ", newturn & current =" +turn
                +", PCTurns = " + Arrays.toString(PCTurns));
        playersTurn = !isPCTurn();
        System.out.println("****Players turn =  " + playersTurn + ", newturn & current =" +turn
                +", PCTurns = " + Arrays.toString(PCTurns));
        if(!playersTurn){
            if(pcPlaying && isPCTurn()){
                //Add delay so not immediate & user sees it
                new CountDownTimer(delay, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) { }
                    @Override
                    public void onFinish() {
                        computersTurn();
                        nextTurn();
                    }
                }.start();
            }
            //TODO Online players tiemout/send reminders
            //Else do nothing, but in future to add funtionality to timeout online player, send
            // reminders etc & end game if too long etc
            else{}
        }
    }

    /****************PC Gameplay Methods****************/
    //Method is for PC/AI gameplay
    private void computersTurn() {
        ImageViewAdded randomLine = randomLine();
        upScore(true,turn);
        removeLine(randomLine);
    }
    //Method picks a remaining line at random
    private ImageViewAdded randomLine() {
        ImageViewAdded chooseLine;
        final Random random = new Random();
        final int min = 0;
        boolean horizontal = (Math.random()<0.5);
        if(noHorizontalPlayableLinesLeft == 0){ horizontal=false; }
        else if(noVerticalPlayableLinesLeft == 0){ horizontal=true; }
        if(horizontal){
            chooseLine = horziontalLinesLeft.get(random.nextInt(horziontalLinesLeft.size()-min)+min);
        }else {
            chooseLine = verticalLinesLeft.get(random.nextInt(verticalLinesLeft.size()-min)+min);
        }
        return chooseLine;
    }
    //Method to check if line sets a box
    private void setFilledBox(ImageViewAdded pcLine) {
        ImageViewAdded line;
        int row = 0, column = 0;
        boolean horizontal;
        if(playersTurn){ line = (ImageViewAdded) view; }
        else { line = pcLine; }

        boolean t = false;
        for (int i = 0; i < layoutInArray.length; i++) {
            for (int j = 0; j < layoutInArray.length; j++) {
                if (layoutInArray[i][j].getTag() == line.getTag()) {
                    column = i;
                    row = j;
                    break;
                }
            }
        }
        //Row spreads horizontall so its the vertival 1, colums spreads vertically so its out is
        // the horizontal one
        try {
            horizontal = isHorizontalLine(line);
            if (horizontal) {
                if (column == 0) {
                    if(topLines(column,row)){ topLinesBox(column,row);
                        upScore(false, turn);}
                }
                else if (column == layoutInArray.length-1) {
                    if(bottomLines(column,row)){bottomLinesBox(column,row); upScore(false, turn); }
                }
                else {
                    if(topLines(column,row)) { topLinesBox(column,row); upScore(false, turn);}
                    if(bottomLines(column,row)) { bottomLinesBox(column,row); upScore(false, turn);}
                }
            } else {
                //Vertical Lines only
                if (row == 0) {
                    if(beforeLines(column,row)){ beforeLinesBox(column,row); upScore(false, turn); }
                }
                else if (row == layoutInArray.length-1) {
                    if(afterLines(column,row)){ afterLinesBox(column,row); upScore(false, turn); }
                }
                else {
                    if (beforeLines(column,row)){ beforeLinesBox(column,row); upScore(false, turn); }
                    if (afterLines(column,row)){ afterLinesBox(column,row); upScore(false, turn); }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void hint(){
        try {
            ImageViewAdded c = randomLine();
            animateLine(c,isHorizontalLine(c));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), R.string.exception_toast_pc_unable_line,Toast.LENGTH_SHORT).show();
        }
    }
    public void undo(){
        ImageViewAdded last = moves.get(moves.size()-1);
        boolean isHorizontal = false;
        try {
            isHorizontal = isHorizontalLine(last);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(isHorizontal){ verticalLinesLeft.add(last);}
        else {horziontalLinesLeft.add(last);}
        moves.remove(moves.size()-1);
        turn--;
        downScore(isHorizontal,turn);
        nextPlayer();
    }

    /*************Meathods used in checking if lines near box*************/
    private boolean bottomLines(int column, int row) {
        ImageViewAdded a = layoutInArray[column-1][row+1];
        ImageViewAdded b = layoutInArray[column-1][row-1];
        ImageViewAdded c =  layoutInArray[column-2][row];
        ImageViewAdded[] check = {a,b,c};
        //animateCheck(check);
        boolean set = isBoxCreated(check);
        return set;
    }
    private boolean topLines(int column, int row) {
        ImageViewAdded a = layoutInArray[column+1][row+1];
        ImageViewAdded b = layoutInArray[column+1][row-1];
        ImageViewAdded c =  layoutInArray[column+2][row];
        ImageViewAdded[] check = {a,b,c};
        //animateCheck(check);
        boolean set = isBoxCreated(check);
        return set;
    }
    private boolean beforeLines(int column, int row) {
        ImageViewAdded a = layoutInArray[column+1][row+1];
        ImageViewAdded b = layoutInArray[column-1][row+1];
        ImageViewAdded c =  layoutInArray[column][row+2];
        ImageViewAdded[] check = {a,b,c};
        //animateCheck(check);
        boolean set = isBoxCreated(check);
        return set;
    }
    private boolean afterLines(int column, int row) {
        ImageViewAdded a = layoutInArray[column+1][row-1];
        ImageViewAdded b = layoutInArray[column-1][row-1];
        ImageViewAdded c =  layoutInArray[column][row-2];
        ImageViewAdded[] check = {a,b,c};
        //animateCheck(check);
        boolean set = isBoxCreated(check);
        return set;
    }
    private boolean isBoxCreated(ImageViewAdded[] lines){
        ImageViewAdded a = lines[0];
        ImageViewAdded b = lines[1];
        ImageViewAdded c = lines[2];
        boolean set = a.isSet() && b.isSet() && c.isSet();
        return set;
    }
    //These methods set the box
    private void setBox(ImageViewAdded box){
        if(localPlayers > 0 && turn == playerTurns[0]){
            box.setImageResource(R.drawable.blueBoxDrawable);
        }else if (noPcPlaying > 0 && turn == PCTurns[0]){
            box.setImageResource(R.drawable.redBoxDrawable);
        }else{
            box.setImageResource(R.drawable.redBoxDrawable);
            box.setColorFilter(Integer.parseInt(playerColors[turn]),PorterDuff.Mode.SRC_ATOP);
        }
    }
    private void topLinesBox(int column, int row){
        ImageViewAdded box = layoutInArray[column+1][row];
        setBox(box);
    }
    private void bottomLinesBox(int column, int row){
        ImageViewAdded box = layoutInArray[column-1][row];
        setBox(box);
    }
    private void beforeLinesBox(int column, int row){
        ImageViewAdded box = layoutInArray[column][row+1];
        setBox(box);
    }
    private void afterLinesBox(int column, int row){
        ImageViewAdded box = layoutInArray[column][row-1];
        setBox(box);
    }
    private void animateLine(final ImageViewAdded i, boolean horizontal){
        final AnimationDrawable animation = new AnimationDrawable();
        final Drawable original = i.getDrawable();
        animation.addFrame(original,100);
        if(horizontal){
            animation.addFrame(getResources().getDrawable(R.drawable.yellowHorizontalDrawable,null),200);
        }else {
            animation.addFrame(getResources().getDrawable(R.drawable.yellowVerticalDrawable,null),200);
        }
        animation.addFrame(original,100);
        animation.setOneShot(false);
        i.setImageDrawable(animation);
        //Block updates to ensure right update after animatiion. Block updates for period of
        // animation
        i.setClickable(false);
        animation.start();
        //Stop animation after x seconds & set back to original
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animation.stop();
                i.setImageDrawable(original);
                i.setClickable(true);
                //refreshing.clearAnimation();
            }
        }, 3000);
    }

    /********************Aux Meathods**********/
    //Method that show waiting toast
    private void showWaitToast(){ String s = getString(R.string.toast_wait_next_player); showToast(s); }
    // Method to show toast of string input
    private void showToast(String s){ Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show(); }
    //Method to check if line is horizontal
    private boolean isHorizontalLine(ImageViewAdded img) throws Exception {
        if(img.getTag().toString().contains(hLineTag)){
            return true;
        }else if(img.getTag().toString().contains(vLineTag)){
            return false;
        }else{
            throw new Exception("Unknown Line");
        }
    }
    //Method gets line tag no, returning only its no.
    private String getLineTagNo(ImageViewAdded line,Boolean isHorizontal){
        if(isHorizontal){
            return line.getTag().toString().replace(hLineTag,"");
        }else{
            return line.getTag().toString().replace(vLineTag,"");
        }
    }
    //Method checks if turn is PCsTrun
    private boolean isPCTurn(){
        for (int pcPlayer : PCTurns) {
            if (turn == pcPlayer) { return true;}
        } return false;
    }
    //Method checks if any more turns left
    private boolean haveTurns(){ return (noPlayableLinesLeft !=0); }
    protected String whoiseTurn(int turn) {
        if(isPCTurn()) {
            if(turn ==0){ return "Turn: Computer 01"+ " ("+turn+")";}
            else if(turn < 10){ return "Turn: Computer 0"+ turn + " ("+turn+")";}
            else{ return "Turn: Computer "+turn + " ("+turn+")";}
        }else{
            if(localPlayers > 0){
            for(int i=0; i< playerTurns.length; i++){
                    if(playerTurns[i] == turn ){
                        if(turn ==0){ }
                        if(i<10){ return "Turn: Player 01"+" ("+turn+")"; }
                        else { return "Turn: Player "+i + " ("+turn+")"; }
                    }
                }
            }
        }
       return "Turn: Unknowned "+turn + " ("+turn+")";
    }

    /***********Getters & Setters **********8*/
    public void setPlayersTurn(boolean playersTurn) { this.playersTurn = playersTurn; }
    public int getBoardSize() { return boardSize; }
    public void setBoardSize(int boardSize) { this.boardSize = boardSize;}
    public boolean isPlayersTurn() { return playersTurn; }
    public int getTurn() { return turn; }
    public void setTurn(int turn) { this.turn = turn;}
    public int getLocalPlayers() { return localPlayers; }
    public void setLocalPlayers(int localPlayers) { this.localPlayers = localPlayers;}
    public int getNoPcPlaying() { return noPcPlaying; }
    public void setNoPcPlaying(int noPcPlaying) { this.noPcPlaying = noPcPlaying; }
    public int[] getPCTurns() { return PCTurns; }
    public void setPCTurns(int[] PCTurns) { this.PCTurns = PCTurns; }
    public int[] getPlayerTurns() { return playerTurns; }
    public void setPlayerTurns(int[] playerTurns) { this.playerTurns = playerTurns; }
    public int[] getScore() { return score; }
    public void setScore(int[] score) { this.score = score; }
    public boolean isPcPlaying() { return pcPlaying; }
    public void setPcPlaying(boolean pcPlaying) { this.pcPlaying = pcPlaying; }
    public int getDelay() { return delay; }
    public void setDelay(int delay) { this.delay = delay; }
    public boolean isRandomTurns() { return randomTurns; }
    public void setRandomTurns(boolean randomTurns) { this.randomTurns = randomTurns; }

    /**********Test Meathods*****************/
    public void testPrintLayout(){
        System.out.println(layoutInArray[0][0].getTag()+","+layoutInArray[0][1].getTag()+","+layoutInArray[0][2].getTag()+ ","+layoutInArray[0][3].getTag()+","+layoutInArray[0][4].getTag()+","+layoutInArray[0][5].getTag()+","+layoutInArray[0][6].getTag());
        System.out.println(layoutInArray[1][0].getTag()+","+layoutInArray[1][1].getTag()+","+layoutInArray[1][2].getTag()+","+layoutInArray[1][3].getTag()+","+layoutInArray[1][4].getTag()+","+layoutInArray[1][5].getTag()+","+layoutInArray[1][6].getTag());
        System.out.println(layoutInArray[2][0].getTag()+","+layoutInArray[2][1].getTag()+","+layoutInArray[2][2].getTag()+","+layoutInArray[2][3].getTag()+","+layoutInArray[2][4].getTag()+","+layoutInArray[2][5].getTag()+","+layoutInArray[2][6].getTag());
        System.out.println(layoutInArray[3][0].getTag()+","+layoutInArray[3][1].getTag()+","+layoutInArray[3][2].getTag()+","+layoutInArray[3][3].getTag()+","+layoutInArray[3][4].getTag()+","+layoutInArray[3][5].getTag()+","+layoutInArray[3][6].getTag());
        System.out.println(layoutInArray[4][0].getTag()+","+layoutInArray[4][1].getTag()+","+layoutInArray[4][2].getTag()+","+layoutInArray[4][3].getTag()+","+layoutInArray[4][4].getTag()+","+layoutInArray[4][5].getTag()+","+layoutInArray[4][6].getTag());
        System.out.println(layoutInArray[5][0].getTag()+","+layoutInArray[5][1].getTag()+","+layoutInArray[5][2].getTag()+","+layoutInArray[5][3].getTag()+","+layoutInArray[5][4].getTag()+","+layoutInArray[5][5].getTag()+","+layoutInArray[5][6].getTag());
        System.out.println(layoutInArray[6][0].getTag()+","+layoutInArray[6][1].getTag()+","+layoutInArray[6][2].getTag()+","+layoutInArray[6][3].getTag()+","+layoutInArray[6][4].getTag()+","+layoutInArray[6][5].getTag()+","+layoutInArray[6][6].getTag());
    }
    //Aux method used to animate lines to check if cheking right lines onClick, not used in game
    private void animateCheck (ImageViewAdded[] in){
        try {
            for (ImageViewAdded i: in){
                animateLine(i, isHorizontalLine(i));
                animateLine(i, isHorizontalLine(i));
                animateLine(i, isHorizontalLine(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



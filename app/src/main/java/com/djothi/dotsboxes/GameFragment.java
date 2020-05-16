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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


public class GameFragment extends Fragment {

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
    private int noPcPlaying;
    private int PCTurns[];
    private int playerTurns[];
    private int onlinePlayers;
    private int localPlayers;
    private int totalPlayers;
    //Gameplay
    private int score[];
    private long time;
    private String hLineTag;
    private String vLineTag;
    private String playerColors[];
    private boolean customP1color;
    //Background
    private ImageViewAdded[][] layoutInArray;
    private List<ImageViewAdded> verticalLinesLeft;
    private List<ImageViewAdded> horziontalLinesLeft;
    private List<ImageViewAdded> moves = new ArrayList<ImageViewAdded>();
    private GameListener activityCommander;
    private View view;

    public GameFragment() {
    }

    public interface GameListener{
        public void GameClicked(int[] score, int turn);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            activityCommander = (GameListener) getActivity();
        }catch (ClassCastException e ){
//            throw new ClassCastException(getActivity().toString() + "Must implement fragment " +
  //                  "interface");
        }
      /*  //TODO Fragment Detach/Attach
        **********Attaching & Detaching fragment for updates*************
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment4);
        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        fragTransaction.detach(currentFragment);
        //GameFragment g = new GameFragment();
        CreateGameBoard g2 = (CreateGameBoard) new CreateGameBoard();
        g2.makeBoard(layout);
        GameFragment g =(GameFragment) g2;
        fragTransaction.attach(currentFragment);
        fragTransaction.commit();*/
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        this.view =inflater.inflate(R.layout.fragment_game, container, false);
        LinearLayout layout = view.findViewById(R.id.linear2);
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
        this.boardSize = getResources().getInteger(R.integer.boardSize);
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
            showToast("BoardLayout Errors");
        }
        makeBoard(layout);
        layoutInArray =  getLayoutArray();
        //testPrintLayout();
        // Inflate the layout for this fragment
        if(!playersTurn){
            nextPlayer();
        }
        return view;
    }
    // Method makes the board - Build O for auto-setting element IDs
    @RequiresApi(api = Build.VERSION_CODES.O)

    /*********Create Environment***************************/
    private void makeBoard(LinearLayout layout) {
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
        boolean done;
        int pcTurn, count = 0, min = 0;
        if(pcPlaying) {
            for (int i = 0; i < PCTurns.length; i++) {
                Random r = new Random();
                do {
                    count++;
                    if(playersTurn) {min=1;}
                    System.out.println(totalPlayers +" " + min);
                    pcTurn = r.nextInt(totalPlayers - min) + min;
                    done = true;
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
                } while (!done && count<100);
                if (count >= 100) {
                    throw new Exception("Cant find new Rand No. for PC");
                }
                PCTurns[i] = pcTurn;
            }

        }

        playerTurns = new int[localPlayers];
        int playercount = 0;
        boolean isAPlyaerTurn = false;
        for (int i=0; i<totalPlayers; i++){
            for(int j=0; j<PCTurns.length; j++){
                if (PCTurns[j] == i){
                    isAPlyaerTurn = false;
                }
            }
            if(isAPlyaerTurn){
                playerTurns[playercount] = i;
                playercount++;
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
    private void customP1Color(){
        //TODO custom P!
        playerColors[0]=getResources().getString(R.string.P0Colour);
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
                //color = Color.RED;
                //System.out.println("Ininital color in int = "+ color);
                playerColors[i] = Integer.toString(color);
                //System.out.println("playaercolor["+i+"] = " + playerColors[i]);
            }
            Arrays.sort(playerTurns);
            Arrays.sort(PCTurns);
            playerColors[playerTurns[0]] = getResources().getString(R.string.P0Colour);
            playerColors[PCTurns[0]] = getResources().getString(R.string.P1Colour);
        }
        //sets 1st human to p0 & 1st pc to p1
        if(customP1color){
            customP1Color();
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
        ImageViewAdded img = (ImageViewAdded) v;
        boolean isHorizontalLine = false;
        try {
            isHorizontalLine = isHorizontalLine(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //On line clicked, upsocre, remove/update the line & change it's color then change turn
        if(playersTurn && !img.isSet()){
            time = System.currentTimeMillis();
            upScore(true, turn);
            removeLine(v,img,isHorizontalLine);
            //TODO caller
            //activityCommander.GameClicked(score,turn);
            //System.out.println("Curren Turn = " + turn + ", isPCPlaying = " + pcPlaying + " " +
              //      ", isPCTurn" + isPCTurn() + ", PCTUrns " + PCTurns.toString());
            nextTurn();
        }else{showWaitToast();}
    }
    //Method just chekes if cubes board
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
    }
    private void downScore(Boolean line, int player)  {
        int lineScore = getResources().getInteger(R.integer.lineScore);
        int boxScore = getResources().getInteger(R.integer.boxScore);
        if(line){ this.score[player]-=lineScore; }
        else { this.score[player]-= boxScore; }
    }
    //Meathod Removes the line & updates it based off players colors, also calls to check if box set
    private void removeLine(View v,ImageViewAdded img, Boolean isHorizontal) {
        img.setSet(true);
        img.setClickable(false);
        noPlayableLinesLeft--;
        if(isHorizontal){
            noHorizontalPlayableLinesLeft--;
            horziontalLinesLeft.remove(img);
            moves.add(img);
            switchLineColor(playerColors[turn],img,true);
        }else{
            noVerticalPlayableLinesLeft--;
            verticalLinesLeft.remove(img);
            moves.add(img);
            //System.out.println(turn);
            switchLineColor(playerColors[turn],img,false);
        }
        setFilledBox(v,img);
    }
    //Auto getting line colors based off inputs, allows for easy upgrades
    private void switchLineColor(String r, ImageViewAdded img, Boolean isHorizontal){
        int line;
        if(isHorizontal){
            switch (r){
                //case "blue": line = R.drawable.blueHorizontalDrawable; break;
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
                    //System.out.println("color in int = "+ line);
            }
        }
    }
    //Method increments the turn
    private void nextTurn(){
        if(haveTurns()){
            turn++;
            if(turn >= totalPlayers){ this.turn = 0;}
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
                new CountDownTimer(getResources().getInteger(R.integer.PCDelay), 1000) {
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
        ImageViewAdded c = randomLine();
        upScore(true,turn);
        boolean isHorizontal = false;
        try {
            isHorizontal = isHorizontalLine(c);
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), R.string.exception_toast_pc_unable_line,Toast.LENGTH_SHORT).show();
        }
        removeLine(view,c,isHorizontal);
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
    private void setFilledBox(View v, ImageViewAdded pcLine) {
        ImageViewAdded line;
        int row = 0, column = 0;
        boolean horizontal = true;
        if(playersTurn){ line = (ImageViewAdded) v; }
        else { line = pcLine; }

        //layout [row][column], gets the clicked line row/colum values]
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
    private void hint(View v){
        try {
            ImageViewAdded c = randomLine();
            animateLine(randomLine(),isHorizontalLine(c));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), R.string.exception_toast_pc_unable_line,Toast.LENGTH_SHORT).show();
        }
    }
    private void undo(View v){
        ImageViewAdded last = moves.get(moves.size());
        boolean isHorizontal = false;
        try {
            isHorizontal = isHorizontalLine(last);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(isHorizontal){ verticalLinesLeft.add(last);}
        else {horziontalLinesLeft.add(last);}
        moves.remove(moves.size());
        turn--;
        downScore(isHorizontal,turn);
        nextPlayer();
    }

    //TODO Animate
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
        if(turn == playerTurns[0]){
           box.setImageResource(R.drawable.blueBoxDrawable);
        }else if (turn == PCTurns[0]){
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
        animation.start();
        //Stop animation after x seconds & set back to original
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animation.stop();
                i.setImageDrawable(original);
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

    /***********Getters & Setters **********8*/
    public void setPlayersTurn(boolean playersTurn) { this.playersTurn = playersTurn; }
    public int getBoardSize() { return boardSize; }
    public void setBoardSize(int boardSize) { this.boardSize = boardSize;}
    public boolean isPlayersTurn() { return playersTurn; }
    public int getTurn() { return turn; }
    public void setTurn(int turn) { this.turn = turn;}
    public int getLocalPlayers() { return localPlayers; }
    public void setLocalPlayers(int localPlayers) { this.localPlayers = localPlayers;}


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



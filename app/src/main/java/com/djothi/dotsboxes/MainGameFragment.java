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
import android.util.Log;
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

    //For Gameplay Settings
    private int boardSize;
    private int currentTurn;
    private int delay;
    private String hLineTag;
    private String vLineTag;
    //Booleans - Info Values
    private boolean isPlayersTurn;
    private boolean isPcPlaying;
    private boolean isRandomTurnsOn;
    //Total values - Gameplay
    private int noPcPlaying;
    private int noOnlinePlayers;
    private int noLocalPlayers;
    private int noTotalPlayers;
    //Total Objects
    private int noTotalPlayableSides;
    private int noTotalInitialHorizontallines;
    private int noTotalInitialVerticallines;
    private int noPlayableLinesLeft;
    private int noHorizontalPlayableLinesLeft;
    private int noVerticalPlayableLinesLeft;
    private int noTotalDots;
    private int noTotalBoxes;
    //Gameplay
    private int[] score;
    private int[] pcTurns;
    private int[] playerTurns;
    private String[] playerColors;
    //Accessibility
    private ImageViewAdded[][] layoutInArray;
    private List<ImageViewAdded> verticalLinesLeft;
    private List<ImageViewAdded> horziontalLinesLeft;
    private List<ImageViewAdded> boxesLeft;
    private List<ImageViewAdded> movesDone = new ArrayList<>();
    /*Main*/
    private View view;
    private LinearLayout layout;
    private GameListener activityCommander;


    //Blank - Constructor
    public MainGameFragment() {
    }
    //Interface (requirementS)
    public interface GameListener{
        void updateTurn(String turn);
        void updatePlayerScore(int score);
        void updatePlayerName(String s);
        void updateAIName(String s);
        void updateAIScore(int score);
        void updateOverallScore(String s);
        void disableButtons();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
             activityCommander = (GameListener) getActivity();
        }catch (ClassCastException e ){
            throw new ClassCastException(requireActivity().toString() +
                    "    Must implement Game fragment interface");
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        this.view = inflater.inflate(R.layout.fragment_game, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*Getting internal data*/
        Log.i("LineUp","**Setting internal data!**");
        //System.out.println();
        assert getArguments() != null;
        this.boardSize = getArguments().getInt("grid");
        this.noLocalPlayers = getArguments().getInt("human");
        this.isPlayersTurn = getArguments().getBoolean("p1Starts");
        if(noLocalPlayers == 0) { isPlayersTurn = false;}
        this.noPcPlaying = getArguments().getInt("AI");
        this.isPcPlaying = noPcPlaying != 0;
        this.noOnlinePlayers =  getResources().getInteger(R.integer.onlinePlayers);
        this.noTotalPlayers = noLocalPlayers + noOnlinePlayers + noPcPlaying;
        if(getArguments().getBoolean("quickMode")){this.delay = 0;} else {this.delay = 1000;}
        this.isRandomTurnsOn = getArguments().getBoolean("randomTurns");
        Log.i("LineUp","Info from Bundle:"
                + "\nBoardSize: "+ boardSize
                + "\nnoLocalPlayers: "+ noLocalPlayers
                + "\nisPlayersTurn: "+ isPlayersTurn
                + "\nisPcPlaying: "+ isPcPlaying
                + "\nnoPcPlaying: "+ noPcPlaying
                + "\nnoOnlinePlayers: "+ noOnlinePlayers
                + "\nnoTotalPlayers: "+ noTotalPlayers
                + "\ndelay: "+ delay
                + "\nisRandomTurnsOn: "+ isRandomTurnsOn
        );
        Setup();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void Setup(){
        //Get Settings from resources
        this.layout = view.findViewById(R.id.linear2);
        this.layout.removeAllViews();
        this.currentTurn = getResources().getInteger(R.integer.turnStart);
        this.score = new int[noTotalPlayers];
        this.playerColors = new String[noTotalPlayers];
        for(int i: score){ i = getResources().getInteger(R.integer.initialScore); }
        this.noTotalPlayableSides = (boardSize*2*(boardSize+1));
        this.noPlayableLinesLeft = noTotalPlayableSides;
        this.noHorizontalPlayableLinesLeft = noTotalPlayableSides /2;
        this.noVerticalPlayableLinesLeft = noTotalPlayableSides - noHorizontalPlayableLinesLeft;
        //Private Settings:
        this.noTotalInitialHorizontallines = (int) (Math.pow(boardSize,2)+boardSize);
        this.noTotalInitialVerticallines = noTotalPlayableSides - noTotalInitialHorizontallines;
        this.noTotalBoxes = (int) Math.pow(boardSize,2);
        this.pcTurns = new int[noPcPlaying];
        this.playerTurns = new int[noLocalPlayers];
        this.hLineTag = getResources().getString(R.string.horizontalLineTagChecker);
        this.vLineTag = getResources().getString(R.string.verticalLineTagChecker);
        //Pring Log:
        Log.i("LineUp","Info from Setup:"
                + "\ncurrentTurn: "+ currentTurn
                + "\nscoreLength: "+ score.length
                + "\nscore: "+ Arrays.toString(score)
                + "\nplayerColorsLength: "+ playerColors.length
                + "\nplayerColors: "+ Arrays.toString(playerColors)
                + "\nnoTotalPlayableSides: "+ noTotalPlayableSides
                + "\nnoHorizontalPlayableLinesLeft: "+ noHorizontalPlayableLinesLeft
                + "\nnoVerticalPlayableLinesLeft: "+ noVerticalPlayableLinesLeft
                + "\nnoTotalInitialHorizontallines: "+ noTotalInitialHorizontallines
                + "\nnoTotalInitialVerticallines: "+ noTotalInitialVerticallines
                + "\nnoTotalBoxes: "+ noTotalBoxes
                + "\npcTurnsLength: "+ pcTurns.length
                + "\npcTurns: "+ Arrays.toString(pcTurns)
                + "\nplayerTurnsLength: "+ playerTurns.length
                + "\nplayerTurns: "+ Arrays.toString(playerTurns)
                + "\nhLineTag: "+ hLineTag
                + "\nvLineTag: "+ vLineTag
        );
        //Setting Up:
        try { calPlayers(); }
        catch (Exception e) { e.printStackTrace(); }
        setPlayerColors();
        makeBoard();
        Log.i("LineUp","Board Made");
        if(!isPlayersTurn){ nextPlayer(); }
        if(noPcPlaying == 0){
            if(noLocalPlayers == 1){ activityCommander.updateAIName(""); }
            else { activityCommander.updateAIName("Player 2"); }
        }
        updateScoreText();
    }

    /*********Create Environment***************************/
    // Method makes the board - Build O for auto-setting element IDs
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void makeBoard() {
        //For Setup
        int gridSize = (boardSize * 2) + 1;
        this.noTotalDots = (int) Math.pow((boardSize+1),2);
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
        ImageViewAdded[] imageViews_verticalLines = new ImageViewAdded[noTotalInitialHorizontallines];
        ImageViewAdded[] imageViews_horziontalLines = new ImageViewAdded[noTotalInitialHorizontallines];
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
            //If Even line, add dots & horizontal lines
            if(i%2 == 0){
                int count = 0;
                for(int j = 0; j< gridSize; j++){
                    /*Checks if max horizontal grid size reached
                     * If not add a dot & increments then check again, if not add a line*/
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
                     * If not add a line & increments then check again, if not add a box*/
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
        //Copy array for finding specific lines later
        this.horziontalLinesLeft = new ArrayList<>(Arrays.asList(Arrays.copyOf(imageViews_horziontalLines,
                imageViews_horziontalLines.length)));
        this.verticalLinesLeft = new ArrayList<>(Arrays.asList(Arrays.copyOf(imageViews_verticalLines,
                imageViews_verticalLines.length)));
        this.layoutInArray =  getLayoutArray();
    }
    //Method calculates the number of players & PCs & set accordingly. Sets random PC turns too
    //Players & PC may have overlapping turns if randomTurns is on
    private void calPlayers() {
        boolean done;
        int pcTurn, min = 0;
        if(isPcPlaying) {
            if(isRandomTurnsOn){
                for (int i = 0; i < pcTurns.length; i++) {
                    Random r = new Random();
                    do {
                        if(isPlayersTurn) {min=1;}
                        pcTurn = r.nextInt(noTotalPlayers - min) + min;
                        done = true;
                        if(isPlayersTurn && noTotalPlayers == 2 && i==0){pcTurn=1;}
                        if(!isPlayersTurn && noTotalPlayers ==2 && i==0){pcTurn=0;}
                        //check that new turn is not equal to an older one.
                        if(i>0){
                            for(int j=0; j<i; j++){
                                if(pcTurn == pcTurns[j]){
                                    done=false;
                                    break;
                                }
                            }
                        }
                    } while (!done);
                    pcTurns[i] = pcTurn;
                }
            }else{
                int j;
                if(!isPlayersTurn){
                    for(int i=0; i<noPcPlaying; i++){ pcTurns[i] = i; }
                }else{
                    j = noLocalPlayers;
                    for(int i=0; i<noPcPlaying; i++){ pcTurns[i]=j+i; }
                }
            }
        }
        //Setting remaining turns as players
        int playerCount =0;
        if(noPcPlaying>0) {
            int[] register = new int[noTotalPlayers];
            for (int i : pcTurns) { register[i] = 1; }
            for (int i = 1; i < register.length; i++) {
                if (register[i] == 0) {
                    playerTurns[playerCount] = i;
                    playerCount++;
                }
            }
        }else{
            for(int i = 0; i< noLocalPlayers; i++){
                playerTurns[i] = i;
            }
        }
    }
    //Gets an Array of the exact board layout in table format
    private ImageViewAdded[][] getLayoutArray(){
        int linesPerDotGrid = (noTotalInitialHorizontallines - boardSize) / boardSize;
        int noDotGrids = noTotalInitialHorizontallines / linesPerDotGrid;
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
            if(hLineCounter != noTotalInitialHorizontallines){throw new Exception("Boxes layout not matching Game");}
            if(vLineCounter != noTotalInitialHorizontallines){throw new Exception("Boxes layout not matching Game");}
            if(boxCounter != noTotalBoxes){throw new Exception("Boxes layout not matching Game");}
        }catch (Exception e){
            e.printStackTrace();
        }
        return finalLayout;
    }
    //Sets Players Colors
    private void setPlayerColors(){
        if(noTotalPlayers == 2){
            //Sets 1st human player color to always equal to blue & next to red
            if(isPlayersTurn){
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
                int color = Color.argb(150, rnd.nextInt(256), rnd.nextInt(256),
                        rnd.nextInt(256));
                playerColors[i] = Integer.toString(color);
            }
            //sets 1st human to p0 & 1st pc to p1 always
            if(noLocalPlayers > 0){
                Arrays.sort(playerTurns);
                playerColors[playerTurns[0]] = getResources().getString(R.string.P0Colour);
            }if (noPcPlaying > 0){
                Arrays.sort(pcTurns);
                playerColors[pcTurns[0]] = getResources().getString(R.string.P1Colour);
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
        this.view = v;
        ImageViewAdded img = (ImageViewAdded) view;
        //On line clicked, upScore, remove/update the line & change it's color then change turn
        if(!isPCTurn(currentTurn)){ isPlayersTurn = true; }
        if(isPlayersTurn && !img.isSet()){
            upScore(true);
            removeLine(img);
            if(noPcPlaying == 0 && noLocalPlayers > 1){ activityCommander.updateAIScore(score[1]);}
            nextTurn();
        }else{showWaitToast();}
    }

    /*********Clicker Extensions/Gameplay**************/
    private void upScore(Boolean line)  {
        int lineScore = getResources().getInteger(R.integer.lineScore);
        int boxScore = getResources().getInteger(R.integer.boxScore);
        if(line){ this.score[currentTurn]+=lineScore; }
        else { this.score[currentTurn]+=boxScore; }
        if(noLocalPlayers > 0 && isPlayersTurn()) {
            activityCommander.updatePlayerScore(score[currentTurn]);
        }else if (noPcPlaying > 0 && isPCTurn(currentTurn)){
            activityCommander.updateAIScore(score[currentTurn]);
        }
        updateScoreText();
    }
    private void downScore(Boolean line)  {
        int lineScore = getResources().getInteger(R.integer.lineScore);
        int boxScore = getResources().getInteger(R.integer.boxScore);
        if(line){ score[currentTurn]-=lineScore; }
        else { score[currentTurn]-=boxScore; }
    }
    private void updateScoreText(){
        int topPlayer1 = 2, topPlayer2 =2, topPlayer3 =2;
        int topPlayer1Score=0, topPlayer2Score=0, topPlayer3Score=0;
        List<Integer> chosen = new ArrayList<>();
        chosen.clear();
        if(isPcPlaying){
            chosen.add(pcTurns[0]);
            if( noLocalPlayers == 0){ chosen.add(pcTurns[1]); }
        }
        if(noLocalPlayers > 0){
            chosen.add(playerTurns[0]);
            Log.i("LineUp","Chosen added p0 ");
            if(!isPcPlaying){ chosen.add(playerTurns[1]); Log.i(
                    "LineUp",
                    "Chosen added p2 ");}
        }
        StringBuilder listString = new StringBuilder();
        for (Integer s : chosen)
        { listString.append(s).append("\t"); }
        Log.i("LineUp","Chosen: "+ listString);
        for(int j=0; j<3; j++){
            for(int i=0; i<score.length; i++){
                if(j==0 && !chosen.contains(i) && score[i]>=topPlayer1Score){topPlayer1Score = score[i]; topPlayer1 = i; chosen.add(i);  break;}
                else if(j==1 && !chosen.contains(i) && score[i]>=topPlayer2Score){topPlayer2Score = score[i]; topPlayer2 = i; chosen.add(i); break;}
                else if(j==2 && !chosen.contains(i) && score[i]>=topPlayer3Score){topPlayer3Score = score[i]; topPlayer3 =i;chosen.add(i); break; }
            }
        }
        Log.i("LineUp", "topPlayer1 = " + topPlayer1
                + "\ntopPlayer2: "+ topPlayer2
                + "\ntopPlayer3: "+ topPlayer3
        );
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(whoseTurn(0, 2)).append(": ").append(score[0]).append("\n");
        for(int i=0; i<noTotalPlayers; i++){
            if(i == 1){ stringBuilder.append(whoseTurn(1, 2)).append(": ").append(score[1]).append("\n");}
            if(i == 2){ stringBuilder.append(whoseTurn(topPlayer1, 2)).append(": ").append(score[topPlayer1]).append("\n");}
            if(i == 3){ stringBuilder.append(whoseTurn(topPlayer2, 2)).append(": ").append(score[topPlayer2]).append("\n");}
            if(i == 4){ stringBuilder.append(whoseTurn(topPlayer3, 2)).append(": ").append(score[topPlayer3]).append("\n");}
        }
       activityCommander.updateOverallScore(stringBuilder.toString());
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
                movesDone.add(img);
                switchLineColor(playerColors[currentTurn],img,true);
            }else{
                noVerticalPlayableLinesLeft--;
                verticalLinesLeft.remove(img);
                movesDone.add(img);
                switchLineColor(playerColors[currentTurn],img,false);
            }
            setFilledBox(img, true);
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
            this.currentTurn++;
            if(currentTurn >= noTotalPlayers){ this.currentTurn = 0;}
            activityCommander.updateTurn(whoseTurn(currentTurn,0));
            nextPlayer();
        }else{ gameOver(); }
    }
    //Method Changes Players
    private void nextPlayer(){
        if(isPCTurn(currentTurn)){
            isPlayersTurn = !isPCTurn(currentTurn);
        }
        updateScoreText();
        if(isPlayersTurn()){
            activityCommander.updatePlayerName(whoseTurn(currentTurn,1));
            activityCommander.updatePlayerScore(score[currentTurn]);
        }else if(noPcPlaying > 0 && isPCTurn(currentTurn)){
            activityCommander.updateAIName(whoseTurn(currentTurn,1));
            activityCommander.updateAIScore(score[currentTurn]);
        }
        if(!isPlayersTurn){
            if(isPcPlaying && isPCTurn(currentTurn)){
                //Add delay so not immediate & user sees it
                new CountDownTimer(delay, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) { }
                    @Override
                    public void onFinish() {
                        computersTurn();
                        if(isPCTurn(currentTurn)){
                            nextTurn();
                        }
                    }
                }.start();
            }
            //TODO Online players tiemout/send reminders (else)
            //Else do nothing, but in future to add funtionality to timeout online player, send
            // reminders etc & end game if too long etc
        }
    }

    /****************PC Gameplay Methods****************/
    //Method is for PC/AI gameplay
    private void computersTurn() {
        if(isPCTurn(currentTurn)){
            ImageViewAdded randomLine = randomLine();
            upScore(true);
            removeLine(randomLine);
        }
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
    private void setFilledBox(ImageViewAdded pcLine,Boolean isNotUndo) {
        ImageViewAdded line;
        int row = 0, column = 0;
        boolean horizontal;
        if(!isNotUndo){ line = pcLine;}
        else if(isPlayersTurn){ line = (ImageViewAdded) view; }
        else { line = pcLine; }
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
                if (column == 0) { checkTopLine(column,row,isNotUndo); }
                else if (column == layoutInArray.length-1) { checkBottomLine(column,row,isNotUndo); }
                else {
                    checkTopLine(column,row,isNotUndo);
                    checkBottomLine(column,row,isNotUndo);
                }
            } else { //Vertical Lines only
                if (row == 0) { checkBeforeLine(column,row,isNotUndo); }
                else if (row == layoutInArray.length-1) { checkAfterLine(column,row,isNotUndo); }
                else {
                    checkBeforeLine(column,row,isNotUndo);
                    checkAfterLine(column,row,isNotUndo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void hint(){
        try {
            ImageViewAdded c = randomLine();
            animateLine(c,isHorizontalLine(c));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), R.string.exception_toast_pc_unable_line,Toast.LENGTH_SHORT).show();
        }
    }
    void undo(){
        if(!(movesDone.size() == 0)){
            ImageViewAdded last = movesDone.get(movesDone.size()-1);
            boolean isHorizontal = false;
            try { isHorizontal = isHorizontalLine(last); }
            catch (Exception e) { e.printStackTrace(); }
            if(isHorizontal){ horziontalLinesLeft.add(last); noHorizontalPlayableLinesLeft++;}
            else {verticalLinesLeft.add(last); noVerticalPlayableLinesLeft++;}
            noPlayableLinesLeft++;
            switchLineColor("blank",last,isHorizontal);
            currentTurn--;
            if (currentTurn == -1) { currentTurn = noTotalPlayers -1;}
            downScore(true);
            setFilledBox(last,false);
            last.setClickable(true);
            last.setSet(false);
            updateScoreText();
            activityCommander.updateTurn(whoseTurn(currentTurn,0));
            movesDone.remove(movesDone.size()-1);
            if(!isPCTurn(currentTurn)){
                activityCommander.updatePlayerName(whoseTurn(currentTurn,1));
                activityCommander.updatePlayerScore(score[currentTurn]);
            }else {
                activityCommander.updateAIName(whoseTurn(currentTurn,1));
                activityCommander.updateAIScore(score[currentTurn]);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nextPlayer();
                    }
                }, 2000);

            }
        }
    }
    private void gameOver(){
        activityCommander.disableButtons();
        int max =0, winner = 0;
        for(int i=0; i<score.length; i++){
            if(score[i] > max){
                max = score[i];
                winner = i;
            }
        }
        showToast("GAME OVER! \n Winner is "+ whoseTurn(winner,1) + "  with a score of " + max);
    }

    /*************Meathods used in checking if lines near box*************/
    private boolean bottomLines(int column, int row) {
        ImageViewAdded a = layoutInArray[column-1][row+1];
        ImageViewAdded b = layoutInArray[column-1][row-1];
        ImageViewAdded c =  layoutInArray[column-2][row];
        ImageViewAdded[] check = {a,b,c};
        //animateCheck(check);
        return isBoxCreated(check);
    }
    private boolean topLines(int column, int row) {
        ImageViewAdded a = layoutInArray[column+1][row+1];
        ImageViewAdded b = layoutInArray[column+1][row-1];
        ImageViewAdded c =  layoutInArray[column+2][row];
        ImageViewAdded[] check = {a,b,c};
        //animateCheck(check);
        return isBoxCreated(check);
    }
    private boolean beforeLines(int column, int row) {
        ImageViewAdded a = layoutInArray[column+1][row+1];
        ImageViewAdded b = layoutInArray[column-1][row+1];
        ImageViewAdded c =  layoutInArray[column][row+2];
        ImageViewAdded[] check = {a,b,c};
        //animateCheck(check);
        return isBoxCreated(check);
    }
    private boolean afterLines(int column, int row) {
        ImageViewAdded a = layoutInArray[column+1][row-1];
        ImageViewAdded b = layoutInArray[column-1][row-1];
        ImageViewAdded c =  layoutInArray[column][row-2];
        ImageViewAdded[] check = {a,b,c};
        //animateCheck(check);
        return isBoxCreated(check);
    }
    private boolean isBoxCreated(ImageViewAdded[] lines){
        ImageViewAdded a = lines[0];
        ImageViewAdded b = lines[1];
        ImageViewAdded c = lines[2];
        return a.isSet() && b.isSet() && c.isSet();
    }
    private void checkTopLine(int column, int row, boolean isNotUndo){
        if(topLines(column,row)){
            if(isNotUndo){
                upScore(false);
                topLinesBox(column,row,true);
            }
            else{
                downScore(false);
                topLinesBox(column,row,false);
            }
        }
    }
    private void checkBottomLine(int column, int row, boolean isNotUndo){
        if(bottomLines(column,row)){
            if(isNotUndo){
                upScore(false);
                bottomLinesBox(column,row,true);
            }
            else{
                downScore(false);
                bottomLinesBox(column,row,false);
            }
        }
    }
    private void checkBeforeLine(int column, int row, boolean isNotUndo){
        if(beforeLines(column,row)){
            if(isNotUndo){
                upScore(false);
                beforeLinesBox(column,row,true);
            }
            else{
                downScore(false);
                beforeLinesBox(column,row,false);
            }
        }
    }
    private void checkAfterLine(int column, int row, boolean isNotUndo){
        if(afterLines(column,row)){
            if(isNotUndo){
                upScore(false);
                afterLinesBox(column,row,true);
            }
            else{
                downScore(false);
                afterLinesBox(column,row,false);
            }
        }
    }
    private void topLinesBox(int column, int row, boolean isNotUndo){
        ImageViewAdded box = layoutInArray[column+1][row];
        if(isNotUndo){setBox(box,true);}
        else {setBox(box,false); }
    }
    private void bottomLinesBox(int column, int row, boolean isNotUndo){
        ImageViewAdded box = layoutInArray[column-1][row];
        if(isNotUndo){setBox(box,true);}
        else {setBox(box,false); }
    }
    private void beforeLinesBox(int column, int row, boolean isNotUndo){
        ImageViewAdded box = layoutInArray[column][row+1];
        if(isNotUndo){setBox(box,true);}
        else {setBox(box,false); }
    }
    private void afterLinesBox(int column, int row, boolean isNotUndo){
        ImageViewAdded box = layoutInArray[column][row-1];
        if(isNotUndo){setBox(box,true);}
        else {setBox(box,false); }
    }
    /*********************These methods set the box***************/
    private void setBox(ImageViewAdded box, boolean isNotUndo){
        if(isNotUndo){
            if(noLocalPlayers > 0 && currentTurn == playerTurns[0]){
                box.setImageResource(R.drawable.blueBoxDrawable);
            }else if (noPcPlaying > 0 && currentTurn == pcTurns[0]){
                box.setImageResource(R.drawable.redBoxDrawable);
            }else{
                box.setImageResource(R.drawable.redBoxDrawable);
                box.setColorFilter(Integer.parseInt(playerColors[currentTurn]),PorterDuff.Mode.SRC_ATOP);
            }
        }else {
            setBoxBlank(box);
        }
    }
    private void setBoxBlank(ImageViewAdded box){
        box.setImageResource(R.drawable.blankBoxDrawable);
        box.setColorFilter(Color.TRANSPARENT,PorterDuff.Mode.SRC_ATOP);
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
        //Block updates to ensure right update after animation. Block updates for period of
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
    /********************Aux Meathods*************************/
    //Method that show waiting toast
    private void showWaitToast(){ String s = getString(R.string.toast_wait_next_player); showToast(s); }
    // Method to show toast of string input
    private void showToast(String s){ Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show(); }
    //Method to check if line is horizontal
    private boolean isHorizontalLine(ImageViewAdded img) throws Exception {
        if(img.getTag().toString().contains(hLineTag)){ return true; }
        else if(img.getTag().toString().contains(vLineTag)){ return false; }
        else{ throw new Exception("Unknown Line"); }
    }
    //Method gets line tag no, returning only its no.
    private String getLineTagNo(ImageViewAdded line,Boolean isHorizontal){
        if(isHorizontal){ return line.getTag().toString().replace(hLineTag,""); }
        else{ return line.getTag().toString().replace(vLineTag,""); }
    }
    //Method checks if turn is PCsTrun
    private  boolean isPCTurn(int turn){
        if(noPcPlaying > 0){
            for (int pcPlayer : pcTurns) {
                if (turn == pcPlayer) { return true;}
            }
        }
        return false;
    }
    //Checks if there is anymore turns
    private boolean haveTurns(){ return (noPlayableLinesLeft !=0); }
    //Type 0 (turn): includes turn text & full,
    // Type 2(norm): full without turn text,
    // Type 3: Short Form
    private String whoseTurn(int turn, int type) {
        String[][] returnsTypes = new String[3][5];
        returnsTypes[0][0] = "Turn: Computer 1"+ " ("+turn+")";
        returnsTypes[1][0] = "Computer 1";
        returnsTypes[2][0] = "C1";
        if(isPcPlaying){
            returnsTypes[0][1] = "Turn: Computer "+(Arrays.binarySearch(pcTurns,turn)+1)+ " ("+turn+")";
            returnsTypes[1][1] = "Computer "+ (Arrays.binarySearch(pcTurns,turn)+1);
            returnsTypes[2][1] = "C"+ (Arrays.binarySearch(pcTurns,turn)+1);
        }
        returnsTypes[0][2] = "Turn: Player 1"+" ("+turn+")";
        returnsTypes[1][2] = "Player 1";
        returnsTypes[2][2] = "P1";
        if(noLocalPlayers > 0 ){
            returnsTypes[0][3] = "Turn: Player "+(Arrays.binarySearch(playerTurns,turn)+1)+" " + "("+turn+")";
            returnsTypes[1][3] = "Player "+(Arrays.binarySearch(playerTurns,turn)+1);
            returnsTypes[2][3] = "P"+(Arrays.binarySearch(playerTurns,turn)+1);
        }
        returnsTypes[0][4] = "Turn: Unknowned "+turn + " ("+turn+")";
        returnsTypes[1][4] = "Unknowned "+turn + " ("+turn+")";
        returnsTypes[2][4] = "Unk"+turn + " ("+turn+")";

        if(isPCTurn(turn)) {
            if(turn ==0){ return returnsTypes[type][0];}
            else{ return returnsTypes[type][1];}
        }else{
            if(noLocalPlayers > 0){
                for (int playerTurn : playerTurns) {
                    if (playerTurn == turn) {
                        if (turn == 0) { return returnsTypes[type][2]; }
                        else { return returnsTypes[type][3]; }
                    }
                }
            }
        }
        return returnsTypes[type][4];
    }

    /***********Getters & Setters **********8*/
    void setPlayersTurn(boolean playersTurn) { this.isPlayersTurn = playersTurn; }
    void setBoardSize(int boardSize) { this.boardSize = boardSize;}
    private boolean isPlayersTurn() { return isPlayersTurn; }
    void setNoLocalPlayers(int noLocalPlayers) { this.noLocalPlayers = noLocalPlayers;}
    void setNoPcPlaying(int noPcPlaying) { this.noPcPlaying = noPcPlaying; }
    void setPcPlaying(boolean pcPlaying) { this.isPcPlaying = pcPlaying; }
    void setDelay(int delay) { this.delay = delay; }
    void setRandomTurnsOn(boolean randomTurnsOn) { this.isRandomTurnsOn = randomTurnsOn; }

    /**********Test Meathods*****************/
    private void testPrintLayout(){
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



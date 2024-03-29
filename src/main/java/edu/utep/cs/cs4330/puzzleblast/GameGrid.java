package edu.utep.cs.cs4330.puzzleblast;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameGrid{

    private static List<Square> squares;
    private static int gridSize;
    private static int maxInitValues; // prevent too many values for squares at once
    private static int maxValue;
    private static boolean maxReached;
    private SquareGridAdapter adapter;
    private Context context;
    private GameDBHelper helper;
    private GameTimer timer;
    private static int filledSpaces;
    private Animation expandAnim;
    private Listener endGameListener;

    private static final int GAME_INCREMENT = 2;//base number for the game

    private GameGrid() {
        gridSize = 4;
        squares = new ArrayList<>();
        maxInitValues = 4;
        filledSpaces = 0;
        maxValue = 2048;
        maxReached = false;
    }
   //Listener for end game dialog
    public interface Listener{
        void endGameScore(long score);
    }

    private static final GameGrid INSTANCE = new GameGrid();

    public static GameGrid getInstance() {
        return INSTANCE;
    }

    /**
     * Adds new values to the empty list of squares
     */
    public synchronized void initSquares() {
        Thread thread = new Thread(() -> {
            int count = 0;
            for(int row = 0; row < gridSize;row++) {
                for(int col = 0; col < gridSize;col++) {
                    int random = randomValue();
                    Log.d("random value", String.valueOf(random));
                    if((row != 0 && col != 0) && (row != gridSize-1 && col != gridSize-1) || count < maxInitValues) {
                        //don't create values in middle of grid
                        Square square = new Square(row, col, 0, findImage(0));
                        squares.add(square);
                    }
                    else {
                        Square square = new Square(row, col, random, findImage(random));
                        squares.add(square);
                    }
                    if(random != 0) {
                        count++;
                        filledSpaces++;
                    }
                }
            }
            maxInitValues = 3;
            Activity act = (Activity)context;
            act.runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
            });
            Log.d("init square", "finished making list");
        });
        thread.start();
    }

    /**
     * Sets the values of all the squares to 0 then adds some new values
     * in random locations
     */
    public synchronized void resetBoard() {
        maxInitValues = 4;
        maxReached = false;
        Thread thread = new Thread(() -> {
            for(int i = 0; i < squares.size();i++) {
                Square square = squares.get(i);
                square.setImage(findImage(0));
                square.setValue(0);
                square.setAnim(null);
            }
            for (int i = 0; i < maxInitValues;i++) {
                Random rand = new Random();
                Square square = squares.get(rand.nextInt(squares.size()));
                square.setValue(2);
                square.setImage(findImage(2));
                square.setAnim(expandAnim);
            }
            maxInitValues = 3;
            Activity act = (Activity)context;
            act.runOnUiThread(() -> adapter.notifyDataSetChanged());
        });
        thread.start();
    }

    /**
     * Finds and returns an image that matches the corresponding given value
     * @param value int
     * @return Drawable
     */
    private synchronized Drawable findImage(int value) {
        Drawable img;

        switch (value) {
            case 0:
                img = context.getDrawable(R.drawable.green_square);
                break;
            case 2:
                img = context.getDrawable(R.drawable.green_square_2);
                break;
            case 4:
                img = context.getDrawable(R.drawable.green_square_4);
                break;
            case 8:
                img = context.getDrawable(R.drawable.green_square_8);
                break;
            case 16:
                img = context.getDrawable(R.drawable.green_square_16);
                break;
            case 32:
                img = context.getDrawable(R.drawable.green_square_32);
                break;
            case 64:
                img = context.getDrawable(R.drawable.green_square_64);
                break;
            case 128:
                img = context.getDrawable(R.drawable.green_square_128);
                break;
            case 256:
                img = context.getDrawable(R.drawable.green_square_256);
                break;
            case 512:
                img = context.getDrawable(R.drawable.green_square_512);
                break;
            case 1024:
                img = context.getDrawable(R.drawable.green_square_1024);
                break;
            case 2048:
                img = context.getDrawable(R.drawable.green_square_2048);
                break;
            default:
                img = context.getDrawable(R.drawable.green_square_max);
        }

        return img;
    }

    /**
     * Sets game to easy mode by changing max value
     */
    public void setEasyMode() {
        maxValue = 16;
    }

    /**
     * Sets game to normal mode by changing max value
     */
    public void setNormalMode() {
        maxValue = 2048;
    }

    /**
     * Shifts all the squares in the list so that they are all moved to the
     * left in the the 2 dimensional grid
     */
    public synchronized void shiftLeft() {
        Thread thread = new Thread(() -> {
            for(int index = 0; index < squares.size()-1;index++) {
                Square startSquare = squares.get(index);
                int targetVal = startSquare.getValue();

                int incrIndex = index+1;
                int row = index / gridSize;
                Square addSquare = squares.get(incrIndex);
                if(targetVal == 0) {
                    while (incrIndex/gridSize == row && incrIndex < squares.size()) {
                        addSquare = squares.get(incrIndex);
                        if (addSquare.getValue() != 0) {
                            startSquare.setValue(addSquare.getValue());
                            startSquare.setImage(findImage(addSquare.getValue()));
                            addSquare.setValue(0);
                            addSquare.setImage(findImage(0));
                            filledSpaces--;
                            break;
                        }
                        incrIndex++;
                    }
                }
                else if(addSquare.getValue() == 0){
                    while (incrIndex/gridSize == row && incrIndex < squares.size()) {
                        addSquare = squares.get(incrIndex);
                        if (addSquare.getValue() == targetVal) {
                            startSquare.setValue(targetVal * GAME_INCREMENT);
                            startSquare.setImage(findImage(startSquare.getValue()));
                            addSquare.setValue(0);
                            addSquare.setImage(findImage(0));
                            filledSpaces--;
                            break;
                        }
                        incrIndex++;
                    }
                }
                else if(addSquare.getValue() == targetVal) {
                    startSquare.setValue(targetVal * GAME_INCREMENT);
                    startSquare.setImage(findImage(targetVal * GAME_INCREMENT));
                    addSquare.setValue(0);
                    addSquare.setImage(findImage(0));
                    filledSpaces--;
                }
            }
            addSquares();
            //for debugging
            for(int i =0;i < squares.size();i++) {
                Log.d(" Printing left", " " + String.valueOf(squares.get(i).getValue())
                        + squares.get(i).getRow() +
                        String.valueOf(squares.get(i).getCol()));
            }

            Activity act = (Activity)context;
            act.runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
            });
            Log.d("shift left", "finished shifting left");
            reachedMax();
        });
        thread.start();
    }

    /**
     * Shifts all the squares in the list so that they are all moved to the
     * right in the the 2 dimensional grid
     */
    public synchronized void shiftRight() {
        Thread thread = new Thread(() -> {
            for(int index = squares.size()-1; index > 0;index--) {
                Square startSquare = squares.get(index);
                int targetVal = startSquare.getValue();

                int incrIndex = index-1;
                int row = index / gridSize;
                Square addSquare = squares.get(incrIndex);
                if(targetVal == 0) {
                    while (incrIndex/gridSize == row && incrIndex >= 0) {
                        addSquare = squares.get(incrIndex);
                        if (addSquare.getValue() != 0) {
                            startSquare.setValue(addSquare.getValue());
                            startSquare.setImage(findImage(addSquare.getValue()));
                            addSquare.setValue(0);
                            addSquare.setImage(findImage(0));
                            break;
                        }
                        incrIndex--;
                    }
                }
                else if(addSquare.getValue() == 0){
                    while (incrIndex/gridSize == row && incrIndex >= 0) {
                        addSquare = squares.get(incrIndex);
                        if (addSquare.getValue() == targetVal) {
                            startSquare.setValue(targetVal * GAME_INCREMENT);
                            startSquare.setImage(findImage(startSquare.getValue()));
                            addSquare.setValue(0);
                            addSquare.setImage(findImage(0));
                            break;
                        }
                        incrIndex--;
                    }
                }
                else if(addSquare.getValue() == targetVal) {
                    startSquare.setValue(targetVal * GAME_INCREMENT);
                    startSquare.setImage(findImage(targetVal * GAME_INCREMENT));
                    addSquare.setValue(0);
                    addSquare.setImage(findImage(0));
                }
            }
            addSquares();
            //for debugging
            for(int i =0;i < squares.size();i++) {
                Log.d(" Printing right", " " + String.valueOf(squares.get(i).getValue())
                        + squares.get(i).getRow() +
                        String.valueOf(squares.get(i).getCol()));
            }

            Activity act = (Activity)context;
            act.runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
            });
            Log.d("shift right", "finished shifting right");
            reachedMax();
        });
        thread.start();
    }

    /**
     * Shifts all the squares in the list so that they are all moved upward
     * in the the 2 dimensional grid
     */
    public synchronized void shiftUp() {
        Thread thread = new Thread(() -> {
            for(int index = 0; index < squares.size()-gridSize;index++) {
                Square startSquare = squares.get(index);
                int targetVal = startSquare.getValue();

                int incrIndex = index+gridSize;
                Square addSquare = squares.get(incrIndex);
                if(targetVal == 0) {
                    while (incrIndex < gridSize*gridSize) {
                        addSquare = squares.get(incrIndex);
                        if (addSquare.getValue() != 0) {
                            startSquare.setValue(addSquare.getValue());
                            startSquare.setImage(findImage(addSquare.getValue()));
                            addSquare.setValue(0);
                            addSquare.setImage(findImage(0));
                            break;
                        }
                        incrIndex+=gridSize;
                    }
                }
                else if(addSquare.getValue() == 0){
                    while (incrIndex < gridSize*gridSize) {
                        addSquare = squares.get(incrIndex);
                        if (addSquare.getValue() == targetVal) {
                            startSquare.setValue(targetVal * GAME_INCREMENT);
                            startSquare.setImage(findImage(startSquare.getValue()));
                            addSquare.setValue(0);
                            addSquare.setImage(findImage(0));
                            break;
                        }
                        incrIndex+=gridSize;
                    }
                }
                else if(addSquare.getValue() == targetVal) {
                    startSquare.setValue(targetVal * GAME_INCREMENT);
                    startSquare.setImage(findImage(targetVal * GAME_INCREMENT));
                    addSquare.setValue(0);
                    addSquare.setImage(findImage(0));
                }
            }
            addSquares();
            //for debugging
            for(int i =0;i < squares.size();i++) {
                Log.d(" Printing up", " " + String.valueOf(squares.get(i).getValue())
                        + squares.get(i).getRow() +
                        String.valueOf(squares.get(i).getCol()));
            }

            Activity act = (Activity)context;
            act.runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
            });
            Log.d("shift up", "finished shifting up");
            reachedMax();
        });
        thread.start();
    }

    /**
     * Shifts all the squares in the list so that they are all moved downward
     * in the the 2 dimensional grid
     */
    public synchronized void shiftDown() {
        Thread thread = new Thread(() -> {
            for(int index = squares.size()-1; index >= gridSize;index--) {
                Square startSquare = squares.get(index);
                int targetVal = startSquare.getValue();

                int incrIndex = index-gridSize;
                Square addSquare = squares.get(incrIndex);
                if(targetVal == 0) {
                    while (incrIndex >= 0) {
                        addSquare = squares.get(incrIndex);
                        if (addSquare.getValue() != 0) {
                            startSquare.setValue(addSquare.getValue());
                            startSquare.setImage(findImage(addSquare.getValue()));
                            addSquare.setValue(0);
                            addSquare.setImage(findImage(0));
                            break;
                        }
                        incrIndex-=gridSize;
                    }
                }
                else if(addSquare.getValue() == 0){
                    while (incrIndex >= 0) {
                        addSquare = squares.get(incrIndex);
                        if (addSquare.getValue() == targetVal) {
                            startSquare.setValue(targetVal * GAME_INCREMENT);
                            startSquare.setImage(findImage(startSquare.getValue()));
                            addSquare.setValue(0);
                            addSquare.setImage(findImage(0));
                            break;
                        }
                        incrIndex-=gridSize;
                    }
                }
                else if(addSquare.getValue() == targetVal) {
                    startSquare.setValue(targetVal * GAME_INCREMENT);
                    startSquare.setImage(findImage(targetVal * GAME_INCREMENT));
                    addSquare.setValue(0);
                    addSquare.setImage(findImage(0));
                }
            }
            addSquares();
            //for debugging
            for(int i =0;i < squares.size();i++) {
                Log.d(" Printing down", " " + String.valueOf(squares.get(i).getValue())
                        + squares.get(i).getRow() +
                        String.valueOf(squares.get(i).getCol()));
            }

            Activity act = (Activity)context;
            act.runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
            });
            Log.d("shift down", "finished shifting down");
            reachedMax();
        });
        thread.start();
    }

    /**
     * Determines whether the max value has been reached
     * then notifies the activity if it has
     * @return boolean
     */
    private synchronized boolean reachedMax() {
        if(maxReached) {
            return true;
        }
        for (int index = 0; index < gridSize * gridSize; index++) {
            if (squares.get(index).getValue() == maxValue) {
                maxReached = true;
                addScore();
                Activity act = (Activity)context;
                act.runOnUiThread(() -> {
                    endGameListener.endGameScore(timer.getTimeRemaining());
                });
                return true;
            }
        }

        return false;
    }

    /**
     * Adds the current score to the database
     */
    private void addScore() {
        timer.pauseTimer();
        helper.addScore(timer.getTimeRemaining());
    }

    /**
     * Adds a random amount of new squares in a random location
     * on the grid
     */
    private synchronized void addSquares() {
        int count = 0;
        Random rand = new Random();
        for(int index = 0;index < squares.size();index+= rand.nextInt(4)) {
            Square square = squares.get(index);
            if(square.getValue() != 0) {
                continue;
            }
            int random = randomValue();
            Log.d("random value", String.valueOf(random));
            if(count < maxInitValues) {
                //don't create values in middle of grid
                square.setValue(random);
                square.setImage(findImage(random));
                //animation
                if(random != 0){
                    square.setAnim(expandAnim);
                }
                else{
                    square.removeAnim();
                }
            }
            if(random != 0) {
                count++;
                filledSpaces++;
            }
        }
        Log.d("add squares", "finished adding squares");
    }

    public void setAdapter(SquareGridAdapter adapt) {
        adapter = adapt;
    }

    public void setContext(Context context) {
        this.context = context;
        endGameListener = (Listener) context;
        //animation
        expandAnim = AnimationUtils.loadAnimation(context, R.anim.animation_pop);
    }

    public void setHelper(GameDBHelper help) {
        helper = help;
    }

    public void setTimer(GameTimer time) {
        timer = time;
    }

    public List<Square> getSquares() {
        return squares;
    }
    public boolean maxIsReached() {
        return maxReached;
    }

    /**
     * Returns a value of either zero or 2 randomly
     * @return
     */
    private int randomValue() {
        Random rand = new Random();
        return rand.nextInt(2) * GAME_INCREMENT;
    }
}

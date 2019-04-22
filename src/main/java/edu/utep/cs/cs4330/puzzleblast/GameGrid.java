package edu.utep.cs.cs4330.puzzleblast;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameGrid{

    private static List<Square> squares;
    private static int gridSize;
    private static int maxInitValues; // prevent too many values for squares at once
    private SquareGridAdapter adapter;
    private Context context;

    private GameGrid() {
        gridSize = 4;
        squares = new ArrayList<>();
        maxInitValues = 5;
    }

    private static final GameGrid INSTANCE = new GameGrid();


    public static GameGrid getInstance() {
        return INSTANCE;
    }



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
                    }
                }
            }
            maxInitValues = 2;
            Activity act = (Activity)context;
            act.runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
            });
            Log.d("init square", "finished making list");
        });
        thread.start();
    }

    private synchronized Drawable findImage(int value) {
        Drawable img;

        switch (value) {
            case 3:
                img = context.getDrawable(R.drawable.green_square_3);
                break;
            case 9:
                img = context.getDrawable(R.drawable.green_square_9);
                break;
            default:
                img = context.getDrawable(R.drawable.green_square);
        }

        return img;
    }

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
                        if (addSquare.getValue() != 0) {
                            startSquare.setValue(addSquare.getValue());
                            startSquare.setImage(findImage(addSquare.getValue()));
                            addSquare.setValue(0);
                            addSquare.setImage(findImage(0));
                            break;
                        }
                        addSquare = squares.get(incrIndex);
                        incrIndex++;
                    }

                }
                else if(addSquare.getValue() == 0){
                    while (incrIndex/gridSize == row && incrIndex < squares.size()) {
                        if (addSquare.getValue() == targetVal) {
                            startSquare.setValue(targetVal * 3);
                            startSquare.setImage(findImage(startSquare.getValue()));
                            addSquare.setValue(0);
                            addSquare.setImage(findImage(0));
                            break;
                        }

                        addSquare = squares.get(incrIndex);
                        incrIndex++;
                    }
                }
                else if(addSquare.getValue() == targetVal) {
                    startSquare.setValue(targetVal * 3);
                    startSquare.setImage(findImage(targetVal *3));
                    addSquare.setValue(0);
                    addSquare.setImage(findImage(0));
                }
            }
            addSquares();
            //for debugging
            for(int i =0;i < squares.size();i++) {
                Log.d(" Printing squares", " " + String.valueOf(squares.get(i).getValue()) + squares.get(i).getRow() +
                        String.valueOf(squares.get(i).getCol()));
            }

            Activity act = (Activity)context;
            act.runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
            });
            Log.d("shift left", "finished shifting left");

        });
        thread.start();
    }

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
            }
            if(random != 0) {
                count++;
            }
        }
        Log.d("add squares", "finished adding squares");
    }

    public void setAdapter(SquareGridAdapter adapt) {
        adapter = adapt;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Square> getSquares() {
        return squares;
    }

    private int randomValue() {
        Random rand = new Random();
        return rand.nextInt(2) *3;
    }
}

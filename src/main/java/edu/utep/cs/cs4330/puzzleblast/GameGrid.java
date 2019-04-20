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
        maxInitValues = 4;
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
                    if(row != 0 && col != 0 || count == maxInitValues) {
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
            default:
                img = context.getDrawable(R.drawable.green_square);
        }

        return img;
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

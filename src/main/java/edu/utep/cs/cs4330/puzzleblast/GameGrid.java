package edu.utep.cs.cs4330.puzzleblast;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameGrid{

    private static List<Square> squares;
    private static int gridSize;
    private SquareGridAdapter adapter;
    private Context context;

    private GameGrid() {
        gridSize = 4;
        squares = new ArrayList<>();
    }

    private static final GameGrid INSTANCE = new GameGrid();


    public static GameGrid getInstance() {
        return INSTANCE;
    }



    public void initSquares() {
        Thread thread = new Thread(() -> {

            List<Square> newList = new ArrayList<>();

            for(int row = 0; row < gridSize;row++) {
                for(int col = 0; col < gridSize;col++) {
                    Square square = new Square(row, col, randomValue());
                    squares.add(square);
                }
            }
            squares = newList;
            Activity act = (Activity)context;
            act.runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
            });
            Log.d("init square", "finished making list");
        });
        thread.start();
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
        return rand.nextInt(1) *2;
    }
}

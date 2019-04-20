package edu.utep.cs.cs4330.puzzleblast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameGrid {

    private static List<Square> squares;
    private static int gridSize;
    private static final GameGrid INSTANCE = new GameGrid();

    private GameGrid() {
        if(squares == null) {
            squares = initSquares();
            gridSize = 4;
        }
    }
    public static GameGrid getInstance() {
        return INSTANCE;
    }



    private List<Square> initSquares() {
        Random rand = new Random();
        List<Square> newList = new ArrayList<>();

        for(int row = 0; row < gridSize;row++) {
            for(int col = 0; col < gridSize;col++) {
                Square square = new Square(row, col, rand.nextInt(1) *2);
                newList.add(square);
            }
        }

        return newList;
    }

    private int randomValue() {
        return 2;
    }
}

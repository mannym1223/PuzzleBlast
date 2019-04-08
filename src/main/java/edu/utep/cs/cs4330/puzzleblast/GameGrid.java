package edu.utep.cs.cs4330.puzzleblast;

import java.util.ArrayList;
import java.util.List;

public class GameGrid {

    private static List<List<Square>> squares;
    private static int gridSize;

    public GameGrid() {
        if(squares == null) {
            squares = initSquares();
            gridSize = 4;
        }
    }

    private List<List<Square>> initSquares() {
        List<List<Square>> newList = new ArrayList<>();

        for(int row = 0; row < gridSize;row++) {
            for(int col = 0; col < gridSize;col++) {
                Square square = new Square(row, col, randomValue());
                newList.get(row).add(col, square);
            }
        }

        return newList;
    }

    private int randomValue() {
        return 2;
    }
}

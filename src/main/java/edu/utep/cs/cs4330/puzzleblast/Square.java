package edu.utep.cs.cs4330.puzzleblast;

import android.graphics.drawable.Drawable;

public class Square {

    private int row;
    private int col;
    private int value;
    private Drawable image;

    public Square(int startRow, int startCol, int startValue, Drawable img) {
        value=startValue;
        row=startRow;
        col=startCol;
        image = img;
    }

    public Drawable getImage() {
        return image;
    }

}

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

    public int getValue() {
        return value;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}

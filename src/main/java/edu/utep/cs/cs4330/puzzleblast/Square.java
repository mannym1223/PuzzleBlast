package edu.utep.cs.cs4330.puzzleblast;

import android.graphics.drawable.Drawable;
import android.view.animation.Animation;

public class Square {

    private int row;
    private int col;
    private int value;
    private Drawable image;
    //animation
    private Animation expandAnim;

    public Square(int startRow, int startCol, int startValue, Drawable img) {
        value=startValue;
        row=startRow;
        col=startCol;
        image = img;
    }
    //animation
    public Animation getAnim(){
        return expandAnim;
    }
    //animation
    public void setAnim(Animation anim){
        this.expandAnim = anim;
    }
    //animation
    public void removeAnim(){
        this.expandAnim = null;
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

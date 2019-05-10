package edu.utep.cs.cs4330.puzzleblast;

import android.graphics.drawable.Drawable;
import android.view.animation.Animation;

public class Square {

    private int row;
    private int col;
    private int value;
    private Drawable image;
    private Animation expandAnim;

    public Square(int startRow, int startCol, int startValue, Drawable img) {
        value=startValue;
        row=startRow;
        col=startCol;
        image = img;
    }

    /**
     * @return animation object to be used
     */
    public Animation getAnim(){
        return expandAnim;
    }
    //animation
    public void setAnim(Animation anim){
        this.expandAnim = anim;
    }

    /**
     * Set animation object to null when not in use.
     */
    public void removeAnim(){
        this.expandAnim = null;
    }

    /**
     * @return the Drawable of the current square
     */
    public Drawable getImage() {
        return image;
    }

    /**
     * @return the value of the Square
     */
    public int getValue() {
        return value;
    }

    /**
     * @return the row positon of the square.
     */
    public int getRow() {
        return row;
    }

    /**
     * @return the column position of the square
     */
    public int getCol() {
        return col;
    }

    /**
     *
     * @param row the current row of the Square
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     *
     * @param col the current column of the Square
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     *
     * @param value the current value of the Square
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * @param image the Drawable image to be represented by square
     */
    public void setImage(Drawable image) {
        this.image = image;
    }
}

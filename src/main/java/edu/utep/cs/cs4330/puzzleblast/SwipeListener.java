package edu.utep.cs.cs4330.puzzleblast;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class SwipeListener implements View.OnTouchListener {

    private final GestureDetector gesture;

    public SwipeListener(Context context) {
        gesture = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gesture.onTouchEvent(event);
    }

    //methods to be overriden
    public void onSwipeLeft() {
    }
    public void onSwipeRight() {
    }
    public void onSwipeUp() {
    }
    public void onSwipeDown() {
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            double distanceX = e2.getX() - e1.getX();
            double distanceY = e2.getY() - e1.getY();

            //left and right
            if(Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX)
                    > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if(distanceX < 0) {
                    onSwipeLeft();
                }
                else{
                    onSwipeRight();
                }
            }
            //up and down
            else if(Math.abs(distanceX) < Math.abs(distanceY) && Math.abs(distanceY)
                    > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if(distanceY > 0) {
                    onSwipeUp();
                }
                else {
                    onSwipeDown();
                }
            }

            return false;
        }

    }

}

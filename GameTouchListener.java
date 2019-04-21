package cs4330.cs.utep.puzzleblasts;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class GameTouchListener implements GestureDetector.OnGestureListener {

    private static final int SWIPE_THRESHOLD_VELOCITY = 25;
    private static final int SWIPE_THRESHOLD = 250;
    private final GameView mView;

    public GameTouchListener(GameView view) {
        super();
        this.mView = view;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent down, MotionEvent move, float velocityX, float velocityY) {
        boolean result = false;

        float diffY = move.getY() - down.getY();
        float diffX = move.getX() - down.getX();

        if(Math.abs(diffX) > Math.abs(diffY)){
            if(Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
                if(diffX > 0){
                    onSwipeRight();
                }
                else{
                    onSwipeLeft();
                }
                result = true;

            }
        }
        else{

            if(Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY){
                if(diffY > 0){
                    onSwipeDown();
                }
                else{
                    onSwipeUP();
                }
                result = true;
            }

        }
        return result;
    }

    private void onSwipeUP() {
    }

    private void onSwipeDown() {
    }

    private void onSwipeLeft() {
    }

    private void onSwipeRight() {
    }
}

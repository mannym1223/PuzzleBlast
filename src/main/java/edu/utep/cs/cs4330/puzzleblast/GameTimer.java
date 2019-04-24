package cs4330.cs.utep.puzzletest;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class GameTimer {

    private CountDownTimer countDownTimer;
    private long timeRemaining;
    private long startTime;
    private boolean isRunning;
    private static final long GAME_TIME_INTERVAL = 1000;

    public GameTimer(long startTime) {
        this.startTime = startTime;
        this.timeRemaining = startTime;
    }


    public void startGameTimer(){
        countDownTimer = new CountDownTimer(timeRemaining, GAME_TIME_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                updateGameTimerText();
            }

            @Override
            public void onFinish() {
                isRunning = false;
            }
        }.start();

        isRunning = true;

    }


    public void updateGameTimerText(){
        int minutes = (int) (timeRemaining / 1000) / 60;
        int seconds = (int) (timeRemaining / 1000) % 60;

        String timerText = String.format(Locale.getDefault(), "%02d:02d", minutes, seconds);

    }

    private void pauseTimer(){
        countDownTimer.cancel();
        isRunning = false;
    }

    private void resetTimer(){
        timeRemaining = startTime;
        updateGameTimerText();

    }

    private void setTimer(long timer){


    }


}

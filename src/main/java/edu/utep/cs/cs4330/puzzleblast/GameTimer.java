package edu.utep.cs.cs4330.puzzleblast;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.Locale;

public class GameTimer {

    private CountDownTimer countDownTimer;
    private long timeRemaining;
    private long startTime;
    private boolean isRunning;
    private TextView timerText;
    private static final long GAME_TIME_INTERVAL = 1000;

    GameTimer(long startTime, TextView text) {
        this.startTime = startTime;
        this.timeRemaining = startTime;
        timerText = text;
    }
    void startGameTimer(){
        countDownTimer = new CountDownTimer(timeRemaining, GAME_TIME_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isRunning) {
                    timeRemaining = millisUntilFinished;
                    updateGameTimerText();
                }
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

        timerText.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));

    }

    public void pauseTimer(){
        //countDownTimer.cancel();
        isRunning = false;
    }

    public void resumeTimer() {
        isRunning = true;
    }

    public void resetTimer(){
        timeRemaining = startTime;
        updateGameTimerText();
    }

    public void setTimer(long timer){
    }

    public long getTimeRemaining() {
        return timeRemaining;
    }
}
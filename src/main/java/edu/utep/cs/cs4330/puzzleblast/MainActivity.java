package edu.utep.cs.cs4330.puzzleblast;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity implements GameGrid.Listener{

    private GameGrid grid;
    private GameDBHelper helper;
    private SquareGridAdapter gridAdapter;
    private GameTimer timer;
    private Button playButton;
    private Button resetButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent scoreIntent = new Intent(this, HighScoreActivity.class);
            startActivity(scoreIntent);
        });

        GridView board = findViewById(R.id.boardView);

        playButton = findViewById(R.id.playButton);
        resetButton = findViewById(R.id.resetButton);

        timer = new GameTimer(300000, findViewById(R.id.scoreText));

        playButton.setOnClickListener(v -> timer.startGameTimer());
        resetButton.setOnClickListener(v -> {
            grid.resetBoard();
            timer.pauseTimer();
            timer = new GameTimer(300000, findViewById(R.id.scoreText));
            grid.setTimer(timer);
        });

        helper = new GameDBHelper(this);

        grid = GameGrid.getInstance();
        grid.initSquares();
        grid.setContext(this);
        grid.setHelper(helper);
        grid.setTimer(timer);
        grid.setEndingText(findViewById(R.id.endGameText));

        gridAdapter = new SquareGridAdapter(this);
        grid.setAdapter(gridAdapter);
        board.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();


        board.setOnTouchListener(new SwipeListener(this) {
            @Override
            public void onSwipeLeft() {
                grid.shiftLeft();
            }
            @Override
            public void onSwipeRight() {
                grid.shiftRight();
            }
            @Override
            public void onSwipeUp() {
                grid.shiftUp();
            }
            @Override
            public void onSwipeDown() {
                grid.shiftDown();
            }
        });

        TextView sensText = findViewById(R.id.sensorText);
        new GameTiltDetect(this) {
            @Override
            public void onLeftTilt() {
                sensText.setText("Left");
                grid.shiftLeft();
            }
            @Override
            public void onRightTilt() {
                sensText.setText("Right");
                grid.shiftRight();
            }
            @Override
            public void onUpTilt() {
                sensText.setText("Up");
                grid.shiftUp();
            }
            @Override
            public void onDownTilt() {
                sensText.setText("Down");
                grid.shiftDown();
            }
            @Override
            public void onCentered() {
                sensText.setText("Centered");
            }
        };

    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.pauseTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.resumeTimer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.easy_mode) {
            grid.setEasyMode();
            showToast("Easy mode set");
            return true;
        }
        if (id == R.id.normal_mode) {
            grid.setNormalMode();
            showToast("Normal mode set");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    //end game dialog
    public void displayDialog(long score) {
        AlertDialog.Builder mBuild = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.game_end, null);

        MediaPlayer mp = MediaPlayer.create(this, R.raw.tada);

        TextView scoreTextView = view.findViewById(R.id.scoreTextView);
        scoreTextView.setText(Long.toString(score));

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.animation);
        ImageView gameEndImageView = view.findViewById(R.id.gameEndImageView);
        gameEndImageView.setImageResource(R.drawable.green_square_max);

        mBuild.setView(view)
                .setNegativeButton("OK", (dialog, id) -> {

                });

        mBuild.show();
        mp.start();
        gameEndImageView.startAnimation(anim);
    }

    @Override
    public void endGameScore(long score) {
        displayDialog(score);
        grid.resetBoard();
        timer.pauseTimer();
        timer = new GameTimer(300000, findViewById(R.id.scoreText));
    }
}

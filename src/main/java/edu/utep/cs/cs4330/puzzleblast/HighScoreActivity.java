package cs4330.cs.utep.dbtest;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class HighScoreActivity extends AppCompatActivity {

    GameScoreCursorAdapter gameScoreCursorAdapter;
    ListView listView;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        listView = findViewById(R.id.gameScoreListView);
        GameDBHelper gameDBHelper = new GameDBHelper(this);

        cursor = gameDBHelper.allScores();


        new Handler().post(new Runnable() {

            @Override
            public void run() {
                gameScoreCursorAdapter = new GameScoreCursorAdapter(HighScoreActivity.this, cursor);
                listView.setAdapter(gameScoreCursorAdapter);
            }

        });



    }
}

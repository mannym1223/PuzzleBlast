package edu.utep.cs.cs4330.puzzleblast;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class GameScoreCursorAdapter extends CursorAdapter {

    TextView scoreTextView;
    TextView timeStampTextView;

    public GameScoreCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.score_layout, parent, false);
    }

    /**
     * Sets the view elements based on cursor set.
     * Represents scores and timestamps of the game.
     *
     * @param view the view to be updated
     * @param context the context of the application
     * @param cursor the query set stored
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        scoreTextView = view.findViewById(R.id.scoreTextView);
        timeStampTextView = view.findViewById(R.id.timeStampTextView);

        long score = cursor.getInt(cursor.getColumnIndexOrThrow(GameDBHelper.COLUMN_SCORE));
        String timeStamp = cursor.getString(cursor.getColumnIndexOrThrow(GameDBHelper.COLUMN_TIMESTAMP));

        scoreTextView.setText(Long.toString(score));
        timeStampTextView.setText(timeStamp);
    }
}

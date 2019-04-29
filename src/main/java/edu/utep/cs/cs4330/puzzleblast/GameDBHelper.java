package edu.utep.cs.cs4330.puzzleblast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;


public class GameDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "highScore.db";

    public static final String TABLE_GAME = "gameScores";
    public static final String COLUMN_TIMESTAMP = "timeStamp";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_ID = "_id";


    public GameDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String table = "CREATE TABLE " + TABLE_GAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_SCORE + " INTEGER NOT NULL, " +
                COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ");";


        sqLiteDatabase.execSQL(table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME);
        onCreate(sqLiteDatabase);
    }


    public void addScore(long score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = db.query(TABLE_GAME, new String[]{"*"}, COLUMN_SCORE + " = " + "(SELECT MIN(" + COLUMN_SCORE + ") FROM " + TABLE_GAME + ")", null, null, null, null);

        cursor.moveToFirst();

        long minScoreInDb = cursor.getLong(cursor.getColumnIndex(COLUMN_SCORE));
        long minIdInDb = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));


        if(getCount() == 12 && score > minScoreInDb) {
            delete(minIdInDb);
            values.put(COLUMN_SCORE, score);
        }

        else{
            values.put(COLUMN_SCORE, score);
        }

        long id = db.insert(TABLE_GAME, null, values);

        db.close();
    }

    public long getCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_GAME);

        return count;
    }

    public Cursor allScores() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GAME, null, null, null, null, null, COLUMN_SCORE+" DESC");

        return cursor;
    }


    public void delete(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GAME, COLUMN_ID + " = ?", new String[] { Long.toString(id) } );
    }


    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_GAME);

        db.close();
    }



}

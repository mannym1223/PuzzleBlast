package edu.utep.cs.cs4330.puzzleblast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GameDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "highScoredb";

    public static final String TABLE_GAME = "gameScores";
    public static final String COLUMN_TIMESTAMP = "timeStamp";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_ID = "_id";

    public GameDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }


    /**
     * Creates new sqli database to store user's scores.
     *
     * @param sqLiteDatabase the database to create
     */
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

    /**
     *
     * Adds users score to the database,
     * using cursor to retrieve query result used to replace minimum score
     *
     * @param score to be added to database
     */
    public synchronized void addScore(long score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = db.query(TABLE_GAME, new String[]{"*"}, COLUMN_SCORE + " = " + "(SELECT MIN(" + COLUMN_SCORE + ") FROM " + TABLE_GAME + ")", null, null, null, null);

        cursor.moveToFirst();

        long minScoreInDb = 0;
        long minIdInDb = 0;

        if(getCount() != 0) {
            minScoreInDb = cursor.getLong(cursor.getColumnIndex(COLUMN_SCORE));
            minIdInDb = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
        }

        if(getCount() == 10 && score > minScoreInDb) {
            delete(minIdInDb);
            values.put(COLUMN_SCORE, score);
        }

        else{
            values.put(COLUMN_SCORE, score);
        }

        db.insert(TABLE_GAME, null, values);

        db.close();
    }

    /**
     * Get the count of database entries.
     *
     * @return count entries in database
     */

    public long getCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_GAME);

        return count;
    }

    /**
     *
     * Generate a cursor with all database scores
     *
     * @return cursor object containing the query of all database scores
     */
    public Cursor allScores() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GAME, null, null, null, null, null, COLUMN_SCORE+" DESC");

        return cursor;
    }

    /**
     * Delete entries from database provided the id
     *
     * @param id the id to be deleted
     */
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

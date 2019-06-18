package com.example.android.tic_tac_toe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class display_besttime extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="Showtime.db";
    public static final int DATABASE_VERSION=1;
    public  static final String TABLE_NAME="Showtime_data";
    public static final String C1="GAME_NO";
    public static final String C2="TIME";
    public display_besttime(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    String createTable = "CREATE TABLE " + TABLE_NAME + " (GAME_NO INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "TIME INTEGER)";
    db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
public boolean addData(String time) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(C2, time);
    long disp = db.insert(TABLE_NAME, null, contentValues);
    if (disp == -1) {
        return false;
    } else {
        return true;
    }
}
public Cursor getcontents()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        return data;
    }

}

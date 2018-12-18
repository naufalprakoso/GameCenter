package com.asd.gamecenter.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.asd.gamecenter.data.Key;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = Key.DATABASE;
    private static final int DATABASE_VERSION = 4;
    private static final String SQL_CREATE_TABLE_CART = String.format("CREATE TABLE %s"
                    + " (%s TEXT PRIMARY KEY," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL, "+
                    " %s INTEGER NOT NULL, "+
                    " %s INTEGER NOT NULL, "+
                    " %s REAL NOT NULL, " +
                    " %s INTEGER NOT NULL)",
            Key.TABLE_CART,
            "CartID",
            "CartName",
            "CartDescription",
            "CartGenre",
            "CartStock",
            "CartPrice",
            "CartRating",
            "CartQty"
    );

    private static final String SQL_CREATE_TABLE_MY_GAMES = String.format("CREATE TABLE %s"
                    + " (%s TEXT PRIMARY KEY," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL, "+
                    " %s INTEGER NOT NULL)",
            Key.TABLE_MY_GAMES,
            "MyGameID",
            "MyGameName",
            "MyGameDescription",
            "MyGameGenre",
            "MyGamePlayingHour"
    );

    private static final String SQL_CREATE_TABLE_USER = String.format("CREATE TABLE %s"
                    + " (%s TEXT PRIMARY KEY," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL, "+
                    " %s TEXT NOT NULL, "+
                    " %s TEXT NOT NULL, "+
                    " %s TEXT NOT NULL, "+
                    " %s INTEGER NOT NULL, "+
                    " %s TEXT NOT NULL)",
            Key.TABLE_USER,
            "UserID", //0
            "UserName", //1
            "UserEmail",//2
            "UserPassword",//3
            "UserBirthday",//4
            "UserPhone",//5
            "UserGender",//6
            "UserBalance",//7
            "UserStatus"//8
    );

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_CART);
        db.execSQL(SQL_CREATE_TABLE_USER);
        db.execSQL(SQL_CREATE_TABLE_MY_GAMES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Key.TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + Key.TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + Key.TABLE_MY_GAMES);
        onCreate(db);
    }
}

package com.asd.gamecenter.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "GameCenterDB";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_CART = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL, "+
                    " %s INTEGER NOT NULL, "+
                    " %s INTEGER NOT NULL, "+
                    " %s REAL NOT NULL)",
            "MsCart",
            "GameID",
            "GameName",
            "GameDescription",
            "GameGenre",
            "GameStock",
            "GamePrice",
            "GameRating"
    );

    private static final String SQL_CREATE_TABLE_USER = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL, "+
                    " %s TEXT NOT NULL, "+
                    " %s TEXT NOT NULL, "+
                    " %s TEXT NOT NULL, "+
                    " %s INTEGER NOT NULL, "+
                    " %s TEXT NOT NULL)",
            "MsUser",
            "UserID",
            "UserName",
            "UserEmail",
            "UserPassword",
            "UserBirthday",
            "UserPhone",
            "UserGender",
            "UserBalance",
            "UserStatus"
    );

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_CART);
        db.execSQL(SQL_CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "MsCart");
        db.execSQL("DROP TABLE IF EXISTS " + "MsUser");
        onCreate(db);
    }
}

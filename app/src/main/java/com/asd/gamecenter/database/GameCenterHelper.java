package com.asd.gamecenter.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.asd.gamecenter.model.Game;

import java.util.ArrayList;

public class GameCenterHelper {
    private static String TABLE_CART = "MsCart";
    private static String TABLE_USER = "MsCart";
    private Context context;
    private DatabaseHelper dataBaseHelper;

    private SQLiteDatabase database;

    public GameCenterHelper(Context context) {
        this.context = context;
    }

    public void open() throws SQLException {
        dataBaseHelper = new DatabaseHelper(context);
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();
    }

    public void insertUser(Game game) {
        ContentValues initialValues =  new ContentValues();
        initialValues.put("GameName", game.getName());
        initialValues.put("GameDescription", game.getDescription());
        initialValues.put("GameGenre", game.getGenre());
        initialValues.put("GameStock", game.getStock());
        initialValues.put("GamePrice", game.getPrice());
        initialValues.put("GameRating", game.getRating());
        database.insert(TABLE_USER, null, initialValues);
    }

    public ArrayList<Game> viewGame() {
        ArrayList<Game> arrayList = new ArrayList<>();
        Cursor cursor = database.query(
                TABLE_CART,
                null,
                null,
                null,
                null,
                null,
                "GameID DESC",
                null);
        cursor.moveToFirst();
        Game game;

        if (cursor.getCount() > 0) {
            do {
                game = new Game();
                game.setId(cursor.getInt(cursor.getColumnIndexOrThrow("GameID")));
                game.setName(cursor.getString(cursor.getColumnIndexOrThrow("GameName")));
                game.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("GameDescription")));
                game.setGenre(cursor.getString(cursor.getColumnIndexOrThrow("GameGenre")));
                game.setStock(cursor.getInt(cursor.getColumnIndexOrThrow("GameStock")));
                game.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow("GamePrice")));
                game.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow("GameRating")));

                arrayList.add(game);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        cursor.close();

        return arrayList;
    }

    public void insertGame(Game game) {
        ContentValues initialValues =  new ContentValues();
        initialValues.put("GameName", game.getName());
        initialValues.put("GameDescription", game.getDescription());
        initialValues.put("GameGenre", game.getGenre());
        initialValues.put("GameStock", game.getStock());
        initialValues.put("GamePrice", game.getPrice());
        initialValues.put("GameRating", game.getRating());
        database.insert(TABLE_CART, null, initialValues);
    }

    public void deleteGame(int id) {
        database.delete(TABLE_CART,  "GameID = '" + id + "'", null);
    }

    public void updateGame(Game game, int id){
        ContentValues initialValues =  new ContentValues();
        initialValues.put("GameName", game.getName());
        initialValues.put("GameDescription", game.getDescription());
        initialValues.put("GameGenre", game.getGenre());
        initialValues.put("GameStock", game.getStock());
        initialValues.put("GamePrice", game.getPrice());
        initialValues.put("GameRating", game.getRating());
        database.update(TABLE_CART, initialValues, "GameId = '" + id + "'", null);
    }
}

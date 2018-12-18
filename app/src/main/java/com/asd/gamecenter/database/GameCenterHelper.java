package com.asd.gamecenter.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.asd.gamecenter.data.Key;
import com.asd.gamecenter.model.Game;
import com.asd.gamecenter.model.User;

import java.util.ArrayList;

public class GameCenterHelper {
    private static String TABLE_CART = Key.TABLE_CART;
    private static String TABLE_USER = Key.TABLE_USER;
    private static String TABLE_MY_GAMES = Key.TABLE_MY_GAMES;

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

    public void insertUser(User user) {
        ContentValues initialValues =  new ContentValues();
        initialValues.put("UserID", user.getId());
        initialValues.put("UserName", user.getName());
        initialValues.put("UserEmail", user.getEmail());
        initialValues.put("UserPassword", user.getPassword());
        initialValues.put("UserBirthday", user.getBirthday());
        initialValues.put("UserPhone", user.getPhone());
        initialValues.put("UserStatus", user.getStatus());
        initialValues.put("UserGender", user.getGender());
        initialValues.put("UserBalance", user.getBalance());
        database.insert(TABLE_USER, null, initialValues);
    }

    public void updateBalance(int amount, String id){
        ContentValues initialValues =  new ContentValues();
        initialValues.put("UserBalance", amount);
        database.update(TABLE_USER, initialValues, "UserID = '" + id + "'", null);
    }

    public User auth(User user){
        Cursor cursor = database.query(TABLE_USER,
                new String[]{"UserID", //0
                        "UserName", //1
                        "UserEmail",//2
                        "UserPassword",}, //3
                 "UserEmail = ?",
                new String[]{user.getEmail()},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            User user1 = new User(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3));

            if (user.getPassword().equals(user1.getPassword())) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(Key.APP_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();

                edit.putString(Key.USER_ID, user1.getId());
                edit.putString(Key.USER_NAME, user1.getName());
                edit.putString(Key.USER_EMAIL, user1.getEmail());
                edit.commit();

                return user1;
            }
        }

        return null;
    }

    public ArrayList<Game> viewCart() {
        ArrayList<Game> arrayList = new ArrayList<>();
        Cursor cursor = database.query(
                TABLE_CART,
                null,
                null,
                null,
                null,
                null,
                "CartID DESC",
                null);
        cursor.moveToFirst();

        Game game;

        if (cursor.getCount() > 0) {
            do{
                game = new Game();
                game.setId(cursor.getString(cursor.getColumnIndexOrThrow("CartID")));
                game.setName(cursor.getString(cursor.getColumnIndexOrThrow("CartName")));
                game.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("CartDescription")));
                game.setGenre(cursor.getString(cursor.getColumnIndexOrThrow("CartGenre")));
                game.setStock(cursor.getInt(cursor.getColumnIndexOrThrow("CartStock")));
                game.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow("CartPrice")));
                game.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow("CartRating")));
                game.setQty(cursor.getInt(cursor.getColumnIndexOrThrow("CartQty")));

                arrayList.add(game);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        cursor.close();

        return arrayList;
    }

    public ArrayList<Game> viewMyGame() {
        ArrayList<Game> arrayList = new ArrayList<>();
        Cursor cursor = database.query(
                TABLE_MY_GAMES,
                null,
                null,
                null,
                null,
                null,
                "MyGameID DESC",
                null);
        cursor.moveToFirst();
        Game game;

        if (cursor.getCount() > 0) {
            do {
                game = new Game();
                game.setId(cursor.getString(cursor.getColumnIndexOrThrow("MyGameID")));
                game.setName(cursor.getString(cursor.getColumnIndexOrThrow("MyGameName")));
                game.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("MyGameDescription")));
                game.setGenre(cursor.getString(cursor.getColumnIndexOrThrow("MyGameGenre")));
                game.setStock(cursor.getInt(cursor.getColumnIndexOrThrow("MyGameStock")));
                game.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow("MyGamePrice")));
                game.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow("MyGameRating")));
                game.setPlayingHour(cursor.getInt(cursor.getColumnIndexOrThrow("MyGamePlayingHour")));

                arrayList.add(game);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        cursor.close();

        return arrayList;
    }


    public boolean checkCartState(String id){
        boolean result = false;
        Cursor cursor = database.query(
                TABLE_CART,
                new String[]{"CartID"},
                null,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            do{
                if (cursor.getString(cursor.getColumnIndexOrThrow("CartID")).equals(id)){
                    result = true;
                }
                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }

        cursor.close();

        return result;
    }

    public int getTotalPriceCart() {
        int totalPrice = 0;

        Cursor cursor = database.query(
                TABLE_CART,
                new String[]{"CartPrice", "CartQty"},
                null,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            do{
                totalPrice += cursor.getInt(cursor.getColumnIndexOrThrow("CartPrice"))
                        * cursor.getInt(cursor.getColumnIndexOrThrow("CartQty"));
                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }

        cursor.close();

        return totalPrice;
    }

    // prakoso@gmail.com | 123123
    // naufalprakoso24@gmail.com | 123321

    public int getWalletBalance(String id) {
        Cursor cursor = database.query(
                TABLE_USER,
                new String[]{"UserID", "UserBalance"},
                "UserID = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
        cursor.moveToFirst();

        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            return cursor.getInt(cursor.getColumnIndexOrThrow("UserBalance"));
        }

        cursor.close();

        return 0;
    }

    public void insertGame(Game game) {
        ContentValues initialValues =  new ContentValues();
        initialValues.put("CartID", game.getId());
        initialValues.put("CartName", game.getName());
        initialValues.put("CartDescription", game.getDescription());
        initialValues.put("CartGenre", game.getGenre());
        initialValues.put("CartStock", game.getStock());
        initialValues.put("CartPrice", game.getPrice());
        initialValues.put("CartRating", game.getRating());
        initialValues.put("CartQty", 1);
        database.insert(TABLE_CART, null, initialValues);
    }

    public void deleteGame(String id) {
        database.delete(TABLE_CART,  "CartID = '" + id + "'", null);
    }

    public void updateCartQty(int qty, String id){
        ContentValues initialValues =  new ContentValues();
        initialValues.put("CartQty", qty);
        database.update(TABLE_CART, initialValues, "CartID = '" + id + "'", null);
    }
}

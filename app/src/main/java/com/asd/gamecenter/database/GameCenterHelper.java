package com.asd.gamecenter.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.asd.gamecenter.data.Key;
import com.asd.gamecenter.model.Game;
import com.asd.gamecenter.model.User;

import java.util.ArrayList;

public class GameCenterHelper {
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

    // prakoso@gmail.com | qweqwe
    // vektoraccelerator@gmail.com | 123321
    // naufalprakoso24@gmail.com | qweqwe

    @SuppressLint({"ApplySharedPref", "Recycle"})
    public User auth(User user){
        Cursor cursor = database.query(TABLE_USER,
                new String[]{"UserID", //0
                        "UserName", //1
                        "UserEmail",//2
                        "UserPassword",
                        "UserPhone",},
                 "UserEmail = ?",
                new String[]{user.getEmail()},
                null, null, null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            do{
                User user1 = new User(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
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
            }while (!cursor.isAfterLast());
        }

        return null;
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
                game.setPlayingHour(cursor.getInt(cursor.getColumnIndexOrThrow("MyGamePlayingHour")));

                arrayList.add(game);

                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        cursor.close();

        return arrayList;
    }

    public boolean checkMyGamesState(String id){
        boolean result = false;
        Cursor cursor = database.query(
                TABLE_MY_GAMES,
                new String[]{"MyGameID"},
                null,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            do{
                if (cursor.getString(cursor.getColumnIndexOrThrow("MyGameID")).equals(id)){
                    result = true;
                    break;
                }
                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }

        cursor.close();

        return result;
    }

    public User getUserProfile(String id) {
        Cursor cursor = database.query(
                TABLE_USER,
                new String[]{"UserID", "UserName", "UserPhone", "UserEmail"},
                "UserID = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0){
            do{
                if (cursor.moveToFirst() && cursor.getCount() > 0) {
                    if (cursor.getString(cursor.getColumnIndexOrThrow("UserID")).equals(id)) {
                        User user = new User();
                        user.setName(cursor.getString(cursor.getColumnIndexOrThrow("UserName")));
                        user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("UserPhone")));
                        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("UserEmail")));
                        return user;
                    }
                }
                cursor.moveToNext();
            }while(!cursor.isAfterLast());
        }

        cursor.close();

        return null;
    }

    public Integer countMyGame() {
        String countQuery = "SELECT  * FROM " + TABLE_MY_GAMES;
        Cursor cursor = database.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public void updatePlayingHours(String myGameId, int result){
        ContentValues initialValues =  new ContentValues();
        initialValues.put("MyGamePlayingHour", result);
        database.update(TABLE_MY_GAMES, initialValues, "MyGameID = '" + myGameId + "'", null);
    }

    public int checkoutCart(Game game, String userId){
        ContentValues initialValues =  new ContentValues();
        initialValues.put("MyGameID", game.getId());
        initialValues.put("MyGameName", game.getName());
        initialValues.put("MyGameDescription", game.getDescription());
        initialValues.put("MyGameGenre", game.getGenre());
        initialValues.put("MyGamePlayingHour", 0);
        initialValues.put("MyGameUserId", userId);
        database.insert(TABLE_MY_GAMES, null, initialValues);
        return 0;
    }
}

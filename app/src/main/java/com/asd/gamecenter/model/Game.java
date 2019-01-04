package com.asd.gamecenter.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {

    private int stock, price, qty, playingHour;
    private double rating;
    private String name, description, genre, id, userId;

    public Game(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPlayingHour() {
        return playingHour;
    }

    public void setPlayingHour(int playingHour) {
        this.playingHour = playingHour;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.genre);
        dest.writeInt(this.stock);
        dest.writeInt(this.price);
        dest.writeDouble(this.rating);
        dest.writeInt(this.qty);
        dest.writeInt(this.playingHour);
        dest.writeString(this.userId);
    }

    private Game(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        genre = in.readString();
        stock = in.readInt();
        price = in.readInt();
        rating = in.readDouble();
        qty = in.readInt();
        playingHour = in.readInt();
        userId = in.readString();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}

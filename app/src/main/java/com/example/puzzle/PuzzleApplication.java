package com.example.puzzle;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.puzzle.dish.DishManager;
import com.example.puzzle.model.User;
import com.example.puzzle.utils.StaticValue;

import java.util.ArrayList;

public class PuzzleApplication extends Application {

    private static Context mContext;

    private static User mUser;

    private static DishManager dm;

    private static ArrayList<String> picturePath;

    public static ArrayList<String> getPicturePath() {
        return picturePath;
    }

    public static void setPicturePath(ArrayList<String> picturePath) {
        PuzzleApplication.picturePath = picturePath;
    }

    public static int getLevel() {
        return level;
    }

    public static void setLevel(int level) {
        if (level < 3) return;
        PuzzleApplication.level = level;
        initDishManager();
    }

    private static int level = 3;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mContext == null) mContext = getApplicationContext();
        SharedPreferences pref = getSharedPreferences(StaticValue.SP_NAME, MODE_PRIVATE);
        setLevel(pref.getInt(StaticValue.SP_LEVEL, 3));
    }

    public static User getmUser() {
        return mUser;
    }

    public static void setmUser(User mUser) {
        PuzzleApplication.mUser = mUser;
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static void initDishManager() {
        dm = new DishManager(level);
    }

    public static DishManager getDishManager() {
        return dm;
    }
}

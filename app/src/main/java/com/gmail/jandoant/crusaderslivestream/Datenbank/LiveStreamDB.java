package com.gmail.jandoant.crusaderslivestream.Datenbank;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Datenbank für die LiveStreamApp
 * <p/>
 * <ol>
 * </li>Tabelle für die einzelnen Spiele</li>
 * </li>Tabelle für die einzelnen Spielzüge eines Spiels</li>
 * </ol>
 */
public class LiveStreamDB extends SQLiteOpenHelper {

    //Datenbank
    private final String DB_NAME = "DB_LiveStream";
    //Tabelle für die Spiele
    private final String TABLE_GAMES = "games";
    //Spalten
    private final String COLUMN_GAME_ID = "_id";
    private final String COLUMN_GAME_DATE = "gamedate";
    private final String COLUMN_GAME_TIME = "gametime";
    private final String COLUMN_GAME_PLACE = "gameplace";
    private final String COLUMN_GAME_AWAYTEAM = "awayteam";
    private final String COLUMN_GAME_HOMETEAM = "hometeam";
    private final String COLUMN_GAME_RESULT_HOME = "result_away";
    private final String COLUMN_GAME_RESULT_AWAY = "result_home";
    private final String COLUMN_RESULT_IMAGE_PATH = "result_image_url";


    //Konstruktor
    public LiveStreamDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

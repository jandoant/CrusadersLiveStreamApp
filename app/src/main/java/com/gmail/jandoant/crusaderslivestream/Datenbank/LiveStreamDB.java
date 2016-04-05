package com.gmail.jandoant.crusaderslivestream.Datenbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.gmail.jandoant.crusaderslivestream.Spiel.Game;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * Datenbank für die LiveStreamApp
 * <p/>
 * <ol>
 * </li>Tabelle für die einzelnen Spiele</li>
 * </li>Tabelle für die einzelnen Spielzüge eines Spiels</li>
 * </ol>
 */
public class LiveStreamDB extends SQLiteOpenHelper {
    //Tabelle für die Spiele
    public static final String TABLE_GAMES = "games";
    //Datenbank
    private static final String DB_NAME = "livestream.db";
    private static final int DB_VERSION = 1;
    private final String TAG = "LiveStream";
    private final String CLASS_NAME = "LiveStreamDB";
    //Spalten
    private final String COLUMN_GAME_ID = "_id";
    private final String COLUMN_GAME_DATE = "gamedate";
    private final String COLUMN_GAME_TIME = "gametime";
    private final String COLUMN_GAME_PLACE = "gameplace";
    private final String COLUMN_GAME_AWAYTEAM = "awayteam";
    private final String COLUMN_GAME_HOMETEAM = "hometeam";
    private final String COLUMN_GAME_RESULT_HOME = "result_away";
    private final String COLUMN_GAME_RESULT_AWAY = "result_home";
    private final String COLUMN_GAME_QUARTER = "gamequarter";
    private final String COLUMN_GAME_TIMESTAMP = "timestamp_game";
    Context context;


    //Konstruktor
    public LiveStreamDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //DB mit diesen Spalten erstellen
        String query = "CREATE TABLE " + TABLE_GAMES + "(" +
                COLUMN_GAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COLUMN_GAME_DATE + " TEXT ," +
                COLUMN_GAME_TIME + " TEXT ," +
                COLUMN_GAME_HOMETEAM + " TEXT ," +
                COLUMN_GAME_AWAYTEAM + " TEXT ," +
                COLUMN_GAME_RESULT_HOME + " INTEGER ," +
                COLUMN_GAME_RESULT_AWAY + " INTEGER ," +
                COLUMN_GAME_PLACE + " TEXT ," +
                COLUMN_GAME_QUARTER + " INTEGER ," +
                COLUMN_GAME_TIMESTAMP + " INTEGER " +
                ");";
        db.execSQL(query);
        backupDB();
        Log.d(TAG, CLASS_NAME + ": eine neue Datenbank wurde erstellt");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
        onCreate(db);
        Log.d(TAG, CLASS_NAME + ": Datenbank onUpgrade()");
    }

    public void clearDBTable(String tableName) {
        //Tabelle der DB löschen
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        //--Backup der DB auf SD-Karte schreiben
        backupDB();
        //leere Tabelle mit den gleichen Spalten erstellen
        onCreate(db);
    }

    //ein Spiel zur DB hinzufügen
    public void addGame(Game game) {

        //Timestamp erzeugen
        DateTime now = new DateTime();
        long timestamp = now.getMillis();

        //--Daten des Spiel auslesen
        ContentValues values = new ContentValues();
        //DATUM UND UHRZEIT
        values.put(COLUMN_GAME_DATE, game.getGameDate().toString());
        values.put(COLUMN_GAME_TIME, game.getGameTime().toString());
        //GEGNER
        values.put(COLUMN_GAME_HOMETEAM, game.getHomeTeam());
        values.put(COLUMN_GAME_AWAYTEAM, game.getAwayTeam());
        //TIMESTAMP
        values.put(COLUMN_GAME_TIMESTAMP, timestamp);

        //--Daten in DB einfügen
        SQLiteDatabase db = getWritableDatabase();
        long row = db.insert(TABLE_GAMES, null, values);
        Toast.makeText(context, "Row: " + row, Toast.LENGTH_SHORT).show();
        //--Check ob Operation erfolgreich
        if (row != -1) {
            Toast.makeText(context, "Das Spiel wurde erfolgreich erstellt", Toast.LENGTH_SHORT).show();
            backupDB();
        } else {
            Toast.makeText(context, "Fehler beim Schreiben in die Datenbank", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    /**
     * gibt alle GameObjekte der DB in ArrayList aus
     *
     * @return ArrayList(Game)
     */
    public ArrayList<Game> dbAllGamesDataToArray() {

        //neue ArrayListe estellen
        ArrayList<Game> arrayListGames = new ArrayList<Game>();

        //Datenbank-Abfrage (DB-ID, Datum, Uhrzeit, Heimteam, Auswärtsteam, Punktestand Heim/Auswärts) --> in Cursor speichern
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GAMES + " WHERE 1";
        Cursor myCursor = db.rawQuery(query, null);

        //alle Ergebnisse der Abfrage durchgehen und nacheinander in ArrayList speichern
        myCursor.moveToFirst();
        while (!myCursor.isAfterLast()) {
            Game myGame = new Game();
            //dem Spiel-Objekt die Werte aus der DB zuweisen
            setGameFieldsFromDB(myCursor, myGame);
            //Den Spieler der aktuellen Cursorposition der ArrayList hinzufügen
            arrayListGames.add(myGame);
            //Cursor zur nächsten Position weiterwandern lassen
            myCursor.moveToNext();
        }
        //gibt alle GameObjekte der DB in ArrayList aus
        return arrayListGames;
    }

    /**
     * dem Game-Objekt die gespeicherten Werte aus der DB zuweisen
     *
     * @param myCursor
     * @param myGame
     */
    private void setGameFieldsFromDB(Cursor myCursor, Game myGame) {
        //--ID
        myGame.set_id(getGameID(myCursor));
        //--Datum und Uhrzeit
        myGame.setGameDate(getGameDateFromDB(myCursor));
        myGame.setGameTime(getGameTimeFromDB(myCursor));
        myGame.setQuarter(getGameQuarterFromDB(myCursor));
        //--Gegner
        myGame.setHomeTeam(getHometeamFromDB(myCursor));
        myGame.setAwayTeam(getAwayteamFromDB(myCursor));
        //--Aktueller Punktestand
        myGame.setAktuellePunkteHome(getGameResultHome(myCursor));
        myGame.setAktuellePunkteAway(getGameResultAway(myCursor));
        //--Timestamp
        myGame.setTimestamp(getGameTimestampFromDB(myCursor));
        //Spielort
        myGame.setGameOrt(getGameOrtFromDB(myCursor));
    }

    private int getGameQuarterFromDB(Cursor myCursor) {
        return myCursor.getInt(myCursor.getColumnIndex(COLUMN_GAME_QUARTER));
    }


    public Game getGameFromDbByID(int id) {
        Game myGame = new Game();
        //Datenbank-Abfrage (DB-ID, Datum, Uhrzeit, Heimteam, Auswärtsteam, Punktestand Heim/Auswärts) --> in Cursor speichern
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GAMES + " WHERE " + COLUMN_GAME_ID + " = " + id;
        Log.d("TAG", query);
        Cursor myCursor = db.rawQuery(query, null);
        myCursor.moveToFirst();
        setGameFieldsFromDB(myCursor, myGame);
        return myGame;
    }

    public void updateGameQuarterInDB(Game myGame) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_GAMES +
                " SET " + COLUMN_GAME_QUARTER + " = " + myGame.getQuarter() +
                " WHERE " + COLUMN_GAME_ID + " = " + myGame.get_id();

        db.execSQL(query);
        db.close();
    }




    private int getGameResultHome(Cursor myCursor) {
        return myCursor.getInt(myCursor.getColumnIndex(COLUMN_GAME_RESULT_HOME));
    }

    private int getGameResultAway(Cursor myCursor) {
        return myCursor.getInt(myCursor.getColumnIndex(COLUMN_GAME_RESULT_AWAY));
    }

    private int getGameID(Cursor myCursor) {
        return myCursor.getInt(myCursor.getColumnIndex(COLUMN_GAME_ID));
    }

    private DateTime getGameTimestampFromDB(Cursor myCursor) {
        long timestamp = myCursor.getInt(myCursor.getColumnIndex(COLUMN_GAME_TIMESTAMP));
        return new DateTime(timestamp);
    }

    private LocalDate getGameDateFromDB(Cursor cursor) {
        //GameDate-Feld aus DB auslesen
        String strGameDate = cursor.getString(cursor.getColumnIndex(COLUMN_GAME_DATE));
        return new LocalDate(strGameDate);
    }

    private LocalTime getGameTimeFromDB(Cursor cursor) {
        //GameTime-Feld aus DB auslesen
        String strGameTime = cursor.getString(cursor.getColumnIndex(COLUMN_GAME_TIME));
        return new LocalTime(strGameTime);
    }

    private String getHometeamFromDB(Cursor cursor) {
        //Hometeam-Feld aus DB auslesen
        return cursor.getString(cursor.getColumnIndex(COLUMN_GAME_HOMETEAM));
    }

    private String getAwayteamFromDB(Cursor cursor) {
        //Hometeam-Feld aus DB auslesen
        return cursor.getString(cursor.getColumnIndex(COLUMN_GAME_AWAYTEAM));
    }

    private String getGameOrtFromDB(Cursor cursor) {
        //Hometeam-Feld aus DB auslesen
        return cursor.getString(cursor.getColumnIndex(COLUMN_GAME_PLACE));
    }


    private void backupDB() {
        try {
            writeToSD();
            Log.d(TAG, CLASS_NAME + ": DB auf auf SD-Karte gespeichert");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToSD() throws IOException {
        File sd = Environment.getExternalStorageDirectory();

        String DB_PATH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DB_PATH = context.getFilesDir().getAbsolutePath().replace("files", "databases") + File.separator;
        } else {
            DB_PATH = context.getFilesDir().getPath() + context.getPackageName() + "/databases/";
        }

        //Falls SD-Karte beschreibbar ist
        if (sd.canWrite()) {
            String currentDBPath = DB_NAME;
            String backupDBPath = "livestream.db";
            File currentDB = new File(DB_PATH, currentDBPath);
            File backupDB = new File(sd, backupDBPath);
            //--falls eine Datenbank existiert
            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
        }
    }


}
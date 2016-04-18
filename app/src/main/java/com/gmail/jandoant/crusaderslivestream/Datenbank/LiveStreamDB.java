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
import com.gmail.jandoant.crusaderslivestream.Spiel.Team;

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

    //TABELLE Games
    public static final String TABLE_GAMES = "games";
    //TABELLE Teams
    public static final String TABLE_TEAMS = "teams";
    //DATENBANK
    private static final String DB_NAME = "livestream.db";
    private static final int DB_VERSION = 1;
    private final String TAG = "LiveStream";
    private final String CLASS_NAME = "LiveStreamDB";
    //--SPALTEN Games
    private final String COLUMN_GAME_ID = "_id";
    private final String COLUMN_GAME_DATE = "gamedate";
    private final String COLUMN_GAME_TIME = "gametime";
    private final String COLUMN_GAME_PLACE = "gameplace";
    private final String COLUMN_GAME_AWAYTEAM = "awayteam_id";
    private final String COLUMN_GAME_HOMETEAM = "hometeam_id";
    private final String COLUMN_GAME_RESULT_HOME = "result_home";
    private final String COLUMN_GAME_RESULT_AWAY = "result_away";
    private final String COLUMN_GAME_QUARTER = "gamequarter";
    private final String COLUMN_GAME_TIMESTAMP = "timestamp_game";
    //--CREATE Games
    private final String QUERY_CREATE_TABLE_GAMES = "CREATE TABLE IF NOT EXISTS " + TABLE_GAMES + "(" +
            COLUMN_GAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            COLUMN_GAME_DATE + " TEXT ," +
            COLUMN_GAME_TIME + " TEXT ," +
            COLUMN_GAME_HOMETEAM + " INTEGER ," +
            COLUMN_GAME_AWAYTEAM + " INTEGER ," +
            COLUMN_GAME_RESULT_HOME + " INTEGER ," +
            COLUMN_GAME_RESULT_AWAY + " INTEGER ," +
            COLUMN_GAME_PLACE + " TEXT ," +
            COLUMN_GAME_QUARTER + " INTEGER ," +
            COLUMN_GAME_TIMESTAMP + " INTEGER " +
            ");";
    //--SPALTEN Teams
    private final String COLUMN_TEAM_ID = "_id";
    private final String COLUMN_TEAM_NAME = "name";
    private final String COLUMN_TEAM_ABKUERZUNG = "abkuerzung";
    private final String COLUMN_TEAM_ALTERSKLASSE = "altersklasse";
    private final String COLUMN_TEAM_ISCHEMNITZ = "bool_is_chemnitz";
    //--CREATE Teams
    private final String QUERY_CREATE_TABLE_TEAMS = "CREATE TABLE IF NOT EXISTS " + TABLE_TEAMS + "(" +
            COLUMN_TEAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            COLUMN_TEAM_NAME + " TEXT ," +
            COLUMN_TEAM_ABKUERZUNG + " TEXT ," +
            COLUMN_TEAM_ISCHEMNITZ + " INTEGER ," +
            COLUMN_TEAM_ALTERSKLASSE + " INTEGER " +
            ");";


    Context context;


    //Konstruktor
    public LiveStreamDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //DB mit diesen Tabellen erstellen
        db.execSQL(QUERY_CREATE_TABLE_GAMES);
        db.execSQL(QUERY_CREATE_TABLE_TEAMS);
        backupDB();
        Log.d(TAG, CLASS_NAME + ": eine neue Datenbank wurde erstellt");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS);

        onCreate(db);
        Log.d(TAG, CLASS_NAME + ": Datenbank onUpgrade()");
    }

    public void clearDbTable(String tableName) {
        //Tabelle der DB löschen
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        //--Backup der DB auf SD-Karte schreiben
        backupDB();
        //leere Tabelle mit den gleichen Spalten erstellen
        onCreate(db);
    }


    public void addGame(Game game) {
        //ein Spiel zur DB hinzufügen
        //Timestamp erzeugen
        DateTime now = new DateTime();
        long timestamp = now.getMillis();

        //--Daten des übergebenen Spiels auslesen
        ContentValues values = new ContentValues();
        //DATUM UND UHRZEIT
        values.put(COLUMN_GAME_DATE, game.getGameDate().toString());
        values.put(COLUMN_GAME_TIME, game.getGameTime().toString());
        //GEGNER
        values.put(COLUMN_GAME_HOMETEAM, game.getHomeTeam().getId());
        values.put(COLUMN_GAME_AWAYTEAM, game.getAwayTeam().getId());
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

    public void addTeam(Team team) {
        //ein Team zur DB hinzufügen
        //--Daten des übergebenen Teams auslesen
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEAM_NAME, team.getName());
        values.put(COLUMN_TEAM_ABKUERZUNG, team.getAbkuerzung());
        values.put(COLUMN_TEAM_ALTERSKLASSE, team.getAltersklasse());

        if (team.isChemnitzTeam()) {
            values.put(COLUMN_TEAM_ISCHEMNITZ, 1);
        } else if (!team.isChemnitzTeam()) {
            values.put(COLUMN_TEAM_ISCHEMNITZ, 0);
        }

        //--Daten in TABELLE_TEAMS einfügen
        SQLiteDatabase db = getWritableDatabase();
        long row = db.insert(TABLE_TEAMS, null, values);

        //--Check ob Operation erfolgreich
        if (row != -1) {
            Toast.makeText(context, "Die " + team.getName() + " wurden erfolgreich in DB eingefügt", Toast.LENGTH_SHORT).show();
            backupDB();
        } else {
            Log.d(TAG, CLASS_NAME + ": Fehler beim Erstellen des Teams: " + team.getName() + "in DB!");
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

    public ArrayList<Team> dbTeamsDataToArrayByAge(int altersklasse) {

        //neue ArrayListe estellen
        ArrayList<Team> arrayListTeams = new ArrayList<Team>();

        //Datenbank-Abfrage (DB-ID, Datum, Uhrzeit, Heimteam, Auswärtsteam, Punktestand Heim/Auswärts) --> in Cursor speichern
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TEAMS +
                " WHERE " + COLUMN_TEAM_ALTERSKLASSE + " = " + altersklasse +
                " ORDER BY " + COLUMN_TEAM_ID + " ASC";
        Cursor myCursor = db.rawQuery(query, null);

        //alle Ergebnisse der Abfrage durchgehen und nacheinander in ArrayList speichern
        myCursor.moveToFirst();
        while (!myCursor.isAfterLast()) {
            Team myTeam = new Team();
            //dem Spiel-Objekt die Werte aus der DB zuweisen
            setTeamFieldsFromDB(myCursor, myTeam);
            //Den Spieler der aktuellen Cursorposition der ArrayList hinzufügen
            arrayListTeams.add(myTeam);
            //Cursor zur nächsten Position weiterwandern lassen
            myCursor.moveToNext();
        }
        //gibt alle GameObjekte der DB in ArrayList aus
        return arrayListTeams;
    }

    private void setGameFieldsFromDB(Cursor myCursor, Game myGame) {
        //--ID
        myGame.set_id(myCursor.getInt(myCursor.getColumnIndex(COLUMN_GAME_ID)));
        //--Datum
        String strGameDate = myCursor.getString(myCursor.getColumnIndex(COLUMN_GAME_DATE));
        myGame.setGameDate(new LocalDate(strGameDate));
        //--Uhrzeit
        String strGameTime = myCursor.getString(myCursor.getColumnIndex(COLUMN_GAME_TIME));
        myGame.setGameTime(new LocalTime(strGameTime));
        //--Gegner
        int homeTeamId = myCursor.getInt(myCursor.getColumnIndex(COLUMN_GAME_HOMETEAM));
        myGame.setHomeTeam(getTeamFromDbByID(homeTeamId));
        int awayTeamId = myCursor.getInt(myCursor.getColumnIndex(COLUMN_GAME_AWAYTEAM));
        myGame.setAwayTeam(getTeamFromDbByID(awayTeamId));
        //--Quarter
        myGame.setQuarter(myCursor.getInt(myCursor.getColumnIndex(COLUMN_GAME_QUARTER)));
        //--Aktueller Punktestand
        myGame.setAktuellePunkteHome(myCursor.getInt(myCursor.getColumnIndex(COLUMN_GAME_RESULT_HOME)));
        myGame.setAktuellePunkteAway(myCursor.getInt(myCursor.getColumnIndex(COLUMN_GAME_RESULT_AWAY)));
        //--Timestamp
        long timestamp = myCursor.getInt(myCursor.getColumnIndex(COLUMN_GAME_TIMESTAMP));
        myGame.setTimestamp(new DateTime(timestamp));
        //Spielort
        myGame.setGameOrt(myCursor.getString(myCursor.getColumnIndex(COLUMN_GAME_PLACE)));
    }

    private void setTeamFieldsFromDB(Cursor myCursor, Team myTeam) {

        //--ID
        myTeam.setId(myCursor.getInt(myCursor.getColumnIndex(COLUMN_TEAM_ID)));
        //--Name
        myTeam.setName(myCursor.getString(myCursor.getColumnIndex(COLUMN_TEAM_NAME)));
        //--Abkürzung
        myTeam.setAbkuerzung(myCursor.getString(myCursor.getColumnIndex(COLUMN_TEAM_ABKUERZUNG)));
        //--Altersklasse
        myTeam.setAltersklasse(myCursor.getInt(myCursor.getColumnIndex(COLUMN_TEAM_ALTERSKLASSE)));
        //ChemnitzTeam?
        int isChemnitzTeam = myCursor.getInt(myCursor.getColumnIndex(COLUMN_TEAM_ISCHEMNITZ));
        if (isChemnitzTeam == 0) {
            myTeam.setChemnitzTeam(false);
        } else if (isChemnitzTeam == 1) {
            myTeam.setChemnitzTeam(true);
        }
    }


    public Game getGameFromDbByID(int gameID) {
        Game myGame = new Game();
        //Datenbank-Abfrage
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GAMES + " WHERE " + COLUMN_GAME_ID + " = " + gameID;
        Cursor myCursor = db.rawQuery(query, null);
        //Datenbankfelder in GameObjekt speichern
        myCursor.moveToFirst();
        setGameFieldsFromDB(myCursor, myGame);
        return myGame;
    }

    public Team getTeamFromDbByID(int teamID) {

        Team myTeam = new Team();
        //Datenbank-Abfrage
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TEAMS + " WHERE " + COLUMN_TEAM_ID + " = " + teamID;
        Cursor myCursor = db.rawQuery(query, null);
        //Datenbanfeder in TeamObjekt speichern
        myCursor.moveToFirst();
        setTeamFieldsFromDB(myCursor, myTeam);
        return myTeam;
    }

    public void updateGameQuarterInDB(Game myGame) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues updateValues = new ContentValues();
        updateValues.put("GameQuarter", myGame.getQuarter());
        db.update(TABLE_GAMES, updateValues, COLUMN_GAME_ID + " = " + myGame.get_id(), null);
        Log.d(TAG, CLASS_NAME + " Update durchgeführt " + myGame.getQuarter());
        backupDB();
        db.close();
    }

    public void updatePunkteHomeinDB(Game myGame) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "UPDATE " + TABLE_GAMES +
                " SET " + COLUMN_GAME_RESULT_HOME + " = " + myGame.getAktuellePunkteHome() +
                " WHERE " + COLUMN_GAME_ID + " = " + myGame.get_id();
        db.execSQL(query);
        backupDB();
        db.close();
    }

    public void updatePunkteAwayinDB(Game myGame) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "UPDATE " + TABLE_GAMES +
                " SET " + COLUMN_GAME_RESULT_AWAY + " = " + myGame.getAktuellePunkteAway() +
                " WHERE " + COLUMN_GAME_ID + " = " + myGame.get_id();
        db.execSQL(query);
        backupDB();
        db.close();
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
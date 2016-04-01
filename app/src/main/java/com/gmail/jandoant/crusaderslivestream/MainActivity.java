package com.gmail.jandoant.crusaderslivestream;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.jandoant.crusaderslivestream.Datenbank.LiveStreamDB;
import com.gmail.jandoant.crusaderslivestream.Spiel.Game;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

/**
 * LAUNCHER ACTIVITY
 * 1. prüft ob bereits ein Twitter-Login besteht
 * 1.1 falls nicht, wird der User zur TwitterLoginActivity weitergeleitet
 * 1.2.falls ja, kann der User entscheiden, ob er einen LiveStream für ein Spiel oder eine neue Trainingsankündigung erstellen möchte
 * 2. je nach Entscheidung (Klick auf Button) wird der User an die SpielActivity_alt oder TrainingActivity weitergeleitet
 * 3. ein OptionsMenu für das Twitter-LogOut wird erstellt
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    //Twitter
    private static final String TWITTER_KEY = "auGACOKWN30mo7CvA1zEDsepl";
    private static final String TWITTER_SECRET = "o0ATvvqaDA81gMPhmcneWYNR90pOoKeN3apLuFBsWj04PO2G1A";
    private final String TAG = "LiveStream";
    private final String CLASS_NAME = "MainActivity";
    TwitterSession jdActiveTwitterSession;
    //UI
    Toolbar toolbar;
    FloatingActionButton fab_createNewGame;
    TextView tv_games;
    //Daten
    //--Datenbank
    LiveStreamDB db;
    //--Recycler View
    ArrayList<Game> gameArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpUI();


        /*
        //Twitter-Login + SetUpUI()
        manageTwitterLogin();
        */

        //Datenbankverbindung aufbauen
        db = new LiveStreamDB(this, null, null, 1);
        Log.d(TAG, CLASS_NAME + ": onResume()");
        //Aktuelle Array-Liste auslesen


    }//ENDE onCreate()

    @Override
    protected void onResume() {
        super.onResume();
        updateGameList();
    }

    private void manageTwitterLogin() {
        //Aktive Twitter Session ermitteln
        jdActiveTwitterSession = getActiveTwitterSession();

        //Prüfen on Nutzer bereits eine aktive Twitter-Session auf dem Gerät besitzt
        if (jdActiveTwitterSession == null) {
            //noch keine aktive Twitter-Session vorhanden-->TwitterLoginActivity starten
            Intent intent = new Intent(getApplicationContext(), TwitterLoginActivity.class);
            startActivity(intent);
            /*
            MainActivity schließen um doppeltes Starten dieser Activity zu vermeiden,
            da TwitterLoginActicity wieder dahin zurückführt
            */
            finish();
        } else {
            //es ist bereits aktive Twitter-Session vorhanden
            setUpUI();
        }
    }

    private TwitterSession getActiveTwitterSession() {
        TwitterSession activeTwitterSession;
        //APP-bei Twitter und Fabric anmelden
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        //Aktuelle Twitter-Session ermitteln
        activeTwitterSession = Twitter.getSessionManager().getActiveSession();
        return activeTwitterSession;
    }

    /**
     * Methode setzt die UI auf
     * 1. Verknüpfen der UI-Elemente mit XML
     * 2. Button bei onClick-Listener registrieren
     */
    private void setUpUI() {
        //Activity-Layout mit XML verknüpfen
        setContentView(R.layout.activity_main);
        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        //TextView
        tv_games = (TextView) findViewById(R.id.tv_games);
        //FAB-neues Spiel erstellen
        fab_createNewGame = (FloatingActionButton) findViewById(R.id.fab_creategame_main);
        /* Was passiert, wenn Spiel-Button gedrückt wird? */
        fab_createNewGame.setOnClickListener(this);

    }//ENDE setUpUI()

    /**
     * Anonyme onClick-Methode für jeden registrierten View
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab_creategame_main:
                //Spiel-Activity starten
                startActivity(new Intent(getApplicationContext(), CreateGameActivity.class));
                break;
            default:
                break;
        }
    }//ENDE onClick()


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }//ENDE onCreateOptionsMenu()


    /**
     * Methode definiert, was passsiert, wenn ein Item des OptionsMenus selektiert wurde
     * 1. falls es sich um den LogOut-MenuPunkt handelt, wird ein Toast ausgegeben, der die erfolgreiche Abmeldung verkündet
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.twitter_logout:
                //Twitter LogOut realisieren
                Twitter.logOut();
                //Toast über erfolgreiche Twitter-Session-Abmeldung
                Toast.makeText(MainActivity.this, "Du wurdest erfolgreich abgemeldet!", Toast.LENGTH_SHORT).show();
                //Login-Activity wird aufgerufen, damit sich der Nutzer (wieder/anders)anmelden kann
                startActivity(new Intent(MainActivity.this, TwitterLoginActivity.class));
                /*MainActivity schließen um doppeltes Starten dieser Activity zu vermeiden,
                da TwitterLoginActicity wieder dahin zurückführt
                */
                finish();
                return true;
            case R.id.twitter_clearGameTable:
                //Spieltabelle der DB löschen
                db.clearDBTable(LiveStreamDB.TABLE_GAMES);
                db.close();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }//ENDE onOptionsItemSelected()


    private void postTweet(String tweet_message) {
        StatusesService jdStatusService = Twitter.getApiClient().getStatusesService();

        /*Paramater für den Post:
        > tweet_message--Der zu postende Text
        > mein Tweet ist eine Antwort auf diese Tweet ID
        > Tweet ist nicht jugendfrei?
        > Geo-Koordinaten des Tweets
        > STRING ID der Geolocation
        > sollen Koordinaten angezeigt werden?
        > soll Nutzername in Timeline verborgen werden?
        > IDs von angehangenden Medien
        > Methode, die nach dem Poten ausgeführt werden soll
        */

        //placeID Chemnitz, Sachsen
        String placeIDChemnitz = "971f3290b678d97e";
        jdStatusService.update(tweet_message, null, false, 50.8057521, 12.9325027, placeIDChemnitz, false, false, null, new Callback<Tweet>() {
            //Was passiert, wenn Tweet erfolgreich gepostet wurde?
            @Override
            public void success(Result<Tweet> result) {
                Toast.makeText(getApplicationContext(), "Dein Tweet wurde erfolgreich gesendet", Toast.LENGTH_SHORT).show();
            }

            //Was passiert, wenn Tweet nicht erfolgreich gepostet wurde?
            @Override
            public void failure(TwitterException e) {
                Toast.makeText(getApplicationContext(), "Leider ist etwas schief gelaufen.", Toast.LENGTH_SHORT).show();
                //Ausgabe des Fehlers in der Konsole
                e.printStackTrace();
            }
        });

    }//ENDE postTweet()

    private void updateGameList() {
        gameArrayList = db.dbAllGamesDataToArray();
    }




}//ENDE MainActivity

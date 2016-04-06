package com.gmail.jandoant.crusaderslivestream.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gmail.jandoant.crusaderslivestream.Adapter.GameListAdapter;
import com.gmail.jandoant.crusaderslivestream.Datenbank.LiveStreamDB;
import com.gmail.jandoant.crusaderslivestream.R;
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
public class MainActivity extends AppCompatActivity implements View.OnClickListener, GameListAdapter.OnItemClickListener {

    public static final String BUNDLE_GAME_ID = "ClickedGameId";

    //Twitter
    private static final String TWITTER_KEY = "auGACOKWN30mo7CvA1zEDsepl";
    private static final String TWITTER_SECRET = "o0ATvvqaDA81gMPhmcneWYNR90pOoKeN3apLuFBsWj04PO2G1A";
    static ArrayList<Game> gameArrayList;
    private final String TAG = "LiveStream";
    private final String CLASS_NAME = "MainActivity";
    TwitterSession jdActiveTwitterSession;
    //UI
    Toolbar toolbar;
    FloatingActionButton fab_createNewGame;
    RecyclerView rv_gamecards;
    //Daten
    //--Datenbank
    LiveStreamDB db;
    //--Recycler View
    GameListAdapter gameListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Datenbankverbindung aufbauen
        db = new LiveStreamDB(this);
        setUpUI();
        //Twitter-Login + SetUpUI()
        //manageTwitterLogin();

    }//ENDE onCreate()

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
        gameListAdapter.setOnItemClickListener(this);
    }

    private void setUpUI() {
        setContentView(R.layout.activity_main);
        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        //RecyclerView
        rv_gamecards = (RecyclerView) findViewById(R.id.rv_gamecards);
        //-LayoutManager
        RecyclerView.LayoutManager myLayoutManager = new LinearLayoutManager(this);
        //--Layout-Manager mit RecyclerView verknüpfen
        rv_gamecards.setLayoutManager(myLayoutManager);
        //--Scrolling-Verhalten
        rv_gamecards.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getScrollState() != 0) {
                    fab_createNewGame.hide();
                } else {
                    fab_createNewGame.show();
                }
            }
        });
        //FAB
        fab_createNewGame = (FloatingActionButton) findViewById(R.id.fab_creategame_main);
        //--onClick registrieren
        if (fab_createNewGame != null) {
            fab_createNewGame.setOnClickListener(this);
        }

    }//ENDE setUpUI()

    private void updateUI() {
        //aktuelle Spielliste auslesen
        gameArrayList = db.dbAllGamesDataToArray();
        gameListAdapter = new GameListAdapter(gameArrayList);
        rv_gamecards.setAdapter(gameListAdapter);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab_creategame_main:
                //Spiel-Activity starten
                startActivity(new Intent(getApplicationContext(), CreateGameActivity.class));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.twitter_logout:
                //Twitter LogOut realisieren
                Twitter.logOut();
                Toast.makeText(MainActivity.this, "Du wurdest erfolgreich abgemeldet!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, TwitterLoginActivity.class));
                finish();
                break;
            case R.id.clearGameTable:
                //Spieltabelle der DB löschen
                db.clearDbTable(LiveStreamDB.TABLE_GAMES);
                db.clearDbTable(LiveStreamDB.TABLE_TEAMS);
                updateUI();
                break;
            case R.id.create_team:
                Intent intent = new Intent(MainActivity.this, CreateTeamActivity.class);
                startActivity(intent);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }//ENDE onOptionsItemSelected()

    @Override
    public void onItemClick(int position, View view) {
        Intent intent = new Intent(MainActivity.this, GameDetailActivity.class);
        //Game-ID an Detail Activity schicken
        int gameID = gameArrayList.get(position).get_id();
        intent.putExtra(BUNDLE_GAME_ID, gameID);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);




    }//ENDE onItemClick()

    @Override
    public void onItemLongClick(int position, View view) {

    }//ENDE on ItemLongClick()


    // ######################################### TWITTER ####################################################
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
    }//ENDE getActiveTwitterSession()

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




}//ENDE MainActivity

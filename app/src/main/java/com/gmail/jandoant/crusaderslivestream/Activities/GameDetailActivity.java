package com.gmail.jandoant.crusaderslivestream.Activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.gmail.jandoant.crusaderslivestream.Datenbank.LiveStreamDB;
import com.gmail.jandoant.crusaderslivestream.Fragments.HeaderFragment;
import com.gmail.jandoant.crusaderslivestream.Fragments.KickoffFragment;
import com.gmail.jandoant.crusaderslivestream.Fragments.PlayFragment;
import com.gmail.jandoant.crusaderslivestream.R;
import com.gmail.jandoant.crusaderslivestream.Spiel.Game;

public class GameDetailActivity extends FragmentActivity {

    //Daten
    LiveStreamDB db;
    Game myGame;
    int gameID;
    Bundle extras;
    //Fragments
    private KickoffFragment kickoffFragment;
    private HeaderFragment headerFragment;
    private PlayFragment playFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        //empfängt die aus der MainActivity übergebene Game-ID
        extras = getIntent().getExtras();
        gameID = extras.getInt(MainActivity.BUNDLE_GAME_ID);

        //Datenbankzugriff
        db = new LiveStreamDB(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //aktuellstes Gme-Objekt aus der DB-auslesen (deshalb in onResume() )
        myGame = db.getGameFromDbByID(gameID);
        //Fragments instanziieren
        headerFragment = (HeaderFragment) Fragment.instantiate(this, HeaderFragment.class.getName(), extras);
        kickoffFragment = (KickoffFragment) Fragment.instantiate(this, KickoffFragment.class.getName(), null);
        playFragment = (PlayFragment) Fragment.instantiate(this, PlayFragment.class.getName(), null);

        //FragmentTransaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.header_gamedetail, headerFragment);
        if (myGame.getQuarter() == 0) {
            transaction.add(R.id.content_gamedetail, kickoffFragment);
        } else {
            transaction.add(R.id.content_gamedetail, playFragment);
        }
        transaction.commit();




    }


}

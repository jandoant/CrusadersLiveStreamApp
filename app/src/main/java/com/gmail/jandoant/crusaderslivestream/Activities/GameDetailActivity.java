package com.gmail.jandoant.crusaderslivestream.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gmail.jandoant.crusaderslivestream.Datenbank.LiveStreamDB;
import com.gmail.jandoant.crusaderslivestream.R;
import com.gmail.jandoant.crusaderslivestream.Spiel.Game;

public class GameDetailActivity extends AppCompatActivity {

    //Daten
    LiveStreamDB db;
    Game myGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        //Datenbankzugriff
        db = new LiveStreamDB(this);

        //empfängt die aus der MainActivity übergebene Game-ID
        Bundle extras = getIntent().getExtras();
        int gameID = extras.getInt("ClickedGameID");

        myGame = db.getGameFromDbByID(gameID);

        Toast.makeText(GameDetailActivity.this, String.valueOf(myGame.get_id()), Toast.LENGTH_SHORT).show();


        //ToDo: Anzeige der zum Spiel gehörenden Plays, Create Plays Activity starten


    }
}

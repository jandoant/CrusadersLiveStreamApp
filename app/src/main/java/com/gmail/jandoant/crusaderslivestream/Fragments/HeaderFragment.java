package com.gmail.jandoant.crusaderslivestream.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gmail.jandoant.crusaderslivestream.Activities.MainActivity;
import com.gmail.jandoant.crusaderslivestream.Datenbank.LiveStreamDB;
import com.gmail.jandoant.crusaderslivestream.R;
import com.gmail.jandoant.crusaderslivestream.Spiel.Game;


public class HeaderFragment extends Fragment {
    //UI
    View fragmentLayout;
    Toolbar toolbar;
    FrameLayout fl_container;
    //GAME
    int gameID;
    Game myGame;
    LiveStreamDB db;
    private TextView tv_gameID;
    private TextView tv_gamedate;
    private TextView tv_gametime;
    private TextView tv_quarter;
    private TextView tv_hometeam;
    private TextView tv_awayteam;
    private TextView tv_punkte_home;
    private TextView tv_punkte_away;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new LiveStreamDB(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentLayout = inflater.inflate(R.layout.fragment_header_max_gamedetail, null);
        setUpUI();
        return fragmentLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gameID = getArguments().getInt(MainActivity.BUNDLE_GAME_ID);
        myGame = db.getGameFromDbByID(gameID);
        updateUI(myGame);

    }


    private void setUpUI() {
        //Verknüpfung aller Viewa mit XML
        //--Datum und Uhrzeit
        tv_gamedate = (TextView) fragmentLayout.findViewById(R.id.tv_gamedate_gamedetail);
        tv_gametime = (TextView) fragmentLayout.findViewById(R.id.tv_gametime_gamedetail);
        tv_quarter = (TextView) fragmentLayout.findViewById(R.id.tv_quarter_gamedetail);
        //--Gegner
        tv_hometeam = (TextView) fragmentLayout.findViewById(R.id.tv_team_home_gamedetail);
        tv_awayteam = (TextView) fragmentLayout.findViewById(R.id.tv_team_away_gamedetail);
        //Ergebnis
        tv_punkte_home = (TextView) fragmentLayout.findViewById(R.id.tv_punkte_home_gamedetail);
        tv_punkte_away = (TextView) fragmentLayout.findViewById(R.id.tv_punkte_away_gamedetail);
        //GameID
        tv_gameID = (TextView) fragmentLayout.findViewById(R.id.tv_gameid_gamedetail);
    }

    @Override
    public void onResume() {
        //updateUI();
        //TODO: anzeige der korrekten GameDaten im Fragment --> aus MainActivity holen

        super.onResume();
    }


    public void updateUI(Game myGame) {

        //--Datum und Uhrzeit
        tv_gamedate.setText(myGame.getGameDate().toString());
        tv_gametime.setText(myGame.getGameTime().toString());
        tv_quarter.setText(myGame.getStrQuarter());
        //--Gegner
        tv_hometeam.setText(myGame.getHomeTeam());
        tv_awayteam.setText(myGame.getAwayTeam());
        //Ergebnis
        tv_punkte_home.setText(String.valueOf(myGame.getAktuellePunkteHome()));
        tv_punkte_away.setText(String.valueOf(myGame.getAktuellePunkteAway()));
        //GameID
        tv_gameID.setText(String.valueOf(myGame.get_id()));
    }


}

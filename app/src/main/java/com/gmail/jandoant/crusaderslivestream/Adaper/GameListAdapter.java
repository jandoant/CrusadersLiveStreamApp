package com.gmail.jandoant.crusaderslivestream.Adaper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.jandoant.crusaderslivestream.R;
import com.gmail.jandoant.crusaderslivestream.Spiel.Game;

import java.util.ArrayList;

/**
 * Created by Jan on 01.04.2016.
 */
public class GameListAdapter extends RecyclerView.Adapter<GameListViewHolder> {

    //Klassenfelder = Datenquelle
    private ArrayList<Game> gameArrayList;

    //Konstruktor
    public GameListAdapter(ArrayList<Game> gameArrayList) {
        this.gameArrayList = gameArrayList;
    }

    @Override
    public GameListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //leere Platzhalter-View mit Row-Layout erzeugen
        LayoutInflater itemLayoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = itemLayoutInflater.inflate(R.layout.sublayout_rv_main, parent, false);
        //leeres Platzhalter-Objekt mit diesem Layout wird erzeugt
        GameListViewHolder myVh = new GameListViewHolder(itemView);

        return myVh;
    }

    @Override
    public void onBindViewHolder(GameListViewHolder holder, int position) {


        //Daten des Spielers auf dieser Adapterposition aus Player-Objekt auslesen
        //--Datum und Uhrzeit
        String gameDate = gameArrayList.get(position).getGameDate().toString();
        String gameTime = gameArrayList.get(position).getGameTime().toString();
        String quarter = gameArrayList.get(position).getStrQuarter();
        //--Gegner
        String homeTeam = gameArrayList.get(position).getHomeTeam();
        String awayTeam = gameArrayList.get(position).getAwayTeam();
        //--Punktestand
        int punkteHome = gameArrayList.get(position).getAktuellePunkteHome();
        int punkteAway = gameArrayList.get(position).getAktuellePunkteAway();

        //UI-Elemente des ViewHolders mit diesen Daten füllen
        //--Datum und Uhrzeit
        holder.tv_gamedate.setText(gameDate);
        holder.tv_gametime.setText(gameTime);
        holder.tv_quarter.setText(quarter);
        //--Gegner
        holder.tv_hometeam.setText(homeTeam);
        holder.tv_awayteam.setText(awayTeam);
        //Ergebnis
        holder.tv_punkte_home.setText(String.valueOf(punkteHome));
        holder.tv_punkte_away.setText(String.valueOf(punkteAway));

    }

    @Override
    public int getItemCount() {
        return gameArrayList.size();
    }
}

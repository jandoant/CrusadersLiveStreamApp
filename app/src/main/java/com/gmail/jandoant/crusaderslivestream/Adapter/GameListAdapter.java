package com.gmail.jandoant.crusaderslivestream.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.jandoant.crusaderslivestream.R;
import com.gmail.jandoant.crusaderslivestream.Spiel.Game;

import java.util.ArrayList;

/**
 * Created by Jan on 01.04.2016.
 */
public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameListViewHolder> {

    private static OnItemClickListener onItemClickListener;


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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        GameListAdapter.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }

    public class GameListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        //alle dynamisch erzeugten Views (aus Datenquelle)
        //--Datum und Uhrzeit
        public TextView tv_gamedate;
        public TextView tv_gametime;
        public TextView tv_quarter;
        //--Gegner
        public TextView tv_hometeam;
        public TextView tv_awayteam;
        //Ergebnis
        public TextView tv_punkte_home;
        public TextView tv_punkte_away;
        //CardView
        public CardView cv_game_card;

        public GameListViewHolder(View itemView) {
            super(itemView);

            //Verknüpfung mit XML
            //--Datum und Uhrzeit
            tv_gamedate = (TextView) itemView.findViewById(R.id.tv_gamedate_gamecard);
            tv_gametime = (TextView) itemView.findViewById(R.id.tv_gametime_gamecard);
            tv_quarter = (TextView) itemView.findViewById(R.id.tv_quarter_gamecard);
            //--Gegner
            tv_hometeam = (TextView) itemView.findViewById(R.id.tv_team_home_gamecard);
            tv_awayteam = (TextView) itemView.findViewById(R.id.tv_team_away_gamecard);
            //Ergebnis
            tv_punkte_home = (TextView) itemView.findViewById(R.id.tv_punkte_home_gamecard);
            tv_punkte_away = (TextView) itemView.findViewById(R.id.tv_punkte_away_gamecard);
            //--CardView
            cv_game_card = (CardView) itemView.findViewById(R.id.cv_gamecard);
            cv_game_card.setClickable(true);
            cv_game_card.setOnClickListener(this);


        }

        public View getItemView() {
            return this.itemView;
        }


        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(getAdapterPosition(), view);
        }


        @Override
        public boolean onLongClick(View view) {
            onItemClickListener.onItemLongClick(getAdapterPosition(), view);
            return true;
        }
    }




}

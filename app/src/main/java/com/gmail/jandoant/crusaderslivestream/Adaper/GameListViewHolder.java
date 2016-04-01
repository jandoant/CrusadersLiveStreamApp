package com.gmail.jandoant.crusaderslivestream.Adaper;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gmail.jandoant.crusaderslivestream.R;

/**
 * Created by Jan on 01.04.2016.
 */
public class GameListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

    }


}

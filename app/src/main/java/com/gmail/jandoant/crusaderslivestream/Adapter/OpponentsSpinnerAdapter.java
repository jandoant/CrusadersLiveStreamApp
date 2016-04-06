package com.gmail.jandoant.crusaderslivestream.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gmail.jandoant.crusaderslivestream.R;
import com.gmail.jandoant.crusaderslivestream.Spiel.Team;

import java.util.ArrayList;

/**
 * Created by Jan on 06.04.2016.
 */
public class OpponentsSpinnerAdapter extends ArrayAdapter<Team> {

    Context context;
    ArrayList<Team> teamArrayList;
    int sublayoutResource;

    TextView tv_teamname;


    public OpponentsSpinnerAdapter(Context context, ArrayList<Team> teamArrayList) {
        super(context, R.layout.sublayout_spinner_opponents, teamArrayList);
        this.context = context;
        this.teamArrayList = teamArrayList;
        this.sublayoutResource = R.layout.sublayout_spinner_opponents;
    }

    private View getCustomView(int position) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(sublayoutResource, null);

        tv_teamname = (TextView) layout.findViewById(R.id.tv_teamname_spinner);

        Team myTeam = teamArrayList.get(position);

        tv_teamname.setText(myTeam.getName());


        return layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position);
    }
}

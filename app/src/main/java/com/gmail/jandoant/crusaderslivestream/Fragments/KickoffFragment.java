package com.gmail.jandoant.crusaderslivestream.Fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gmail.jandoant.crusaderslivestream.R;

public class KickoffFragment extends Fragment implements View.OnClickListener {


    //Fragment Layout
    View fragmentLayout;
    Button btn_kickoff;
    //Listener
    OnKickoffButtonClickListener myCallback;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentLayout = inflater.inflate(R.layout.fragment_kickoff, null);
        btn_kickoff = (Button) fragmentLayout.findViewById(R.id.btn_kickoff_plays);
        btn_kickoff.setOnClickListener(this);
        return fragmentLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            myCallback = (OnKickoffButtonClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnKickoffButtonClickListener");
        }


    }

    @Override
    public void onClick(View view) {
        myCallback.onKickoffButtonClick(view);
    }

    // Container Activity must implement this interface
    public interface OnKickoffButtonClickListener {
        void onKickoffButtonClick(View view);
    }



}

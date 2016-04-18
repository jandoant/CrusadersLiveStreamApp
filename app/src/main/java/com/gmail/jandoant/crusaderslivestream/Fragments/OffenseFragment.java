package com.gmail.jandoant.crusaderslivestream.Fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gmail.jandoant.crusaderslivestream.R;
import com.gmail.jandoant.crusaderslivestream.Spiel.Play;

/**
 * Auswahl der Offense-Spielzüge
 */
public class OffenseFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    ImageButton btn_back_pass, btn_ok_pass;
    Button btn_td, btn_fg, btn_bv, btn_run, btn_pass;
    LinearLayout layout_container_td, layout_container_fg, layout_container_bv;
    View layout, sublayout_td;

    EditText et_qb, et_wr;

    CheckBox checkbox_xp1, checkbox_xp2;
    Button btn_tweet;

    //Spielzug
    Play myPlay;
    String event;
    String qb, wr;
    String team;

    //Listener
    OnOffenseTweetClickListener myCallback;




    public OffenseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        qb = null;
        wr = null;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate MainLayout des Fragments
        layout = inflater.inflate(R.layout.fragment_offense, container, false);
        //Buttons
        btn_td = (Button) layout.findViewById(R.id.btn_td_offense);
        btn_fg = (Button) layout.findViewById(R.id.btn_fg_offense);
        btn_bv = (Button) layout.findViewById(R.id.btn_bv_offense);

        //Sub-Layout-Container
        layout_container_td = (LinearLayout) layout.findViewById(R.id.container_sublayout_td_offense);
        layout_container_fg = (LinearLayout) layout.findViewById(R.id.ll_fg_offense);
        layout_container_bv = (LinearLayout) layout.findViewById(R.id.ll_bv_offense);
        //--Sichtbarkeit
        layout_container_td.setVisibility(View.GONE);
        layout_container_fg.setVisibility(View.GONE);
        layout_container_bv.setVisibility(View.GONE);


        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Hauptbuttons des Fragment Layouts
        btn_td.setOnClickListener(this);
        btn_fg.setOnClickListener(this);
        btn_bv.setOnClickListener(this);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            myCallback = (OnOffenseTweetClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnOffenseTweetClickListener");
        }


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //Klick auf TD-Button
            case R.id.btn_td_offense:
                Toast.makeText(getContext(), "TD", Toast.LENGTH_SHORT).show();
                btn_td.setText("TD");

                //Anzeige Steuern
                layout_container_td.setVisibility(View.VISIBLE);
                layout_container_fg.setVisibility(View.GONE);
                layout_container_bv.setVisibility(View.GONE);

                //Event des Spielzuges festlegen
                myPlay = new Play();
                myPlay.setTeam("Offense");
                myPlay.setEvent("Touchdown");

                //Run/Pass Dialog wird angezeigt (Auswahl Pass oder Run)
                setUpPassRunDialog();
                break;

            //-Auswahl PASStouchdown
            case R.id.btn_pass_offense:
                //Pass Dialog wird angezeigt (Auswahl QB und WR)
                setUpPassDialog();
                myPlay.setType("Pass");
                break;
            //--Auswahl back von PASS
            case R.id.btn_back_pass_offense:
                //erneut Anzeige Run/Pass Dialog
                setUpPassRunDialog();
                break;
            //--Auswahl ok von PASS
            case R.id.btn_ok_pass_offense:
                //In ExtraPunkt-Dialog wechseln, wenn beide Felder ausgefüllt sind
                if (!isEmpty(et_wr) && !isEmpty(et_qb)) {
                    myPlay.setQb(et_qb.getText().toString());
                    myPlay.setWr(et_wr.getText().toString());
                    setUpXpDialog();
                }

                //Hochzählen des Spielstandes um 6 Punkte für Chemnitz-Team
                //Tweeten
                //zurück zu Offense-Fragment Main Ansicht
                break;
            //--Auswahl Tweet bei TD
            case R.id.btn_tweet_td_offense:
                myCallback.onOffenseTweetClick(view, myPlay);


                //Hochzählen des Spielstandes um 6 Punkte für Chemnitz-Team
                //Tweeten


                break;


            //Auswahl RUNspielzug
            case R.id.btn_run_offense:
                Toast.makeText(getContext(), "Run", Toast.LENGTH_SHORT).show();
                break;


            //Klick auf FG-Button
            case R.id.btn_fg_offense:
                layout_container_td.setVisibility(View.GONE);
                layout_container_fg.setVisibility(View.VISIBLE);
                layout_container_bv.setVisibility(View.GONE);
                btn_fg.setText("FG");
                //Event des Spielzuges festlegen
                myPlay = new Play();
                myPlay.setEvent("Fieldgoal");
                myPlay.setTeam("Offense");
                break;

            //Klick auf Ballverlust-Button
            case R.id.btn_bv_offense:

                layout_container_td.setVisibility(View.GONE);
                layout_container_fg.setVisibility(View.GONE);
                layout_container_bv.setVisibility(View.VISIBLE);
                btn_td.setText("BV");
                //Event des Spielzuges festlegen
                myPlay = new Play();
                myPlay.setEvent("Ballverlust");
                myPlay.setTeam("Offense");
                break;


            default:
                break;

        }


    }


    private View showLayout(int layoutResourceId, ViewGroup container) {
        container.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View sublayout = inflater.inflate(layoutResourceId, null);
        container.addView(sublayout);
        return sublayout;
    }

    private void setUpPassRunDialog() {
        //Sublayout Lauf/Pass-Auswahl in container laden
        sublayout_td = showLayout(R.layout.sublayout_offense_td_runpass, layout_container_td);
        btn_pass = (Button) sublayout_td.findViewById(R.id.btn_pass_offense);
        btn_pass.setOnClickListener(this);
    }

    private void setUpPassDialog() {
        //setUp Passdialog
        sublayout_td = showLayout(R.layout.sublayout_offense_td_pass, layout_container_td);
        //--Buttons
        btn_back_pass = (ImageButton) sublayout_td.findViewById(R.id.btn_back_pass_offense);
        btn_ok_pass = (ImageButton) sublayout_td.findViewById(R.id.btn_ok_pass_offense);
        btn_back_pass.setOnClickListener(this);
        btn_ok_pass.setOnClickListener(this);
        //--EditTexts
        et_qb = (EditText) sublayout_td.findViewById(R.id.et_qb_offense);
        et_wr = (EditText) sublayout_td.findViewById(R.id.et_wr_offense);
    }

    private void setUpXpDialog() {
        sublayout_td = showLayout(R.layout.sublayout_offense_td_xp, layout_container_td);

        //CheckBoxes
        checkbox_xp1 = (CheckBox) sublayout_td.findViewById(R.id.checkbox_xp_offense_1);
        checkbox_xp2 = (CheckBox) sublayout_td.findViewById(R.id.checkbox_xp_offense_2);
        checkbox_xp1.setOnCheckedChangeListener(this);
        checkbox_xp2.setOnCheckedChangeListener(this);

        //Button
        btn_tweet = (Button) sublayout_td.findViewById(R.id.btn_tweet_td_offense);
        btn_tweet.setOnClickListener(this);

    }   //Tweet-Button


    /**
     * ermittelt ob EditText leer ist
     *
     * @param etText
     * @return true, wenn EditText leer ist, wenn etwas drin steht: false
     */
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        //verhindern, dass beide CheckBoxes gechecked sind, aber beide ungechecked sein können (Unterschied zu RadioButton)
        switch (compoundButton.getId()) {

            case R.id.checkbox_xp_offense_1:
                if (b == true) {
                    checkbox_xp2.setChecked(false);
                }
                break;

            case R.id.checkbox_xp_offense_2:
                if (b == true) {
                    checkbox_xp1.setChecked(false);
                }
                break;
        }
    }

    // Container Activity must implement this interface
    public interface OnOffenseTweetClickListener {
        void onOffenseTweetClick(View view, Play myPlay);
    }


}
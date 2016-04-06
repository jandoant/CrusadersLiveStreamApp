package com.gmail.jandoant.crusaderslivestream.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gmail.jandoant.crusaderslivestream.Datenbank.LiveStreamDB;
import com.gmail.jandoant.crusaderslivestream.R;
import com.gmail.jandoant.crusaderslivestream.Spiel.Team;

public class CreateTeamActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    EditText et_name, et_abk;
    Spinner spinner;
    LiveStreamDB db;
    int altersklasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        db = new LiveStreamDB(this);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_createteam);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //EditTextes
        et_name = (EditText) findViewById(R.id.et_name_createteam);
        et_abk = (EditText) findViewById(R.id.et_abkuerzung_createteam);

        //Spinner
        spinner = (Spinner) findViewById(R.id.spinner_alter_createteam);
        //--Array Adapter mit Daten füllen
        ArrayAdapter spinnerToolbarAdapter = ArrayAdapter.createFromResource(this, R.array.altersklassen_array, android.R.layout.simple_spinner_dropdown_item);
        spinnerToolbarAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //--SpinnerAdapter mit Spinner verknüpfen
        spinner.setAdapter(spinnerToolbarAdapter);
        //--onItemSelectedListener registrieren
        spinner.setOnItemSelectedListener(this);

        //Button
        Button btn_submit = (Button) findViewById(R.id.btn_createteam);
        if (btn_submit != null) {
            btn_submit.setOnClickListener(this);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        switch (position) {

            case 0:
                altersklasse = 0;
                break;
            case 1:
                altersklasse = Team.ALTERSKLASSE_HERREN;
                break;
            case 2:
                altersklasse = Team.ALTERSKLASSE_A;
                break;
            case 3:
                altersklasse = Team.ALTERSKLASSE_B;
                break;
            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {

        if (altersklasse != 0 && !isEmpty(et_name) && !isEmpty(et_abk)) {
            createTeam();
            et_name.setText("");
            et_abk.setText("");
            spinner.setSelection(0);
        } else {
            Toast.makeText(CreateTeamActivity.this, "Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show();
        }


    }

    private void createTeam() {
        Team myTeam = new Team();
        myTeam.setName(et_name.getText().toString());
        myTeam.setAbkuerzung(et_abk.getText().toString());
        myTeam.setAltersklasse(altersklasse);
        db.addTeam(myTeam);

    }


    /**
     * ermittelt ob EditText leer ist
     *
     * @param etText
     * @return true, wenn EditText leer ist, wenn etwas drin steht: false
     */
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }


}

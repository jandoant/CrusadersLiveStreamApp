package com.gmail.jandoant.crusaderslivestream;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class CreateGameActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //UI
    Toolbar toolbar;
    Button btn_date, btn_time, btn_createGame;
    DatePickerDialog datepicker;
    TimePickerDialog timepicker;
    TextView tv_date, tv_time;
    Spinner spinner_toolbar, spinner_away, spinner_home;
    int spinnerCounter;

    //Daten
    ArrayAdapter<CharSequence> spinnerOpponentsAdapter;

    String team_away;
    String team_home;
    String[] opponents;
    String[] teamsChemnitz;

    //Datum und Zeit
    DateTime nowDate;
    LocalDate gameDate;
    LocalTime gameTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);


        setUpUI();
        gameTime = null;
        gameDate = null;

        //TODO: create new Game und on submit write infos to DB, up navigation


    }


    /**
     * setUpUI
     * <ol>
     * <li>Toolbar mit Up Navigation</li>
     * <li>Buttons für Datums- und Uhrzeiteingabe</li>
     * <li>TextViews für Datums- und Uhrzeitanzeige</li>
     * <li>Spinner zur Auswahl von Heim- und Auswärtsteam</li>
     * <li>Submit-Button um neues Spiel zu erstellen und Daten in Datenbank zu schreiben </li>
     * </ol>
     */
    private void setUpUI() {

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_creategame);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Buttons
        btn_date = (Button) findViewById(R.id.btn_date_creategame);
        btn_time = (Button) findViewById(R.id.btn_time_creategame);
        btn_createGame = (Button) findViewById(R.id.btn_submit_creategame);
        //--Aktivierbarkeit

        //--onClick-Registration
        btn_date.setOnClickListener(this);
        btn_time.setOnClickListener(this);
        btn_createGame.setOnClickListener(this);

        //Text Views
        tv_date = (TextView) findViewById(R.id.tv_date_creategame);
        tv_time = (TextView) findViewById(R.id.tv_time_creategame);

        //Spinners
        spinnerCounter = 0;
        spinner_toolbar = (Spinner) findViewById(R.id.spinner_toolbar_creategame);
        spinner_away = (Spinner) findViewById(R.id.spinner_away_creategame);
        spinner_home = (Spinner) findViewById(R.id.spinner_home_creategame);
        //--Array Adapter mit Daten füllen
        ArrayAdapter<CharSequence> spinnerToolbarAdapter = ArrayAdapter.createFromResource(this, R.array.chemnitz_teams_array, android.R.layout.simple_spinner_item);
        spinnerOpponentsAdapter = ArrayAdapter.createFromResource(this, R.array.crusaders_opponents_array, android.R.layout.simple_spinner_item);
        //--Dropdown Layout
        spinnerToolbarAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOpponentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //--Adapter mit Spinner verknüpfen
        spinner_toolbar.setAdapter(spinnerToolbarAdapter);
        spinner_away.setAdapter(spinnerOpponentsAdapter);
        spinner_home.setAdapter(spinnerOpponentsAdapter);
        //--onItemClick--Registration
        spinner_toolbar.setOnItemSelectedListener(this);
        spinner_away.setOnItemSelectedListener(this);
        spinner_home.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_date_creategame:
                showDatePickerDialog();
                break;
            case R.id.btn_time_creategame:
                showTimePickerDialog();
                break;
            case R.id.btn_submit_creategame:
                createGame();
                break;
            default:
                break;
        }
    }

    private void createGame() {
        //prüfen, ob alle Felder ausgefüllt wurden
        if (gameDate != null && gameTime != null && team_home != opponents[0] && team_away != opponents[0]) {
            Log.d("TEAMS", "Spiel wurde erstellt");
            Toast.makeText(CreateGameActivity.this, "Spiel wurde erstellt", Toast.LENGTH_SHORT).show();
            //ToDo: Datenbankzugriff und schreiben in diese, nach Erstellung Wechsel in den Create-Play-Bereich
        }
    }

    private void showDatePickerDialog() {
        nowDate = new DateTime().now();

        datepicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //--Eingabe des Users im Datum speichern
                gameDate = new LocalDate().withYear(year).withMonthOfYear(month + 1).withDayOfMonth(day);
                //--nach Auswahl Anzeige in TextViews
                String strYear, strMonth, strDay, strAusgabe;
                strYear = String.valueOf(gameDate.getYear());
                strMonth = String.valueOf(gameDate.getMonthOfYear());
                strDay = String.valueOf(gameDate.getDayOfMonth());
                strAusgabe = strDay + "." + strMonth + "." + strYear;
                tv_date.setText(strAusgabe);

                //--sofort danach TimePicker zeigen
                showTimePickerDialog();
            }
        }, nowDate.getYear(), nowDate.getMonthOfYear() - 1, nowDate.getDayOfMonth());
        datepicker.show();
    }

    private void showTimePickerDialog() {
        nowDate = new DateTime().now();
        timepicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                //--Eingabe des Users in der Uhrzeit speichern
                gameTime = new LocalTime().withHourOfDay(hour).withMinuteOfHour(minute);

                //--nach Eingabe Anzeige in TextView
                String strStunde, strMinute, strAusgabe;
                strStunde = String.valueOf(gameTime.getHourOfDay());
                strMinute = String.valueOf(gameTime.getMinuteOfHour());
                strAusgabe = strStunde + ":" + strMinute + " Uhr";
                tv_time.setText(strAusgabe);
            }
        }, nowDate.getHourOfDay(), nowDate.getMinuteOfHour(), true);
        timepicker.show();


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Log.d("TEAMS", "onItemSelected()");

        switch (adapterView.getId()) {
            //Auswahl Heimteam
            case R.id.spinner_home_creategame:
                Log.d("TEAMS", "onItemSelected(HOME)");
                team_home = opponents[position];
                //--wenn das ausgewählte Heimteam nicht Chemnitz ist, setze Auswärtsteam auf Chemnitz
                if (position != 1) {
                    spinner_away.setSelection(1, true);
                    team_away = opponents[1];
                }
                //--verhindert doppeltes Auftreten von Chemnitz
                if (team_home == opponents[1] && team_away == opponents[1]) {
                    spinner_away.setSelection(0);
                }
                break;
            //Auswahl Auswärtsteam
            case R.id.spinner_away_creategame:
                Log.d("TEAMS", "onItemSelected(AWAY)");
                team_away = opponents[position];
                //--wenn das ausgewählte Auswärtsteam nicht Chemnitz ist, setze Heimteam auf Chemnitz
                if (position != 1) {
                    spinner_home.setSelection(1, true);
                    team_home = opponents[1];
                }
                //--verhindert doppeltes Auftreten von Chemnitz
                if (team_home == opponents[1] && team_away == opponents[1]) {
                    spinner_home.setSelection(0);
                }
                break;
            //Auswahl der Chemnitzes Chemnitzer Teams
            case R.id.spinner_toolbar_creategame:


                switch (position) {
                    //CRUSADERS
                    case 0:
                        opponents = getResources().getStringArray(R.array.crusaders_opponents_array);
                        spinnerOpponentsAdapter = ArrayAdapter.createFromResource(this, R.array.crusaders_opponents_array, android.R.layout.simple_spinner_item);
                        spinner_home.setAdapter(spinnerOpponentsAdapter);
                        spinner_away.setAdapter(spinnerOpponentsAdapter);
                        break;
                    //VARLETS
                    case 1:
                        opponents = getResources().getStringArray(R.array.varlets_opponents_array);
                        spinnerOpponentsAdapter = ArrayAdapter.createFromResource(this, R.array.varlets_opponents_array, android.R.layout.simple_spinner_item);
                        spinner_home.setAdapter(spinnerOpponentsAdapter);
                        spinner_away.setAdapter(spinnerOpponentsAdapter);
                        break;
                    //CLAYMORES
                    case 2:
                        opponents = getResources().getStringArray(R.array.claymores_opponents_array);
                        spinnerOpponentsAdapter = ArrayAdapter.createFromResource(this, R.array.claymores_opponents_array, android.R.layout.simple_spinner_item);
                        spinner_home.setAdapter(spinnerOpponentsAdapter);
                        spinner_away.setAdapter(spinnerOpponentsAdapter);
                        break;
                }
            default:
                break;
        }
        Log.d("TEAMS", "Home= " + team_home + " Away= " + team_away);
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_creategame, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submit_creategame:
                createGame();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.gmail.jandoant.crusaderslivestream.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import com.gmail.jandoant.crusaderslivestream.Adapter.OpponentsSpinnerAdapter;
import com.gmail.jandoant.crusaderslivestream.Datenbank.LiveStreamDB;
import com.gmail.jandoant.crusaderslivestream.R;
import com.gmail.jandoant.crusaderslivestream.Spiel.Game;
import com.gmail.jandoant.crusaderslivestream.Spiel.Team;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;

public class CreateGameActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String CLASS_NAME = "CreateGameActivity";
    private final String TAG = "LiveStream";

    //UI
    Toolbar toolbar;
    Button btn_date;
    Button btn_Date, btn_time, btn_createGame;
    DatePickerDialog datepicker;
    TimePickerDialog timepicker;
    TextView tv_date, tv_time;
    Spinner spinner_toolbar, spinner_away, spinner_home;
    int spinnerCounter;

    //Daten
    //--Spinner
    ArrayAdapter spinnerOpponentsAdapter;
    ArrayAdapter<CharSequence> spinnerToolbarAdapter;
    //Teams
    Team teamAway;
    Team teamHome;
    ArrayList<Team> opponents;
    //--Datenbank
    LiveStreamDB db;
    //--Datum und Zeit
    DateTime nowDate;
    LocalDate gameDate;
    LocalTime gameTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        spinnerCounter = 0;
        //Datenbankzugriff herstellen
        db = new LiveStreamDB(this);


        //UI
        setUpUI();
        gameTime = null;
        gameDate = null;
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
        //--onClick-Registration
        btn_date.setOnClickListener(this);
        btn_time.setOnClickListener(this);
        btn_createGame.setOnClickListener(this);

        //Spinners
        spinner_toolbar = (Spinner) findViewById(R.id.spinner_toolbar_creategame);
        spinner_away = (Spinner) findViewById(R.id.spinner_away_creategame);
        spinner_home = (Spinner) findViewById(R.id.spinner_home_creategame);
        //--Array Adapter mit Daten füllen
        spinnerToolbarAdapter = ArrayAdapter.createFromResource(this, R.array.chemnitz_teams_array, android.R.layout.simple_spinner_dropdown_item);
        //--Dropdown Layout
        spinnerToolbarAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //--Adapter mit Spinner verknüpfen
        spinner_toolbar.setAdapter(spinnerToolbarAdapter);
        //--onClick-Registration
        spinner_toolbar.setOnItemSelectedListener(this);
        spinner_home.setOnItemSelectedListener(this);
        spinner_away.setOnItemSelectedListener(this);


        //Text Views
        tv_date = (TextView) findViewById(R.id.tv_date_creategame);
        tv_time = (TextView) findViewById(R.id.tv_time_creategame);

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
        if (gameDate != null && gameTime != null) {
            Game myGame = new Game(gameDate, gameTime, teamHome, teamAway);
            db.addGame(myGame);
            db.close();
            finish();
        } else {
            Toast.makeText(CreateGameActivity.this, "Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show();
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
        //ToDO: Auswahlkompfort hinzufügen
        switch (adapterView.getId()) {
            //Auswahl Heimteam
            case R.id.spinner_home_creategame:
                spinner_home.setPrompt("Heimteam");
                teamHome = opponents.get(position);
                break;
            //Auswahl Auswärtsteam
            case R.id.spinner_away_creategame:
                spinner_away.setPrompt("Auswärtsteam");
                teamAway = opponents.get(position);
                break;
            //Auswahl der Chemnitzes Chemnitzer Teams über Toolbar
            case R.id.spinner_toolbar_creategame:
                switch (position) {
                    //CRUSADERS
                    case 0:
                        opponents = db.dbTeamsDataToArrayByAge(Team.ALTERSKLASSE_HERREN);
                        break;
                    //VARLETS
                    case 1:
                        opponents = db.dbTeamsDataToArrayByAge(Team.ALTERSKLASSE_A);
                        break;
                    //CLAYMORES
                    case 2:
                        opponents = db.dbTeamsDataToArrayByAge(Team.ALTERSKLASSE_B);
                        break;
                }
                spinnerOpponentsAdapter = new OpponentsSpinnerAdapter(this, opponents);
                spinner_home.setAdapter(spinnerOpponentsAdapter);
                spinner_away.setAdapter(spinnerOpponentsAdapter);
                break;
            default:
                break;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Interface Method Auto-generated
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

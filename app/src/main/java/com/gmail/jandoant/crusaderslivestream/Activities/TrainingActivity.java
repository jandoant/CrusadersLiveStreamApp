package com.gmail.jandoant.crusaderslivestream.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.gmail.jandoant.crusaderslivestream.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TrainingActivity extends Activity {

    // ### UI-Elemente ###
    //CheckBoxes
    CheckBox chkTrainingCRU;
    CheckBox chkTrainingVAR;
    CheckBox chkTrainingCLM;
    CheckBox chkTrainingFK;
    //Button
    Button btnPostTraining;


    String[] teamnamen = {"Crusaders",
            "Varlets",
            "Claymores",
            "Flag Knights"};

    String[] alter = {"ab 19J",
            "von 16-19J",
            "von 13-16J",
            "bis 13J"};

    String[] uhrzeit = {"19.30 Uhr",
            "18 Uhr",
            "16 Uhr",
            "16 Uhr"};

    String[] trainingsort = {"Leichtathletikhalle im Sportforum",
            "Sporthalle Luisenschule",
            "Sporthalle Albert-Schweitzer-Schule",
            "Sporthalle Albert-Schweitzer-Schule"};

    String[] mapsURL = {"www.google.de/maps/@50.8057521,12.9325027,204m/data=!3m1!1e3",
            "www.google.de/maps/place/Untere+Luisenschule/@50.8409534,12.9007113,17z/data=!3m1!4b1!4m2!3m1!1s0x41722a2c05e00b63:0xd8a0babf0389a9e3",
            "www.google.de/maps/place/Albert-K%C3%B6hler-Stra%C3%9Fe+48,+09122+Chemnitz/@50.7928207,12.8883608,242m/data=!3m1!1e3!4m2!3m1!1s0x47a74884569d5375:0xd3cf365476c135f",
            "www.google.de/maps/place/Albert-K%C3%B6hler-Stra%C3%9Fe+48,+09122+Chemnitz/@50.7928207,12.8883608,242m/data=!3m1!1e3!4m2!3m1!1s0x47a74884569d5375:0xd3cf365476c135f"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        setUpUI();


    }

    private void setUpUI() {
        //UI-Elemente mit XML verknüpfen
        chkTrainingCRU = (CheckBox) findViewById(R.id.checkBox_trainingCRU);
        chkTrainingVAR = (CheckBox) findViewById(R.id.checkBox_trainingVAR);
        chkTrainingCLM = (CheckBox) findViewById(R.id.checkBox_trainingCLM);
        chkTrainingFK = (CheckBox) findViewById(R.id.checkBox_trainingFK);
        btnPostTraining = (Button) findViewById(R.id.btn_postTraining);

        final CheckBox[] chkBoxen = {chkTrainingCRU, chkTrainingVAR, chkTrainingCLM, chkTrainingFK};


        btnPostTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //für alle ausgewählten CheckBoxen wird Tweet abgesetzt
                for (int i = 0; i < 4; i++) {
                    if (chkBoxen[i].isChecked()) {
                        String message;
                        message = "Wie jeden " + getWochentag() + " heute Football-Training für alle " + alter[i] + ", " + uhrzeit[i] + ", " + trainingsort[i] + " " + mapsURL[i] + " #chemnitz";
                        postTweet(message);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }
            }
        });


    }

    private void postTweet(String text) {
        StatusesService jdStatusService = Twitter.getApiClient().getStatusesService();

        /*Paramater für den Post:
        > text--Der zu postende Text
        > mein Tweet ist eine Antwort auf diese Tweet ID
        > Tweet ist nicht jugendfrei?
        > Geo-Koordinaten des Tweets
        > STRING ID der Geolocation
        > sollen Koordinaten angezeigt werden?
        > soll Nutzername in Timeline verborgen werden?
        > IDs von angehangenden Medien
        > Methode, die nach dem Poten ausgeführt werden soll
        */


        //placeID Chemnitz, Sachsen
        String placeIDChemnitz = "971f3290b678d97e";


        jdStatusService.update(text, null, false, 50.8057521, 12.9325027, placeIDChemnitz, false, false, null, new Callback<Tweet>() {
            //Was passiert, wenn Tweet erfolgreich gepostet wurde?
            @Override
            public void success(Result<Tweet> result) {
                Toast.makeText(getApplicationContext(), "Dein Tweet wurde erfolgreich gesendet", Toast.LENGTH_SHORT).show();
            }

            //Was passiert, wenn Tweet nicht erfolgreich gepostet wurde?
            @Override
            public void failure(TwitterException e) {
                Toast.makeText(getApplicationContext(), "Leider ist etwas schief gelaufen.", Toast.LENGTH_SHORT).show();
                //Ausgabe des Fehlers in der Konsole
                e.printStackTrace();
            }
        });

    }


    private String getWochentag() {
        Calendar myCal = new GregorianCalendar();
        myCal.setTime(new Date());
        int dayOfWeek = myCal.get(Calendar.DAY_OF_WEEK);

        String[] wochentage = {"Samstag", "Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"};
        return wochentage[dayOfWeek];
    }
}
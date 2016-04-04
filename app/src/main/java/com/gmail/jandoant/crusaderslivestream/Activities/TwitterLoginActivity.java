package com.gmail.jandoant.crusaderslivestream.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.gmail.jandoant.crusaderslivestream.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class TwitterLoginActivity extends Activity {

    //Variablen initialisieren
    //### Twitter Login Button ###
    TwitterLoginButton jdTwitterLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Activity-Layout mit XML verknüpfen
        setContentView(R.layout.activity_twitter_login);

        //TwitterLoginButton mit XML verknüpfen
        jdTwitterLoginBtn = (TwitterLoginButton) findViewById(R.id.twitter_login_btn);

        //CallBack, wenn Twitter Button aktiviert wird (was soll passieren?)
        jdTwitterLoginBtn.setCallback(new Callback<TwitterSession>() {

            //Wird ausgeführt, wenn Login erfolgreich war
            @Override
            public void success(Result<TwitterSession> result) {

                //Username des angemeldeten Twitter-Accounts auslesen
                String jdUsername = result.data.getUserName().toString();
                //Login-Erfolgsmeldung über Toast
                Toast.makeText(TwitterLoginActivity.this, "Login für @" + jdUsername + " war efolgreich!", Toast.LENGTH_SHORT).show();
                //zu Main-Activity wechseln
                startActivity(new Intent(TwitterLoginActivity.this, MainActivity.class));
                //Login-Activity nach erfolgreicher Anmeldung wieder schließen
                finish();

                //eine neue Twitter-Session ist nun aktiv

            }

            //Wird ausgeführt, wenn Login fehlerhaft war
            @Override
            public void failure(TwitterException e) {
                Toast.makeText(TwitterLoginActivity.this, "Das Login ist leider fehlgeschlagen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Rückmeldung an Twitter Button, ob Anmeldung erfolgreich
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Übergabe der Result-Parameter an TwitterLoginButton
        jdTwitterLoginBtn.onActivityResult(requestCode, resultCode, data);
    }
}

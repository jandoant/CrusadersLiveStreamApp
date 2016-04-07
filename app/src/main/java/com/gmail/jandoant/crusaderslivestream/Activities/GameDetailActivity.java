package com.gmail.jandoant.crusaderslivestream.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.jandoant.crusaderslivestream.Adapter.ViewPagerAdapter;
import com.gmail.jandoant.crusaderslivestream.Datenbank.LiveStreamDB;
import com.gmail.jandoant.crusaderslivestream.Fragments.DefenseFragment;
import com.gmail.jandoant.crusaderslivestream.Fragments.KickoffFragment;
import com.gmail.jandoant.crusaderslivestream.Fragments.OffenseFragment;
import com.gmail.jandoant.crusaderslivestream.Fragments.SpecialteamsFragment;
import com.gmail.jandoant.crusaderslivestream.R;
import com.gmail.jandoant.crusaderslivestream.Spiel.Game;

public class GameDetailActivity extends AppCompatActivity implements KickoffFragment.OnKickoffButtonClickListener {

    public TextView tv_gamedate;
    public TextView tv_quarter;
    //--Gegner
    public TextView tv_hometeam;
    public TextView tv_awayteam;
    //Ergebnis
    public TextView tv_standing;
    //UI
    Toolbar toolbar;
    TextView tv_gameID;
    //Daten
    LiveStreamDB db;
    Game myGame;
    int gameID;
    Bundle extras;
    ViewPagerAdapter adapter;
    KickoffFragment kickoffFragment = new KickoffFragment();
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        //empfängt die aus der MainActivity übergebene Game-ID
        extras = getIntent().getExtras();
        gameID = extras.getInt(MainActivity.BUNDLE_GAME_ID);
        //Datenbankzugriff
        db = new LiveStreamDB(this);
        //UI-mit XML verknüpfen
        setUpUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Aktuellstest Game-Objekt aus DB erstellen und UI mit diesen Daten aktualisieren
        myGame = db.getGameFromDbByID(gameID);
        updateUI(myGame);
    }

    private void setUpUI() {
        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_gamedetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //TabLayout
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        //TextViews
        //--ID
        tv_gameID = (TextView) findViewById(R.id.tv_gameid_gamedetail);
        //--Datum und Uhrzeit
        tv_gamedate = (TextView) findViewById(R.id.tv_gamedate_gamedetail);
        tv_quarter = (TextView) findViewById(R.id.tv_quarter_gamedetail);
        //--Gegner
        tv_hometeam = (TextView) findViewById(R.id.tv_team_home_gamedetail);
        tv_awayteam = (TextView) findViewById(R.id.tv_team_away_gamedetail);
        //--Ergebnis
        tv_standing = (TextView) findViewById(R.id.tv_standing_gamedetail);
    }

    private void updateUI(Game myGame) {

        viewPager.removeAllViews();
        //update View Pager
        if (myGame.getQuarter() == 0) {
            setUpKickoffViewPager(viewPager);
        } else if (myGame.getQuarter() > 0) {
            setupViewPager(viewPager);
        }
        tabLayout.setupWithViewPager(viewPager);

        //update TextView in Header
        tv_gamedate.setText(myGame.getGameDate().toString());
        tv_gameID.setText(String.valueOf(myGame.get_id()));
        tv_quarter.setText(myGame.getStrQuarter());
        tv_hometeam.setText(myGame.getHomeTeam().getAbkuerzung());
        tv_awayteam.setText(myGame.getAwayTeam().getAbkuerzung());
        tv_standing.setText(myGame.generateGameStanding());

    }

    private void setupViewPager(ViewPager viewPager) {

        //Fragmente werden in ViewPagerAdapter gespeichert
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OffenseFragment(), "OFF");
        adapter.addFragment(new DefenseFragment(), "DEF");
        adapter.addFragment(new SpecialteamsFragment(), "ST");
        viewPager.setAdapter(adapter);
    }

    private void setUpKickoffViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(kickoffFragment, "KICKOFF");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onKickoffButtonClick(View view) {
        //Quarter um eins hochzählen (Spiel beginnt)
        myGame.setQuarter(myGame.getQuarter() + 1);
        String tweet = generateKickoffTweet();
        Toast.makeText(GameDetailActivity.this, tweet, Toast.LENGTH_LONG).show();
        //GameObjekt in DB updaten
        db.updateGameQuarterInDB(myGame);
        //TabLayout updaten
        updateUI(myGame);
    }

    private String generateKickoffTweet() {

        //TODO: richtigen TweetString generieren und bei Twitter posten --> Abkürzungen, Hashtag
        String tweet = null;
        tweet = "KICKOFF beim FootballSpiel " + myGame.getHomeTeam().getName() + " gegen " + myGame.getAwayTeam().getName() + "!" + " Verfolge das Spiel bei Twitter oder Facebook unter dem Hashtag " + myGame.generateGameHashtag();
        return tweet;
    }
}

package com.gmail.jandoant.crusaderslivestream.Spiel;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * Created by Jan on 28.03.2016.
 */
public class Game {
    //ID
    private int _id;
    //Datum und Uhrzeit
    private LocalDate gameDate;
    private LocalTime gameTime;
    private DateTime timestamp;

    private int quarter;

    //Gegner
    private String homeTeam;
    private String awayTeam;
    //Aktueller Punktestand
    private int aktuellePunkteHome;
    private int aktuellePunkteAway;
    //Spielort
    private String gameOrt;
    //KONSTRUKTOR
    public Game() {
        this.aktuellePunkteAway = 0;
        this.aktuellePunkteHome = 0;
        this.quarter = 0;
    }


    /**
     * Erstellt ein neues Spiel
     * @param gameDate
     * @param gameTime
     * @param awayTeam
     * @param homeTeam
     */
    public Game(LocalDate gameDate, LocalTime gameTime, String homeTeam, String awayTeam) {
        this.gameDate = gameDate;
        this.gameTime = gameTime;
        this.awayTeam = awayTeam;
        this.homeTeam = homeTeam;
        this.aktuellePunkteAway = 0;
        this.aktuellePunkteHome = 0;
        this.quarter = 0;
    }

    public int get_id() {
        return _id;
    }


    public void set_id(int _id) {
        this._id = _id;
    }

    public int getAktuellePunkteAway() {
        return aktuellePunkteAway;
    }

    public void setAktuellePunkteAway(int aktuellePunkteAway) {
        this.aktuellePunkteAway = aktuellePunkteAway;
    }

    public int getAktuellePunkteHome() {
        return aktuellePunkteHome;
    }

    public void setAktuellePunkteHome(int aktuellePunkteHome) {
        this.aktuellePunkteHome = aktuellePunkteHome;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public LocalDate getGameDate() {
        return gameDate;
    }

    public void setGameDate(LocalDate gameDate) {
        this.gameDate = gameDate;
    }

    public LocalTime getGameTime() {
        return gameTime;
    }

    public void setGameTime(LocalTime gameTime) {
        this.gameTime = gameTime;
    }

    public String getGameOrt() {
        return gameOrt;
    }

    public void setGameOrt(String gameOrt) {
        this.gameOrt = gameOrt;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public String getStrQuarter() {
        if (this.quarter == 0) {
            return "Spiel hat noch nicht begonnen";
        } else if (this.quarter <= 4) {
            return this.quarter + ". Quarter";
        } else if (this.quarter > 5) {
            return "finished";
        } else return "irgendwas ist schief gelaufen";
    }
}

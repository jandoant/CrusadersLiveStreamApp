package com.gmail.jandoant.crusaderslivestream.Spiel;

import org.joda.time.LocalDate;

/**
 * Created by Jan on 28.03.2016.
 */
public class Spiel {

    private int _id;
    private LocalDate termin;
    private String ort;
    private String homeTeam;
    private String awayTeam;
    private int aktuellePunkteHome;
    private int aktuellePunkteAway;

    /**
     * Aufruf bei Erstellung eines neuen Spiels
     *
     * @param awayTeam
     * @param homeTeam
     * @param termin
     */

    public Spiel(String awayTeam, String homeTeam, LocalDate termin) {
        this.awayTeam = awayTeam;
        this.homeTeam = homeTeam;
        this.termin = termin;
        this.aktuellePunkteAway = 0;
        this.aktuellePunkteHome = 0;
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

    public String getHomeTeam() {
        return homeTeam;
    }

    public LocalDate getTermin() {
        return termin;
    }

    public void setTermin(LocalDate termin) {
        this.termin = termin;
    }
}

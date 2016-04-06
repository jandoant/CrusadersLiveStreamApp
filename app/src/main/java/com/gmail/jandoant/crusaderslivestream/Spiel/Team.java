package com.gmail.jandoant.crusaderslivestream.Spiel;

/**
 * Created by Jan on 05.04.2016.
 */
public class Team {

    public static final int ALTERSKLASSE_HERREN = 1;
    public static final int ALTERSKLASSE_A = 2;
    public static final int ALTERSKLASSE_B = 3;
    private int id;
    private String name;
    private String abkuerzung;
    private int altersklasse;


    public Team() {
    }

    public Team(String name, String abkuerzung) {
        this.abkuerzung = abkuerzung;
        this.name = name;
    }

    public String getAbkuerzung() {
        return abkuerzung;
    }

    public void setAbkuerzung(String abkuerzung) {
        this.abkuerzung = abkuerzung;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAltersklasse() {
        return altersklasse;
    }

    public void setAltersklasse(int altersklasse) {
        this.altersklasse = altersklasse;
    }
}

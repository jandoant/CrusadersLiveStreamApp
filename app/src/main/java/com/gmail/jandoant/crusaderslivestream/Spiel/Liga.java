package com.gmail.jandoant.crusaderslivestream.Spiel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jan on 05.04.2016.
 */
public class Liga {

    public static final int ALTERSKLASSE_HERREN = 1;
    public static final int ALTERSKLASSE_A = 2;
    public static final int ALTERSKLASSE_B = 3;

    //Konstruktor
    public Liga() {
    }

    public ArrayList<Team> getArrayOfTeamsOfAge(int altersklasse) {

        ArrayList<Team> arrayListTeams;
        arrayListTeams = new ArrayList<Team>();

        switch (altersklasse) {

            case ALTERSKLASSE_HERREN:

                arrayListTeams.addAll(Arrays.asList(
                        new Team("Chemnitz Crusaders", "CRU"),
                        new Team("Radebeul Suburbian Foxes", "RSF"),
                        new Team("Jenaer Hanfrieds", "JHF"),
                        new Team("Erfurt Indigos", "ERF"),
                        new Team("Spandau Bulldogs", "SBD"),
                        new Team("Berlin Thunderbirds", "BTB")
                ));

                break;
            case ALTERSKLASSE_A:
                arrayListTeams.addAll(Arrays.asList(
                        new Team("Chemnitz Varlets", "VAR"),
                        new Team("Potsdam Royals", "PDR"),
                        new Team("Berlin Kobras", "BKO"),
                        new Team("BerlinBears", "BEA"),
                        new Team("Leipzig Lions", "LIO"),
                        new Team("Berlin Thunderbirds", "BTB")
                ));

                break;
            case ALTERSKLASSE_B:
                arrayListTeams.addAll(Arrays.asList(
                        new Team("Chemnitz Claymores", "CLM"),
                        new Team("Berlin Kobras", "BKO"),
                        new Team("Berlin Rebels", "BRB"),
                        new Team("Berlin Bears", "BB"),
                        new Team("Dresden Monarchs", "DDM")
                ));
                break;
        }
        return arrayListTeams;

    }

}

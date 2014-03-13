package edu.kit.iti.formal.mandatsverteilung.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kandidat;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kreiswahlvorschlag;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Landesliste;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;

/**
 * Klasse, die die Kandidaten und Kreiswahlvorschläge einer Bundestagswahl
 * erstellt. Benötigt eine Bundestagswahl und eine Datei, in der alle Kandidaten
 * aufgelistet sind
 * 
 * @author Jan
 * @version 1.0
 */
class CSVKandidatenParser extends CSVParser implements KandidatenParser {

    /**
     * Legt fest, in welcher Spalte die Abkürzung des Bundeslands der
     * Landesliste des Bewerbers steht.
     */
    private static final int landesListenIDZelle = 4;

    /**
     * Legt fest, in welcher Spalte der Landeslistenplatz des Bewerbers steht.
     */
    private static final int landesListenNummerZelle = 5;
    /**
     * Legt fest, in welcher Spalte der Name des Bewerbers steht.
     */
    private static final int namensZelle = 0;
    /**
     * Legt fest, in welcher Spalte der Parteiname steht. Es wird von 0 aus
     * gezählt.
     */
    private static final int parteiNamenZelle = 2;
    /**
     * Legt fest, in welcher Spalte der Wahlkreis des Bewerbers steht.
     */
    private static final int wahlkreisZelle = 3;
    private final Map<Landesliste, Integer> landeslistenPlaetze;
    /**
     * Datenstruktur für die Parteien.
     */
    private final Dictionary<String, Partei> parteien = new Hashtable<String, Partei>();
    /**
     * ArrayList für alle Partei-Namen. Nützlich zum Iterieren über alle
     * Parteien.
     */
    private final ArrayList<String> parteiNamen = new ArrayList<>();

    CSVKandidatenParser() {
        super();
        beginneBeiZeile = 1;
        landeslistenPlaetze = new HashMap<>();
    }

    /**
     * Erzeugt eine neue Partei.
     * 
     * @param name
     *            Der Name der neuen Partei
     * @return eine neue Partei mit diesem Namen
     */
    private Partei createPartei(String name) {
        return new Partei(name);
    }

    @Override
    protected void dateiendeErreicht() {
        for (String s : parteiNamen) {
            Partei p = parteien.get(s);
            assert p != null;
            @SuppressWarnings("unused")
            boolean erfolg = wahl.addPartei(p);
            assert erfolg = true;
        }
    }

    @Override
    protected String fehlerhafteDatei() {
        return "Der Fehler trat in der Kandidaten-Datei auf.";
    }

    @Override
    public void parseKandidatenDatei(File f, Bundestagswahl b)
            throws IOException {
        wahl = b;
        this.parseDatei(new FileInputStream(f));
    }

    @Override
    public void parseKandidatenDatei(InputStream in, Bundestagswahl b)
            throws IOException {
        wahl = b;
        this.parseDatei(in);
    }

    @Override
    protected void parseZeile(String[] zeile) throws IOException {
        if (zeile.length != 6) {
            throw new IOException(this.fehlerInZelle(0)
                    + ImportFehler.FALSCHE_ZEILEN_LAENGE.toString());
        }
        // Erstellt für jede Zeile einen neuen Kandidaten und fügt ihn einer
        // Partei hinzu, (erstellt diese, wenn es sie noch nicht gibt)
        // wenn er auf Wahlkreisebene antritt, und fügt ihn auf der Landesliste
        // hinzu, falls er auf einer Landesliste steht.
        // Finde Partei
        Partei curPartei = parteien.get(zeile[parteiNamenZelle]);
        if ((curPartei == null)
                && !zeile[parteiNamenZelle].equals("Anderer KWV")) {
            // Partei noch nicht in Hashtabelle
            curPartei = this.createPartei(zeile[parteiNamenZelle]);
            parteien.put(zeile[parteiNamenZelle], curPartei);
            parteiNamen.add(zeile[parteiNamenZelle]);
        }
        Kandidat curKandidat;
        // Wenn KWV, erzeuge stattdessen einen KWV. (Int parsen kann
        // fehlschlagen)
        if (zeile[wahlkreisZelle].isEmpty()) {
            curKandidat = new Kandidat(zeile[namensZelle]);
        } else {
            int wahlkreisnummer = 0;
            try {
                wahlkreisnummer = Integer.parseInt(zeile[wahlkreisZelle]);
            } catch (NumberFormatException e) {
                throw new IOException(this.fehlerInZelle(wahlkreisZelle)
                        + ImportFehler.KEINE_ZAHL_GEFUNDEN.toString());
            }
            if (wahl.getWahlkreis(wahlkreisnummer) == null) {
                throw new IOException(this.fehlerInZelle(wahlkreisZelle)
                        + ImportFehler.KEIN_WAHLKREIS_GEFUNDEN.toString());
            }
            curKandidat = new Kreiswahlvorschlag(zeile[namensZelle],
                    wahl.getWahlkreis(wahlkreisnummer));
            Kreiswahlvorschlag k = (Kreiswahlvorschlag) curKandidat;
            k.setPartei(curPartei);
        }
        if (!zeile[landesListenIDZelle].isEmpty()
                && !zeile[landesListenNummerZelle].isEmpty()) {
            // Finde Landesliste
            Bundesland curBundesland = wahl
                    .getBundeslandAbk(zeile[landesListenIDZelle]);
            Landesliste curLL = curPartei.getLandesliste(curBundesland);
            if (curLL == null) {
                curLL = new Landesliste();
                landeslistenPlaetze.put(curLL, 1);
                curPartei.setLandesliste(curBundesland, curLL);
            }
            int landeslistenposition = 0;
            try {
                landeslistenposition = Integer
                        .parseInt(zeile[landesListenNummerZelle]);
            } catch (NumberFormatException e) {
                throw new IOException(
                        this.fehlerInZelle(landesListenNummerZelle)
                                + ImportFehler.KEINE_ZAHL_GEFUNDEN.toString());
            }
            curLL.addKandidat(curKandidat, landeslistenposition);
            int platz = landeslistenPlaetze.get(curLL);
            platz++;
            landeslistenPlaetze.put(curLL, platz);
        }
    }
}

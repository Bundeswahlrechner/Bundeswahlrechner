package edu.kit.iti.formal.mandatsverteilung.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

/**
 * Klasse, die die Bundesländer einer Bundestagswahl erstellt. Benötigt eine
 * Bundestagswahl und eine CSV-Datei, die Einwohnerzahlen, Namen etc. enthält.
 * 
 * @author Jan
 * @version 1.0
 */
class CSVBundeslaenderParser extends CSVParser implements BundeslaenderParser {

    /**
     * Legt fest, in welcher Spalte die Abkürzung steht.
     */
    private static final int abkuerzungsFeld = 1;
    /**
     * Legt fest, in welcher Spalte die Einwohnerzahl steht
     */
    private static final int einwohnerZahlFeld = 2;
    /**
     * Legt fest, in welcher Spalte der Name steht.
     */
    private static final int namensFeld = 0;

    CSVBundeslaenderParser() {
        super();
        beginneBeiZeile = 1;
    }

    @Override
    protected String fehlerhafteDatei() {
        return "Der Fehler trat in der Bundesländer-Datei auf.";
    }

    /**
     * Fügt der Bundestagswahl ein neues Bundesland hinzu.
     * 
     * @param name
     *            Der Name des neuen Bundeslands
     * @param abkuerzung
     *            Die Abkürzung des neuen Bundeslands (für Kandidatenliste
     *            benötigt)
     * @param einwohnerzahl
     *            Die Einwohnerzahl des neuen Bundeslands (für Sitzzahl pro
     *            Bundesland benötigt)
     */
    private void fuegeBundeslandHinzu(String name, String abkuerzung,
            int einwohnerzahl) {
        wahl.addBundesland(new Bundesland(name, abkuerzung, einwohnerzahl));
    }

    @Override
    public void parseBundeslaenderDatei(File f, Bundestagswahl b)
            throws IOException {
        wahl = b;
        this.parseDatei(new FileInputStream(f));
    }

    @Override
    public void parseBundeslaenderDatei(InputStream in, Bundestagswahl b)
            throws IOException {
        wahl = b;
        this.parseDatei(in);
    }

    @Override
    protected void parseZeile(String[] zeile) throws IOException {
        if (zeile.length != 3) {
            throw new IOException(this.fehlerInZelle(0)
                    + ImportFehler.FALSCHE_ZEILEN_LAENGE.toString());
        }
        int einwohnerzahl = 0;
        try {
            einwohnerzahl = Integer.parseInt(zeile[einwohnerZahlFeld]);
        } catch (NumberFormatException e) {
            throw new IOException(this.fehlerInZelle(einwohnerZahlFeld)
                    + ImportFehler.KEINE_ZAHL_GEFUNDEN.toString());
        }
        this.fuegeBundeslandHinzu(zeile[namensFeld], zeile[abkuerzungsFeld],
                einwohnerzahl);
    }

}

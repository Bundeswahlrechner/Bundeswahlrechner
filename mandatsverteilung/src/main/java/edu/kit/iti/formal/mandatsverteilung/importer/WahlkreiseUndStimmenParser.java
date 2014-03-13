package edu.kit.iti.formal.mandatsverteilung.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

/**
 * Interface für einen Wahlkreise und Stimmen-Parser.
 * 
 * @author Jan
 * @version 1.0
 */
public interface WahlkreiseUndStimmenParser {

    /**
     * Parst die Ergebnis-Datei und setzt die Stimmen der Bundestagswahl
     * entsprechend. Zuvor muss setzeDatei() aufgerufen worden sein! In der
     * Bundestagswahl müssen außerdem bereits Bundesländer, Wahlkreise,
     * Kandidaten, Parteien, etc. bereits existieren.
     */
    public void parseStimmen() throws IOException;

    /**
     * Parst die Ergebnis-Datei und erzeugt jeden Wahlkreis darin. Zuvor muss
     * setzeDatei() aufgerufen worden sein! In der Bundestagswahl müssen
     * außerdem die Bundesländer bereits existieren.
     * 
     * @throws IOException
     *             Falls beim Parsen etwas schieflief
     */
    public void parseWahlkreise() throws IOException;

    /**
     * Gibt dem Parser die Möglichkeit, einen zweiten Stream zu erhalten, um die
     * Datei 2x zu parsen.
     * 
     * @param in
     *            Der Stream der Datei
     */
    public void reinitialisiere(InputStream in);

    /**
     * Legt die zu parsende Datei fest.
     * 
     * @param datei
     *            Die Datei mit den Stimmergebnissen
     * @param b
     *            Die zu ändernde Bundestagswahl
     */
    public void setzeDaten(File datei, Bundestagswahl b) throws IOException;

    /**
     * @param in
     *            InputStream mit den Stimmergebnissen
     * @param b
     *            Die zu ändernde Bundestagswahl
     */
    public void setzeDaten(InputStream in, Bundestagswahl b) throws IOException;
}

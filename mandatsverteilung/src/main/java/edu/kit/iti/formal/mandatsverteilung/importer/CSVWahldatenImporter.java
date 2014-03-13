package edu.kit.iti.formal.mandatsverteilung.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.BerechnungsfabrikMitSperrklauselpruefer;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Bundeswahlgesetz2013Fabrik;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Wahlverfahren;

/**
 * Importer für Wahldaten im CSV-Format.
 * 
 * @author Jan
 * @version 1.0
 */
public class CSVWahldatenImporter extends WahldatenImporter {

    /**
     * Datei mit Namen, Abkürzungen und Einwohnerzahlen der Bundesländer. Wenn
     * andere Bundesländer verwendet werden sollen, muss diese manuell angepasst
     * werden.
     */
    File bundeslaenderDatei = null;
    InputStream[] dateien = null;
    /**
     * Datei, in der alle Wahlbewerber aufgelistet sind. Vom Bundeswahlleiter
     * als "Tab23_Wahlbewerber_a.csv" herunterladbar.
     */
    File kandidatenDatei = null;

    /**
     * Datei, in der Wahlkreise und Stimmdaten stehen. Vom Bundeswahlleiter als
     * "kerg.csv" herunterladbar.
     */
    File wahlkreiseUndStimmenDatei = null;

    public CSVWahldatenImporter(File wahlkreiseUndStimmenDatei,
            File kandidatenDatei, File bundeslaenderDatei) {
        super(new CSVParserFabrik());
        this.wahlkreiseUndStimmenDatei = wahlkreiseUndStimmenDatei;
        this.kandidatenDatei = kandidatenDatei;
        this.bundeslaenderDatei = bundeslaenderDatei;
    }

    public CSVWahldatenImporter(InputStream datei1, InputStream reinitDatei1,
            InputStream datei2, InputStream datei3) {
        super(new CSVParserFabrik());
        dateien = new InputStream[4];
        dateien[0] = datei1;
        dateien[1] = reinitDatei1;
        dateien[2] = datei2;
        dateien[3] = datei3;
    }

    @Override
    public Bundestagswahl importieren() throws IOException {
        Bundestagswahl result = new Bundestagswahl();
        if ((wahlkreiseUndStimmenDatei == null || kandidatenDatei == null || bundeslaenderDatei == null)
                && dateien == null) {
            throw new AssertionError(
                    "Eine der Dateien im WahldatenImporter war null!");
        }
        CSVValidator valid = new CSVValidator();
        BundeslaenderParser bp = fabrik.erzeugeBundeslaenderParser();
        WahlkreiseUndStimmenParser wp = fabrik
                .erzeugeWahlkreiseUndStimmenParser();
        KandidatenParser kp = fabrik.erzeugeKandidatenParser();
        if (dateien == null) {
            bp.parseBundeslaenderDatei(bundeslaenderDatei, result);
            wp.setzeDaten(wahlkreiseUndStimmenDatei, result);
            wp.parseWahlkreise();
            kp.parseKandidatenDatei(kandidatenDatei, result);
            wp.parseStimmen();
        } else {
            bp.parseBundeslaenderDatei(dateien[3], result);
            wp.setzeDaten(dateien[0], result);
            wp.parseWahlkreise();
            wp.reinitialisiere(dateien[1]);
            kp.parseKandidatenDatei(dateien[2], result);
            wp.parseStimmen();
            for (InputStream s : dateien) {
                s.close();
            }
        }
        // Standardwahlverfahren wird von der Bundeswahlgesetz2013Fabrik
        // erzeugt
        BerechnungsfabrikMitSperrklauselpruefer fabrik = new Bundeswahlgesetz2013Fabrik();
        Wahlverfahren verfahren = fabrik.erstelleBerechnungsverbund(result);

        // Wahlergebnis berechnen
        verfahren.berechneWahlergebnis();
        valid.validiereBundeslaender(result);
        valid.validiereKandidaten(result);
        return result;
    }
}

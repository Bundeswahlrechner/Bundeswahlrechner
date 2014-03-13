package edu.kit.iti.formal.mandatsverteilung.importer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import au.com.bytecode.opencsv.CSVReader;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

/**
 * Oberklasse aller CSV-Parser.
 * 
 * @author Jan
 * @version 1.0
 */
abstract class CSVParser {

    /**
     * Zeile, die momentan geparst wird. Nützlich, um Fehler auszugeben.
     */
    protected int aktuelleZeile;
    /**
     * Zeile, in der der CSVReader zu lesen beginnen soll. Nützlich, um die
     * ersten x Zeilen zu überspringen.
     */
    protected int beginneBeiZeile = 0;
    /**
     * CSVReader zum Einlesen einer CSV-Datei.
     */
    protected CSVReader reader;
    /**
     * Bundestagswahl, der dieser Parser neue Objekte hinzufügt.
     */
    protected Bundestagswahl wahl;

    CSVParser() {
    }

    /**
     * Hook, der aufgerufen wird, wenn das Dateiende erreicht wird. Nützlich, um
     * z.B. die Datenstrukturen zeilenweise aufzubauen und in dieser Methode
     * dann der Bundestagswahl zu übergeben.
     */
    protected void dateiendeErreicht() {
    }

    protected abstract String fehlerhafteDatei();

    /**
     * Gibt einen String, der in der GUI angezeigt werden kann, und auf einen
     * Fehler in der aktuellen Zeile hinweist, zurück.
     * 
     * @param zelle
     *            Die Zelle, in der der Fehler auftrat. Wird mit ausgegeben
     * @return Fehlerstring für die GUI
     */
    protected String fehlerInZelle(int zelle) {
        StringBuilder sb = new StringBuilder();
        sb.append("Fehler: Ein Fehler trat auf in Zeile ");
        sb.append(aktuelleZeile + 1);
        sb.append(", Zelle ");
        sb.append(zelle + 1);
        sb.append("!\n");
        sb.append(this.fehlerhafteDatei());
        return sb.toString();
    }

    /**
     * Initialer Aufruf, der die Datei parst. Wird von den Implementierungen der
     * Interfaces aufgerufen.
     * 
     * @param in
     *            InputStream für die einzulesende Datei
     */
    protected void parseDatei(InputStream in) throws IOException {
        // Momentan statisch mit Semikolon als Delimiter
        reader = new CSVReader(new InputStreamReader(in, "ISO-8859-1"), ';');
        String[] curLine;
        aktuelleZeile = beginneBeiZeile;
        for (int i = 0; i < beginneBeiZeile; i++) {
            curLine = reader.readNext();
        }
        while (null != (curLine = reader.readNext())) {
            if (curLine.length > 1) {
                this.parseZeile(curLine);
            }
            aktuelleZeile++;
        }

        // Am Schluss:
        reader.close();
        this.dateiendeErreicht();

    }

    /**
     * Parst die aus der Datei ausgelesene Zeile.
     * 
     * @param zeile
     *            String-Array der Zeile, jede Zelle in der Datei ist ein String
     *            im Array wird in der Unterklasse implementiert
     */
    protected abstract void parseZeile(String[] zeile) throws IOException;
}

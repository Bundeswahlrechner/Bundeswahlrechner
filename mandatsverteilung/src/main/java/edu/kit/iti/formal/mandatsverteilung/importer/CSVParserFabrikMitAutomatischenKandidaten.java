package edu.kit.iti.formal.mandatsverteilung.importer;

/**
 * Klasse, die CSVParser zurückgibt und jeder Landesliste, die existiert,
 * mindestens 150 Kandidaten zuteilt. Teil des Entwurfsmusters 'Abstrakte
 * Fabrik'
 * 
 * @author Jan
 * @version 1.0
 */
public class CSVParserFabrikMitAutomatischenKandidaten implements ParserFabrik {

    private Integer n = null;

    public CSVParserFabrikMitAutomatischenKandidaten() {
    }

    public CSVParserFabrikMitAutomatischenKandidaten(int n) {
        this.n = n;
    }

    @Override
    public BundeslaenderParser erzeugeBundeslaenderParser() {
        BundeslaenderParser result = new CSVBundeslaenderParser();
        return result;
    }

    @Override
    public KandidatenParser erzeugeKandidatenParser() {
        KandidatenParser result;
        if (n == null) {
            result = new AutomatischerKandidatenParser();
        } else {
            result = new AutomatischerKandidatenParser(n);
        }
        return result;
    }

    @Override
    public WahlkreiseUndStimmenParser erzeugeWahlkreiseUndStimmenParser() {
        WahlkreiseUndStimmenParser result = new CSVWahlkreiseUndStimmenParser();
        return result;
    }
}

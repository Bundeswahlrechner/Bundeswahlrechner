package edu.kit.iti.formal.mandatsverteilung.importer;

/**
 * Klasse, die die konkreten CSVParser zurückgibt. Teil des Entwurfsmusters
 * 'Abstrakte Fabrik'
 * 
 * @author Jan
 * @version 1.0
 */
public class CSVParserFabrik implements ParserFabrik {
	public BundeslaenderParser erzeugeBundeslaenderParser() {
		BundeslaenderParser result = new CSVBundeslaenderParser();
		return result;
	}

	public KandidatenParser erzeugeKandidatenParser() {
		KandidatenParser result = new CSVKandidatenParser();
		return result;
	}

	public WahlkreiseUndStimmenParser erzeugeWahlkreiseUndStimmenParser() {
		WahlkreiseUndStimmenParser result = new CSVWahlkreiseUndStimmenParser();
		return result;
	}
}

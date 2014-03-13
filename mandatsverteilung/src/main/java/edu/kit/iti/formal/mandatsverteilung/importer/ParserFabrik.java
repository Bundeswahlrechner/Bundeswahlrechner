package edu.kit.iti.formal.mandatsverteilung.importer;


/**
 * Allgemeine Parser-Fabrik. Teil des Entwurfsmusters 'Abstrakte Fabrik' In den
 * Implementierungen werden die implementierenden Klassen erzeugt und
 * zurückgegeben.
 * 
 * @author Jan
 * @version 1.0
 */
public interface ParserFabrik {
	/**
	 * Gibt den Bundesländer-Parser zurück.
	 * 
	 * @param b
	 *            Bundestagswahl, auf der der Parser operieren soll
	 * @return Einen Bundesländer-Parser, der aus einer Bundesländer-Datei die
	 *         entsprechende Datenstruktur anlegt.
	 */
	public BundeslaenderParser erzeugeBundeslaenderParser();

	/**
	 * Gibt den Kandidaten-Parser zurück.
	 * 
	 * @param b
	 *            Bundestagswahl, auf der der Parser operieren soll
	 * @return Einen Kandidaten-Parser, der aus einer Kandidaten-Datei die
	 *         entsprechende Datenstruktur anlegt.
	 */
	public KandidatenParser erzeugeKandidatenParser();

	/**
	 * Gibt den Wahlkreise und Stimmen-Parser zurück.
	 * 
	 * @param b
	 *            Bundestagswahl, auf der der Parser operieren soll
	 * @return Einen Wahlkreise und Stimmen-Parser, der aus einer Ergebnis-Datei
	 *         die entsprechende Datenstruktur anlegt.
	 */
	public WahlkreiseUndStimmenParser erzeugeWahlkreiseUndStimmenParser();
}

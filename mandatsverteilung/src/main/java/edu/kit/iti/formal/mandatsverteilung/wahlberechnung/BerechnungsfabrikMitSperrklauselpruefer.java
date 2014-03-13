package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

/**
 * Abstrake Berechnungsfabrik. Konkrete Implementierungen bspw.
 * Bundeswahlgesetz2013Fabrik. Verwendet das Entwurfsmuster Abstrake Fabrik.
 */
public abstract class BerechnungsfabrikMitSperrklauselpruefer extends
		Berechnungsfabrik<WahlverfahrenMitSperrklausel> {

	@Override
	public WahlverfahrenMitSperrklausel erstelleBerechnungsverbund(
			Bundestagswahl bundestagswahl) {
		WahlverfahrenMitSperrklausel wahlverfahren = super
				.erstelleBerechnungsverbund(bundestagswahl);
		Sperrklauselpruefer sperrklauselpruefer = this
				.erstelleSperrklauselpruefer(bundestagswahl);
		wahlverfahren.setzeSperrklauselpruefer(sperrklauselpruefer);
		return wahlverfahren;
	}

	/**
	 * Erzeugt den Sperrklauselpruefer.
	 * 
	 * @return der erzeugte Sperrklauselpruefer
	 */
	protected abstract Sperrklauselpruefer erstelleSperrklauselpruefer(
			Bundestagswahl bundestagswahl);

	/**
	 * Erzeugt ein Wahlverfahren mit Sperrklausel
	 * 
	 * @param bundestagswahl
	 *            - Die Bundestagswahl, auf die das Verfahren angewendet werden
	 *            soll.
	 * @param zuteilungsverfahren
	 *            Das Zuteilungsverfahren, das Teil der Wahlverfahrens sein
	 *            soll.
	 * @return das erzeugte Wahlverfahren
	 */
	@Override
	protected abstract WahlverfahrenMitSperrklausel erstelleWahlverfahren(
			Bundestagswahl bundestagswahl,
			Zuteilungsverfahren zuteilungsverfahren);
}

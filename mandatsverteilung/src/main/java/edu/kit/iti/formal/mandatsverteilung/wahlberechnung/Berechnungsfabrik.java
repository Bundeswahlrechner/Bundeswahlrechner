package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

/**
 * Abstrake Berechnungsfabrik. Konkrete Implementierungen bspw.
 * Bundeswahlgesetz2013Fabrik. Verwendet das Entwurfsmuster Abstrake Fabrik.
 */
public abstract class Berechnungsfabrik<T extends Wahlverfahren> {

	/**
	 * Erzeugt einen Berechnungsverbund.
	 * 
	 * @param bundestagswahl
	 *            - Die Bundestagswahl, auf die das Verfahren angewendet werden
	 *            soll.
	 * @return Ein Wahlverfahren mit passendem Sitzzuteilungsverfahren.
	 */
	public T erstelleBerechnungsverbund(Bundestagswahl bundestagswahl) {
		Zuteilungsverfahren zuteilungsverfahren = erstelleZuteilungsverfahren();
		T wahlverfahren = erstelleWahlverfahren(bundestagswahl,
				zuteilungsverfahren);
		return wahlverfahren;
	}

	/**
	 * Erzeugt das Wahlverfahren
	 * 
	 * @param bundestagswahl
	 *            - Die Bundestagswahl, auf die das Verfahren angewendet werden
	 *            soll.
	 * @param zuteilungsverfahren
	 *            Das Zuteilungsverfahren, das Teil der Wahlverfahrens sein
	 *            soll.
	 * @return das erzeugte Wahlverfahren
	 */
	protected abstract T erstelleWahlverfahren(Bundestagswahl bundestagswahl,
			Zuteilungsverfahren zuteilungsverfahren);

	/**
	 * Erzeugt das Zuteilungsverfahren.
	 * 
	 * @return das erzeugte Zuteilungsverfahren
	 */
	protected abstract Zuteilungsverfahren erstelleZuteilungsverfahren();
}

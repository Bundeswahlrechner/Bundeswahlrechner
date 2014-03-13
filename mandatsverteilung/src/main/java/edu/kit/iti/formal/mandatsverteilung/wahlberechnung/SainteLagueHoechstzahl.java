package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Ein {@link Zuteilungsverfahren} auf Basis des Sainte-Lague-Verfahrens. Die
 * Berechnung erfolgt entsprechend der Höchstzahlmethode.
 * 
 * @author tillneuber
 * @version 1.1
 */
public class SainteLagueHoechstzahl extends Divisorverfahren {

	/**
	 * {@inheritDoc}
	 * <p>
	 * In dieser Klasse wird die nächste Prioritätszahl entsprechend der
	 * Sainte-Lague-Höchstzahlmethode berechnet.
	 */
	@Override
	protected double berechnePrioritaetszahl(int zuteilungswert,
			int iterationsschritt) {
		return zuteilungswert / (iterationsschritt - 0.5);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Die Prioritätszahlen werden in diesem Verfahren absteigend sortiert.
	 */
	@Override
	protected Iterator<Double> berechnePrioritaetszahlenreihenfolge(
			List<Double> zahlen) {
		Collections.sort(zahlen);
		Collections.reverse(zahlen);
		return zahlen.iterator();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SainteLagueHoechstzahl clone() {
		return new SainteLagueHoechstzahl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Sainte-Lague Höchstzahl";
	}

}
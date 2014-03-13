package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Ein {@link Zuteilungsverfahren} auf Basis des Sainte-Lague-Verfahrens. Die
 * Berechnung erfolgt entsprechend der Rangmasszahlmethode.
 * 
 * @author tillneuber
 * @version 1.1
 */
public class SainteLagueRangmasszahl extends Divisorverfahren {

	/**
	 * {@inheritDoc}
	 * <p>
	 * In dieser Klasse wird die nächste Prioritätszahl entsprechend der
	 * Sainte-Lague-Rangmasszahlmethode berechnet.
	 */
	@Override
	protected double berechnePrioritaetszahl(int zuteilungswert,
			int iterationsschritt) {
		return 1 / (zuteilungswert / (iterationsschritt - 0.5));
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Die Prioritätszahlen werden in diesem Verfahren aufsteigend sortiert.
	 */
	@Override
	protected Iterator<Double> berechnePrioritaetszahlenreihenfolge(
			List<Double> zahlen) {
		Collections.sort(zahlen);
		return zahlen.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SainteLagueRangmasszahl clone() {
		return new SainteLagueRangmasszahl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Sainte-Lague Rangmaßzahl";
	}
}
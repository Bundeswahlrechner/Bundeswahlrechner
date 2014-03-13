package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Ein {@link Zuteilungsverfahren} auf Basis des D'Hondt-Verfahrens. Die
 * Berechnung erfolgt entsprechend der Höchstzahlmethode.
 * 
 * @author Peter Steinmetz, tillneuber
 * @version 1.1
 */
public class DHondt extends Divisorverfahren {

	/**
	 * {@inheritDoc}
	 * <p>
	 * In dieser Klasse wird die nächste Prioritätszahl entsprechend der
	 * D'Hondt-Methode berechnet.
	 */
	@Override
	protected double berechnePrioritaetszahl(int zuteilungswert,
			int iterationsschritt) {
		return zuteilungswert / iterationsschritt;
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
	public DHondt clone() {
		return new DHondt();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "D'Hondt";
	}

}

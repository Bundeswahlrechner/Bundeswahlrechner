package edu.kit.iti.formal.mandatsverteilung.datenhaltung;

import java.util.Iterator;

public class LandeslistenMandateIterator extends FilterIterator<Kandidat> {

	/**
	 * Iterator über die Kandidaten auf der Landesliste.
	 */
	private final Iterator<Kandidat> kandidatenIterator;

	/**
	 * Anzahl der zu vergebenden Sitze.
	 */
	private int verbleibendeSitzzahl;

	/**
	 * Erzeugt einen Iterator für die Kandidaten, die über die Liste in den
	 * Bundestag einziehen.
	 * 
	 * @param kandidatenIterator
	 *            - Iterator über die Kandidaten auf der Landesliste.
	 * @param sitzzahl
	 *            - Anzahl der zu vergebenden Sitze.
	 */
	public LandeslistenMandateIterator(Iterator<Kandidat> kandidatenIterator,
			int sitzzahl) {
		super();
		this.kandidatenIterator = kandidatenIterator;
		verbleibendeSitzzahl = sitzzahl;
		this.internalFindNextReady();
	}

	/**
	 * Benutzt den kandidatenIterator, um den nächsten Kandidaten zu finden, der
	 * nicht seinen Wahlkreis gewonnen hat und somit über die Landesliste
	 * einzieht, sofern noch Plätze auf der Liste zu vergeben sind.
	 */
	@Override
	protected Kandidat internalFindNext() {
		Kandidat next = null;
		while ((verbleibendeSitzzahl > 0) && kandidatenIterator.hasNext()) {
			Kandidat kandidat = kandidatenIterator.next();
			if (!kandidat.istWahlkreisGewinner()) {
				next = kandidat;
				verbleibendeSitzzahl--;
				break;
			}
		}
		return next;
	}

}

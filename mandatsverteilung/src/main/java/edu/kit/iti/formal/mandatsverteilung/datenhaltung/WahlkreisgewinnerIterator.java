package edu.kit.iti.formal.mandatsverteilung.datenhaltung;

import java.util.Iterator;

/**
 * Iterator über die Wahlkreisgewinner
 */
class WahlkreisgewinnerIterator extends FilterIterator<Kreiswahlvorschlag> {

	private final Partei partei;
	private final Iterator<Wahlkreis> wahlkreisIterator;

	/**
	 * Erzeugt einen Iterator, der über die Kreiswahlvorschläge der übergebenen
	 * Partei itertiert, die ihren Wahlkreis gewonnen haben.
	 * 
	 * @param wahlkreisIterator
	 *            - Iterator über die Wahlkreise, die betrachtet werden sollen
	 */
	WahlkreisgewinnerIterator(Iterator<Wahlkreis> wahlkreisIterator,
			Partei partei) {
		super();
		this.wahlkreisIterator = wahlkreisIterator;
		this.partei = partei;
		this.internalFindNextReady();
	}

	/**
	 * Benutzt den wahlkreisIterator, um den nächsten Wahlkreisgewinner der
	 * Partei zu finden.
	 */
	@Override
	protected Kreiswahlvorschlag internalFindNext() {
		Kreiswahlvorschlag next = null;
		while (wahlkreisIterator.hasNext()) {
			Wahlkreis wahlkreis = wahlkreisIterator.next();
			Kreiswahlvorschlag gewinner = wahlkreis.getGewinner();
			if (gewinner == null) {
				throw new IllegalStateException("Gewinner wure nicht gesetzt.");
			}
			if (gewinner.getPartei() == partei) {
				next = gewinner;
				break;
			}
		}
		return next;
	}

}

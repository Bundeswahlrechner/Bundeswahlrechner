/**
 * 
 */
package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Ein {@link Zuteilungsverfahren} auf Basis des Hare/Niemeyer-Verfahrens.
 * 
 * @author Peter Steinmetz
 * 
 */
public class HareNiemeyer implements Zuteilungsverfahren {

	/** Die Größe des zu verteilenden Kontingents. */
	private int kontingent;

	/**
	 * Die Zuteilungsobjekte und ihr, in der Methode {@code berechneZuteilung}
	 * bestimmes, Zuteilungsergebnis.
	 */
	private Map<Zuteilbar, Integer> zuteilungsobjekte;

	/**
	 * Erzeugt ein neues {@link Zuteilungsverfahren}, dass nach dem
	 * Hare/Niemeyer-Verfahrens arbeitet.
	 */
	public HareNiemeyer() {
		zuteilungsobjekte = new HashMap<Zuteilbar, Integer>();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Die Verteilung erfolgt in dieser Klasse nach dem Hare/Niemeyer-Verfahren.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Zuteilungsverfahren
	 * #berechneZuteilung()
	 */
	@Override
	public void berechneZuteilung() {
		if (zuteilungsobjekte.isEmpty()) {
			throw new IllegalStateException(
					"Es kann keine Verteilung durchgeführt werden, wenn noch keine Zuteilungsobjekte gesetzt wurde.");
		}
		int verteilteSitze = 0;
		// Prüfe, ob die Summe der Zuteilungswerte der Zuteilungsobjekte größer
		// als 0 ist.
		Iterator<Zuteilbar> ztIt = zuteilungsobjekte.keySet().iterator();
		while (ztIt.hasNext()) {
			if (ztIt.next().getZuteilungswert() > 0) {
				// Ein Zuteilungswert > 0 wurde gefunden, die Überprüfung kann
				// abgebrochen werden.
				break;
			}
			if (!(ztIt.hasNext())) {
				// Es gibt kein weiteres Zuteilungsobjekt mehr - es wurden also
				// nur negative Zuteilungswerte oder 0 als Zuteilungswert
				// spezifiziert.
				throw new IllegalArgumentException(
						"Keines der gesetzten Zuteilungsobjekte hat einen Zuteilungswert größer 0.");
			}
		}
		int zuteilungswertGesamtzahl = 0;
		for (Zuteilbar zt : zuteilungsobjekte.keySet()) {
			// Speicher Zuteilungswert zwischen zur Performanceoptimierung.
			int zuteilungswert = zt.getZuteilungswert();
			// Prüfe ob Zuteilungswert gültig ist.
			if (zuteilungswert < 0) {
				throw new IllegalArgumentException("Das Zuteilungsobjekt "
						+ zt.toString()
						+ " spezifizert einen negativen Zuteilungswert");
			}
			zuteilungswertGesamtzahl += zt.getZuteilungswert();
		}
		double divisor = ((double) zuteilungswertGesamtzahl)
				/ ((double) kontingent);
		Map<Double, Set<Zuteilbar>> nachkommastellen = new HashMap<Double, Set<Zuteilbar>>();
		for (Zuteilbar zt : zuteilungsobjekte.keySet()) {
			int ztSitze = (int) (zt.getZuteilungswert() / divisor);
			zuteilungsobjekte.put(zt, ztSitze);
			double nachkommastelle = ((zt.getZuteilungswert()) / divisor)
					- ztSitze;
			if (nachkommastellen.get(nachkommastelle) == null) {
				nachkommastellen.put(((zt.getZuteilungswert()) / divisor)
						- ztSitze, new HashSet<Zuteilbar>());
			}
			nachkommastellen
					.get(((zt.getZuteilungswert()) / divisor) - ztSitze)
					.add(zt);
			verteilteSitze += ztSitze;
		}
		List<Double> quoten = new LinkedList<Double>(nachkommastellen.keySet());
		Collections.sort(quoten);
		Collections.reverse(quoten);
		// int diff = kontingent - verteilteSitze;
		Iterator<Double> it = quoten.iterator();
		// while (diff > 0) {
		while (verteilteSitze < kontingent) {
			Double aktuelleNachkommastelle = it.next();
			for (Zuteilbar zt : nachkommastellen.get(aktuelleNachkommastelle)) {
				zuteilungsobjekte.put(zt, zuteilungsobjekte.get(zt) + 1);
				// diff--;
				verteilteSitze++;
				if (verteilteSitze == kontingent) {
					break;
				}
			}
		}
	}

	@Override
	public HareNiemeyer clone() {
		HareNiemeyer result = new HareNiemeyer();
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Zuteilungsverfahren
	 * #getZuteilungsergebnis
	 * (edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Zuteilbar)
	 */
	@Override
	public int getZuteilungsergebnis(Zuteilbar objekt) {
		return zuteilungsobjekte.get(objekt);
	}

	/**
	 * {@inheritDoc}
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Zuteilungsverfahren
	 * #setZuteilungskontingent(int)
	 */
	@Override
	public void setZuteilungskontingent(int kontingent) {
		if (kontingent < 0) {
			throw new IllegalArgumentException(
					"Das Kontingent muss positiv oder 0 sein.");
		}
		this.kontingent = kontingent;
	}

	/**
	 * {@inheritDoc}
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Zuteilungsverfahren
	 * #setZuteilungsobjekte(java.util.Collection)
	 */
	@Override
	public void setZuteilungsobjekte(Collection<? extends Zuteilbar> objekte) {
		if (objekte.size() == 0) {
			throw new IllegalArgumentException(
					"Es kann keine leere Menge an Zuteilungsobjekten gesetzt werden.");
		}
		// Neue Zuteilungsobjekte anlegen, da das Verfahren ggf. mehrfach mit
		// unterschiedlichen Objekten benutzt wird.
		zuteilungsobjekte = new HashMap<Zuteilbar, Integer>();
		for (Zuteilbar objekt : objekte) {
			zuteilungsobjekte.put(objekt, 0);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Hare-Niemeyer";
	}

}

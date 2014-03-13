package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Modelliert ein {@link Zuteilungsverfahren}, dass mit einer Divisormethode
 * arbeitet.
 * 
 * @author tillneuber
 * @version 1.1
 */
public abstract class Divisorverfahren implements Cloneable,
		Zuteilungsverfahren {

	/** Die Größe des zu verteilenden Kontingents. */
	private int kontingent;

	/**
	 * Die errechneten Prioritätszahlen, die die Reihenfolge, in der das
	 * Kontingent zugeteilt wird festlegen, und die jeweils zugehörigen
	 * Zuteilungsobjekte.
	 */
	private Map<Double, Set<Zuteilbar>> prioritaetszahlen;

	/**
	 * Die Zuteilungsobjekte und ihr, in der Methode {@code berechneZuteilung}
	 * bestimmes, Zuteilungsergebnis.
	 */
	private Map<Zuteilbar, Integer> zuteilungsobjekte;

	/**
	 * Erzeugt ein neues {@link Zuteilungsverfahren}, dass nach der
	 * Höchstzahlmethode des Sainte-Lague-Verfahrens arbeitet.
	 */
	public Divisorverfahren() {
		zuteilungsobjekte = new HashMap<Zuteilbar, Integer>();
		prioritaetszahlen = new HashMap<Double, Set<Zuteilbar>>();
	}

	/**
	 * Einschubmethode zur Berechnung der nächsten Prioritätszahl aus einem
	 * Zuteilungswert und dem Iterationsschritt der angibt, die wievielte
	 * Prioritätszahl bestimmt werden soll.
	 * 
	 * @param zuteilungswert
	 *            der Zuteilungswert, zu dem die Prioritätszahl berechnet wird
	 * @param iterationsschritt
	 *            der aktuelle Iterationsschritt
	 * @return
	 */
	protected abstract double berechnePrioritaetszahl(int zuteilungswert,
			int iterationsschritt);

	/**
	 * Einschubmethode zum Sortieren der Prioritätszahlen entsprechend der dort
	 * spezifizierten Reihenfolge.
	 * 
	 * @param zahlen
	 *            die zu ordnenden Zahlen
	 * @return ein Iterator, der die geordneten Zahlen liefert
	 */
	protected abstract Iterator<Double> berechnePrioritaetszahlenreihenfolge(
			List<Double> zahlen);

	/**
	 * {@inheritDoc}
	 * <p>
	 * Die Verteilung erfolgt in dieser Klasse nach einem Divisorverfahren. Die
	 * Divisionsergebnisse werden in der jeweiligen Unterklasse bestimmt.
	 */
	@Override
	public void berechneZuteilung() {
		if (zuteilungsobjekte.isEmpty()) {
			throw new IllegalStateException(
					"Es kann keine Verteilung durchgeführt werden, wenn noch keine Zuteilungsobjekte gesetzt wurde.");
		}
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
		// Berechnung erfolgt evtl. mehrfach mit verschiedenen
		// Zuteilungsobjekten/Kontingenten, setze deshalb die berechneten
		// Zwischenergebnisse in Form der Prioritätszahlen zurück.
		prioritaetszahlen = new HashMap<Double, Set<Zuteilbar>>();
		// Betrachte im folgenden alle Zuteilungsobjekte nacheinander.
		for (Zuteilbar zt : zuteilungsobjekte.keySet()) {
			// Zuteilungswert für folgende Prioritätszahlenberechnung merken!
			// Bei einigen Zuteilungsobjekten muss der Zuteilungswert aufwändig
			// berechnet werden (Bsp.: Zweitstimmenzahl bei Landeslisten) -
			// Zwischenspeichern dieses Wertes führt zu signifikanter
			// Performanceverbesserung (tlw. bis zu 5x schneller!).
			int zuteilungswert = zt.getZuteilungswert();
			if (zuteilungswert < 0) {
				throw new IllegalArgumentException("Das Zuteilungsobjekt "
						+ zt.toString()
						+ " spezifizert einen negativen Zuteilungswert");
			}
			// Maximal muss die berechnete Anzahl an Prioritätszahlen eines
			// Zuteilungsobjekts der Kontingentshöhe entsprechen (genau dann,
			// wenn das gesamte Kontingent nur einem Zuteilungsobjekt zugeordnet
			// wird). Berechne deshalb für jedes Zuteilungsobjekt so viele
			// Prioritätszahlen.
			for (int i = 1; i < (kontingent + 1); i++) {
				// Berechne die nächste Höchstzahl.
				double prioritaetszahl = berechnePrioritaetszahl(
						zuteilungswert, i);
				// Fallunterscheidung: Gibt es bereits ein Zuteilungsobjekt, dem
				// diese Prioritätszahl zugeordnet wurde?
				if (prioritaetszahlen.get(prioritaetszahl) == null) {
					// neue Prioritätszahl: Lege Parteienmenge an, die dieser
					// Prioritätszahl zugeordnet wird und füge die aktuelle
					// Partei in die Menge ein.
					Set<Zuteilbar> neueZuordnung = new HashSet<Zuteilbar>();
					neueZuordnung.add(zt);
					prioritaetszahlen.put(prioritaetszahl, neueZuordnung);
				} else {
					// Prioritätszahl wurde bereits einem anderen Objekt
					// zugeordnet: Füge das aktuelle Objekt in die bestehende
					// Menge der Zuteilungsobjekte ein, die der Prioritätszahl
					// zugeordnet sind.
					prioritaetszahlen.get(prioritaetszahl).add(zt);
				}
			}
		}
		List<Double> zahlen = new LinkedList<Double>(prioritaetszahlen.keySet());
		// Ordne die Prioritätszahlen entsprechend der in der Unterklasse
		// festgelegten Reihenfolge; iteriere anschließend über die geordneten
		// Zahlen.
		Iterator<Double> it = berechnePrioritaetszahlenreihenfolge(zahlen);
		// Gibt an, wie viel bereits auf die Zuteilungsobjekte verteilt wurde.
		int zugeteiltesKontingent = 0;
		// Verteile solange, bis das zugeteilteKontingent dem tatsächlichen
		// entspricht.
		while (zugeteiltesKontingent < kontingent) {
			double aktuelleZahl = it.next();
			// Iteriere über die Zuteilungsobjekte, die der aktuellen
			// Prioritätszahl zugeordnet sind.
			Iterator<Zuteilbar> aktuelleZuteilungsobjekte = prioritaetszahlen
					.get(aktuelleZahl).iterator();
			// Beachte: Sind der aktuellen Höchstzahl bspw. 3
			// Zuteilungsobjekte zugeordnet, aber es muss nur noch 1
			// Kontingentselement verteilt werden, muss die Verteilung
			// entsprechend abbrechen; deshalb erneute for-Schleife bei der
			// Zuweisung von Kontingentselementen auf die Zuteilungsobjekte
			// die der aktuellen Prioritätszahl zugeordnet sind.
			while ((zugeteiltesKontingent < kontingent)
					&& aktuelleZuteilungsobjekte.hasNext()) {
				Zuteilbar aktuellesZuteilungsobjekt = aktuelleZuteilungsobjekte
						.next();
				// Erhöhe Zuteilungsergebnis um 1.
				zuteilungsobjekte.put(aktuellesZuteilungsobjekt,
						zuteilungsobjekte.get(aktuellesZuteilungsobjekt) + 1);
				zugeteiltesKontingent++;
			}

		}
	}

	@Override
	public abstract Divisorverfahren clone();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getZuteilungsergebnis(Zuteilbar objekt) {
		return zuteilungsobjekte.get(objekt);
	}

	/**
	 * {@inheritDoc}
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

}
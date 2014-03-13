package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Ein {@link Zuteilungsverfahren} auf Basis des Sainte-Lague-Verfahrens. Die
 * Berechnung erfolgt nach der Divisormethode.
 * 
 * @author tillneuber
 * @version 1.1
 * 
 */
public class SainteLagueIterativ implements Zuteilungsverfahren {

	/**
	 * Gibt die Zeit in Millisekunden an, die dieser Algorithmus höchstens
	 * rechnen darf, bevor auf ein Alternativverfahren gewechselt wird.
	 */
	private static final long BERECHNUNGSZEIT = 15;

	/**
	 * Gibt an, um wie viel Prozent der verwendete Divisor pro Iterationsschritt
	 * maximal verändert wird. (Bsp.: 0.02 bedeutet eine maximale Veränderung um
	 * 2%)
	 */
	private static final double DIVISOR_VERAENDERUNG = 0.02;

	/**
	 * Hilfsmethode zum Runden von Zahlen. Es wird mathematisch korrekt
	 * gerundet, bei 0.5 entscheidet aber der Zufall, ob auf- oder abgerundet
	 * wird.
	 * 
	 * @param zahl
	 *            die zu rundende Zahl
	 * @return das Rundungsergebnis
	 */
	private static int zufaelligRunden(double zahl) {
		// Prüfe ob Nachkommaanteil gerade 0.5
		if ((zahl % 1) != 0.5) {
			// Nachkommaanteil ungleich 0.5: Runde mathematisch.
			return (int) Math.round(zahl);
		} else {
			// Nachkommaanteil gleich 0.5: Runde zufällig auf oder ab.
			// Bestimme hierzu eine Zufallszahl, die 0 oder 1 annimmt und Runde
			// anschließend entsprechend.
			int zufall = (int) Math.round(Math.random());
			if (zufall == 1) {
				return (int) Math.ceil(zahl);
			} else {
				return (int) Math.floor(zahl);
			}
		}
	}

	/** Die Größe des zu verteilenden Kontingents. */
	private int kontingent;

	/**
	 * Die Zuteilungsobjekte und ihr, in der Methode {@code berechneZuteilung}
	 * bestimmes, Zuteilungsergebnis.
	 */
	private Map<Zuteilbar, Integer> zuteilungsobjekte;

	/**
	 * Erzeugt ein neues {@link Zuteilungsverfahren}, dass nach der
	 * Divisormethode des Sainte-Lague-Verfahrens arbeitet.
	 */
	public SainteLagueIterativ() {
		zuteilungsobjekte = new HashMap<Zuteilbar, Integer>();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Die Verteilung erfolgt in dieser Klasse nach der Divisormethode des
	 * Sainte-Lague-Verfahrens.
	 */
	@Override
	public void berechneZuteilung() {
		if (zuteilungsobjekte.isEmpty()) {
			throw new IllegalStateException(
					"Es kann keine Verteilung durchgeführt werden, wenn noch keine Zuteilungsobjekte gesetzt wurde.");
		}
		// Speichere die Anfangszeit, um Algorithmus nach BERECHNUNGSZEIT
		// abbrechen zu können.
		long startZeit = System.currentTimeMillis();
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
		// Falls das Kontingent noch nicht oder auf 0 gesetzt wurde muss nicht
		// verteilt werden - alle Zuteilungsergebnisse sind dann 0.
		if (kontingent > 0) {
			// Ermittle zunächst den Anfangsdivisor: Bestimme hierzu die Summe
			// der
			// Zuteilungswerte und teile diese durch das zu verteilende
			// Kontingent.
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
			double divisor = zuteilungswertGesamtzahl / kontingent;
			int diff;
			do {
				// verteiltesKontingent gibt nach der Iteration über die
				// Zuteilungsobjekte
				// die durch den momentanen Divisor verteilten
				// Kontingentsanteile
				// an; für einen korrekten Divisor muss verteilteSitze =
				// kontingent
				// gelten
				int verteiltesKontingent = 0;
				for (Zuteilbar zt : zuteilungsobjekte.keySet()) {
					// Bestimme das Zuteilungsergebnis für dieses
					// Zuteilungsobjekt
					// mit dem aktuellen Divisor
					int ztErgebnis = zufaelligRunden(zt.getZuteilungswert()
							/ divisor);
					verteiltesKontingent += ztErgebnis;
					zuteilungsobjekte.put(zt, ztErgebnis);
				}
				// Bestimme die Differenz zwischen dem tatsächlichen Kontingent
				// und
				// dem verteilten Kontingent.
				diff = kontingent - verteiltesKontingent;
				if (diff > 0) {
					// Zu wenig des Kontingents wurde verteilt; verringere
					// deshalb
					// den Divisor durch Multiplikation mit einem Faktor im
					// Intervall (1 - DIVISOR_VERÄNDERUNG, 1], insbesondere also
					// Faktor <= 1; in der nächsten Iteration wird dadurch mehr
					// verteilt
					divisor *= (1 - (DIVISOR_VERAENDERUNG * Math.random()));
				} else if (diff < 0) {
					// Zu viel des Kontingents wurde verteilt; erhöhe deshalb
					// den
					// Divisor durch Multiplikation mit einem Faktor im
					// Intervall
					// [1, 1 + DIVISOR_VERÄNDERUNG], insbesondere also Faktor >=
					// 1; in der nächsten Iteration wird dadurch weniger
					// verteilt
					divisor *= (1 + (DIVISOR_VERAENDERUNG * Math.random()));
				}
				// Überprüfe, ob maximale Berechnungszeit verbraucht ist. Dies
				// kann insbesondere passieren, wenn für die gegebenen Parameter
				// kein Divisor existiert. Wechsle in diesem Fall auf
				// SainteLagueHoechstzahl.
				if (System.currentTimeMillis() - startZeit > BERECHNUNGSZEIT) {
					// Erstelle alternatives Berechnungsverfahren mit den
					// aktuellen Parametern.
					Zuteilungsverfahren alternativ = new SainteLagueHoechstzahl();
					alternativ.setZuteilungskontingent(kontingent);
					alternativ.setZuteilungsobjekte(zuteilungsobjekte.keySet());
					alternativ.berechneZuteilung();
					// Frage die Berechnungsergebnisse ab.
					for (Zuteilbar zt : zuteilungsobjekte.keySet()) {
						zuteilungsobjekte.put(zt,
								alternativ.getZuteilungsergebnis(zt));
					}
					// Breche iterative Berechnung ab - Ergebnisse liegen jetzt
					// vor.
					break;
				}
			} while (diff != 0);
		}
	}

	@Override
	public SainteLagueIterativ clone() {
		return new SainteLagueIterativ();
	}

	@Override
	public int getZuteilungsergebnis(Zuteilbar objekt) {
		return zuteilungsobjekte.get(objekt);
	}

	@Override
	public void setZuteilungskontingent(int kontingent) {
		if (kontingent < 0) {
			throw new IllegalArgumentException(
					"Das Kontingent muss positiv oder 0 sein.");
		}
		this.kontingent = kontingent;
	}

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
		return "Sainte-Lague Iterativ";
	}

}

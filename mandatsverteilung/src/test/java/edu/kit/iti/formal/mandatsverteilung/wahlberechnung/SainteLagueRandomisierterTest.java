package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.RepeatRule.Repeat;

/**
 * Testfälle für die Sainte-Lague-Verfahren {@link SainteLagueHoechstzahl},
 * {@link SainteLagueIterativ} und {@link SainteLagueRangmasszahl}.
 * 
 * @author tillneuber
 * 
 */
public class SainteLagueRandomisierterTest {

	/** Gibt an, mit wie vielen {@link Zuteilungsobjekt}en getestet wird. */
	private final int ANZAHL_ZUTEILUNGSOBJEKTE = 10;

	/** Regel zur mehrfachen Durchführung eines Tests. */
	@Rule
	public RepeatRule repeatRule = new RepeatRule();

	/**
	 * Gibt die abgelaufene Gesamtzeit an.
	 */
	@SuppressWarnings("unused")
	private long timer = 0;

	/** Die verschiedenen Sainte-Lague-Verfahren, die getestet werden. */
	private List<Zuteilungsverfahren> verfahren;

	/** Die zu Zuteilungsobjekte, auf die das Kontingent verteilt wird. */
	private List<Zuteilbar> zuteilungsobjekte;

	@Before
	public void setUp() throws Exception {
		verfahren = new LinkedList<Zuteilungsverfahren>();
		zuteilungsobjekte = new LinkedList<Zuteilbar>();
		verfahren.add(new SainteLagueIterativ());
		verfahren.add(new SainteLagueRangmasszahl());
		verfahren.add(new SainteLagueHoechstzahl());
		for (int k = 0; k < ANZAHL_ZUTEILUNGSOBJEKTE; k++) {
			// Bundesländer werden als Zuteilungsobjekte verwendet. Der
			// Zuteilungswert liegt zwischen 0 und 9.999.999.
			Zuteilbar zt = new Bundesland("Nummer: " + Integer.toString(k),
					Integer.toString(k), (int) (Math.random() * 10000000));
			zuteilungsobjekte.add(zt);
		}
		for (Zuteilungsverfahren zuteiler : verfahren) {
			zuteiler.setZuteilungsobjekte(zuteilungsobjekte);
		}
	}

	/**
	 * Verteilt mehrfach mit den verschiedenen Saint-Lague-Varianten das
	 * aktuelle Kontingent auf die Zuteilungsobjekte. Anschließend wird
	 * überprüft, ob die Summe der Zuteilungsergebnisse dem Kontingent
	 * entspricht und ob alle Sainte-Lague-Varianten zu denselben
	 * Verteilungsergebnissen gelangen.
	 * <p>
	 * Beachte: Bei diesem Test wird immer ein positives Kontingent verteilt.
	 */
	@Test
	@Repeat(times = 100)
	public void testeZufaelligMitPositivemKontingent() {
		// Bestimme zufälliges aber positives Kontingent.
		int kontingent = (int) ((Math.random() * 1000) + 1);
		for (Zuteilungsverfahren zuteiler : verfahren) {
			zuteiler.setZuteilungskontingent(kontingent);
			long aktuelleStartZeit = System.currentTimeMillis();
			zuteiler.berechneZuteilung();
			timer += (System.currentTimeMillis() - aktuelleStartZeit);
			// Output für Performancemessungen
			// System.out.println(zuteiler.toString() + " " + timer);
		}
		// Berechne das tatsächlich verteilte Kontingent.
		int verteiltesKontingent = 0;
		for (Zuteilbar zt : zuteilungsobjekte) {
			// Wähle ein beliebiges Zuteilungsverfahren als Vergleichswert.
			int vergleichswert = verfahren.get(0).getZuteilungsergebnis(zt);
			verteiltesKontingent += vergleichswert;
			for (Zuteilungsverfahren zuteiler : verfahren) {
				assertEquals(vergleichswert, zuteiler.getZuteilungsergebnis(zt));
			}
		}
		assertEquals(kontingent, verteiltesKontingent);
	}

}

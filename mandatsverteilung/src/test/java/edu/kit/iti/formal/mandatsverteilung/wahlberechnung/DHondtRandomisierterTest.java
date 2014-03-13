/**
 * 
 */
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
 * Testfälle für {@link DHondt}-Verfahren
 * 
 * @author Peter Steinmetz
 * 
 */
public class DHondtRandomisierterTest {

	/** Gibt an, mit wie vielen {@link Zuteilungsobjekt}en getestet wird. */
	private final int		ANZAHL_ZUTEILUNGSOBJEKTE	= 10;

	/** Regel zur mehrfachen Durchführung eines Tests. */
	@Rule
	public RepeatRule		repeatRule					= new RepeatRule();

	/** Das Hare-Niemeyer-Verfahren, das getestet wird. */
	private DHondt			verfahren;

	/** Die zu Zuteilungsobjekte, auf die das Kontingent verteilt wird. */
	private List<Zuteilbar>	zuteilungsobjekte;

	@Before
	public void setUp() throws Exception {
		verfahren = new DHondt();
		zuteilungsobjekte = new LinkedList<Zuteilbar>();
		for (int k = 0; k < ANZAHL_ZUTEILUNGSOBJEKTE; k++) {
			// Bundesländer werden als Zuteilungsobjekte verwendet. Der
			// Zuteilungswert liegt zwischen 0 und 9.999.999.
			Zuteilbar zt = new Bundesland("Nummer: " + Integer.toString(k),
					Integer.toString(k), (int) (Math.random() * 10000000));
			zuteilungsobjekte.add(zt);
		}
		verfahren.setZuteilungsobjekte(zuteilungsobjekte);
	}

	/**
	 * Verteilt mehrfach mit dem {@link DHondt}-Verfahren das
	 * aktuelle Kontingent auf die Zuteilungsobjekte. Anschließend wird
	 * überprüft, ob die Summe der Zuteilungsergebnisse dem Kontingent
	 * entspricht..
	 * <p>
	 * Beachte: Bei diesem Test wird immer ein positives Kontingent verteilt.
	 */
	@Test
	@Repeat(times = 10)
	public void testeZufaelligMitPositivemKontingent() {
		// Bestimme zufälliges aber positives Kontingent.
		int kontingent = (int) ((Math.random() * 1000) + 1);
		verfahren.setZuteilungskontingent(kontingent);
		verfahren.berechneZuteilung();
		// Berechne das tatsächlich verteilte Kontingent.
		int verteiltesKontingent = 0;
		for (Zuteilbar zt : zuteilungsobjekte) {
			verteiltesKontingent += verfahren.getZuteilungsergebnis(zt);
		}
		assertEquals(kontingent, verteiltesKontingent);
	}

}

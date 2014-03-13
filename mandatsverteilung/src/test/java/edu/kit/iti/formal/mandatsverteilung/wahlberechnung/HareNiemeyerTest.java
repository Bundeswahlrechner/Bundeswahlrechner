/**
 * 
 */
package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;

/**
 * Testfälle für {@link HareNiemeyer}-Verfahren
 * 
 * @author Peter Steinmetz
 * 
 */
public class HareNiemeyerTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/** Das Hare-Niemeyer-Verfahren, das getestet wird. */
	private HareNiemeyer verfahren;

	/**
	 * initialisiert das Verfahren vor jedem auszuführenden Testfall neu
	 */
	@Before
	public void setUp() {
		verfahren = new HareNiemeyer();
	}

	/**
	 * Testdaten nach
	 * http://www.bundeswahlleiter.de/de/glossar/texte/Hare_Niemeyer.html
	 * (Stand: 06.02.2014)
	 */
	@Test
	public void testeBundeswahlleiterBeispiel() {
		Zuteilbar[] zt = new Zuteilbar[3];
		zt[0] = new Bundesland("A", "A", 10000);
		zt[1] = new Bundesland("B", "B", 6000);
		zt[2] = new Bundesland("C", "C", 1500);
		List<Zuteilbar> zuteilungsobjekte = new LinkedList<Zuteilbar>();
		for (int i = 0; i < zt.length; i++) {
			zuteilungsobjekte.add(zt[i]);
		}
		verfahren.setZuteilungsobjekte(zuteilungsobjekte);
		verfahren.setZuteilungskontingent(8);
		verfahren.berechneZuteilung();
		assertEquals(4, verfahren.getZuteilungsergebnis(zt[0]));
		assertEquals(3, verfahren.getZuteilungsergebnis(zt[1]));
		assertEquals(1, verfahren.getZuteilungsergebnis(zt[2]));
	}

	/**
	 * Führe das Testbeispiel des Bundeswahlleiters mit den geclonten Verfahren
	 * durch. Testdaten:
	 * http://www.bundeswahlleiter.de/de/glossar/texte/Hare_Niemeyer.html
	 * (Stand: 06.02.2014)
	 */
	@Test
	public void testeBundeswahlleiterBeispielMitGeclonetenVerfahren() {
		Zuteilbar[] zt = new Zuteilbar[3];
		zt[0] = new Bundesland("A", "A", 10000);
		zt[1] = new Bundesland("B", "B", 6000);
		zt[2] = new Bundesland("C", "C", 1500);
		List<Zuteilbar> zuteilungsobjekte = new LinkedList<Zuteilbar>();
		for (int i = 0; i < 3; i++) {
			zuteilungsobjekte.add(zt[i]);
		}
		Zuteilungsverfahren clone = verfahren.clone();
		clone.setZuteilungsobjekte(zuteilungsobjekte);
		clone.setZuteilungskontingent(8);
		clone.berechneZuteilung();
		assertEquals(4, clone.getZuteilungsergebnis(zt[0]));
		assertEquals(3, clone.getZuteilungsergebnis(zt[1]));
		assertEquals(1, clone.getZuteilungsergebnis(zt[2]));
	}

	/**
	 * Setze Zuteilungsobjekte mit summiertem Zuteilungswert 0.
	 */
	@Test
	public void testeGesamterZuteilungswert0() {
		thrown.expect(IllegalArgumentException.class);
		List<Zuteilbar> zuteilungsobjekte = new LinkedList<Zuteilbar>();
		zuteilungsobjekte.add(new Bundesland("Zuteilungsobjekt 1", "1", 0));
		zuteilungsobjekte.add(new Bundesland("Zuteilungsobjekt 2", "2", 0));
		verfahren.setZuteilungsobjekte(zuteilungsobjekte);
		verfahren.setZuteilungskontingent(1);
		verfahren.berechneZuteilung();
	}

	/**
	 * Setzt eine leere Liste als Zuteilungsobjektmenge.
	 */
	@Test
	public void testeLeereMengeAlsZuteilungsobjekte() {
		thrown.expect(IllegalArgumentException.class);
		verfahren.setZuteilungsobjekte(new LinkedList<Zuteilbar>());
	}

	@Test
	public void testeMitInvalidemKontingent() {
		thrown.expect(IllegalArgumentException.class);
		verfahren.setZuteilungskontingent(-1);
	}

	/**
	 * Setze ein Zuteilungsobjekt mit negativem Zuteilungswert.
	 */
	@Test
	public void testeNegativenZuteilungswert() {
		thrown.expect(IllegalArgumentException.class);
		List<Zuteilbar> zuteilungsobjekte = new LinkedList<Zuteilbar>();
		zuteilungsobjekte.add(new Bundesland("Zuteilungsobjekt 1", "1", 1));
		zuteilungsobjekte.add(new Bundesland("Zuteilungsobjekt 2", "2", -1));
		verfahren.setZuteilungsobjekte(zuteilungsobjekte);
		verfahren.setZuteilungskontingent(1);
		verfahren.berechneZuteilung();
	}

	/**
	 * Teste die Verteilung bei einem Kontingent von 0.
	 */
	@Test(timeout = 100)
	public void testeNullKontingent() {
		List<Zuteilbar> zuteilungsobjekte = new LinkedList<Zuteilbar>();
		zuteilungsobjekte.add(new Bundesland("Zuteilungsobjekt 1", "1", 1));
		verfahren.setZuteilungsobjekte(zuteilungsobjekte);
		verfahren.setZuteilungskontingent(0);
		verfahren.berechneZuteilung();
		assertEquals(0,
				verfahren.getZuteilungsergebnis(zuteilungsobjekte.get(0)));
	}

	@Test
	public void testeVerteilungBeiGleicherStimmzahl() {
		List<Zuteilbar> zuteilungsobjekte = new LinkedList<Zuteilbar>();
		zuteilungsobjekte.add(new Bundesland("Bayern", "BY", 10));
		zuteilungsobjekte.add(new Bundesland("Baden-Württemberg", "BW", 10));
		verfahren.setZuteilungsobjekte(zuteilungsobjekte);
		verfahren.setZuteilungskontingent(10);
		verfahren.berechneZuteilung();
		for (Zuteilbar zt : zuteilungsobjekte) {
			assertEquals(5, verfahren.getZuteilungsergebnis(zt));
		}
	}

	@Test(timeout = 250)
	public void testeVerteilungBeiGleicherStimmzahlMitUnpassendemKontingent() {
		List<Zuteilbar> zuteilungsobjekte = new LinkedList<Zuteilbar>();
		zuteilungsobjekte.add(new Bundesland("Bayern", "BY", 10));
		zuteilungsobjekte.add(new Bundesland("Baden-Württemberg", "BW", 10));
		verfahren.setZuteilungsobjekte(zuteilungsobjekte);
		verfahren.setZuteilungskontingent(11);
		verfahren.berechneZuteilung();
		int verteiltesKontingent = 0;
		for (Zuteilbar zt : zuteilungsobjekte) {
			assertTrue(5 <= verfahren.getZuteilungsergebnis(zt)
					&& verfahren.getZuteilungsergebnis(zt) <= 6);
			verteiltesKontingent += verfahren.getZuteilungsergebnis(zt);
		}
		assertEquals(11, verteiltesKontingent);
	}

	/**
	 * Teste die Verteilung eines Kontingents von 5 auf zwei Zuteilungsobjekte
	 * mit gleichem Zuteilungswert.
	 */
	@Test
	public void testeVerteilungWennKeinOptimalesEregebnisMoeglich() {
		List<Zuteilbar> zuteilungsobjekte = new LinkedList<Zuteilbar>();
		zuteilungsobjekte.add(new Bundesland("Zuteilungsobjekt 1", "1", 5));
		zuteilungsobjekte.add(new Bundesland("Zuteilungsobjekt 2", "2", 5));
		verfahren.setZuteilungsobjekte(zuteilungsobjekte);
		verfahren.setZuteilungskontingent(5);
		verfahren.berechneZuteilung();
		assertTrue(2 <= verfahren.getZuteilungsergebnis(zuteilungsobjekte
				.get(0))
				&& verfahren.getZuteilungsergebnis(zuteilungsobjekte.get(0)) <= 3);
		assertTrue(2 <= verfahren.getZuteilungsergebnis(zuteilungsobjekte
				.get(1))
				&& verfahren.getZuteilungsergebnis(zuteilungsobjekte.get(1)) <= 3);
		assertEquals(
				5,
				verfahren.getZuteilungsergebnis(zuteilungsobjekte.get(0))
						+ verfahren.getZuteilungsergebnis(zuteilungsobjekte
								.get(1)));
	}

	/**
	 * Testdaten nach http://de.wikipedia.org/wiki/Hare-Niemeyer-Verfahren
	 * (Stand: 06.02.2014)
	 */
	@Test
	public void testeWikipediaBeispiel() {
		// Lege die Zuteilungsobjekte an; werden später zur Verifikation
		// benötigt.
		Zuteilbar[] zt = new Zuteilbar[4];
		zt[0] = new Bundesland("A", "A", 216);
		zt[1] = new Bundesland("B", "B", 310);
		zt[2] = new Bundesland("C", "C", 22);
		zt[3] = new Bundesland("D", "D", 32);
		List<Zuteilbar> zuteilungsobjekte = new LinkedList<Zuteilbar>();
		for (int i = 0; i < zt.length; i++) {
			zuteilungsobjekte.add(zt[i]);
		}
		verfahren.setZuteilungsobjekte(zuteilungsobjekte);
		verfahren.setZuteilungskontingent(100);
		verfahren.berechneZuteilung();
		assertEquals(37, verfahren.getZuteilungsergebnis(zt[0]));
		assertEquals(53, verfahren.getZuteilungsergebnis(zt[1]));
		assertEquals(4, verfahren.getZuteilungsergebnis(zt[2]));
		assertEquals(6, verfahren.getZuteilungsergebnis(zt[3]));
	}

	@Test
	public void testeWikipediaBeispielMitGeclonetemVerfahren() {
		HareNiemeyer clone = verfahren.clone();
		Zuteilbar[] zt = new Zuteilbar[4];
		zt[0] = new Bundesland("A", "A", 216);
		zt[1] = new Bundesland("B", "B", 310);
		zt[2] = new Bundesland("C", "C", 22);
		zt[3] = new Bundesland("D", "D", 32);
		List<Zuteilbar> zuteilungsobjekte = new LinkedList<Zuteilbar>();
		for (int i = 0; i < zt.length; i++) {
			zuteilungsobjekte.add(zt[i]);
		}
		clone.setZuteilungsobjekte(zuteilungsobjekte);
		clone.setZuteilungskontingent(100);
		clone.berechneZuteilung();
		assertEquals(37, clone.getZuteilungsergebnis(zt[0]));
		assertEquals(53, clone.getZuteilungsergebnis(zt[1]));
		assertEquals(4, clone.getZuteilungsergebnis(zt[2]));
		assertEquals(6, clone.getZuteilungsergebnis(zt[3]));
	}

	/**
	 * Ruft die Zuteilungsberechnung auf ohne zuvor Zuteilungsobjekte zu
	 * setzten.
	 */
	@Test
	public void testeZuteilungsberechnungOhneZuteilungsobjekte() {
		thrown.expect(IllegalStateException.class);
		verfahren.berechneZuteilung();
	}

	/**
	 * Setze ein Zuteilungsobjekt mit Zuteilungswert 0.
	 */
	@Test
	public void testeZuteilungswert0() {
		List<Zuteilbar> zuteilungsobjekte = new LinkedList<Zuteilbar>();
		zuteilungsobjekte.add(new Bundesland("Zuteilungsobjekt 1", "1", 0));
		zuteilungsobjekte.add(new Bundesland("Zuteilungsobjekt 2", "2", 1));
		verfahren.setZuteilungsobjekte(zuteilungsobjekte);
		verfahren.setZuteilungskontingent(1);
		verfahren.berechneZuteilung();
		assertEquals(1,
				verfahren.getZuteilungsergebnis(zuteilungsobjekte.get(1)));
	}

	@Test
	public void toStringTest() {
		assertEquals("Hare-Niemeyer", verfahren.toString());
	}
}

package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;

/**
 * Testfälle für die verschiedenen Sainte-Lauge-Varianten.
 * 
 * @author tillneuber
 * 
 */
@RunWith(value = Parameterized.class)
public class SainteLagueTest {

	@Parameters(name = "Verfahren: {0}")
	public static Iterable<Object[]> verfahren() {
		Object[][] verfahren = new Object[][] {
				{ new SainteLagueHoechstzahl() },
				{ new SainteLagueRangmasszahl() },
				{ new SainteLagueIterativ() } };
		return Arrays.asList(verfahren);
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/** Das Sainte-Lague-Verfahren, das getestet wird. */
	private final Zuteilungsverfahren verfahren;

	public SainteLagueTest(Zuteilungsverfahren verfahren) {
		this.verfahren = verfahren;
	}

	/**
	 * Testdaten nach
	 * http://www.bundeswahlleiter.de/de/glossar/texte/Saint_Lague_Schepers.html
	 * (Stand: 11.01.14)
	 */
	@Test
	public void testeBundeswahlleiterBeispiel() {
		Zuteilbar[] zt = new Zuteilbar[3];
		zt[0] = new Bundesland("Zuteilungsobjekt 1", "1", 10000);
		zt[1] = new Bundesland("Zuteilungsobjekt 2", "2", 6000);
		zt[2] = new Bundesland("Zuteilungsobjekt 3", "3", 1500);
		List<Zuteilbar> zuteilungsobjekte = new LinkedList<Zuteilbar>();
		for (int i = 0; i < 3; i++) {
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
	 * http://www.bundeswahlleiter.de/de/glossar/texte/Saint_Lague_Schepers.html
	 * (Stand: 11.01.14)
	 */
	@Test
	public void testeBundeswahlleiterBeispielMitGeclonetenVerfahren() {
		Zuteilbar[] zt = new Zuteilbar[3];
		zt[0] = new Bundesland("Zuteilungsobjekt 1", "1", 10000);
		zt[1] = new Bundesland("Zuteilungsobjekt 2", "2", 6000);
		zt[2] = new Bundesland("Zuteilungsobjekt 3", "3", 1500);
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

	/**
	 * Überprüfe, dass die Zuteilungsverfahren bei negativem Kontingent eine
	 * Exception werfen.
	 */
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

	/**
	 * Überprüfe die toString()-Methoden auf Korrektheit.
	 */
	@Test
	public void testeToString() {
		if (verfahren instanceof SainteLagueIterativ) {
			assertEquals("Ein Verfahren liefert einen falschen Namen.",
					"Sainte-Lague Iterativ", verfahren.toString());
		} else if (verfahren instanceof SainteLagueHoechstzahl) {
			assertEquals("Ein Verfahren liefert einen falschen Namen.",
					"Sainte-Lague Höchstzahl", verfahren.toString());
		} else

		if (verfahren instanceof SainteLagueRangmasszahl) {
			assertEquals("Ein Verfahren liefert einen falschen Namen.",
					"Sainte-Lague Rangmaßzahl", verfahren.toString());
		}

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
	 * Testdaten nach http://de.wikipedia.org/wiki/Sainte-Laguë-Verfahren#
	 * Berechnungsbeispiel_nach_dem_H.C3.B6chstzahlverfahren (Stand: 08.01.14)
	 */
	@Test
	public void testeWikipediaBeispiel() {
		// Lege die Zuteilungsobjekte an; werden später zur Verifikation
		// benötigt.
		Zuteilbar[] zt = new Zuteilbar[3];
		zt[0] = new Bundesland("Zuteilungsobjekt 1", "1", 5200);
		zt[1] = new Bundesland("Zuteilungsobjekt 2", "2", 1700);
		zt[2] = new Bundesland("Zuteilungsobjekt 3", "3", 3100);
		List<Zuteilbar> zuteilungsobjekte = new LinkedList<Zuteilbar>();
		for (int i = 0; i < 3; i++) {
			zuteilungsobjekte.add(zt[i]);
		}
		verfahren.setZuteilungsobjekte(zuteilungsobjekte);
		verfahren.setZuteilungskontingent(15);
		verfahren.berechneZuteilung();
		assertEquals(8, verfahren.getZuteilungsergebnis(zt[0]));
		assertEquals(2, verfahren.getZuteilungsergebnis(zt[1]));
		assertEquals(5, verfahren.getZuteilungsergebnis(zt[2]));
	}

	/**
	 * Ruft die Zuteilungsberechnung auf ohne zuvor Zuteilungsobjekte zu
	 * setzten.
	 */
	@Test
	public void testeZuteilungsberechnungOhneZuteilungsobjekte() {
		thrown.expect(IllegalStateException.class);
		// Erzeuge nicht-initialisiertes Verfahren durch clonen des aktuellen
		// Verfahrens.
		Zuteilungsverfahren neuesVerfahren = verfahren.clone();
		neuesVerfahren.setZuteilungskontingent(1);
		neuesVerfahren.berechneZuteilung();
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
}

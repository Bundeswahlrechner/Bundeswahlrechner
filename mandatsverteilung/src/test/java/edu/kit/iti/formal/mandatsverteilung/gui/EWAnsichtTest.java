package edu.kit.iti.formal.mandatsverteilung.gui;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kandidat;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kreiswahlvorschlag;

public class EWAnsichtTest {

	EinWahlAnsicht EWA;

	GUI gui;

	/**
	 * Testet die Daten der EinWahlansicht.
	 */
	@Test
	public void datenTest() {
		EWA = new EinWahlAnsicht(gui.aktiveWahl());

		Object[] bundeslaenderVergleich = { "Bundeslandnummer",
				"Bundeslandname", "CDU", "SPD", "DIE LINKE", "GRÜNE", "CSU" };
		assertArrayEquals(bundeslaenderVergleich, EWA.bundeslaenderKopfZeile);

		Object[] bundestagsVergleich = { "Gesamtsitzzahl", "CDU", "SPD",
				"DIE LINKE", "GRÜNE", "CSU" };

		assertArrayEquals(bundestagsVergleich, EWA.bundestagsKopfZeile);

		Object[][] bundestagsVergleichsDaten = { { 631, 255, 193, 64, 63, 56 } };
		testeDaten(bundestagsVergleichsDaten, EWA.bundestagsDaten);

		Object[][] bundeslaenderDatenVergleich = {
				{ 1, "Baden-Württemberg", 43, 20, 5, 10, 0 },
				{ 2, "Bayern", 0, 22, 4, 9, 56 },
				{ 3, "Berlin", 9, 8, 6, 4, 0 },
				{ 4, "Brandenburg", 9, 5, 5, 1, 0 },
				{ 5, "Bremen", 2, 2, 1, 1, 0 },
				{ 6, "Hamburg", 5, 5, 1, 2, 0 },
				{ 7, "Hessen", 21, 16, 3, 5, 0 },
				{ 8, "Mecklenburg-Vorpommern", 6, 3, 3, 1, 0 },
				{ 9, "Niedersachsen", 31, 25, 4, 6, 0 },
				{ 10, "Nordrhein-Westfalen", 63, 52, 10, 13, 0 },
				{ 11, "Rheinland-Pfalz", 16, 10, 2, 3, 0 },
				{ 12, "Saarland", 4, 3, 1, 1, 0 },
				{ 13, "Sachsen", 17, 6, 8, 2, 0 },
				{ 14, "Sachsen-Anhalt", 9, 4, 5, 1, 0 },
				{ 15, "Schleswig-Holstein", 11, 9, 1, 3, 0 },
				{ 16, "Thüringen", 9, 3, 5, 1, 0 } };
		testeDaten(bundeslaenderDatenVergleich, EWA.bundeslaenderDaten);
	}

	/**
	 * Testet, ob eine Partei mit nur einem Direktmandat angezeigt wird.
	 */
	@Test
	public void mlpd() {
		Bundestagswahl wahl = gui.aktiveWahl();
		Kreiswahlvorschlag k = new Kreiswahlvorschlag("AAAAAA",
				wahl.getWahlkreis(87));
		k.setPartei(wahl.getPartei("MLPD"));
		k.setStimmen(1000000);
		wahl.getWahlverfahren().berechneWahlergebnis();
		EinWahlAnsicht e = new EinWahlAnsicht(wahl);
		assertEquals("MLPD",
				e.bundeslaenderKopfZeile[e.bundeslaenderKopfZeile.length - 1]);
		assertEquals("MLPD",
				e.bundestagsKopfZeile[e.bundestagsKopfZeile.length - 1]);
		assertEquals("AAAAAA", e.wahlkreisGewinnerDaten[0][2]);
		boolean f = false;
		for (int i = 0; i < e.kandidatDaten.length; i++) {
			if ("AAAAAA".equals(e.kandidatDaten[i][1])
					&& "MLPD".equals(e.kandidatDaten[i][3])) {
				f = true;
			}
		}
		assertTrue(f);
		new EinWahlKuchendiagrammAnsicht(wahl);
		new EinWahlStabdiagrammAnsicht(wahl);
		new EinWahlTabellenAnsicht(wahl);
	}

	/**
	 * Test für neu hinzugefügtes Bundesland.
	 */
	@Test
	public void neuesBundesland() {
		Bundestagswahl wahl = gui.aktiveWahl();
		wahl.addBundesland(new Bundesland("ABC", "ABCDE", 0));
		EWA = new EinWahlAnsicht(wahl);
		Object[][] array = EWA.bundeslaenderDaten;
		assertEquals(1, array[0][0]);
		assertEquals("ABC", array[0][1]);
		for (int i = 2; i < array[0].length; i++) {
			assertEquals(0, array[0][i]);
		}
	}

	/**
	 * Testet, ob Parteilose Kandidaten angezeigt werden und die Erstellung
	 * keine Exceptions wirft.
	 */
	@Test
	public void parteilose() {
		Bundestagswahl wahl = gui.aktiveWahl();
		Kreiswahlvorschlag k = new Kreiswahlvorschlag("Per Pedes",
				wahl.getWahlkreis(87));
		k.setStimmen(1000000);
		wahl.getWahlverfahren().berechneWahlergebnis();
		EinWahlAnsicht e = new EinWahlAnsicht(wahl);
		assertEquals("Parteilose",
				e.bundeslaenderKopfZeile[e.bundeslaenderKopfZeile.length - 1]);
		assertEquals("Parteilose",
				e.bundestagsKopfZeile[e.bundestagsKopfZeile.length - 1]);
		assertEquals("Per Pedes", e.wahlkreisGewinnerDaten[0][2]);
		boolean b = false;
		for (Kandidat ka : wahl.getAbgeordnete()) {
			if (ka.getLandesliste() != null) {
				b = true;
			}
		}
		assertTrue(b);
		boolean f = false;
		for (int i = 0; i < e.kandidatDaten.length; i++) {
			if ("Per Pedes".equals(e.kandidatDaten[i][1])
					&& "Parteilose".equals(e.kandidatDaten[i][3])) {
				f = true;
			}
		}
		assertTrue(f);
		new EinWahlKuchendiagrammAnsicht(wahl);
		new EinWahlStabdiagrammAnsicht(wahl);
	}

	/**
	 * SetUp.
	 */
	@Before
	public void setUp() {
		gui = new GUI();
		gui.neueWahl.doClick();
	}

	/**
	 * Testet ob die Daten für das Erstellen von Tabellen und Diagrammen richtig
	 * initialisiert wurden.
	 */
	@Test
	public void TabellenWahlansichtTest() {
		EWA = new EinWahlAnsicht(gui.aktiveWahl());
		assertEquals(EWA.bundeslaenderDaten.length, 16);
		// Testet ob Alle 16 Bundesländer in der Bundesländertabelle
		// initialisiert wurden
		assertEquals(EWA.bundeslaenderKopfZeile.length, gui.aktiveWahl()
				.eingezogeneParteien().size() + 2);
		// Testet ob die richtige Anzahl an Parteien in der Bundesländertabelle
		// initialisiert wurden
		assertEquals(EWA.wahlkreisErststimmen.length, 299);
		// Testet ob Alle 299 Wahlkreise in der Erststimmentabelle initialisiert
		// wurden
		assertEquals(EWA.wahlkreisErststimmenKopfzeile.length, gui.aktiveWahl()
				.eingezogeneParteien().size() + 2);
		// Testet ob die richtige Anzahl an Parteien in der Erststimmentabelle
		// initialisiert wurden
		assertEquals(EWA.kandidatDaten.length, 631);
		// Testet ob Alle 631 Bundestagsabgeordneten in der Tabelle
		// initialisiert wurden
		assertEquals(EWA.wahlkreisDaten.length, 299);
		// Testet ob Alle 299 Wahlkreise in der Zweitstimmentabelle
		// initialisiert wurden
		assertEquals(EWA.wahlkreisGewinnerDaten.length, 299);
		// Testet ob Alle 299 Wahlkreise in der Wahlkreisgewinnertabelle
		// initialisiert wurden
		assertEquals(EWA.prozentansichtDaten.length, 1);
		assertEquals(EWA.prozentansichtKopfZeile.length, gui.aktiveWahl()
				.eingezogeneParteien().size() + 1);
		// Testet ob die richtige Anzahl an Parteien in der Prozenttabelle
		// initialisiert wurden
		assertEquals(EWA.bundestagsDaten.length, 1);
		assertEquals(EWA.bundestagsKopfZeile.length, gui.aktiveWahl()
				.eingezogeneParteien().size() + 1);
		// Testet ob die richtige Anzahl an Parteien der Bundestagstabelle
		// initialisiert wurden

	}

	/**
	 * Tear down.
	 */
	@After
	public void tearDown() {
		gui.dispose();
		gui = null;
	}

	/**
	 * Private Hilfsmethode, die testet, ob zwei doppelte Arrays "gleich" sind.
	 * 
	 * @param erwartet
	 *            erwartetes Array
	 * @param daten
	 *            tatsächliches array
	 */
	private void testeDaten(Object[][] erwartet, Object[][] daten) {
		for (int i = 0; i < erwartet.length; i++) {
			assertArrayEquals(erwartet[i], daten[i]);
		}
	}
}

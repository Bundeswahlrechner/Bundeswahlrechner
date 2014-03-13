package edu.kit.iti.formal.mandatsverteilung.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kreiswahlvorschlag;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Wahlkreis;

public class NWahltest {
	/**
	 * Test für neu hinzugefügtes Bundesland.
	 */
	private GUI gui;

	/**
	 * Testet, ob n-Wahlen mit verschiedenen Bundesländern verglichen werden
	 * können.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void bundeslandZuviel() {
		gui.neueWahl.doClick();
		Bundestagswahl wahl = gui.aktiveWahl();
		wahl.addBundesland(new Bundesland("ABC", "ABCDE", 0));
		gui.menu.vergleicheWahlen();
	}

	/**
	 * Testet, ob 2-Wahlen mit verschiedenen Bundesländern verglichen werden
	 * können.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void bundeslandZuviel2() {
		Bundestagswahl wahl = gui.aktiveWahl();
		wahl.addBundesland(new Bundesland("ABC", "ABCDE", 0));
		gui.menu.vergleicheWahlen();
	}

	/**
	 * Testet, ob ein neues Bundesland einer Wahl hinzugefügt werden kann ohne
	 * dass es zu Exceptions kommt.
	 */
	@Test
	public void neuesBundesland() {
		gui.wahlEntfernen();
		Bundestagswahl wahl = gui.aktiveWahl();
		wahl.addBundesland(new Bundesland("ABC", "ABCDE", 0));

		EinWahlAnsicht EWA = new EinWahlAnsicht(wahl);
		Object[][] array = EWA.bundeslaenderDaten;
		assertEquals(1, array[0][0]);
		assertEquals("ABC", array[0][1]);
		for (int i = 2; i < array[0].length; i++) {
			assertEquals(0, array[0][i]);
		}
		gui.neueWahl.doClick();
		wahl = gui.aktiveWahl();
		wahl.addBundesland(new Bundesland("ABC", "ABCDE", 0));
		gui.menu.vergleicheWahlen();
	}

	/**
	 * Grundfunktion.
	 */
	@Test
	public void nWahlen() {
		gui.neueWahl.doClick();
		gui.menu.vergleicheWahlen();
		assertNotNull(gui.aktiveWahl());
	}

	/**
	 * Testet, ob Parteilose Kandidaten fehler produzieren.
	 */
	@Test
	public void parteilos() {
		gui.wahlEntfernen();
		gui.wahlEntfernen();
		Bundestagswahl[] wahlen = new Bundestagswahl[3];
		for (int i = 0; i < wahlen.length; i++) {
			gui.neueWahl.doClick();
			wahlen[i] = gui.aktiveWahl();
			Wahlkreis a = new Wahlkreis("Hao", 999);
			a.setGewinner(new Kreiswahlvorschlag("ad", a));
			wahlen[i].getBundeslandName("Bayern").addWahlkreis(a);
			wahlen[i].getWahlverfahren().berechneWahlergebnis();
		}
		gui.menu.vergleicheWahlen();

	}

	/**
	 * SetUp.
	 */
	@Before
	public void setUp() {
		gui = new GUI();
		gui.neueWahl.doClick();
		gui.neueWahl.doClick();

	}

	/**
	 * TearDown.
	 */
	@After
	public void tearDown() {
		gui.dispose();
		gui = null;
	}

	/**
	 * Testet, ob n Wahlen mit verschiedenen Wahlkreisen verglichen werden
	 * können.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void wahlkreisZuviel() {
		Bundestagswahl[] wahlen = new Bundestagswahl[3];
		for (int i = 0; i < wahlen.length; i++) {
			gui.neueWahl.doClick();
			wahlen[i] = gui.aktiveWahl();
		}
		Wahlkreis a = new Wahlkreis("Hao", 999);
		a.setGewinner(new Kreiswahlvorschlag("ad", a));
		wahlen[0].getBundeslandName("Bayern").addWahlkreis(a);
		new NWahlenVergleichsAnsicht(wahlen,
				(List<Partei>) wahlen[0].eingezogeneParteien());

	}

	/**
	 * Testet, ob 2 Wahlen mit verschiedenen Wahlkreisen verglichen werden
	 * können.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void wahlkreisZuviel2() {
		Bundestagswahl[] wahlen = new Bundestagswahl[2];
		for (int i = 0; i < wahlen.length; i++) {
			gui.neueWahl.doClick();
			wahlen[i] = gui.aktiveWahl();
		}
		Wahlkreis a = new Wahlkreis("Hao", 999);
		a.setGewinner(new Kreiswahlvorschlag("ad", a));
		wahlen[0].getBundeslandName("Bayern").addWahlkreis(a);
		new ZweiWahlenStabdiagrammAnsicht(wahlen[0], wahlen[1],
				(List<Partei>) wahlen[0].eingezogeneParteien());

	}

	/**
	 * Grundfunktion.
	 */
	@Test
	public void zweiWahlen() {
		gui.menu.vergleicheWahlen();
		assertNotNull(gui.aktiveWahl());
	}

	/**
	 * Grundfunktion.
	 */
	@Test
	public void zwischenErgebnisansicht() {
		gui.menu.zwischenergebnis.doClick();
		gui.menu.zwischenergebnisAnsicht.b.doClick();
		assertFalse(gui.menu.zwischenergebnisAnsicht.isVisible());
	}

	/**
	 * 
	 */
	@Test
	public void zwischenErgebnisansichtMLPD() {
		Bundestagswahl wahl = gui.aktiveWahl();
		Kreiswahlvorschlag k = new Kreiswahlvorschlag("A", wahl.getWahlkreis(1));
		k.setPartei(wahl.getPartei("MLPD"));
		k.setStimmen(1000000);
		wahl.getWahlverfahren().berechneWahlergebnis();
		gui.menu.zwischenergebnis.doClick();
		boolean f = false;
		for (int i = 0; i < gui.menu.zwischenergebnisAnsicht.beschriftung.length; i++) {
			if ("MLPD".equals(gui.menu.zwischenergebnisAnsicht.beschriftung[i])) {
				f = true;
			}
		}
		assertTrue(f);

	}
}

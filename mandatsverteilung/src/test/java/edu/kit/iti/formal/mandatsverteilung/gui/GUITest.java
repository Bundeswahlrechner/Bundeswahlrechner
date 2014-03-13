/**
 * 
 */
package edu.kit.iti.formal.mandatsverteilung.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.swing.JTabbedPane;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

/**
 * Tests für die {@link GUI}
 * 
 * @author Peter Steinmetz
 * 
 */
public class GUITest {

	/**
	 * die {@link GUI}-Klasse die getestet wird
	 */
	private GUI gui;

	@Before
	public void setUp() {
		gui = new GUI();
	}

	@After
	public void tearDown() {
		gui.dispose();
	}

	/**
	 * Testet, ob {@code aktiveWahl()} korrekte Werte zurückgibt und ob alle
	 * Tabs eine Wahlansicht enthalten
	 */
	@Test(expected = NullPointerException.class)
	public void testeAktiveWahl() {
		assertNull(gui.aktiveWahl());
		gui.neueWahl.doClick();
		assertNotNull(gui.aktiveWahl());

		((JTabbedPane) gui.tabs.getSelectedComponent()).setSelectedIndex(0);
		assertNotNull(gui.tabs.getSelectedComponent());
		((JTabbedPane) gui.tabs.getSelectedComponent()).setSelectedIndex(1);
		assertNotNull(gui.tabs.getSelectedComponent());
		((JTabbedPane) gui.tabs.getSelectedComponent()).setSelectedIndex(2);
		assertNotNull(gui.tabs.getSelectedComponent());
		((JTabbedPane) gui.tabs.getSelectedComponent()).setSelectedIndex(3);
		assertNotNull(gui.tabs.getSelectedComponent());
		((JTabbedPane) gui.tabs.getSelectedComponent()).setSelectedIndex(0);
		assertNotNull(gui.tabs.getSelectedComponent());
		((JTabbedPane) gui.tabs.getSelectedComponent()).setSelectedIndex(1);
		assertNotNull(gui.tabs.getSelectedComponent());
		((JTabbedPane) gui.tabs.getSelectedComponent()).setSelectedIndex(2);
		assertNotNull(gui.tabs.getSelectedComponent());
		((JTabbedPane) gui.tabs.getSelectedComponent()).setSelectedIndex(3);
		assertNotNull(gui.tabs.getSelectedComponent());

		gui.ersetzeAktiveWahl(null);
		assertNull(gui.aktiveWahl());
	}

	/**
	 * Testet, ob {@code anzahlWahlen} und {@code getWahlen} korrekte Werte
	 * zurückgeben nachdem eine zufällige Anzahl (zwischen 1 und 10) an Wahlen
	 * angelegt wurden und nachdem diese wieder entfernt wurden..
	 */
	@Test
	public void testeAnzahlWahlen() {
		assertEquals(0, gui.anzahlWahlen());
		assertNull(gui.getWahlen());
		gui.wahlEntfernen.doClick();
		int anzahl = 1 + (int) Math.random() * 10;
		for (int i = 1; i <= anzahl; i++) {
			gui.neueWahl.doClick();
			assertEquals(i, gui.anzahlWahlen());
			assertEquals(i, gui.getWahlen().length);
		}
		for (int j = anzahl; j > 0; j--) {
			assertEquals(j, gui.anzahlWahlen());
			assertEquals(j, gui.getWahlen().length);
			gui.wahlEntfernen.doClick();
		}
	}

	/**
	 * Testet, ob die Knöpfe richt de-/aktiviert werden
	 */
	@Test
	public void testeDeAktivierungDerKnöpfe() {
		assertFalse(gui.wahlEntfernen.isEnabled());
		assertFalse(gui.wahlKopieren.isEnabled());
		assertTrue(gui.neueWahl.isEnabled());
		gui.neueWahl.doClick();
		assertTrue(gui.wahlEntfernen.isEnabled());
		assertTrue(gui.wahlKopieren.isEnabled());
		gui.wahlEntfernen.doClick();
		assertFalse(gui.wahlEntfernen.isEnabled());
		assertFalse(gui.wahlKopieren.isEnabled());
	}

	/**
	 * Testet, ob {@code ersetzeAktiveWahl} korrekt funktionert.
	 */
	@Test
	public void testeErsetzeAktiveWahl() {
		gui.neueWahl.doClick();
		Bundestagswahl wahl = gui.aktiveWahl();
		gui.ersetzeAktiveWahl(wahl);
		assertEquals(wahl, gui.aktiveWahl());
	}

	@Test
	public void testeNeueWahlButton() {
		assertTrue(gui.neueWahl.isEnabled());
		gui.neueWahl.doClick();
		assertNotNull(gui.aktiveWahl());
		assertEquals(1, gui.anzahlWahlen());
	}

	@Test
	public void testeWahlEntfernenButton() {
		assertFalse(gui.wahlEntfernen.isEnabled());
		gui.neueWahl.doClick();
		assertEquals(1, gui.anzahlWahlen());
		assertTrue(gui.wahlEntfernen.isEnabled());
		gui.wahlEntfernen.doClick();
		assertEquals(0, gui.anzahlWahlen());
	}

	@Test
	public void testeWahlKopierenButton() {
		assertFalse(gui.wahlKopieren.isEnabled());
		gui.neueWahl.doClick();
		Bundestagswahl wahl = gui.aktiveWahl();
		assertTrue(gui.wahlKopieren.isEnabled());
		gui.wahlKopieren.doClick();
		assertNotNull(gui.aktiveWahl());
		assertFalse(wahl.equals(gui.aktiveWahl()));

	}

}

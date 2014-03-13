/**
 * 
 */
package edu.kit.iti.formal.mandatsverteilung.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Peter Steinmetz
 * 
 */
public class GeneriereWahldatenFensterTest {

	/**
	 * Das zu testende {@link GeneriereWahldatenFenster}
	 */
	private GeneriereWahldatenFenster fenster;

	/**
	 * Die zugehörige {@link GUI}-Klasse
	 */
	private GUI gui;

	@Before
	public void setUp() {
		gui = new GUI();
		gui.neueWahl.doClick();
		fenster = new GeneriereWahldatenFenster(gui, false);
	}

	@After
	public void tearDown() {
		fenster.dispose();
		gui.dispose();
	}

	@Test
	public void testeAbbruchButton() {
		fenster.generieren.doClick();
		while (fenster.worker.isAlive()) {
			fenster.abbrechenKnopf.doClick();
		}
		assertEquals(1, gui.anzahlWahlen());
	}

	/**
	 * Testet die Generierung mit Einschränkung der absoluten Sitzzahl und
	 * überprüft, ob Teile des {@link GeneriereWahldatenFenster}s korrekt
	 * deaktiviert werden.
	 */
	@Test
	public void testeGenerierungMitAbsoluterSitzzahlEinschraenkung() {
		fenster.box1.setSelected(false);
		fenster.box2.setSelected(false);
		fenster.box2.setSelected(true);

		assertTrue(fenster.parteiAuswahl.isEnabled());
		fenster.parteiAuswahl.setSelectedItem("FDP");
		assertTrue(fenster.sitzzahlArt.isEnabled());
		fenster.sitzzahlArt.setSelectedItem("absolute Sitzzahl");
		fenster.box2.setSelected(false);
		fenster.box2.setSelected(true);
		fenster.sitzzahlArt.setSelectedItem("relative Sitzzahl");
		fenster.box2.setSelected(false);
		fenster.box2.setSelected(true);
		fenster.sitzzahlArt.setSelectedItem("absolute Sitzzahl");
		assertTrue(fenster.sitzzahlAbs.isEnabled());
		assertFalse(fenster.sitzzahlRel.isEnabled());
		assertTrue(fenster.abweichung2.isEnabled());
		assertFalse(fenster.sitzzahlGesamt.isEnabled());
		assertFalse(fenster.abweichung1.isEnabled());

		fenster.sitzzahlAbs.setValue(40);
		fenster.abweichung2.setValue(5);

		fenster.generieren.doClick();
		while (fenster.worker.isAlive()) {
		}
		assertEquals(2, gui.anzahlWahlen());

		gui.tabs.setSelectedIndex(gui.anzahlWahlen() - 1);
		int absoluteSitzzahl = gui.aktiveWahl().getPartei("FDP")
				.getOberverteilungSitzzahl();
		assertTrue(35 <= absoluteSitzzahl && absoluteSitzzahl <= 45);

	}

	/**
	 * Testet die Generierung mit Einschränkung der Gesamtsitzzahl und
	 * überprüft, ob Teile des {@link GeneriereWahldatenFenster}s korrekt
	 * deaktiviert werden.
	 */
	@Test
	public void testeGenerierungMitGesamtsitzzahlEinschraenkung() {
		fenster.box1.setSelected(false);
		fenster.box2.setSelected(false);
		fenster.box1.setSelected(true);

		assertFalse(fenster.parteiAuswahl.isEnabled());
		assertFalse(fenster.sitzzahlArt.isEnabled());
		assertFalse(fenster.sitzzahlAbs.isEnabled());
		assertFalse(fenster.sitzzahlRel.isEnabled());
		assertFalse(fenster.abweichung2.isEnabled());
		assertTrue(fenster.sitzzahlGesamt.isEnabled());
		assertTrue(fenster.abweichung1.isEnabled());

		fenster.abweichung1.setValue(10);
		fenster.sitzzahlGesamt.setValue(640);

		fenster.generieren.doClick();
		while (fenster.worker.isAlive()) {
		}
		assertEquals(2, gui.anzahlWahlen());

		gui.tabs.setSelectedIndex(gui.anzahlWahlen() - 1);
		int sitzzahl = gui.aktiveWahl().getSitzzahl();
		assertTrue(630 <= sitzzahl && sitzzahl <= 650);

	}

	/**
	 * Testet die Generierung mit Einschränkung der absoluten Sitzzahl und
	 * überprüft, ob Teile des {@link GeneriereWahldatenFenster}s korrekt
	 * deaktiviert werden.
	 */
	@Test
	public void testeGenerierungMitGesamtsitzzahlEinschraenkungUndAbsoluterSitzzahlEinschraenkung() {
		fenster.box1.setSelected(false);
		fenster.box2.setSelected(false);
		fenster.box1.setSelected(true);
		fenster.box2.setSelected(true);

		assertTrue(fenster.parteiAuswahl.isEnabled());
		fenster.parteiAuswahl.setSelectedItem("FDP");
		assertTrue(fenster.sitzzahlArt.isEnabled());
		fenster.sitzzahlArt.setSelectedItem("absolute Sitzzahl");
		assertTrue(fenster.sitzzahlAbs.isEnabled());
		assertFalse(fenster.sitzzahlRel.isEnabled());
		assertTrue(fenster.abweichung2.isEnabled());
		assertTrue(fenster.sitzzahlGesamt.isEnabled());
		assertTrue(fenster.abweichung1.isEnabled());

		fenster.sitzzahlGesamt.setValue(650);
		fenster.abweichung1.setValue(10);
		fenster.sitzzahlAbs.setValue(40);
		fenster.abweichung2.setValue(10);

		fenster.generieren.doClick();
		while (fenster.worker.isAlive()) {
		}
		assertEquals(2, gui.anzahlWahlen());

		gui.tabs.setSelectedIndex(gui.anzahlWahlen() - 1);
		int gesamtsitzzahl = gui.aktiveWahl().getSitzzahl();
		assertTrue(640 <= gesamtsitzzahl && gesamtsitzzahl <= 660);

		int absoluteSitzzahl = gui.aktiveWahl().getPartei("FDP")
				.getOberverteilungSitzzahl();
		assertTrue(30 <= absoluteSitzzahl && absoluteSitzzahl <= 50);

	}

	/**
	 * Testet die Generierung mit Einschränkung der relativen Sitzzahl und
	 * überprüft, ob Teile des {@link GeneriereWahldatenFenster}s korrekt
	 * deaktiviert werden.
	 */
	@Test
	public void testeGenerierungMitRelativerSitzzahlEinschraenkung() {
		fenster.box1.setSelected(false);
		fenster.box2.setSelected(true);

		assertTrue(fenster.parteiAuswahl.isEnabled());
		fenster.parteiAuswahl.setSelectedItem("FDP");
		assertTrue(fenster.sitzzahlArt.isEnabled());
		fenster.sitzzahlArt.setSelectedItem("relative Sitzzahl");
		assertFalse(fenster.sitzzahlAbs.isEnabled());
		assertTrue(fenster.sitzzahlRel.isEnabled());
		assertTrue(fenster.abweichung2.isEnabled());
		assertFalse(fenster.sitzzahlGesamt.isEnabled());
		assertFalse(fenster.abweichung1.isEnabled());

		fenster.sitzzahlRel.setValue(10);
		fenster.abweichung2.setValue(5);

		fenster.generieren.doClick();
		while (fenster.worker.isAlive()) {
		}
		assertEquals(2, gui.anzahlWahlen());

		gui.tabs.setSelectedIndex(gui.anzahlWahlen() - 1);
		int absoluteSitzzahl = gui.aktiveWahl().getPartei("FDP")
				.getOberverteilungSitzzahl();
		double relativeSitzzahl = ((double) absoluteSitzzahl)
				/ ((double) gui.aktiveWahl().getSitzzahl());
		assertTrue(0.05 <= relativeSitzzahl && relativeSitzzahl <= 0.15);

	}

	@Test
	public void testeGenerierungOhneEinschraenkung() {
		fenster.box1.setSelected(false);
		fenster.box2.setSelected(false);

		fenster.generieren.doClick();
		assertEquals(1, gui.anzahlWahlen());
	}
}

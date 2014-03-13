/**
 * 
 */
package edu.kit.iti.formal.mandatsverteilung.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

/**
 * @author Peter Steinmetz
 * 
 */
public class EinWahlDeutschlandAnsichtTest {

	/**
	 * Die zu testende {@link EinWahlDeutschlandAnsicht}
	 */
	private EinWahlDeutschlandAnsicht ansicht;
	/**
	 * Die zugehörige {@link Bundestagswahl}
	 */
	private Bundestagswahl wahl;

	/**
	 * initialisiert die {@link EinWahlDeutschlandAnsicht} mit der standartmäßig
	 * importierten {@link Bundestagswahl}
	 */
	@Before
	public void setUp() {
		GUI gui = new GUI();
		gui.neueWahl.doClick();
		wahl = gui.aktiveWahl();
		ansicht = new EinWahlDeutschlandAnsicht(wahl, false);
		gui.dispose();
	}

	/**
	 * testet dia Anzahl der Tabs und prüft, ob diese nicht null sind.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testeTabs() {
		ansicht.zeigeWahlkreiskarteButton.doClick();
		int parteiAnzahl = wahl.eingezogeneParteien().size();
		int i;
		for (i = 0; i < parteiAnzahl + 1; i++) {
			ansicht.tabs.setSelectedIndex(i);
			assertNotNull(ansicht.tabs.getSelectedComponent());
		}
		ansicht.tabs.setSelectedIndex(0);
		assertEquals(parteiAnzahl + 1, ansicht.tabs.getComponentCount());
		ansicht.tabs.getComponentAt(i); // hier wird die Exception erwartet
	}
}

/**
 * 
 */
package edu.kit.iti.formal.mandatsverteilung.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.awt.Color;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests für das {@link EinstellungsFenster}
 * 
 * @author Peter Steinmetz
 * 
 */
public class EinstellungsFensterTest {

	/**
	 * Das zu testende {@link EinstellungsFenster}
	 */
	private EinstellungsFenster fenster;
	/**
	 * Die zugehörige {@link GUI}-Klasse
	 */
	private GUI gui;

	@Before
	public void setUp() {
		gui = new GUI();
		gui.neueWahl.doClick();
		fenster = new EinstellungsFenster(gui, false);
	}

	@After
	public void tearDown() {
		fenster.setVisible(false);
		fenster.dispose();

		gui.setVisible(false);
		gui.dispose();
	}

	/**
	 * Testet, ob Auswahl und Einstellungen korrekt übernommen und bei erneutem
	 * Öffnen eines {@link BerechnungsOptionenFester}s angezeigt werden.
	 */
	@Test
	public void testeAuswahlundEinstellungen() {
		assertNotNull(fenster.auswahl);
		int zufall = (int) Math.random() * fenster.auswahl.length;
		fenster.auswahl[zufall].farbeAendern.doClick();
		Color farbe = fenster.auswahl[zufall].farbeAendern.getBackground();
		fenster.uebernehmen.doClick();
		fenster = new EinstellungsFenster(gui, false);
		assertEquals(farbe,
				fenster.auswahl[zufall].farbeAendern.getBackground());
	}

	/**
	 * Testet, ob die Wahl neu berechnet wird, obwohl keine Veränderungen
	 * vorgenommen wurden
	 */
	@Test
	public void testeNeuberechnugOhneVeraenderung() {
		fenster.uebernehmen.doClick();
		assertFalse(fenster.isVisible());
	}
}

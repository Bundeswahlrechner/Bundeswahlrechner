/**
 * 
 */
package edu.kit.iti.formal.mandatsverteilung.gui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Peter Steinmetz
 * 
 */
public class MenuleisteTest {

	/**
	 * Die zugehörige {@link GUI}-Klasse
	 */
	private GUI gui;

	/**
	 * Die zu testende {@link Menuleiste}
	 */
	private Menuleiste menu;

	@Before
	public void setUp() {
		gui = new GUI();
		menu = new Menuleiste(gui, false);
		gui.setMenu(menu);
	}

	@After
	public void tearDown() {
		gui.dispose();
	}

	/**
	 * Testet, ob bei verschiedenen Anzahlen an angezeigten Wahlen die Buttons
	 * in der {@link Menuleiste} korrekt deaktiviert und aktiviert werden. Zudem
	 * wird getestet, ob die verschiedenen Fenster tatsächlich geöffnet werden.
	 */
	@Test
	public void testeMenuKomponenten() {
		assertTrue(menu.isEnabled());
		assertFalse(menu.berechnungsOptionen.isEnabled());
		assertFalse(menu.einstellungen.isEnabled());
		assertFalse(menu.exportieren.isEnabled());
		assertFalse(menu.generieren.isEnabled());
		assertFalse(menu.modifikation.isEnabled());
		assertFalse(menu.wahlenVergleichen.isEnabled());
		assertFalse(menu.zwischenergebnis.isEnabled());
		assertTrue(menu.importieren.isEnabled());

		assertNull(menu.berechnungsOptionenFenster);
		assertNull(menu.einstellungsFenster);
		assertNull(menu.exportFenster);
		assertNull(menu.generiereWahldatenFenster);
		assertNull(menu.modifikationsAnsicht);
		assertNull(menu.importFenster);

		gui.neueWahl.doClick();
		assertTrue(menu.berechnungsOptionen.isEnabled());
		assertTrue(menu.einstellungen.isEnabled());
		assertTrue(menu.exportieren.isEnabled());
		assertTrue(menu.generieren.isEnabled());
		assertTrue(menu.modifikation.isEnabled());
		assertTrue(menu.zwischenergebnis.isEnabled());
		assertFalse(menu.wahlenVergleichen.isEnabled());

		gui.neueWahl.doClick();
		assertTrue(menu.wahlenVergleichen.isEnabled());
		menu.wahlenVergleichen.doClick();
		menu.berechnungsOptionen.doClick();
		assertNotNull(menu.berechnungsOptionenFenster);
		menu.berechnungsOptionenFenster.neuBerechnenButton.doClick();
		menu.einstellungen.doClick();
		assertNotNull(menu.einstellungsFenster);
		menu.einstellungsFenster.uebernehmen.doClick();
		menu.exportieren.doClick();
		assertNotNull(menu.exportFenster);
		menu.exportFenster.exportieren.doClick();
		menu.generieren.doClick();
		assertNotNull(menu.generiereWahldatenFenster);
		menu.generiereWahldatenFenster.dispose();
		menu.modifikation.doClick();
		assertNotNull(menu.modifikationsAnsicht);
		menu.modifikationsAnsicht.neuBerechnen.doClick();
		menu.wahlenVergleichen.doClick();
		menu.importieren.doClick();
		assertNotNull(menu.importFenster);
		menu.importFenster.importieren.doClick();
		menu.zwischenergebnis.doClick();
		assertNotNull(menu.zwischenergebnisAnsicht);
		menu.zwischenergebnisAnsicht.b.doClick();

		gui.wahlKopieren.doClick();
		menu.wahlenVergleichen.doClick();
		gui.wahlEntfernen.doClick();
		gui.wahlEntfernen.doClick();

		assertTrue(menu.berechnungsOptionen.isEnabled());
		assertTrue(menu.einstellungen.isEnabled());
		assertTrue(menu.exportieren.isEnabled());
		assertTrue(menu.generieren.isEnabled());
		assertTrue(menu.modifikation.isEnabled());
		assertFalse(menu.wahlenVergleichen.isEnabled());
		menu.wahlenVergleichen.doClick();

		gui.wahlEntfernen.doClick();
		assertFalse(menu.berechnungsOptionen.isEnabled());
		assertFalse(menu.einstellungen.isEnabled());
		assertFalse(menu.exportieren.isEnabled());
		assertFalse(menu.generieren.isEnabled());
		assertFalse(menu.modifikation.isEnabled());
		assertFalse(menu.wahlenVergleichen.isEnabled());
	}
}

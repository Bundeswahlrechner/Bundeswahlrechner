/**
 * 
 */
package edu.kit.iti.formal.mandatsverteilung.gui;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests für das {@link BerechnungsOptionenFenster}
 * 
 * @author Peter Steinmetz
 * 
 */
public class BerechnungsOptionenFensterTest {

	/**
	 * Das zu testende {@link BerechnungsOptionenFenster}
	 */
	private BerechnungsOptionenFenster fenster;
	/**
	 * Die zugehörige {@link GUI}-Klasse
	 */
	private GUI gui;

	@Before
	public void setUp() {
		gui = new GUI();
		gui.neueWahl.doClick();
		fenster = new BerechnungsOptionenFenster(gui, false);
	}

	@After
	public void tearDown() {
		fenster.dispose();
		gui.dispose();
	}

	/**
	 * Testet, ob Auswahl und Einstellungen korrekt übernommen und bei erneutem
	 * Öffnen eines {@link BerechnungsOptionenFester}s angezeigt werden
	 */
	@Test
	public void testeAuswahlUndEinstellungen() {

		for (int i = 0; i < 5; i++) {
			fenster.fabrikAuswahl.setSelectedItem("Benutzerdefiniert");
			fenster.zuteilungsverfahrenAuswahl.setSelectedIndex(i);
			if ("Hare-Niemeyer".equals(fenster.zuteilungsverfahrenAuswahl
					.getSelectedItem())) {
				fenster.prozenthuerdeAuswahl.setValue(0.04);
				;
			}
			fenster.neuBerechnenButton.doClick();
			fenster = new BerechnungsOptionenFenster(gui, false);
			assertEquals(gui.aktiveWahl().getBerechnungsoptionen()
					.getZuteilungsverfahren(),
					fenster.zuteilungsverfahrenAuswahl.getSelectedItem());
		}
		fenster.zuteilungsverfahrenAuswahl
				.setSelectedItem("Sainte-Lague Iterativ");
		for (int j = 0; j < fenster.wahlverfahrenAuswahl.getComponentCount(); j++) {
			fenster.wahlverfahrenAuswahl.setSelectedIndex(j);
			fenster.neuBerechnenButton.doClick();
			fenster = new BerechnungsOptionenFenster(gui, false);
			assertEquals(j, fenster.wahlverfahrenAuswahl.getSelectedIndex());
		}

		fenster.grundmandateAuswahl.setValue(10);
		fenster.neuBerechnenButton.doClick();
		fenster = new BerechnungsOptionenFenster(gui, false);
		assertEquals(10, fenster.grundmandateAuswahl.getValue());

		fenster.prozenthuerdeAuswahl.setValue(0.06);
		fenster.neuBerechnenButton.doClick();
		fenster = new BerechnungsOptionenFenster(gui, false);
		assertEquals(0.06, fenster.prozenthuerdeAuswahl.getValue());

	}

	/**
	 * Prüft, ob die initialen Werte mit den Standartwerten des
	 * Bundeswahlgesetzes von 2013 übereinstimmen.
	 */
	@Test
	public void testeStandardWerteFuerBundeswahlgesetz2013() {
		fenster.fabrikAuswahl.setSelectedItem("Bundeswahlgesetz 2013");
		fenster.neuBerechnenButton.doClick();
		fenster = new BerechnungsOptionenFenster(gui, false);

		assertEquals(3, fenster.grundmandateAuswahl.getValue());
		assertEquals(0.05, fenster.prozenthuerdeAuswahl.getValue());
		assertEquals("Bundeswahlgesetz 2013",
				fenster.wahlverfahrenAuswahl.getSelectedItem());
	}

	@Test
	public void testeStandardWerteFuerBundeswahlgesetzOhneAusgleichsmandate() {
		fenster.fabrikAuswahl
				.setSelectedItem("Bundeswahlgesetz ohne Ausgleichsmandate");
		fenster.neuBerechnenButton.doClick();
		fenster = new BerechnungsOptionenFenster(gui, false);

		assertEquals(3, fenster.grundmandateAuswahl.getValue());
		assertEquals(0.05, fenster.prozenthuerdeAuswahl.getValue());
		assertEquals("Bundeswahlgesetz ohne Ausgleichsmandate",
				fenster.fabrikAuswahl.getSelectedItem());
	}
}

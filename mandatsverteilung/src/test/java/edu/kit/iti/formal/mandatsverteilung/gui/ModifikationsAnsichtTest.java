/**
 * 
 */
package edu.kit.iti.formal.mandatsverteilung.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Wahlkreis;

/**
 * @author Peter Steinmetz
 * 
 */
public class ModifikationsAnsichtTest {

	/**
	 * Die zu testende {@link ModifikationsAnsicht}
	 */
	private ModifikationsAnsicht fenster;
	/**
	 * Die zugehörige {@link GUI}-Klasse
	 */
	private GUI gui;

	@Before
	public void setUp() {
		gui = new GUI();
		gui.neueWahl.doClick();
		fenster = new ModifikationsAnsicht(gui, false);
	}

	@After
	public void tearDown() {
		fenster.dispose();
		gui.dispose();
	}

	/**
	 * Testet für die Bundesländertabelle, ob Eingegebene Daten korrekt
	 * übernommen und bei erneutem
	 * Öffnen einer {@link ModifikationsAnsicht} angezeigt werden. Zudem wird
	 * übeprüft, ob unveränderliche Spalten tatsächlich nicht verändert werden
	 * können.
	 */
	@Test
	public void testeAuswahlundEinstellungenDerBlTabelle() {
		assertNotNull(fenster.blTabelle);
		int zufallsZeileBl = (int) Math.random()
				* fenster.blTabelle.getRowCount();
		assertFalse(fenster.blTabelle.isCellEditable(zufallsZeileBl, 0));

		int zufallsSpalteBl = 0;
		do {
			zufallsSpalteBl = (int) Math.random()
					* (fenster.blTabelle.getColumnCount() - 1) + 1;
		} while (!(fenster.blTabelle.isCellEditable(zufallsZeileBl,
				zufallsSpalteBl)));
		fenster.blTabelle.setValueAt("123", zufallsZeileBl, zufallsSpalteBl);

		fenster.neuBerechnen.doClick();
		fenster = new ModifikationsAnsicht(gui, false);
		assertEquals("123",
				fenster.blTabelle.getValueAt(zufallsZeileBl, zufallsSpalteBl));

	}

	/**
	 * Testet für die Wahlkreiserststimmentabelle, ob Eingegebene Daten korrekt
	 * übernommen und bei erneutem
	 * Öffnen einer {@link ModifikationsAnsicht} angezeigt werden. Zudem wird
	 * übeprüft, ob unveränderliche Spalten tatsächlich nicht verändert werden
	 * können.
	 */
	@Test
	public void testeAuswahlundEinstellungenDerWkErststimmenTabelle() {
		assertNotNull(fenster.wkErststimmenTabelle);

		// ermittle per Zufall einen Wahlkreis sowie einen
		// Spaltenindex für den die Tabelle modifizierbar ist
		int zeile = (int) Math.random() * gui.aktiveWahl().wahlkreisAnzahl();
		assertFalse(fenster.wkErststimmenTabelle.isCellEditable(zeile, 0));
		int spalte = 0;
		while (!(fenster.wkErststimmenTabelle.isCellEditable(zeile, spalte))) {
			spalte++;
		}
		fenster.wkErststimmenTabelle.setValueAt("123", zeile, spalte);

		fenster.neuBerechnen.doClick();
		fenster = new ModifikationsAnsicht(gui, false);
		assertEquals("123",
				fenster.wkErststimmenTabelle.getValueAt(zeile, spalte));

	}

	/**
	 * Testet für die Wahlkreiszweitstimmentabelle, ob Eingegebene Daten korrekt
	 * übernommen und bei erneutem
	 * Öffnen einer {@link ModifikationsAnsicht} angezeigt werden. Zudem wird
	 * übeprüft, ob unveränderliche Spalten tatsächlich nicht verändert werden
	 * können.
	 */
	@Test
	public void testeAuswahlundEinstellungenDerWkZweitstimmenTabelle() {
		assertNotNull(fenster.wkZweitstimmenTabelle);

		// ermittle per Zufall einen Wahlkreis sowie einen
		// Spaltenindex für den die Tabelle modifizierbar ist
		int zeile = (int) Math.random() * gui.aktiveWahl().wahlkreisAnzahl();
		assertFalse(fenster.wkZweitstimmenTabelle.isCellEditable(zeile, 0));
		int spalte = 0;
		while (!(fenster.wkZweitstimmenTabelle.isCellEditable(zeile, spalte))) {
			spalte++;
		}
		fenster.wkZweitstimmenTabelle.setValueAt("123456789", zeile, spalte);

		fenster.neuBerechnen.doClick();
		fenster = new ModifikationsAnsicht(gui, false);
		assertEquals("123456789",
				fenster.wkZweitstimmenTabelle.getValueAt(zeile, spalte));

	}

	/**
	 * Testet die Bundesländertabelle bei Eingabe von unerlaubten Werten
	 */
	@Test(expected = NumberFormatException.class)
	public void testeBlTabelleMitUnerlaubtenWerten() {
		int zufallsZeile = (int) Math.random()
				* gui.aktiveWahl().bundeslaenderAnzahl();
		int zufallsSpalte = 0;
		do {
			zufallsSpalte++;
		} while (!(fenster.blTabelle.getModel().isCellEditable(zufallsZeile,
				zufallsSpalte)));
		fenster.blTabelle.setValueAt("ABC", zufallsZeile, zufallsSpalte);
	}

	/**
	 * Testet, ob die Wahl neu berechnet wird, obwohl keine Veränderungen
	 * vorgenommen wurden
	 */
	@Test
	public void testeNeuberechnugOhneVeraenderung() {
		Bundestagswahl wahl = gui.aktiveWahl();
		fenster.neuBerechnen.doClick();
		assertFalse(fenster.isVisible());
		assertEquals(wahl, gui.aktiveWahl());
	}

	/**
	 * Testet, ob bei Verändrung der Zweitstimmen auf Bundesländerebene auch die
	 * Zweitstimmen in den zugehörigen Wahlkreisen geändert werden und
	 * umgekehrt.
	 */
	@Test
	public void testeTabellenUpdate() {

		// ermittle per Zufall ein Bundesland und einen Wahlkreis sowie
		// Spaltenindizes für die die Tabellen modifizierbar sind
		int blIndex = (int) Math.random()
				* gui.aktiveWahl().bundeslaenderAnzahl();
		Bundesland bl = gui.aktiveWahl().getBundeslandName(
				(String) fenster.blTabelle.getValueAt(blIndex, 0));
		Wahlkreis wk = bl.wahlkreisIterator().next();
		int wkIndex = 0;
		while (!wk.getName().equals(
				fenster.wkZweitstimmenTabelle.getValueAt(wkIndex, 0))) {
			wkIndex++;
		}
		int spalte = 0;
		while (!(fenster.wkZweitstimmenTabelle.isCellEditable(wkIndex, spalte))) {
			spalte++;
		}

		int wkWert = Integer.parseInt((String) fenster.wkZweitstimmenTabelle
				.getValueAt(wkIndex, spalte));
		int blWert = Integer.parseInt((String) fenster.blTabelle.getValueAt(
				blIndex, spalte - 1));

		fenster.blTabelle.setValueAt(
				String.valueOf(blWert + bl.anzahlWahlkreise()), blIndex,
				spalte - 1);

		assertEquals(wkWert + 1,
				Integer.parseInt((String) fenster.wkZweitstimmenTabelle
						.getValueAt(wkIndex, spalte)));

		fenster.blTabelle.setValueAt(
				String.valueOf(blWert - bl.anzahlWahlkreise()), blIndex,
				spalte - 1);

		assertEquals(wkWert - 1,
				Integer.parseInt((String) fenster.wkZweitstimmenTabelle
						.getValueAt(wkIndex, spalte)));

		fenster.blTabelle.setValueAt(String.valueOf(blWert), blIndex,
				spalte - 1);

		assertEquals(wkWert,
				Integer.parseInt((String) fenster.wkZweitstimmenTabelle
						.getValueAt(wkIndex, spalte)));

		fenster.wkZweitstimmenTabelle.setValueAt(String.valueOf(wkWert + 1),
				wkIndex, spalte);

		assertEquals(blWert + 1, Integer.parseInt((String) fenster.blTabelle
				.getValueAt(blIndex, spalte - 1)));

	}

	/**
	 * Testet die Wahlkreiserststimmentabelle bei Eingabe von unerlaubten Werten
	 */
	@Test(expected = NumberFormatException.class)
	public void testeWkErstsitmmenTabelleMitUnerlaubtenWerten() {

		// ermittle per Zufall einen Wahlkreis sowie einen
		// Spaltenindex für den die Tabelle modifizierbar ist
		int zeile = (int) Math.random() * gui.aktiveWahl().wahlkreisAnzahl();
		int spalte = 0;
		while (!(fenster.wkErststimmenTabelle.isCellEditable(zeile, spalte))) {
			spalte++;
		}

		// gib einen "unerlaubten" Wert ein
		fenster.wkErststimmenTabelle.setValueAt("ABC", zeile, spalte);
	}

	/**
	 * Testet die Wahlkreiszwitstimmentabelle bei Eingabe von unerlaubten Werten
	 */
	@Test(expected = NumberFormatException.class)
	public void testeWkZweitstimmenTabelleMitUnerlaubtenWerten() {

		// ermittle per Zufall einen Wahlkreis sowie einen
		// Spaltenindex für den die Tabelle modifizierbar ist
		int zeile = (int) Math.random() * gui.aktiveWahl().wahlkreisAnzahl();
		int spalte = 0;
		while (!(fenster.wkZweitstimmenTabelle.isCellEditable(zeile, spalte))) {
			spalte++;
		}

		// gib einen "unerlaubten" Wert ein
		fenster.wkZweitstimmenTabelle.setValueAt("ABC", zeile, spalte);
	}

}

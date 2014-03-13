/**
 * 
 */
package edu.kit.iti.formal.mandatsverteilung.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Peter Steinmetz
 * 
 */
public class ImportFensterTest {

	/**
	 * Datei mit Wahlkreisen und Stimmen für Standardimport der Bundestagswahl
	 * 2013
	 */
	private final File datei1 = new File("./src/main/resources/kerg.csv");
	/**
	 * Datei mit Kandidaten für Standardimport der Bundestagswahl 2013
	 */
	private final File datei2 = new File(
			"./src/main/resources/Tab23_Wahlbewerber_a.csv");

	/**
	 * Datei mit Bundesländern für Standardimport der Bundestagswahl 2013
	 */
	private final File datei3 = new File("./src/main/resources/bundesL.csv");

	/**
	 * Das zu testende {@link ImportFenster}
	 */
	private ImportFenster fenster;

	/**
	 * Die zugehörige {@link GUI}-Klasse
	 */
	private GUI gui;

	@Before
	public void setUp() {
		gui = new GUI();
		fenster = new ImportFenster(gui, false);
	}

	@After
	public void tearDown() {
		fenster.fc.setSelectedFile(null);
		fenster.dispose();
		gui.dispose();
	}

	/**
	 * Testet mit den CSV-Dateien der Bundestagswahl 2013, ob diese tatsächlich
	 * importiert werden.
	 */
	@Test
	public void testeCSVImport() {
		assertNull(gui.aktiveWahl());

		fenster.dateiFormat.setSelectedItem("CSV");
		fenster.fc.setSelectedFile(datei1);
		fenster.suche1.doClick();
		fenster.fc.approveSelection();
		fenster.fc.setSelectedFile(datei2);
		fenster.suche2.doClick();
		fenster.fc.approveSelection();
		fenster.fc.setSelectedFile(datei3);
		fenster.suche3.doClick();
		fenster.fc.approveSelection();
		fenster.importieren.doClick();

		assertNotNull(gui.aktiveWahl());
	}

	@Test
	public void testeCSVMitErweitertenLandeslistenImport() {
		assertNull(gui.aktiveWahl());

		fenster.dateiFormat.setSelectedItem("CSV mit erweiterten Landeslisten");
		fenster.fc.setSelectedFile(datei1);
		fenster.suche1.doClick();
		fenster.fc.approveSelection();
		fenster.fc.setSelectedFile(datei2);
		fenster.suche2.doClick();
		fenster.fc.approveSelection();
		fenster.fc.setSelectedFile(datei3);
		fenster.suche3.doClick();
		fenster.fc.approveSelection();
		fenster.importieren.doClick();

		assertNotNull(gui.aktiveWahl());
	}

	@Test
	public void testeImportInvaliderBundeslaenderdatei() {

		fenster.fc.setSelectedFile(datei1);
		fenster.suche1.doClick();
		fenster.fc.approveSelection();
		fenster.fc.setSelectedFile(datei2);
		fenster.suche2.doClick();
		fenster.fc.approveSelection();
		fenster.fc.setSelectedFile(datei2);
		fenster.suche3.doClick();
		fenster.fc.approveSelection();
		fenster.importieren.doClick();

		// stellt sicher, dass keine Wahl importiert wurde
		assertNull(gui.aktiveWahl());
		assertEquals(0, gui.anzahlWahlen());
	}

	@Test
	public void testeImportInvaliderKandidatendatei() {

		fenster.fc.setSelectedFile(datei1);
		fenster.suche1.doClick();
		fenster.fc.approveSelection();
		fenster.fc.setSelectedFile(datei1);
		fenster.suche2.doClick();
		fenster.fc.approveSelection();
		fenster.fc.setSelectedFile(datei3);
		fenster.suche3.doClick();
		fenster.fc.approveSelection();
		fenster.importieren.doClick();

		// stellt sicher, dass keine Wahl importiert wurde
		assertNull(gui.aktiveWahl());
		assertEquals(0, gui.anzahlWahlen());
	}

	@Test
	public void testeImportInvaliderWahlkreisUndStimmendatei() {

		fenster.fc.setSelectedFile(datei2);
		fenster.suche1.doClick();
		fenster.fc.approveSelection();
		fenster.fc.setSelectedFile(datei2);
		fenster.suche2.doClick();
		fenster.fc.approveSelection();
		fenster.fc.setSelectedFile(datei3);
		fenster.suche3.doClick();
		fenster.fc.approveSelection();
		fenster.importieren.doClick();

		// stellt sicher, dass keine Wahl importiert wurde
		assertNull(gui.aktiveWahl());
		assertEquals(0, gui.anzahlWahlen());
	}

	@Test
	public void testeImportNachAbbruchDerDateiAuswahl() {

		fenster.fc.setSelectedFile(datei1);
		fenster.suche1.doClick();
		fenster.fc.approveSelection();
		fenster.fc.setSelectedFile(datei2);
		fenster.suche2.doClick();
		fenster.fc.approveSelection();
		fenster.fc.setSelectedFile(datei3);
		fenster.suche3.doClick();
		fenster.fc.approveSelection();

		fenster.suche1.doClick();
		fenster.fc.cancelSelection();
		fenster.suche2.doClick();
		fenster.fc.cancelSelection();
		fenster.suche3.doClick();
		fenster.fc.cancelSelection();

		fenster.importieren.doClick();
		assertNotNull(gui.aktiveWahl());
		assertEquals(1, gui.anzahlWahlen());
	}

	@Test
	public void testeImportOhneBundeslaenderdatei() {

		fenster.fc.setSelectedFile(datei1);
		fenster.suche1.doClick();
		fenster.fc.approveSelection();
		fenster.fc.setSelectedFile(datei2);
		fenster.suche2.doClick();
		fenster.fc.approveSelection();
		fenster.importieren.doClick();

		// stellt sicher, dass keine Wahl importiert wurde
		assertNull(gui.aktiveWahl());
		assertEquals(0, gui.anzahlWahlen());
	}

	@Test
	public void testeImportOhneKandidatendatei() {

		fenster.fc.setSelectedFile(datei1);
		fenster.suche1.doClick();
		fenster.fc.approveSelection();
		fenster.fc.setSelectedFile(datei3);
		fenster.suche3.doClick();
		fenster.fc.approveSelection();
		fenster.importieren.doClick();

		// stellt sicher, dass keine Wahl importiert wurde
		assertNull(gui.aktiveWahl());
		assertEquals(0, gui.anzahlWahlen());
	}

	/**
	 * Stellt sicher, dass eine Datei, die bereits ausgewählt wurde bei erneuter
	 * Dateiauswahl, die abgebrochen wird, nicht auf null gesetzt wird.
	 */
	@Test
	public void testeImportOhneWahlkreisUndStimmendatei() {

		fenster.fc.setSelectedFile(datei2);
		fenster.suche2.doClick();
		fenster.fc.approveSelection();
		fenster.fc.setSelectedFile(datei3);
		fenster.suche3.doClick();
		fenster.fc.approveSelection();
		fenster.importieren.doClick();

		// stellt sicher, dass keine Wahl importiert wurde
		assertNull(gui.aktiveWahl());
		assertEquals(0, gui.anzahlWahlen());
	}

	/**
	 * Stellt sicher, dass keine Exception auftritt, wenn keine Dateien
	 * ausgewählt wurden
	 */
	@Test
	public void testeNullImport() {

		fenster.importieren.doClick();

		assertNull(gui.aktiveWahl());
		assertEquals(0, gui.anzahlWahlen());
	}

}

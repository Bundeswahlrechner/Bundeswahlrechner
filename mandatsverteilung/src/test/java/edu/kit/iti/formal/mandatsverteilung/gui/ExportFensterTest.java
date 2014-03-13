/**
 * 
 */
package edu.kit.iti.formal.mandatsverteilung.gui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.swing.JFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests für das {@link ExportFenster}
 * 
 * @author Peter Steinmetz
 * 
 */
public class ExportFensterTest {

	/**
	 * Das zu testende {@link ExportFenster}
	 */
	private ExportFenster fenster;
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	/**
	 * Die zugehörige {@link GUI}-Klasse
	 */
	private GUI gui;

	@Before
	public void setUp() {
		gui = new GUI();
		gui.neueWahl.doClick();
		gui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		fenster = new ExportFenster(gui, false);
	}

	@After
	public void tearDown() {
		fenster.setVisible(false);
		fenster.dispose();

		gui.setVisible(false);
		gui.dispose();

	}

	@Test
	public void testeCSVExport() {
		fenster.fc.setSelectedFile(folder.getRoot().getAbsoluteFile());
		fenster.suchen1.doClick();
		fenster.fc.approveSelection();
		fenster.exportieren.doClick();
		assertFalse(fenster.isVisible());
		assertTrue("Die Ergebnisdatei wurde nicht exportiert.", (new File(
				folder.getRoot() + File.separator + "Ergebnis.csv")).exists());
		assertTrue("Die Stimmendatei wurde nicht exportiert.",
				(new File(folder.getRoot() + File.separator + "Stimmen.csv"))
						.exists());
		assertTrue("Die Kandidatendatei wurde nicht exportiert.", (new File(
				folder.getRoot() + File.separator + "Kandidaten.csv")).exists());
		assertTrue("Die Bundesländerdatei wurde nicht exportiert.",
				(new File(folder.getRoot() + File.separator
						+ "Bundeslaender.csv")).exists());
	}

	@Test
	public void testeExportMitLeeremPfad() {
		fenster.exportieren.doClick();
		assertFalse(fenster.isVisible());
		assertFalse("Die Ergebnisdatei wurde exportiert.",
				(new File(folder.getRoot() + File.separator + "Ergebnis.csv"))
						.exists());
		assertFalse("Die Stimmendatei wurde exportiert.",
				(new File(folder.getRoot() + File.separator + "Stimmen.csv"))
						.exists());
		assertFalse(
				"Die Kandidatendatei wurde exportiert.",
				(new File(folder.getRoot() + File.separator + "Kandidaten.csv"))
						.exists());
		assertFalse("Die Bundesländerdatei wurde exportiert.",
				(new File(folder.getRoot() + File.separator
						+ "Bundeslaender.csv")).exists());
	}

	@Test
	public void testeGrafikExport() {
		fenster.exportAuswahl.setSelectedIndex(1);
		fenster.fc.setSelectedFile(folder.getRoot().getAbsoluteFile());
		fenster.suchen1.doClick();
		fenster.fc.approveSelection();
		fenster.exportieren.doClick();
		assertFalse(fenster.isVisible());
		assertTrue("Die Bundesländergrafik wurde nicht exportiert.",
				(new File(folder.getRoot() + File.separator
						+ "BundeslaenderGrafik.png")).exists());
		assertTrue("Die Bundesländergrafik wurde nicht exportiert.",
				(new File(folder.getRoot() + File.separator
						+ "BundestagsGrafik.png")).exists());
	}
}

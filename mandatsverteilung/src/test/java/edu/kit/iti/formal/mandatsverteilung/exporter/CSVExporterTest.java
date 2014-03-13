package edu.kit.iti.formal.mandatsverteilung.exporter;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.importer.TestDatenBereitsteller;

public class CSVExporterTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	/**
	 * Importiert die Bundeswahlleiterdaten und exportiert diese anschließend.
	 * Es wird überprüft, ob tatsächlich Datein erzeugt wurden.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testBundeswahlleiterDatenExport() throws IOException {
		CSVExporter exporter = new CSVExporter(
				TestDatenBereitsteller.getStandardBTW());
		System.out.println(folder.getRoot().getAbsolutePath());
		exporter.exportiereCSVDatei(folder.getRoot().getAbsolutePath());
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

	/**
	 * Prüfe, ob exportiere Dateien reimportiert werden können.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testBundeswahlleiterDatenReimport() throws IOException {
		CSVExporter exporter = new CSVExporter(
				TestDatenBereitsteller.getStandardBTW());
		exporter.exportiereCSVDatei(folder.getRoot().getAbsolutePath());
		folder.getRoot().getAbsolutePath();
		InputStream kerg = new FileInputStream(folder.getRoot()
				+ File.separator + "Stimmen.csv");
		InputStream kerg2 = new FileInputStream(folder.getRoot()
				+ File.separator + "Stimmen.csv");
		InputStream bundeslaender = new FileInputStream(folder.getRoot()
				+ File.separator + "Bundeslaender.csv");
		InputStream bewerber = new FileInputStream(folder.getRoot()
				+ File.separator + "Kandidaten.csv");
		@SuppressWarnings("unused")
		Bundestagswahl bt = TestDatenBereitsteller.importiereTestdaten(kerg,
				kerg2, bundeslaender, bewerber);
		// Test war erfolgreich, wenn der Import ohne Exception abläuft.
		assertTrue(true);
	}

	@Test
	public void testGrafikExport() throws IOException {
		Bundestagswahl btw = TestDatenBereitsteller.getStandardBTW();
		CSVExporter exporter = new CSVExporter(btw);
		JFreeChart chart = ChartFactory.createBarChart3D("Bundestagswahl",
				"Partei", "Bundesland", null, PlotOrientation.VERTICAL, false,
				false, false);
		exporter.exportierePNG(chart,
				new FileOutputStream(folder.getRoot().getAbsolutePath()
						+ File.separator + "BundeslaenderGrafik.png"), 500, 500);
		exporter.exportierePNG(chart, new FileOutputStream(folder.getRoot()
				.getAbsolutePath() + File.separator + "BundestagsGrafik.png"),
				500, 500);
		assertTrue("Die Bundesländergrafik wurde nicht exportiert.",
				(new File(folder.getRoot() + File.separator
						+ "BundeslaenderGrafik.png")).exists());
		assertTrue("Die Bundesländergrafik wurde nicht exportiert.",
				(new File(folder.getRoot() + File.separator
						+ "BundestagsGrafik.png")).exists());
	}
}

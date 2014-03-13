package edu.kit.iti.formal.mandatsverteilung.exporter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.jfree.chart.JFreeChart;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

public class CSVExporter {

	/**
	 * zu exportierende Bundestagswahl.
	 */
	private final Bundestagswahl btw;

	/**
	 * Zum exportieren der Stimmen, Bundesländer und Kandidatendaten, die auch
	 * wieder eingelesen werden können.
	 */
	private CSVEingabeDatenExporter datenExporter;

	/**
	 * Zum Exportieren der Ergebnissdaten.
	 */
	private CSVMandateExporter mandateExporter;

	public CSVExporter(Bundestagswahl btw) {
		this.btw = btw;
	}

	/**
	 * Exportiert alle CSV-Dateien an den angegebenen Pfad.
	 * 
	 * @param filename
	 *            Pfad an den Exportiert werden soll
	 */
	public void exportiereCSVDatei(String filename) {
		mandateExporter = new CSVMandateExporter(btw);
		datenExporter = new CSVEingabeDatenExporter(btw);
		if (filename != null) {
			mandateExporter.exportiereMandate(filename + File.separator
					+ "Ergebnis.csv");
			datenExporter.exportiereStimmen(filename + File.separator
					+ "Stimmen.csv");
			datenExporter.exportiereKandidaten(filename + File.separator
					+ "Kandidaten.csv");
			datenExporter.exportiereBundeslaender(filename + File.separator
					+ "Bundeslaender.csv");
		}
	}

	/**
	 * Exportiert Grafiken.
	 * 
	 * @param chart
	 *            Grafik die exportiert werden soll
	 * @param OutputStream
	 *            Datei die geschrieben werden soll
	 * @param width
	 *            breite der exportierten Grafik
	 * @param height
	 *            Höhe der exportierten Grafik
	 */
	public void exportierePNG(JFreeChart chart, OutputStream out, int width,
			int height) {
		try {
			BufferedImage chartImage = chart.createBufferedImage(width, height,
					null);
			ImageIO.write(chartImage, "png", out);
		} catch (Exception e) {

		}
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

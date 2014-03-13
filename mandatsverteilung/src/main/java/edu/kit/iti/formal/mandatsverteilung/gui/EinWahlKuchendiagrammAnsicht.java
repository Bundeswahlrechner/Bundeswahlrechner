package edu.kit.iti.formal.mandatsverteilung.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.HorizontalAlignment;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;

/**
 * Klasse zur Darstellung von Wahlergebnissen in Kuchendiagrammformat.
 * 
 * @author Patrick Stöckle
 * @version 1.0
 */
public class EinWahlKuchendiagrammAnsicht extends EinWahlAnsicht {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Hilfsmethode.
	 * 
	 * @param c1
	 *            color 1.
	 * @param c2
	 *            color 2.
	 * 
	 * @return A radial gradient paint.
	 */
	private static RadialGradientPaint createGradientPaint(Color c1, Color c2) {
		Point2D center = new Point2D.Float(0, 0);
		float radius = 200;
		float[] dist = { 0.0f, 1.0f };
		return new RadialGradientPaint(center, radius, dist, new Color[] { c1,
				c2 });
	}

	/**
	 * Konstruktor.
	 * 
	 * @param wahl
	 *            Wahl
	 */
	public EinWahlKuchendiagrammAnsicht(Bundestagswahl wahl) {
		super(wahl);
		JTabbedPane tabs = new JTabbedPane();
		fuegeKuchenDiagrammHinzu(tabs, bundestagsDaten, bundestagsKopfZeile,
				"Bundestag", 1, 0);
		for (int i = 0; i < bundeslaenderDaten.length; i++) {
			fuegeKuchenDiagrammHinzu(tabs, bundeslaenderDaten,
					bundeslaenderKopfZeile, (String) bundeslaenderDaten[i][1],
					2, i);
		}
		setLayout(new BorderLayout());
		this.add(tabs, BorderLayout.CENTER);
		setVisible(true);
	}

	/**
	 * Erstellt einen Datensatz aus den Parametern.
	 * 
	 * @param daten
	 *            Daten
	 * @param kopfZeile
	 *            Kopfzeile
	 * @param start
	 *            Start der Daten
	 * @param nummer
	 *            Nummer des Bundeslandes
	 * @return {@link PieDataset}
	 */
	private PieDataset erstelleDatenset(Object[][] daten, Object[] kopfZeile,
			int start, int nummer) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (int j = 0; j < daten[nummer].length - start; j++) {
			int help = (Integer) daten[nummer][j + start];
			double d = help;
			if (d != 0) {
				dataset.setValue((String) kopfZeile[j + start], d);
			}
		}

		return dataset;
	}

	/**
	 * Erstellt ein Kuchendiagramm aus den Parametern.
	 * 
	 * @param dataset
	 *            Datenset
	 * @param titel
	 *            Titel
	 * @return {@link JFreeChart}
	 */
	private JFreeChart erstelleKuchenDiagramm(PieDataset dataset, String titel) {
		JFreeChart chart = ChartFactory.createPieChart3D(titel, dataset, true,
				true, false);

		TextTitle t = chart.getTitle();
		t.setHorizontalAlignment(HorizontalAlignment.CENTER);
		t.setFont(new Font("Arial", Font.CENTER_BASELINE, 26));

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setBackgroundPaint(null);
		plot.setInteriorGap(0.04);
		plot.setOutlineVisible(false);

		for (Partei partei : parteienListe) {
			if (partei.getFarbe() != null) {
				plot.setSectionPaint(
						partei.getName(),
						createGradientPaint(partei.getFarbe(),
								partei.getFarbe()));
			}
		}

		plot.setBaseSectionOutlinePaint(Color.GRAY);
		plot.setLabelFont(new Font("Arial", Font.BOLD, 15));
		plot.setLabelLinkPaint(Color.black);
		plot.setLabelPaint(Color.black);
		plot.setLabelBackgroundPaint(null);

		return chart;

	}

	/**
	 * Private Methode, die dem übergebenen JTabbedPane ein Kuchendiagramm mit
	 * den Parametern hinzufügt.
	 * 
	 * @param tabs
	 *            JTabbedPane
	 * @param daten
	 *            Daten
	 * @param kopfZeile
	 *            Beschriftung
	 * @param titel
	 *            Titel
	 * @param start
	 *            Start der Daten
	 * @param nummer
	 *            Nummer des Bundeslandes
	 */
	private void fuegeKuchenDiagrammHinzu(JTabbedPane tabs, Object[][] daten,
			Object[] kopfZeile, String titel, int start, int nummer) {
		PieDataset dataset = erstelleDatenset(daten, kopfZeile, start, nummer);
		JFreeChart chart = erstelleKuchenDiagramm(dataset, titel);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setFillZoomRectangle(true);
		chartPanel.setMouseWheelEnabled(true);
		chartPanel.setPreferredSize(new Dimension(500, 270));
		tabs.addTab(titel, new JScrollPane(chartPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));

	}

}

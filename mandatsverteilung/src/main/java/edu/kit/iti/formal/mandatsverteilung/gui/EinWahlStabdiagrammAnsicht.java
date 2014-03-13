package edu.kit.iti.formal.mandatsverteilung.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.HorizontalAlignment;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

/**
 * Klasse zur Darstellung von Wahlergebnissen in Stabdiagrammformat.
 * 
 * @author Patrick Stöckle
 * @version 1.0
 */
public class EinWahlStabdiagrammAnsicht extends EinWahlAnsicht {
	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor.
	 * 
	 * @param wahl
	 *            Wahl
	 */
	public EinWahlStabdiagrammAnsicht(Bundestagswahl wahl) {
		super(wahl);
		JTabbedPane tabs = new JTabbedPane();

		fuegeStabdiagrammHinzu(tabs, bundestagsDaten, bundestagsKopfZeile,
				"Bundestag", "Parteien", "Sitze", 1);
		fuegeStabdiagrammHinzu(tabs, bundeslaenderDaten,
				bundeslaenderKopfZeile, "Bundesländer", "Parteien", "Sitze", 2);
		int wahlkreisNummer = 0;

		for (; wahlkreisNummer < wahlkreisDaten.length;) {
			final int anzahlWahlkreiseProDiagramm = 10;
			Object[][] help = new Object[anzahlWahlkreiseProDiagramm][];
			if (wahlkreisDaten.length - wahlkreisNummer < anzahlWahlkreiseProDiagramm) {
				help = new Object[wahlkreisDaten.length - wahlkreisNummer][];
			}
			for (int j = 0; j < help.length; j++) {
				help[j] = new Object[wahlkreisDaten[wahlkreisNummer].length];
				for (int k = 0; k < help[j].length; k++) {
					help[j][k] = wahlkreisDaten[wahlkreisNummer][k];
				}
				wahlkreisNummer++;

			}
			String anfang = ((String) help[0][1]).substring(0, 3);
			String ende = ((String) help[help.length - 1][1]).substring(0, 3);
			fuegeStabdiagrammHinzu(tabs, help, wahlkreisKopfZeile, anfang
					+ " - " + ende, "Wahlkreisname", "Stimmen", 2);

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
	 * @return {@link DefaultCategoryDataset}
	 */
	protected DefaultCategoryDataset erstelleDatensatz(Object[][] daten,
			Object[] kopfZeile, int start) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < daten.length; i++) {
			for (int j = 0; j < daten[i].length - start; j++) {
				int help = (Integer) daten[i][j + start];
				double d = help;
				String k = (String) kopfZeile[0];
				if (k.endsWith("Gesamtsitzzahl")) {
					dataset.setValue(d, (String) kopfZeile[j + start],
							"Bundestag");
				} else {
					dataset.setValue(d, (String) kopfZeile[j + start],
							(String) daten[i][1]);
				}
			}
		}
		return dataset;
	}

	/**
	 * Erstellt ein Stabdiagramm aus den Parametern.
	 * 
	 * @param dataset
	 *            Datenset
	 * @param titel
	 *            Titel
	 * @return {@link JFreeChart}
	 */
	protected JFreeChart erstelleDiagramm(CategoryDataset dataset,
			String titel, String reihenBeschriftung, String spaltenBeschriftung) {
		// create the chart...
		JFreeChart chart = ChartFactory.createBarChart3D(titel,
				reihenBeschriftung, spaltenBeschriftung, dataset,
				PlotOrientation.VERTICAL, true, true, false);

		TextTitle t = chart.getTitle();
		t.setHorizontalAlignment(HorizontalAlignment.CENTER);
		t.setFont(new Font("Arial", Font.CENTER_BASELINE, 26));

		chart.setBackgroundPaint(Color.white);
		CategoryPlot plot = (CategoryPlot) chart.getPlot();

		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);

		for (int i = 0; i < parteienListe.size(); i++) {
			if (parteienListe.get(i).getFarbe() != null) {
				GradientPaint gp = new GradientPaint(0.0f, 0.0f, parteienListe
						.get(i).getFarbe(), 0.0f, 0.0f, new Color(parteienListe
						.get(i).getFarbe().getRGB()));
				renderer.setSeriesPaint(i, gp);
			}
		}

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));

		return chart;
	}

	/**
	 * Private Methode, die dem übergebenen JTabbedPane ein Stabdiagramm mit den
	 * Parametern hinzufügt.
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
	 */
	private void fuegeStabdiagrammHinzu(JTabbedPane tabs, Object[][] daten,
			Object[] kopfZeile, String titel, String reihenBeschriftung,
			String spaltenBeschriftung, int start) {
		CategoryDataset dataset = erstelleDatensatz(daten, kopfZeile, start);
		JFreeChart chart = erstelleDiagramm(dataset, titel, reihenBeschriftung,
				spaltenBeschriftung);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setFillZoomRectangle(true);
		chartPanel.setMouseWheelEnabled(true);
		chartPanel.setPreferredSize(new Dimension(500, 270));
		tabs.addTab(titel, new JScrollPane(chartPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));

	}
}

package edu.kit.iti.formal.mandatsverteilung.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.util.List;

import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;

/**
 * Klasse zum Vergleich von beliebig vielen Wahlen in Stabdiagrammform.
 * 
 * @author Patrick Stöckle
 * @version 1.0
 */
public class NWahlenWahlStabdiagrammAnsicht extends NWahlenVergleichsAnsicht
		implements StabdiagrammAnsicht {
	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor.
	 * 
	 * @param wahlen
	 *            Wahlen, die verglichen werden.
	 * @param parteienListe
	 *            Parteien im Vergleich
	 */
	public NWahlenWahlStabdiagrammAnsicht(Bundestagswahl[] wahlen,
			List<Partei> parteienListe) {
		super(wahlen, parteienListe);
		JTabbedPane tabs = new JTabbedPane();
		DiagrammeErsteller.stabdiagrammErstellung(this, tabs);
		setLayout(new BorderLayout());
		this.add(tabs, BorderLayout.CENTER);
		setVisible(true);

	}

	/**
	 * Erstellt ein Stabdiagramm aus den Parametern. Implementiert damit
	 * {@link StabdiagrammAnsicht}.
	 */
	@Override
	public JFreeChart erstelleDiagramm(CategoryDataset dataset, String titel,
			String reihenBeschriftung, String spaltenBeschriftung) {
		// create the chart...
		JFreeChart chart = ChartFactory.createBarChart3D(titel,
				reihenBeschriftung, spaltenBeschriftung, dataset,
				PlotOrientation.VERTICAL, true, true, false);
		chart.setBackgroundPaint(Color.white);
		CategoryPlot plot = (CategoryPlot) chart.getPlot();

		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);

		if (titel.equals("Bundestag")) {
			for (int i = 0; i < wahlAnsichten.length; i++) {
				GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.WHITE,
						0.0f, 0.0f, Color.WHITE);
				renderer.setSeriesPaint(i, gp0);
			}
			for (int i = 0; i < wahlAnsichten[0].parteienListe.size(); i++) {
				for (int j = 0; j < wahlAnsichten.length; j++) {
					if (wahlAnsichten[j].parteienListe.get(i).getFarbe() != null) {
						GradientPaint gp = new GradientPaint(0.0f, 0.0f,
								wahlAnsichten[j].parteienListe.get(i)
										.getFarbe(), 0.0f, 0.0f, new Color(
										wahlAnsichten[j].parteienListe.get(i)
												.getFarbe().getRGB()));
						renderer.setSeriesPaint(wahlAnsichten.length + i
								* wahlAnsichten.length + j, gp);
					}
				}
			}
		} else {
			for (int i = 0; i < wahlAnsichten[0].parteienListe.size(); i++) {
				for (int j = 0; j < wahlAnsichten.length; j++) {
					if (wahlAnsichten[j].parteienListe.get(i).getFarbe() != null) {
						GradientPaint gp = new GradientPaint(0.0f, 0.0f,
								wahlAnsichten[j].parteienListe.get(i)
										.getFarbe(), 0.0f, 0.0f, new Color(
										wahlAnsichten[j].parteienListe.get(i)
												.getFarbe().getRGB()));
						renderer.setSeriesPaint(i * wahlAnsichten.length + j,
								gp);
					}
				}
			}

		}

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;
	}
}

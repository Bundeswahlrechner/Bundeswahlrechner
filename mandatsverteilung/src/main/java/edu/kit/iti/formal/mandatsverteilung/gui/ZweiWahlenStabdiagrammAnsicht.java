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
 * Klasse zum Vergleich von 2 Wahlen in Stabdiagrammform.
 * 
 * @author Patrick Stöckle
 * @version 1.0
 */
public class ZweiWahlenStabdiagrammAnsicht extends ZweiWahlenVergleichsAnsicht
		implements StabdiagrammAnsicht {
	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor.
	 * 
	 * @param wahl1
	 *            erste Wahl, die verglichen wird.
	 * @param wahl2
	 *            zweite Wahl, die verglichen wird.
	 * 
	 * @param parteienListe
	 *            Parteien im Vergleich
	 */
	public ZweiWahlenStabdiagrammAnsicht(Bundestagswahl wahl1,
			Bundestagswahl wahl2, List<Partei> parteienList) {
		super(wahl1, wahl2, parteienList);
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
		// title
				reihenBeschriftung, // domain axis label
				spaltenBeschriftung, // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
				);

		chart.setBackgroundPaint(Color.white);
		CategoryPlot plot = (CategoryPlot) chart.getPlot();

		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);

		if (titel.equals("Bundestag")) {
			for (int i = 0; i < 3; i++) {
				GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.WHITE,
						0.0f, 0.0f, Color.WHITE);
				renderer.setSeriesPaint(i, gp0);
			}
			for (int i = 0; i < wahlAnsicht1.parteienListe.size(); i++) {
				if (wahlAnsicht1.parteienListe.get(i).getFarbe() != null) {
					GradientPaint gp0 = new GradientPaint(0.0f, 0.0f,
							wahlAnsicht1.parteienListe.get(i).getFarbe(), 0.0f,
							0.0f, new Color(wahlAnsicht1.parteienListe.get(i)
									.getFarbe().getRGB()));
					renderer.setSeriesPaint(i * 3 + 3, gp0);
					GradientPaint gp2 = new GradientPaint(0.0f, 0.0f,
							wahlAnsicht1.parteienListe.get(i).getFarbe(), 0.0f,
							0.0f, new Color(wahlAnsicht1.parteienListe.get(i)
									.getFarbe().getRGB()));
					renderer.setSeriesPaint(i * 3 + 5, gp2);
				}
				if (wahlAnsicht2.parteienListe.get(i).getFarbe() != null) {
					GradientPaint gp1 = new GradientPaint(0.0f, 0.0f,
							wahlAnsicht1.parteienListe.get(i).getFarbe(), 0.0f,
							0.0f, new Color(wahlAnsicht1.parteienListe.get(i)
									.getFarbe().getRGB()));
					renderer.setSeriesPaint(i * 3 + 4, gp1);
				}

			}
		} else {
			for (int i = 0; i < wahlAnsicht1.parteienListe.size(); i++) {
				if (wahlAnsicht1.parteienListe.get(i).getFarbe() != null) {
					GradientPaint gp0 = new GradientPaint(0.0f, 0.0f,
							wahlAnsicht1.parteienListe.get(i).getFarbe(), 0.0f,
							0.0f, new Color(wahlAnsicht1.parteienListe.get(i)
									.getFarbe().getRGB()));
					renderer.setSeriesPaint(i * 3, gp0);
					GradientPaint gp2 = new GradientPaint(0.0f, 0.0f,
							wahlAnsicht1.parteienListe.get(i).getFarbe(), 0.0f,
							0.0f, new Color(wahlAnsicht1.parteienListe.get(i)
									.getFarbe().getRGB()));
					renderer.setSeriesPaint(i * 3 + 2, gp2);
				}
				if (wahlAnsicht2.parteienListe.get(i).getFarbe() != null) {
					GradientPaint gp1 = new GradientPaint(0.0f, 0.0f,
							wahlAnsicht1.parteienListe.get(i).getFarbe(), 0.0f,
							0.0f, new Color(wahlAnsicht1.parteienListe.get(i)
									.getFarbe().getRGB()));
					renderer.setSeriesPaint(i * 3 + 1, gp1);
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

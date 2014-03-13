package edu.kit.iti.formal.mandatsverteilung.gui;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;

/**
 * Interface, das den Umgang mit und die Auslagerung von gemeinsam genutztem
 * Code aus den Stabdiagrammansichten heraus vereinfacht.
 * 
 * @author Patrick Stöckle
 * @version 1.0
 */
public interface StabdiagrammAnsicht {
	/**
	 * Erstellt ein Diagramm aus den Parametern.
	 * 
	 * @param dataset
	 *            Datenset
	 * @param titel
	 *            Titel
	 * @param reihenBeschriftung
	 *            Beschriftung
	 * @param spaltenBeschriftung
	 *            Beschriftung
	 * @return {@link JFreeChart}
	 */
	public JFreeChart erstelleDiagramm(CategoryDataset dataset, String titel,
			String reihenBeschriftung, String spaltenBeschriftung);
}

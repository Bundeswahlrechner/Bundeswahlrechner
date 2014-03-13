package edu.kit.iti.formal.mandatsverteilung.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Klasse, die haufig genutzte Methoden zur Diagrammdarstellung bündelt.
 * 
 * @author Patrick Stöckle
 * @version 1.0
 * 
 */
public final class DiagrammeErsteller {
	public static final Font STANDARD = null;
	public static final Font STANDARD2 = null;
	public static final Font STANDARD3 = null;

	/**
	 * Erstellt einen Datensatz für ein Stabdiagramm aus den Parametern.
	 * 
	 * @param daten
	 *            Daten
	 * @param kopfZeile
	 *            Beschriftung
	 * @param start
	 *            Start der Daten
	 * @return {@link DefaultCategoryDataset}
	 */
	private static DefaultCategoryDataset erstelleDatensatz(Object[][] daten,
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
	 * Fügt einem Container eine JTable mit den Daten, der Beschriftung und dem
	 * Titel hinzu.
	 * 
	 * @param tabs
	 *            JTabbedPane
	 * @param daten
	 *            Daten
	 * @param kopfZeile
	 *            Beschriftung
	 * @param s
	 *            Titel
	 */
	protected static void fuegeJTabelleHinzu(Container tabs, Object[][] daten,
			Object[] kopfZeile, String s) {

		TableModel model = new DefaultTableModel(daten, kopfZeile) {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public Class getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}
		};

		JTable jTabelle = new JTable(model);
		jTabelle.getTableHeader().setFont(DiagrammeErsteller.STANDARD2);
		jTabelle.setFont(DiagrammeErsteller.STANDARD3);
		jTabelle.setEnabled(false);
		jTabelle.getTableHeader().setReorderingAllowed(false);
		RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
		jTabelle.setRowSorter(sorter);
		if (tabs.getClass() == JTabbedPane.class) {
			JTabbedPane t = (JTabbedPane) tabs;
			t.addTab(s, new JScrollPane(jTabelle,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		} else {
			tabs.add(new JScrollPane(jTabelle));
		}

	}

	/**
	 * Erzeugt ein Stabdiagramm einer {@link Vergleichsansicht} mit den Daten,
	 * der Beschriftung und dem Titel und fügt diese dem übergebenen JTabbedPane
	 * hinzu.
	 * 
	 * @param vgl
	 *            Vergleichsansicht
	 * @param tabs
	 *            JTabbedPane
	 * @param daten
	 *            Daten
	 * @param kopfZeile
	 *            Beschriftung
	 * @param titel
	 *            Titel
	 * @param reihenBeschriftung
	 *            Beschriftung
	 * @param spaltenBeschriftung
	 *            Beschriftung
	 * @param start
	 *            Start der Daten
	 */
	protected static void fuegeStabdiagrammHinzu(Vergleichsansicht vgl,
			JTabbedPane tabs, Object[][] daten, Object[] kopfZeile,
			String titel, String reihenBeschriftung,
			String spaltenBeschriftung, int start) {
		CategoryDataset dataset = erstelleDatensatz(daten, kopfZeile, start);
		StabdiagrammAnsicht stab = (StabdiagrammAnsicht) vgl;
		JFreeChart chart = stab.erstelleDiagramm(dataset, titel,
				reihenBeschriftung, spaltenBeschriftung);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setFillZoomRectangle(true);
		chartPanel.setMouseWheelEnabled(true);
		chartPanel.setPreferredSize(new Dimension(500, 270));
		tabs.addTab(titel, new JScrollPane(chartPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));

	}

	/**
	 * Erzeugt die Stabdiagramme einer {@link Vergleichsansicht} und fügt diese
	 * dem übergebenen JTabbedPane hinzu.
	 * 
	 * @param vgl
	 *            Vergleichsansicht
	 * @param tabs
	 *            JTabbedPane
	 */
	protected static void stabdiagrammErstellung(Vergleichsansicht vgl,
			JTabbedPane tabs) {
		fuegeStabdiagrammHinzu(vgl, tabs, vgl.bundestagsVergleichsDaten,
				vgl.bundestagsVergleichsKopfZeile, "Bundestag", "Parteien",
				"Sitze", 0);
		int bundeslaenderNummer = 0;
		for (int i = 0; bundeslaenderNummer < vgl.bundeslaenderVergleichsDaten.length; i++) {
			final int anzahlBundeslaenderProDiagramm = 4;
			Object[][] help = new Object[anzahlBundeslaenderProDiagramm][];

			if (vgl.bundeslaenderVergleichsDaten.length - bundeslaenderNummer < anzahlBundeslaenderProDiagramm) {
				help = new Object[vgl.bundeslaenderVergleichsDaten.length
						- bundeslaenderNummer][];
			}

			for (int j = 0; j < help.length; j++) {
				help[j] = new Object[vgl.bundeslaenderVergleichsDaten[bundeslaenderNummer].length];
				for (int k = 0; k < help[j].length; k++) {
					help[j][k] = vgl.bundeslaenderVergleichsDaten[bundeslaenderNummer][k];
				}
				bundeslaenderNummer++;

			}
			fuegeStabdiagrammHinzu(vgl, tabs, help,
					vgl.bundeslaenderVergleichsKopfZeile, "Bundesländer"
							+ (i + 1), "Wahlkreisname", "Stimmen", 2);

		}
	}

	/**
	 * Erzeugt die Tabellen einer {@link Vergleichsansicht} und fügt diese dem
	 * übergebenen JTabbedPane hinzu.
	 * 
	 * @param vgl
	 *            Vergleichsansicht
	 * @param tabs
	 *            JTabbedPane
	 */
	protected static void tabellenErstellung(Vergleichsansicht vgl,
			JTabbedPane tabs) {
		fuegeJTabelleHinzu(tabs, vgl.bundeslaenderVergleichsDaten,
				vgl.bundeslaenderVergleichsKopfZeile,
				"Bundeslaendervergleichstabelle");
		fuegeJTabelleHinzu(tabs, vgl.bundestagsVergleichsDaten,
				vgl.bundestagsVergleichsKopfZeile,
				"Bundestagsvergleichstabelle");
		fuegeJTabelleHinzu(tabs, vgl.wahlkreisVergleichsDaten,
				vgl.wahlkreisVergleichsKopfzeile, "Wahlkreisvergleichstabelle");
		fuegeJTabelleHinzu(tabs, vgl.wahlkreisGewinnerVergleichsDaten,
				vgl.wahlkreisGewinnerVergleichsKopfZeile,
				"Wahlkreisgewinnervergleichstabelle");
	}

	/**
	 * Privater Konstruktor.
	 */
	private DiagrammeErsteller() {
	}

}

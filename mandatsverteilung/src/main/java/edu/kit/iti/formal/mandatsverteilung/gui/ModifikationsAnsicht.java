/**
 * 
 */
package edu.kit.iti.formal.mandatsverteilung.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kreiswahlvorschlag;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Landesliste;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Wahlkreis;

/**
 * 
 * Modifikationsansicht in der der Nutzer die Möglichkeit hat, die Rohdaten
 * einer {@link Bundestagswahl} zu modifizieren
 * 
 * @author Peter
 * 
 */
public class ModifikationsAnsicht extends JDialog {

	/**
	 * private Klasse für eine modifizierbare Tabelle
	 * 
	 * @author Peter
	 * 
	 */
	private class ModificationTable extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Die Rohdaten, die in der Tabelle angezeigt werden
		 */
		private final String[][] rohdaten;

		/**
		 * Die Beschriftung der Spalten der Tabelle
		 */
		private final String[] spaltenBezeichnungen;

		/**
		 * gibt an, ob Daten modifiziert wurden
		 */
		private boolean wurdeModifiziert;

		/**
		 * Konstruktor zur Erzeugung einer modifizierbaren Tabelle
		 * 
		 * @param rohdaten
		 *            Date in der Tabelle
		 * @param spaltenBezeichnungen
		 *            Bezeichnungen der Spalten
		 */
		public ModificationTable(String[][] rohdaten,
				String[] spaltenBezeichnungen) {
			this.rohdaten = rohdaten;
			this.spaltenBezeichnungen = spaltenBezeichnungen;
			setModifiziert(false);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getColumnCount() {
			return spaltenBezeichnungen.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getColumnName(int column) {
			return spaltenBezeichnungen[column];
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getRowCount() {
			return rohdaten.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object getValueAt(int row, int column) {
			return rohdaten[row][column];
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isCellEditable(int row, int column) {
			return column > 1 && rohdaten[row][column] != "-";
		}

		/**
		 * @param wurdeModifiziert
		 *            legt fest, ob Daten modifiziert wurden
		 */
		public void setModifiziert(boolean wurdeModifiziert) {
			this.wurdeModifiziert = wurdeModifiziert;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setValueAt(Object val, int row, int column) {
			try {
				int value = Integer.parseInt((String) val);
				if (value < 0) {
					throw new NumberFormatException();
				} else if (isCellEditable(row, column)) {
					setModifiziert(true);
					rohdaten[row][column] = String.valueOf(value);
					fireTableCellUpdated(row, column);
				}

			} catch (NumberFormatException e) {
				if (isModal()) {
					JOptionPane
							.showMessageDialog(
									ModifikationsAnsicht.this,
									"Sie haben in Zeile "
											+ (row + 1)
											+ ", Spalte "
											+ (column + 1)
											+ " keine positive Ganzzahl eingegeben. \n"
											+ "Bitte geben Sie nur positive Ganzzahlen"
											+ " und keine Buchstaben oder Sonderzeichen ein.",
									"Es ist ein Fehler aufgetreten!",
									JOptionPane.ERROR_MESSAGE);
				}

			}
		}

		/**
		 * @return true, wenn Daten modifiziert wurden
		 */
		protected boolean wurdeModifiziert() {
			return wurdeModifiziert;
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Array mit den Spaltenbezeichnungen der {@link Bundesland}tabelle
	 */
	private String[] blSpaltenBezeichnungen;

	/**
	 * Tabelle mit den Erststimmen der Bundesländer
	 */
	public JTable blTabelle;

	/**
	 * 2-dimensionales Array mit den Zweitstimmen der Bundesländer
	 */
	private String[][] blZweitstimmen;

	/**
	 * Ein {@link Bundesland}-Array mit den Bundesländern der aktuell in der
	 * {@link GUI} angezeigten {@link Bundestagswahl}
	 */
	private Bundesland[] bundeslaender;

	/**
	 * Die zugehörige {@link GUI}
	 */
	private final GUI gui;

	/**
	 * Button über den die {@link Bundestagswahl} mit modifizierten Daten neu
	 * berechnet werden kann
	 */
	public JButton neuBerechnen;

	/**
	 * Ein Array mit den {@link Partei}en der aktuell in der {@link GUI}
	 * angezeigten {@link Bundestagswahl}
	 */
	private Partei[] parteien;

	/**
	 * die aktuell in der {@link GUI} angezeigte {@link Bundestagswahl}
	 */
	private Bundestagswahl wahl;

	/**
	 * Ein Array mit den {@link Wahlkreis}en der aktuell in der {@link GUI}
	 * angezeigten {@link Bundestagswahl}
	 */
	private Wahlkreis[] wahlkreise;

	/**
	 * 2-dimensionales Array mit den Erststimmen der {@link Wahlkreis}e
	 */
	private String[][] wkErststimmen;

	/**
	 * Tabelle mit den Erststimmen der {@link Wahlkreise}
	 */
	public JTable wkErststimmenTabelle;

	/**
	 * Array mit den Spaltenbezeichnungen der {@link Wahlkreis}tabellen
	 */
	private String[] wkSpaltenBezeichnungen;

	/**
	 * 2-dimensionales Array mit den Zweitstimmen der {@link Wahlkreis}e
	 */
	private String[][] wkZweitstimmen;

	/**
	 * Tabelle mit den Zweitstimmen der {@link Wahlkreise}
	 */
	public JTable wkZweitstimmenTabelle;

	/**
	 * Konstruktor für eine Modifikationsansicht mit zugewiesener
	 * (Haupt-)GUI-Klasse
	 * 
	 * @param gui
	 *            {@link GUI}, die der Modifikationsansicht zugewiesen werden
	 *            soll
	 */
	public ModifikationsAnsicht(GUI gui, boolean modal) {
		super(gui, modal);
		this.gui = gui;
		wahl = this.gui.aktiveWahl();
		this.setTitle("Modifikationsansicht");
		neuBerechnen = new JButton("Neu Berechnen");
		neuBerechnen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				neuBerechnen();
				ModifikationsAnsicht.this.gui.setEnabled(true);
			}
		});
		initialisiereTabellen();

		// Überträgt Änderungen in der WahlkreisZweitstimmenTabelle auf die
		// Bundesländertabelle
		wkZweitstimmenTabelle = new JTable(new ModificationTable(
				wkZweitstimmen, wkSpaltenBezeichnungen) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void setValueAt(Object val, int row, int column) {
				String alterWert = wkZweitstimmen[row][column];
				int neuerWert = Integer.parseInt((String) val);
				super.setValueAt(String.valueOf(neuerWert), row, column);

				// Folgendes dient zur Aktualisierung der Zweitstimmen im
				// zugehörigen

				int dif = neuerWert - Integer.parseInt(alterWert);
				int i = 0;
				while (bundeslaender[i] != wahlkreise[row].getBundesland()) {
					i++;
				}
				if (dif != 0) {
					blZweitstimmen[i][column - 1] = String.valueOf(Integer
							.parseInt((String) blTabelle.getValueAt(i,
									column - 1))
							+ dif);
				}

			}

		});

		wkZweitstimmenTabelle.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		wkErststimmenTabelle = new JTable(new ModificationTable(wkErststimmen,
				wkSpaltenBezeichnungen) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void setValueAt(Object val, int row, int column) {
				int neuerWert = Integer.parseInt((String) val);
				super.setValueAt(String.valueOf(neuerWert), row, column);
			}
		});
		wkErststimmenTabelle.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// Überträgt die Änderungen in der Bundesländertabelle auf die
		// zugehörigen Wahlkreise in der Wahlkreiszweitstimmentabelle
		blTabelle = new JTable(new ModificationTable(blZweitstimmen,
				blSpaltenBezeichnungen) {

			/**
					 * 
					 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column > 0 && blZweitstimmen[row][column] != "-";
			}

			@Override
			public void setValueAt(Object val, int row, int column) {
				String alterWert = blZweitstimmen[row][column];
				int neuerWert = Integer.parseInt((String) val);
				super.setValueAt(String.valueOf(neuerWert), row, column);

				// Folgendes dient zur Aktualisierung der Zweitstimmen in den
				// zugehörigen Wahlkreisen.
				// Ist neuerWert hier < 0, so muss eine Endlosschleife
				// verhindert
				// werden.

				if (neuerWert >= 0) {
					int dif = neuerWert - Integer.parseInt(alterWert);
					if (dif != 0) {
						Wahlkreis[] wks = new Wahlkreis[bundeslaender[row]
								.anzahlWahlkreise()];
						Iterator<Wahlkreis> wkIterator = bundeslaender[row]
								.wahlkreisIterator();
						for (int i = 0; wkIterator.hasNext(); i++) {
							wks[i] = wkIterator.next();
						}

						int j = 0;
						do {
							int i = 0;
							while (wahlkreise[i] != wks[j % wks.length]) {
								i++;
							}
							int wkWert = Integer
									.parseInt((String) wkZweitstimmenTabelle
											.getValueAt(i, column + 1));
							if (wkZweitstimmenTabelle.getValueAt(i, column + 1) != "-"
									&& (wkWert != 0 || dif > 0)) {

								wkZweitstimmen[i][column + 1] = String
										.valueOf(wkWert + dif / Math.abs(dif));
								dif -= dif / Math.abs(dif);
							}
							j++;
						} while (dif != 0);

					}
				}
			}
		});

		passeSpaltenbreiteAn(blTabelle, blZweitstimmen, blSpaltenBezeichnungen);
		passeSpaltenbreiteAn(wkErststimmenTabelle, wkErststimmen,
				wkSpaltenBezeichnungen);
		passeSpaltenbreiteAn(wkZweitstimmenTabelle, wkZweitstimmen,
				wkSpaltenBezeichnungen);

		blTabelle.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Wahlkreiserststimmen", new JScrollPane(
				wkErststimmenTabelle, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		tabs.addTab("Wahlkreiszweitstimmen", new JScrollPane(
				wkZweitstimmenTabelle,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		tabs.addTab("Bundesländerzweitstimmen", new JScrollPane(blTabelle,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(tabs, BorderLayout.CENTER);
		cp.add(neuBerechnen, BorderLayout.SOUTH);
		cp.setPreferredSize(new Dimension(700, 500));
		cp.setMinimumSize(new Dimension(550, 150));

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * Initialisiert die Arrays und Tabellen mit den Daten der aktuell in der
	 * {@link GUI} angezeigten {@link Bundestagswahl}. {@link Partei}en, die in
	 * einem {@link Bundesland} oder einem {@link Wahlkreis} nicht antreten,
	 * erhalten ein "-" an der entsprechenden Stelle in der Tabelle. Diese
	 * Stellen sind nicht editierbar!
	 */
	private void initialisiereTabellen() {
		parteien = new Partei[wahl.parteienAnzahl()];
		wahlkreise = new Wahlkreis[wahl.wahlkreisAnzahl()];
		bundeslaender = new Bundesland[wahl.bundeslaenderAnzahl()];

		wkSpaltenBezeichnungen = new String[parteien.length + 2];
		blSpaltenBezeichnungen = new String[parteien.length + 1];

		wkSpaltenBezeichnungen[0] = "Wahlkreisname";
		wkSpaltenBezeichnungen[1] = "Wahlkreisnummer";

		wkZweitstimmen = new String[wahl.wahlkreisAnzahl()][wkSpaltenBezeichnungen.length];
		wkErststimmen = new String[wahl.wahlkreisAnzahl()][wkSpaltenBezeichnungen.length];

		blSpaltenBezeichnungen[0] = "Bundesland";
		blZweitstimmen = new String[wahl.bundeslaenderAnzahl()][blSpaltenBezeichnungen.length];

		Iterator<Bundesland> blIterator = wahl.bundeslaenderIterator();
		for (int i = 0; blIterator.hasNext(); i++) {
			bundeslaender[i] = blIterator.next();
			blZweitstimmen[i][0] = bundeslaender[i].getName();
			for (int j = 1; j < blSpaltenBezeichnungen.length; j++) {
				blZweitstimmen[i][j] = "-";
			}
		}

		Iterator<Wahlkreis> wkIterator = wahl.wahlkreisIterator();
		while (wkIterator.hasNext()) {
			Wahlkreis wk = wkIterator.next();
			int i = wk.getNummer() - 1;
			wahlkreise[i] = wk;
			wkZweitstimmen[i][0] = wk.getName();
			wkErststimmen[i][0] = wk.getName();
			wkZweitstimmen[i][1] = String.valueOf(wk.getNummer());
			wkErststimmen[i][1] = String.valueOf(wk.getNummer());
			for (int j = 2; j < wkSpaltenBezeichnungen.length; j++) {
				wkZweitstimmen[i][j] = "-";
				wkErststimmen[i][j] = "-";
			}
		}

		Iterator<Partei> parteiIterator = wahl.parteienIterator();
		for (int i = 0; parteiIterator.hasNext(); i++) {
			parteien[i] = parteiIterator.next();
		}

		for (int i = 0; i < parteien.length; i++) {
			wkSpaltenBezeichnungen[i + 2] = parteien[i].getName();
			blSpaltenBezeichnungen[i + 1] = parteien[i].getName();

			for (int k = 0; k < wahlkreise.length; k++) {
				Wahlkreis wk = wahl.getWahlkreis(wahlkreise[k].getNummer());
				if (wk.getParteiZweitstimmen(parteien[i]) == 0) {
					wkZweitstimmen[k][i + 2] = "-";
				} else {
					wkZweitstimmen[k][i + 2] = String.valueOf(wk
							.getParteiZweitstimmen(parteien[i]));
				}

				Iterator<Kreiswahlvorschlag> kwvs = wahl
						.getWahlkreis(wahlkreise[k].getNummer())
						.getWahlkreisBewerber().iterator();
				while (kwvs.hasNext()) {
					Kreiswahlvorschlag kwv = kwvs.next();
					if (kwv.getPartei() != null) {
						int l = 0;
						while (l < parteien.length
								&& parteien[l] != kwv.getPartei()) {
							l++;
						}
						wkErststimmen[k][l + 2] = String.valueOf(kwv
								.getStimmen());
					}
				}
			}

			for (int j = 0; j < bundeslaender.length; j++) {
				String neuerWert = "-";
				if (parteien[i].getLandesliste(bundeslaender[j]) != null) {
					neuerWert = String.valueOf(parteien[i].getLandesliste(
							bundeslaender[j]).berechneZweitstimmen());
				}
				blZweitstimmen[j][i + 1] = neuerWert;

			}

		}

	}

	/**
	 * Veranlasst eine Neuberechnng der gerade modifizierten
	 * {@link Bundestagswahl} mit den veränderten Stimmdaten.
	 */
	private void neuBerechnen() {
		// Die Wahl muss nur neu berechnet werde, wenn Daten modifiziert wurden
		if (((ModificationTable) wkErststimmenTabelle.getModel())
				.wurdeModifiziert()
				|| ((ModificationTable) wkZweitstimmenTabelle.getModel())
						.wurdeModifiziert()
				|| ((ModificationTable) blTabelle.getModel())
						.wurdeModifiziert()) {

			Bundestagswahl wahl = gui.aktiveWahl();
			Landesliste[] listen = new Landesliste[parteien.length];

			for (int j = 0; j < bundeslaender.length; j++) {

				for (int i = 0; i < parteien.length; i++) {
					listen[i] = parteien[i].getLandesliste(bundeslaender[j]);
				}

				// Zweitstimmenauswertung
				Wahlkreis[] wks = new Wahlkreis[bundeslaender[j]
						.anzahlWahlkreise()];
				Iterator<Wahlkreis> wkIterator = bundeslaender[j]
						.wahlkreisIterator();
				for (int i = 0; wkIterator.hasNext(); i++) {

					wks[i] = wkIterator.next();
					for (int k = 0; k < parteien.length; k++) {
						int wert = 0;
						if (wkZweitstimmen[wks[i].getNummer() - 1][k + 2] != "-") {
							wert = Integer.parseInt(wkZweitstimmen[wks[i]
									.getNummer() - 1][k + 2]);
						}
						// Zweitstimmen können nur in einem Bundesland abgegeben
						// werden, in dem die jeweilige Partei eine Landesliste
						// eingereicht hat.
						if (parteien[k].getLandesliste(wks[i].getBundesland()) != null) {
							wks[i].setParteiZweitstimmen(parteien[k], wert);
						}
					}

					// Erststimmenauswertung:
					Iterator<Kreiswahlvorschlag> kwvIterator = wks[i]
							.getWahlkreisBewerber().iterator();
					while (kwvIterator.hasNext()) {
						Kreiswahlvorschlag kwv = kwvIterator.next();
						if (kwv.getPartei() != null) {
							int parteiIndex = 0;
							while (parteien[parteiIndex] != kwv.getPartei()) {
								parteiIndex++;
							}
							if (wkErststimmen[wks[i].getNummer() - 1][parteiIndex + 2] != "-") {
								kwv.setStimmen(Integer.parseInt(wkErststimmen[wks[i]
										.getNummer() - 1][parteiIndex + 2]));
							}
						}

					}

				}

			}
			try {
				wahl.getWahlverfahren().berechneWahlergebnis();
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(),
						"Berechnungsfehler", JOptionPane.ERROR_MESSAGE);
			}
			gui.ersetzeAktiveWahl(wahl);
		} else if (isModal()) {
			JOptionPane.showMessageDialog(null,
					"Es wurden keine Daten modifiziert.", "Information",
					JOptionPane.INFORMATION_MESSAGE);
		}
		this.setVisible(false);
		this.dispose();
	}

	/**
	 * Passt die Spaltenbreiten an die jeweiligen Inhalte an
	 * 
	 * @param tabelle
	 * @param daten
	 * @param spaltenBezeichnungen
	 */
	private void passeSpaltenbreiteAn(JTable tabelle, Object[][] daten,
			Object[] spaltenBezeichnungen) {
		for (int i = 0; i < spaltenBezeichnungen.length; i++) {
			int max = 0;

			for (int j = 0; j < daten.length; j++) {
				int temp = (((String) daten[j][i]).length() + 1) * 7;
				if (temp > max) {
					max = temp;

				}
			}
			int temp = (((String) spaltenBezeichnungen[i]).length() + 2) * 7;
			if (temp > max) {
				max = temp;
			}
			if (tabelle != null) {
				tabelle.getColumnModel().getColumn(i).setPreferredWidth(max);
			}

		}
	}

}

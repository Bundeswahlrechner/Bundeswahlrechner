/**
 * 
 */
package edu.kit.iti.formal.mandatsverteilung.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.importer.CSVWahldatenImporter;
import edu.kit.iti.formal.mandatsverteilung.importer.WahldatenImporter;

/**
 * Hauptfenster der graphischen Oberfläche, in dem einzelne
 * {@link Bundestagswahl}en angezeigt werden und von dem aus alle andern Fenster
 * erreichbar sind
 * 
 * @author Peter
 * 
 */
public class GUI extends JFrame {

	/**
	 * Private Unterklasse von JTabbedPane mit den verschiedenen
	 * {@link EinWahlAnsicht}en als Tabs. Dies hat zwar den Nachteil, dass für
	 * jede {@link Bundestagswahl} immer alle Ansichten geladen werden müssen,
	 * aber auch den Vorteil, dass das Wechseln zwischen den Ansichten schneller
	 * erfolgt (da diese bereits geladen sind), was für den Benutzer wesentlich
	 * angenehmer ist.
	 * 
	 * @author Peter
	 * 
	 */
	private class WahlTabPane extends JTabbedPane {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Die dargestellte {@link Bundestagswahl}
		 */
		private Bundestagswahl wahl;

		/**
		 * Konstruktor für ein WahlTabPane mit anzuzeigender
		 * {@link Bundestagswahl} und zugewiesener GUI-Klasse
		 * 
		 * @param wahl
		 *            anzuzeigende {@link Bundestagswahl}
		 * @param gui
		 *            zugewiesene GUI-Klasse
		 */
		public WahlTabPane(final Bundestagswahl wahl) {
			super();
			this.wahl = wahl;

			this.addTab("Tabelle", new EinWahlTabellenAnsicht(wahl));
			this.addTab("Kuchendiagramm", null);
			this.addTab("Stabdiagramm", null);
			this.addTab("Deutschlandkarte", null);
			this.setFont(DiagrammeErsteller.STANDARD2);
			this.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent arg0) {
					// Aus Performanzgründen sollen die verschiedenen
					// Wahlansichten erst geladen werden,/ wenn der
					// entsprechende Tab ausgewählt wird. Ist eine
					// Ansicht bereits geladen, so muss sie nicht neu geladen
					// werden
					if (getSelectedComponent() == null) {
						int index = getSelectedIndex();
						if (index == 1) {
							setComponentAt(index,
									new EinWahlKuchendiagrammAnsicht(wahl));
						} else {
							if (index == 2) {
								setComponentAt(index,
										new EinWahlStabdiagrammAnsicht(wahl));
							} else {
								if (index == 3) {
									setComponentAt(index,
											new EinWahlDeutschlandAnsicht(wahl,
													true));
								}
							}
						}

					}
				}

			});
		}

		/**
		 * Gibt die dargestellte {@link Bundestagswahl} zurück.
		 * 
		 * @return the wahl
		 */
		public Bundestagswahl getWahl() {
			return wahl;
		}

		/**
		 * Legt die darzustellende {@link Bundestagswahl} fest
		 * 
		 * @param wahl
		 *            the wahl to set
		 */
		public void setWahl(Bundestagswahl wahl) {
			this.wahl = wahl;
			setComponentAt(0, new EinWahlTabellenAnsicht(wahl));
			setComponentAt(1, new EinWahlKuchendiagrammAnsicht(wahl));
			setComponentAt(2, new EinWahlStabdiagrammAnsicht(wahl));
			setComponentAt(3, new EinWahlDeutschlandAnsicht(wahl, true));
			this.repaint();
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Main-Methode der Software, in der eine GUI erzeugt und angezeigt wird
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new GUI();
	}

	/**
	 * Datei mit Wahlkreisen und Stimmen für Standardimport der Bundestagswahl
	 * 2013
	 */
	private InputStream datei1;
	/**
	 * Datei mit Kandidaten für Standardimport der Bundestagswahl 2013
	 */
	private InputStream datei2;

	/**
	 * Datei mit Bundesländern für Standardimport der Bundestagswahl 2013
	 */
	private InputStream datei3;

	/**
	 * Die {@link Menuleiste} der GUI
	 */
	public Menuleiste menu;

	/**
	 * JButton über den eine neue {@link Bundestagswahl} hinzugefügt werden kann
	 */
	public JButton neueWahl;

	private InputStream reinitDatei1;

	/**
	 * JTabbedPane mit {@link WahlTabPane}s als Tabs
	 */
	public JTabbedPane tabs;

	/**
	 * JButton über den die gerade angezeigte {@link Bundestagswahl} entfernt
	 * werden kann
	 */
	public JButton wahlEntfernen;

	/**
	 * JButton über den die gerade angezeigte {@link Bundestagswahl} kopiert
	 * werden kann
	 */
	public JButton wahlKopieren;

	/**
	 * Standardkonstruktor einer GUI
	 */
	public GUI() {
		super();
		this.setTitle("Bundeswahlrechner");
		this.setName("Bundeswahlrechner");
		this.setState(JFrame.ICONIFIED);
		this.setIconImage(new ImageIcon(GUI.class.getResource("/Wahl.png"))
				.getImage());

		tabs = new JTabbedPane();
		tabs.setFont(DiagrammeErsteller.STANDARD2);
		setMenu(new Menuleiste(this, true));
		neueWahl = new JButton("Wahl erstellen");
		neueWahl.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				datei1 = GUI.class.getResourceAsStream("/kerg.csv");
				datei2 = GUI.class
						.getResourceAsStream("/Tab23_Wahlbewerber_a.csv");
				datei3 = GUI.class.getResourceAsStream("/bundesL.csv");
				reinitDatei1 = GUI.class.getResourceAsStream("/kerg.csv");
				WahldatenImporter importer = new CSVWahldatenImporter(datei1,
						reinitDatei1, datei2, datei3);
				try {
					neueWahl(importer.importieren());

				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(),
							"Es ist ein Fehler aufgetreten!",
							JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		});

		wahlEntfernen = new JButton("Wahl entfernen");
		wahlEntfernen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				wahlEntfernen();
			}
		});
		wahlEntfernen.setEnabled(false);

		wahlKopieren = new JButton("Wahl duplizieren");
		wahlKopieren.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				wahlKopieren();
			}
		});
		wahlKopieren.setEnabled(false);

		JPanel knöpfe = new JPanel();
		knöpfe.setLayout(new GridLayout(1, 3));
		knöpfe.add(wahlEntfernen);
		knöpfe.add(wahlKopieren);
		knöpfe.add(neueWahl);
		for (int i = 0; i < knöpfe.getComponentCount(); i++) {
			knöpfe.getComponent(i).setFont(DiagrammeErsteller.STANDARD);
		}
		setLayout(new BorderLayout());
		this.add(menu, BorderLayout.NORTH);
		this.add(tabs, BorderLayout.CENTER);
		this.add(knöpfe, BorderLayout.SOUTH);

		this.setExtendedState(MAXIMIZED_BOTH);
		this.setMinimumSize(new Dimension(900, 100));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);

	}

	/**
	 * Liefert die {@link Bundestagswahl} im aktuell angezeigten Tab zurück
	 */
	protected Bundestagswahl aktiveWahl() {
		Bundestagswahl wahl = null;
		if (tabs.getComponentCount() > 0) {
			wahl = ((WahlTabPane) tabs.getSelectedComponent()).getWahl();
		}
		return wahl;
	}

	/**
	 * Gibt die anzahl an aktuellen Wahlen zurück.
	 * 
	 * @return Anzahl
	 */
	protected int anzahlWahlen() {
		return tabs.getTabCount();
	}

	/**
	 * Ersetzt die aktive, gerade angezeigte {@link Bundestagswahl} durch eine
	 * neue Wahl. Dies ist vor allem dann nötig, wenn die aktive
	 * {@link Bundestagswahl} bzw ihre Berechnungsoptionen modifiziert wurden
	 * 
	 * @param wahl
	 */
	protected void ersetzeAktiveWahl(Bundestagswahl wahl) {
		((WahlTabPane) tabs.getSelectedComponent()).setWahl(wahl);
	}

	/**
	 * Gibt die Wahlen zurück.
	 * 
	 * @return Bunndestagswahlen.
	 */
	protected Bundestagswahl[] getWahlen() {
		int i = tabs.getTabCount();
		if (i == 0) {
			return null;
		}
		Bundestagswahl[] wahlen = new Bundestagswahl[i];
		while (i > 0) {
			i--;
			wahlen[i] = ((WahlTabPane) tabs.getComponentAt(i)).getWahl();
		}
		return wahlen;
	}

	/**
	 * Fügt neuen Tab mit einer neuen {@link Bundestagswahl} hinzu.
	 * 
	 * @param wahl
	 *            die hinzuzufügende Wahl
	 */
	protected void neueWahl(Bundestagswahl wahl) {
		WahlTabPane tab = new WahlTabPane(wahl);
		tab.setTabPlacement(JTabbedPane.LEFT);
		tabs.addTab("Wahl " + (tabs.getTabCount() + 1), null, tab,
				"Zu dieser Wahl wechseln");
		tabs.setSelectedIndex(tabs.getTabCount() - 1);
		menu.buttonsAktiv(true);
		wahlEntfernen.setEnabled(true);
		wahlKopieren.setEnabled(true);
	}

	protected void setMenu(Menuleiste menuleiste) {
		menu = menuleiste;
	}

	/**
	 * Aktualisiert die Namen aller Tabs.
	 */
	private void tabsAktualisieren() {
		for (int i = 0; i < tabs.getTabCount(); i++) {
			tabs.setTitleAt(i, "Wahl " + (i + 1));
		}
	}

	/**
	 * Entfernt die {@link Bundestagswahl} im gerade ausgewählten Tab und
	 * aktualisiert die Namen der anderen Tabs, falls dia Anzahl der Tabs > 0
	 * ist.
	 */
	protected void wahlEntfernen() {
		if (tabs.getTabCount() > 0) {
			tabs.remove(tabs.getSelectedIndex());
			tabsAktualisieren();
			int tabAnzahl = tabs.getTabCount();
			if (tabAnzahl < 2) {
				boolean b = tabAnzahl == 1;
				menu.buttonsAktiv(b);
				wahlEntfernen.setEnabled(b);
				wahlKopieren.setEnabled(b);
			}
		}
	}

	/**
	 * Kopiert die aktuell angezeigte {@link Bundestagswahl} in einen neuen Tab.
	 */
	protected void wahlKopieren() {
		if (tabs.getTabCount() > 0) {
			Bundestagswahl wahl = aktiveWahl().clone();
			wahl.getWahlverfahren().berechneWahlergebnis();
			neueWahl(wahl);
		}
	}
}

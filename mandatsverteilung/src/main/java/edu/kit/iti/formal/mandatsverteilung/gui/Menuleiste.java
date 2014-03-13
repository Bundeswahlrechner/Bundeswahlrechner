/**
 * 
 */
package edu.kit.iti.formal.mandatsverteilung.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Bundeswahlgesetz2013;

/**
 * Menüleiste der graphischen Oberfläche, über die Export-, Import- und
 * Einstellungsfenster geoeffnet werden koennen
 * 
 * @author Peter
 * 
 */
public class Menuleiste extends JToolBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected JButton berechnungsOptionen;
	protected BerechnungsOptionenFenster berechnungsOptionenFenster;
	protected JButton einstellungen;
	protected EinstellungsFenster einstellungsFenster;

	protected ExportFenster exportFenster;

	protected JButton exportieren;
	protected JButton generieren;
	protected GeneriereWahldatenFenster generiereWahldatenFenster;
	/**
	 * Die zugehoerige {@link GUI}
	 */
	protected final GUI gui;
	protected ImportFenster importFenster;
	protected JButton importieren;
	private final boolean modaleFenster;
	protected JButton modifikation;
	protected ModifikationsAnsicht modifikationsAnsicht;
	protected JButton wahlenVergleichen;
	protected JButton zwischenergebnis;
	protected Zwischenergebnisansicht zwischenergebnisAnsicht;

	/**
	 * Konstruktor für eine Menüleiste mit zugewieseer (Haupt-)GUI-Klasse
	 * 
	 * @param gui
	 *            GUI-Klasse, die der Menüleiste zugewiesen werden soll
	 */
	public Menuleiste(GUI gui, boolean modaleFenster) {
		super();
		this.gui = gui;
		this.modaleFenster = modaleFenster;
		importieren = new JButton("Importieren");
		exportieren = new JButton("Exportieren");
		einstellungen = new JButton("Einstellungen");
		modifikation = new JButton("Wahldaten modifizieren");
		generieren = new JButton("Wahldaten generieren");
		berechnungsOptionen = new JButton("Berechnungsoptionen");
		wahlenVergleichen = new JButton("Wahlen vergleichen");
		zwischenergebnis = new JButton("Zwischenergebnisse");

		importieren.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				oeffneImport();
			}
		});

		exportieren.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				oeffneExport();
			}
		});

		einstellungen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				oeffneEinstellungen();
			}
		});

		modifikation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				oeffneModifikationsansicht();
			}
		});

		generieren.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				oeffneGeneriereWahldatenFenster();
			}
		});

		berechnungsOptionen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				oeffneBerechnungsOptionenFenster();
			}
		});
		wahlenVergleichen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vergleicheWahlen();

			}
		});

		zwischenergebnis.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				oeffneZwischenergebnisansicht();
			}
		});

		this.buttonsAktiv(false);
		this.add(importieren);
		this.add(exportieren);
		this.add(berechnungsOptionen);
		this.add(einstellungen);
		this.add(modifikation);
		this.add(generieren);
		this.add(wahlenVergleichen);
		this.add(zwischenergebnis);
		for (int i = 0; i < this.getComponentCount(); i++) {
			this.getComponent(i).setFont(DiagrammeErsteller.STANDARD);
		}
		this.setVisible(true);

	}

	/**
	 * Aktiviert oder deaktiviert die Buttons, für deren Funktionen eine Wahl
	 * verfügbar sein muss
	 * 
	 * @param b
	 *            gibt an, ob die Buttons aktiv sein sollen
	 */
	protected void buttonsAktiv(boolean b) {
		berechnungsOptionen.setEnabled(b);
		einstellungen.setEnabled(b);
		exportieren.setEnabled(b);
		generieren.setEnabled(b);
		modifikation.setEnabled(b);
		zwischenergebnis.setEnabled(b);
		wahlenVergleichen.setEnabled(gui.anzahlWahlen() > 1);
	}

	/**
	 * Öffnet das {@link BerechnungsOptionenFenster}.
	 */
	protected void oeffneBerechnungsOptionenFenster() {
		berechnungsOptionenFenster = new BerechnungsOptionenFenster(gui,
				modaleFenster);
	}

	/**
	 * Öffnet das {@link EinstellungsFenster}.
	 */
	public void oeffneEinstellungen() {
		einstellungsFenster = new EinstellungsFenster(gui, modaleFenster);
	}

	/**
	 * Öffnet das {@link ExportFenster}.
	 */
	public void oeffneExport() {
		exportFenster = new ExportFenster(gui, modaleFenster);
	}

	/**
	 * Öffnet das {@link GeneriereWahldatenFenster}.
	 */
	protected void oeffneGeneriereWahldatenFenster() {
		generiereWahldatenFenster = new GeneriereWahldatenFenster(gui,
				modaleFenster);
	}

	/**
	 * Öffnet das {@link ImportFenster}.
	 */
	public void oeffneImport() {
		importFenster = new ImportFenster(gui, modaleFenster);
	}

	/**
	 * Öffnet die {@link ModifikationsAnsicht}.
	 */
	protected void oeffneModifikationsansicht() {
		modifikationsAnsicht = new ModifikationsAnsicht(gui, modaleFenster);
	}

	protected void oeffneZwischenergebnisansicht() {
		if (gui.aktiveWahl().getWahlverfahren().getClass()
				.equals(Bundeswahlgesetz2013.class)) {
			zwischenergebnisAnsicht = new Zwischenergebnisansicht(gui,
					gui.aktiveWahl());
		} else {
			JOptionPane
					.showMessageDialog(gui,
							"Leider bieten wir für dieses Wahlverfahren keine Zwischenergebnisansicht an.");
		}

	}

	/**
	 * Erstellt eine Vergleichsansicht.
	 */
	protected void vergleicheWahlen() {
		int i = gui.anzahlWahlen();
		Bundestagswahl[] wahlen = gui.getWahlen();
		List<Partei> parteienListe = new LinkedList<>();

		for (int j = 0; j < wahlen.length; j++) {
			Iterator<Partei> parteienIterator = wahlen[j].eingezogeneParteien()
					.iterator();
			while (parteienIterator.hasNext()) {
				Partei partei = parteienIterator.next();
				Iterator<Partei> plistIterator = parteienListe.iterator();
				boolean fehlt = true;
				while (plistIterator.hasNext() && fehlt) {
					Partei parteiInListe = plistIterator.next();
					if (partei.getName().equals(parteiInListe.getName())) {
						fehlt = false;
					}
				}
				if (fehlt) {
					parteienListe.add(partei);
				}
			}
		}
		boolean parteilose = false;
		for (int j = 0; j < wahlen.length && !parteilose; j++) {
			if (wahlen[j].getParteiloseMandatstraeger() != 0) {
				parteilose = true;
				parteienListe.add(new Partei("Parteilose"));
			}
		}
		Collections.sort(parteienListe);
		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(800, 400));
		JTabbedPane tabs = new JTabbedPane();
		if (i == 2) {
			Bundestagswahl wahl1 = wahlen[0];
			Bundestagswahl wahl2 = wahlen[1];
			tabs.add("Stabdiagramm", new ZweiWahlenStabdiagrammAnsicht(wahl1,
					wahl2, parteienListe));
			tabs.add("Tabelle", new ZweiWahlenTabellenAnsicht(wahl1, wahl2,
					parteienListe));
		} else {
			tabs.add("Tabellenansicht", new NWahlenTabellenAnsicht(wahlen,
					parteienListe));
			tabs.add("Wahlstabdiagrammansicht",
					new NWahlenWahlStabdiagrammAnsicht(wahlen, parteienListe));
		}
		frame.add(tabs);
		setVisible(true);
		frame.setMinimumSize(new Dimension(900, 300));
		frame.setPreferredSize(new Dimension(1000, 600));
		frame.pack();
		frame.setVisible(true);
	}

}

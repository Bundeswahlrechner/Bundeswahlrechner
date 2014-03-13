package edu.kit.iti.formal.mandatsverteilung.gui;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

/**
 * Klasse zur Darstellung von Wahlergebnissen in Tabellenform.
 * 
 * @author Patrick Stöckle
 * @version 1.0
 */
public class EinWahlTabellenAnsicht extends EinWahlAnsicht {

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
	public EinWahlTabellenAnsicht(Bundestagswahl wahl) {
		super(wahl);

		JTabbedPane tabs = new JTabbedPane();
		tabs.setFont(DiagrammeErsteller.STANDARD2);
		DiagrammeErsteller.fuegeJTabelleHinzu(tabs, wahlkreisErststimmen,
				wahlkreisErststimmenKopfzeile, "Wahlkreiserststimmen");
		DiagrammeErsteller.fuegeJTabelleHinzu(tabs, wahlkreisDaten,
				wahlkreisKopfZeile, "Wahlkreiszweitstimmen");
		DiagrammeErsteller.fuegeJTabelleHinzu(tabs, bundeslaenderDaten,
				bundeslaenderKopfZeile, "Bundesländersitze");
		DiagrammeErsteller.fuegeJTabelleHinzu(tabs, bundestagsDaten,
				bundestagsKopfZeile, "Bundestagsitze");
		DiagrammeErsteller.fuegeJTabelleHinzu(tabs, wahlkreisGewinnerDaten,
				wahlkreisGewinnerKopfZeile, "Wahlkreisgewinner");
		DiagrammeErsteller.fuegeJTabelleHinzu(tabs, kandidatDaten,
				kandidatKopfZeile, "Bundestagsabgeordnete");
		DiagrammeErsteller.fuegeJTabelleHinzu(tabs, prozentansichtDaten,
				prozentansichtKopfZeile, "Prozentansicht");
		setLayout(new BorderLayout());
		this.add(tabs, BorderLayout.CENTER);
		setVisible(true);
	}

}

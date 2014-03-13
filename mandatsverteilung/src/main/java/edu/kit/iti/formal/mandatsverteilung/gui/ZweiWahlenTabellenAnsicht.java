package edu.kit.iti.formal.mandatsverteilung.gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JTabbedPane;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;

/**
 * Klasse zum Vergleich von 2 Wahlen in Tabellenform.
 * 
 * @author Patrick Stöckle
 * @version 1.0
 */
public class ZweiWahlenTabellenAnsicht extends ZweiWahlenVergleichsAnsicht {
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
	public ZweiWahlenTabellenAnsicht(Bundestagswahl wahl1,
			Bundestagswahl wahl2, List<Partei> parteienList) {
		super(wahl1, wahl2, parteienList);
		JTabbedPane tabs = new JTabbedPane();
		DiagrammeErsteller.tabellenErstellung(this, tabs);
		setLayout(new BorderLayout());
		this.add(tabs, BorderLayout.CENTER);
		setVisible(true);
	}
}

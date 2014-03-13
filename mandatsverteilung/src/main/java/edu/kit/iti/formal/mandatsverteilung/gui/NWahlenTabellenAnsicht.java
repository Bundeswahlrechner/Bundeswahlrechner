package edu.kit.iti.formal.mandatsverteilung.gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JTabbedPane;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;

/**
 * Klasse zum Vergleich von beliebig vielen Wahlen in Tabellenform.
 * 
 * @author Patrick Stöckle
 * @version 1.0
 */
public class NWahlenTabellenAnsicht extends NWahlenVergleichsAnsicht {
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
	public NWahlenTabellenAnsicht(Bundestagswahl[] wahlen,
			List<Partei> parteienListe) {
		super(wahlen, parteienListe);
		JTabbedPane tabs = new JTabbedPane();
		DiagrammeErsteller.tabellenErstellung(this, tabs);
		setLayout(new BorderLayout());
		this.add(tabs, BorderLayout.CENTER);
		setVisible(true);
	}

}

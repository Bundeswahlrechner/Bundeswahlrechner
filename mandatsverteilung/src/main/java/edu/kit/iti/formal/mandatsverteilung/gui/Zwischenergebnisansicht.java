package edu.kit.iti.formal.mandatsverteilung.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Landesliste;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;

public class Zwischenergebnisansicht extends JDialog {

	/**
	     * 
	     */
	private static final long serialVersionUID = 1L;

	protected JButton b;

	protected final String[] beschriftung;

	private final Container c;

	public Zwischenergebnisansicht(GUI gui, Bundestagswahl wahl) {
		super(gui);
		beschriftung = new String[wahl.eingezogeneParteien().size() + 1];
		c = new Container();
		c.setLayout(new GridLayout(14, 2));

		erststimmen();
		sperrklausel(wahl);
		landeslistensitze();
		mindestSitzZahl(wahl);
		oberverteilung(wahl);
		unterverteilung(wahl);

		fuegeTextZeileHinzu("7. Schritt: Wahlkreisgewinner ohne erfolgreiche Landesliste");
		fuegeTextFeldHinzu("Nun werden die Oberverteilungssitzzahlen um die Wahlkreisgewinner "
				+ "ohne erfolgreiche Landesliste erhöht, falls diese einer Partei angehören.");
		fuegeTextZeileHinzu("8. Schritt: Überprüfe Mehrheitsklausel");
		fuegeTextFeldHinzu("Zum Schluss wird geprüft, ob die Mehrheitsklausel (BWahlG §6 Absatz 7) angewendet wird.");
		fuegeUnterpunktHinzu("Die Wahlberechnung ist abgeschlossen!");
		b = new JButton("OK");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Zwischenergebnisansicht.this.dispose();
			}
		});
		c.add(b);
		this.add(c, BorderLayout.CENTER);
		this.setTitle("Zwischenergebnisansicht");
		this.setPreferredSize(new Dimension(1920, 1040));
		this.setMinimumSize(new Dimension(800, 400));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.pack();
		setVisible(true);

	}

	private void erststimmen() {
		fuegeTextZeileHinzu("1. Schritt: Erstimmenauswertung");
		fuegeTextFeldHinzu("Jedem Wahlkreis wird derjenige Kreiswahlvorschlag "
				+ "als Wahlkreisgewinner zugeordnet, der die meisten Erststimmen auf sich vereinigt.");
	}

	private void fuegeTextFeldHinzu(String s) {
		JTextArea text = new JTextArea(s);
		text.setFont(new Font("Arial", Font.ITALIC, 14));
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setEditable(false);
		text.setBackground(Color.WHITE);
		text.setForeground(Color.BLACK);
		JScrollPane scrollPane = new JScrollPane(text);
		c.add(scrollPane, BorderLayout.CENTER);
	}

	private void fuegeTextZeileHinzu(String s) {
		JTextField text = new JTextField(s);
		text.setEditable(false);
		text.setHorizontalAlignment(JTextField.LEFT);
		text.setFont(new Font("Arial", Font.BOLD, 20));
		text.setBackground(Color.WHITE);
		text.setForeground(Color.BLACK);
		c.add(text);
	}

	private void fuegeUnterpunktHinzu(String s) {
		JTextField text = new JTextField(s);
		text.setEditable(false);
		text.setHorizontalAlignment(JTextField.LEFT);
		text.setFont(new Font("Arial", Font.ITALIC, 18));
		text.setBackground(Color.WHITE);
		text.setForeground(Color.BLACK);
		c.add(text);
	}

	private void landeslistensitze() {
		fuegeTextZeileHinzu("3. Schritt: Landeslisten");
		fuegeTextFeldHinzu("Berechnet in jedem Bundesland die Pseudoverteilungssitze, die auf "
				+ "die Landeslisten der Parteien entfallen.");
	}

	private void mindestSitzZahl(Bundestagswahl wahl) {
		fuegeTextZeileHinzu("4. Schritt: Mindessitzzahlen");
		fuegeTextFeldHinzu("Für jede Partei wird die Mindessitzzahl gesetzt, ausgehend von den Pseudoverteilungssitzzahlen der Landeslisten und den Wahlkreisgewinnern.");
		fuegeUnterpunktHinzu("Mindestsitzahlverteilung");
		Iterator<Partei> parteienIterator = wahl.eingezogeneParteien()
				.iterator();
		Object[][] daten = new Object[1][];
		daten[0] = new Object[beschriftung.length];
		daten[0][0] = "Mindestsitzzahl";
		int i = 1;
		while (parteienIterator.hasNext()) {
			Partei p = parteienIterator.next();
			daten[0][i++] = p.getMindestsitzzahl();
		}
		DiagrammeErsteller.fuegeJTabelleHinzu(c, daten, beschriftung, "");
	}

	private void oberverteilung(Bundestagswahl wahl) {
		fuegeTextZeileHinzu("5. Schritt: Oberverteilung");
		fuegeTextFeldHinzu("Die Oberverteilungssitzzahl jeder Partei wird nach dem Bundeswahlgesetz 2013 berechnet.");
		fuegeUnterpunktHinzu("Oberverteilung");
		Iterator<Partei> parteienIterator = wahl.eingezogeneParteien()
				.iterator();
		Object[][] daten = new Object[1][];
		daten[0] = new Object[beschriftung.length];
		daten[0][0] = "Oberverteilung";
		int i = 1;
		while (parteienIterator.hasNext()) {
			Partei p = parteienIterator.next();
			daten[0][i++] = p.getOberverteilungSitzzahl();
		}
		DiagrammeErsteller.fuegeJTabelleHinzu(c, daten, beschriftung, "");

	}

	private void sperrklausel(Bundestagswahl wahl) {
		fuegeTextZeileHinzu("2.Schritt: Sperrklauselprüfung");
		fuegeTextFeldHinzu("Laut Bundeswahlgesetz werden bei der Sitzverteilung nach Zweiststimmen nur Parteien berücksichtigt, die mehr als 5% der Zweitstimmen im Wahlgebiet erlangt, 3 Direktmandate gewonnen haben oder eine nationale Minderheit repräsentieren (BWahlG §6 Absatz 3)");

		fuegeUnterpunktHinzu("Folgende Parteien haben die Sperrklausel überwunden:");
		List<Partei> parteienList = (List<Partei>) wahl.eingezogeneParteien();
		Iterator<Partei> parteienIterator = wahl.parteienIterator();
		String result = "";
		while (parteienIterator.hasNext()) {
			Partei p = parteienIterator.next();
			if (p.isSperrklauselUeberwunden()) {
				result = p.getName() + ", " + result;
				parteienList.remove(p);
			}
		}
		fuegeTextFeldHinzu(result);

		fuegeUnterpunktHinzu("Folgende Parteien sind nur über Direktmandate eingezogen:");
		parteienIterator = parteienList.iterator();
		result = "";
		while (parteienIterator.hasNext()) {
			Partei p = parteienIterator.next();
			result = p.getName() + ", " + result;
		}
		if (result.equals("")) {
			result = "Keine Partei ist nur über Direktmandate eingezogen";
		}
		fuegeTextFeldHinzu(result);

		fuegeUnterpunktHinzu("Folglich sind diese Parteien im Bundestag vertreten:");
		parteienIterator = wahl.eingezogeneParteien().iterator();
		result = "";
		beschriftung[0] = "Partei";
		int i = 1;
		while (parteienIterator.hasNext()) {
			Partei p = parteienIterator.next();
			result = p.getName() + ", " + result;
			beschriftung[i++] = p.getName();
		}
		fuegeTextFeldHinzu(result);

	}

	private void unterverteilung(Bundestagswahl wahl) {
		fuegeTextZeileHinzu("6. Schritt: Unterverteilung");
		Object[][] daten = new Object[wahl.bundeslaenderAnzahl()][];
		Iterator<Bundesland> buIterator = wahl.bundeslaenderIterator();
		for (int i = 0; buIterator.hasNext(); i++) {
			Bundesland b = buIterator.next();
			daten[i] = new Object[beschriftung.length];
			daten[i][0] = b.getName();
			Iterator<Partei> parteienIterator = wahl.eingezogeneParteien()
					.iterator();
			for (int j = 0; parteienIterator.hasNext(); j++) {
				Partei p = parteienIterator.next();
				Landesliste ll = p.getLandesliste(b);
				if (ll != null) {
					daten[i][j + 1] = ll.getUnterverteilungSitzzahl();
				} else {
					daten[i][j + 1] = "Keine Liste";
				}
			}

		}
		DiagrammeErsteller.fuegeJTabelleHinzu(c, daten, beschriftung, "");
	}
}
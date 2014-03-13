/**
 * 
 */
package edu.kit.iti.formal.mandatsverteilung.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;

/**
 * Einstellungsfenster, in dem der Benutzer die Möglichkeit hat, die Farben der
 * {@link Partei}en nach Belieben zu ändern
 * 
 * @author Peter
 * 
 */
public class EinstellungsFenster extends JDialog {

	/**
	 * Private Klasse zur Auswahl und Änderung der Farbe einer {@Link
	 * Partei}. Eine Farbauswahl besteht aus einem Label mit dem Namen der
	 * zugewiesenen Partei und einem Button, dessen Farbe der Farbe der Partei
	 * entspricht, und der einen ColorChooser öffnet, mit welchem die Farbe der
	 * Partei geändert werden kann
	 * 
	 * @author Peter
	 * 
	 */
	protected class Farbauswahl extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Die aktuelle Farbe der zugehörigen {@link Partei}
		 */
		private Color farbe;

		/**
		 * Button der einen ColorChooser öffnet, bei dem eine Farbe ausgewählt
		 * werden kann.
		 * <p>
		 * Der Button ist in der Farbe der Partei gefärbt.
		 */
		public JButton farbeAendern;

		/**
		 * Gibt an, ob die Farbe geändert wurde
		 */
		private boolean farbeGeaendert;

		/**
		 * Die zugehörige {@link Partei}
		 */
		private final Partei partei;

		/**
		 * Konstruktor für eine Farbauswahl mit Parteinamen als Label und einem
		 * Button mit dem die Farbe der zugewiesenen {@link Partei} geändert
		 * werden kann und dessen Farbe die Farbe der Partei widerspiegelt.
		 * 
		 * @param partei
		 *            die der Farbauswahl zuzuweisende {@link Partei}
		 */
		public Farbauswahl(Partei partei) {
			super();
			this.partei = partei;
			farbe = partei.getFarbe();

			setFarbeGeaendert(false);
			GridLayout layout = new GridLayout(1, 2);
			layout.setHgap(5);
			this.setLayout(layout);
			JLabel name = new JLabel(partei.getName());
			name.setSize(200, 50);
			farbeAendern = new JButton("Farbe ändern");
			farbeAendern.setBackground(farbe);
			this.setFarbe(farbe);
			farbeAendern.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (isModal()) {
						farbe = JColorChooser.showDialog(null, "Farbauswahl",
								null);
					} else {
						farbe = new Color(new Random().nextFloat(),
								new Random().nextFloat(), new Random()
										.nextFloat());
					}
					if (farbe != null) {
						setFarbe(farbe);
					}
				}
			});
			this.add(farbeAendern);
			this.add(name);
		}

		/**
		 * @return true, wenn die Farbe geändert wurde
		 */
		protected boolean farbeGeaendert() {
			return farbeGeaendert;
		}

		/**
		 * Ändert die Farbe der zugewiesenen {@link Partei} und des
		 * "Farbe ändern"-Buttons
		 * 
		 * @param farbe
		 *            neue Farbe
		 */
		protected void setFarbe(Color farbe) {
			// Farbe muss nur geändert werden, wenn sie nicht der momentan
			// gesetzten Fabe entspricht
			if (partei.getFarbe() != farbe) {
				this.farbe = farbe;
				setFarbeGeaendert(true);
				partei.setFarbe(farbe);
				farbeAendern.setBackground(farbe);
				farbeAendern.repaint();
			}
		}

		/**
		 * @param farbeGeaendert
		 *            legt fest, ob die Farbe geändert wurde
		 */
		private void setFarbeGeaendert(boolean farbeGeaendert) {
			this.farbeGeaendert = farbeGeaendert;
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Ein Array mit einer {@link Farbauswahl} pro {@link Partei} in der aktuell
	 * in der {@link GUI} angezeigten {@link Bundestagswahl}
	 */
	public Farbauswahl[] auswahl;

	/**
	 * Die zu gehörige {@link GUI}
	 */
	private final GUI gui;

	/**
	 * Ein Array mit den {@link Partei}en der aktuell in der {@link GUI}
	 * angezeigten {@link Bundestagswahl}
	 */
	private final Partei[] parteien;

	/**
	 * JButton, der mit dem geänderte Einstellungn für die Wahl übernommen
	 * werden können
	 */
	protected final JButton uebernehmen;

	/**
	 * Erzeugt ein neues Eintellungsfenster
	 * 
	 * @param gui
	 *            zugehörige {@link GUI}
	 */
	public EinstellungsFenster(GUI gui, boolean modal) {
		super(gui, modal);
		this.gui = gui;

		parteien = new Partei[gui.aktiveWahl().parteienAnzahl()];
		Iterator<Partei> parteiIterator = gui.aktiveWahl().parteienIterator();
		for (int i = 0; parteiIterator.hasNext() && i < parteien.length; i++) {
			parteien[i] = parteiIterator.next();
		}

		auswahl = new Farbauswahl[parteien.length];

		this.setTitle("Einstellungen");
		Container contentPane = new Container();
		contentPane.setLayout(new GridLayout((parteien.length + 1) / 2, 2));

		for (int i = 0; i < parteien.length; i++) {
			auswahl[i] = new Farbauswahl(parteien[i]);
			contentPane.add(auswahl[i]);

		}

		uebernehmen = new JButton("Übernehmen");
		uebernehmen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				aendereEinstellungen();
				EinstellungsFenster.this.gui.setEnabled(true);
			}
		});

		this.setLayout(new BorderLayout());
		this.add(contentPane, BorderLayout.CENTER);
		this.add(uebernehmen, BorderLayout.SOUTH);
		this.setPreferredSize(new Dimension(800, 600));
		this.setMinimumSize(new Dimension(600, 400));

		this.pack();
		this.setVisible(true);
	}

	/**
	 * Ändert die Farben aller {link Partei}en entsprechend der getätigten
	 * Auswahl und "schließt" das Einstellungsfenster
	 */
	public void aendereEinstellungen() {
		// Folgendes ist nur nötig, wenn mindestens eine Farbe geändert wurde
		if (wurdeGeaendert()) {
			gui.ersetzeAktiveWahl(gui.aktiveWahl());
		} else {
			if (isModal()) {
				JOptionPane.showMessageDialog(null,
						"Optionen wurden nicht geändert.", "Information",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}

		this.setVisible(false);
		this.dispose();
	}

	/**
	 * @return true, wenn mindestens eine Farbe geändert wurde
	 */
	private boolean wurdeGeaendert() {
		boolean b = false;
		for (int i = 0; i < auswahl.length && !b; i++) {
			b = auswahl[i].farbeGeaendert();
		}
		return b;

	}

}

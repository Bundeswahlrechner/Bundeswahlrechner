package edu.kit.iti.formal.mandatsverteilung.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;
import edu.kit.iti.formal.mandatsverteilung.generierer.Einschraenkung;
import edu.kit.iti.formal.mandatsverteilung.generierer.MandatszahlAbsolut;
import edu.kit.iti.formal.mandatsverteilung.generierer.MandatszahlRelativ;
import edu.kit.iti.formal.mandatsverteilung.generierer.RandomisierterGenerierer;
import edu.kit.iti.formal.mandatsverteilung.generierer.SitzzahlEinschraenkung;

/**
 * Ein Fenster, in dem der Benutzer verschiedene {@link Einschraenkung}en
 * vornehmen kann, anhand derer die aktuell in der {@link GUI} angezeigte
 * {@link Bundestagwahl} verändert wird und soeine neue Bundestagswahl
 * "generiert" wird, die die Einschränkungen (evtl. mit einer gewissen
 * Abweichung) erfüllt.
 * 
 * @author Peter
 * 
 */
public class GeneriereWahldatenFenster extends JDialog implements
		GeneriererBeobachter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected JButton abbrechenKnopf;
	/**
	 * JSpinner, bei dem die Toleranz für die Abweichung von der
	 * {@link Einschraenkung} für die gesamte Sitzzahl im Bundestag festgelegt
	 * werden kann.
	 */
	protected JSpinner abweichung1;

	/**
	 * JSpinner, bei dem die Toleranz für die Abweichung von einer
	 * {@link Einschraenkung} für die Sitzzahl einer {@link Partei} festgelegt
	 * werden kann.
	 */
	protected JSpinner abweichung2;

	/**
	 * JCheckBox mit der entschieden werden kann, ob die {@link Einschraenkung}
	 * der Gesamtsitzzahl bei der Generierung berücksichtigt werden soll oder
	 * nicht.
	 */
	protected JCheckBox box1;

	/**
	 * JCheckBox mit der entschieden werden kann, ob die {@link Einschraenkung}
	 * für die Sitzzahl einer {@link Partei} bei der Generierung berücksichtigt
	 * werden soll oder nicht.
	 */
	protected JCheckBox box2;

	/**
	 * Button, der die Generierung startet
	 */
	protected JButton generieren;

	/**
	 * Die zugehörige {@link GUI}
	 */
	private final GUI gui;

	/**
	 * JComboBox, in der eine {@link Partei} ausgewählt werden kann, für die
	 * {@link Einschraenkungen} festgelegt werden können.
	 */
	protected JComboBox<String> parteiAuswahl;

	/**
	 * Ein Array mit den {@link Partei}en der aktuell in der {@link GUI}
	 * angezeigten {@link Bundestagswahl}
	 */
	private final Partei[] parteien;

	/**
	 * Ein Array mit den Namen der {@link Partei}en der aktuell in der
	 * {@link GUI} angezeigten {@link Bundestagswahl}
	 */
	private final String[] parteiNamen;

	/**
	 * JSpinner, bei dem eine {@link Einschraenkung} für die absolute Sitzzahl
	 * einer {@link Partei} festgelegt werden kann.
	 */
	protected JSpinner sitzzahlAbs;

	/**
	 * JComboBox, in der der Benutzer auswählen kann ob er die relative oder die
	 * absolute Sitzzahl einer {@link Partei} festlegen will
	 */
	protected JComboBox<String> sitzzahlArt;

	/**
	 * JSpinner, bei dem eine {@link Einschraenkung} für die gesamte Sitzzahl
	 * des Bundestags festgelegt werden kann.
	 */
	protected JSpinner sitzzahlGesamt;

	/**
	 * JSpinner, bei dem eine {@link Einschraenkung} für die relative Sitzzahl
	 * einer {@link Partei} festgelegt werden kann.
	 */
	protected JSpinner sitzzahlRel;

	/**
	 * JLabel, das den Status des Generierers angibt.
	 */
	private final JLabel statusLabel;

	/**
	 * Die aktuell in der {@link GUI} angezeigte {@link Bundestagswahl}
	 */
	private final Bundestagswahl wahl;

	protected Thread worker;

	/**
	 * Erzeugt ein neues GeneriereWahldatenFenster
	 * 
	 * @param gui
	 *            zugehörige {@link GUI}
	 */
	public GeneriereWahldatenFenster(GUI gui, boolean modal) {
		super(gui, modal);
		this.gui = gui;
		statusLabel = new JLabel("Default-Wert");
		abbrechenKnopf = new JButton("Generierung abbrechen");
		this.setTitle("Generiere Wahldaten");
		wahl = gui.aktiveWahl();
		parteien = new Partei[wahl.parteienAnzahl()];
		Iterator<Partei> parteiIterator = wahl.parteienIterator();
		for (int i = 0; parteiIterator.hasNext() && (i < parteien.length); i++) {
			parteien[i] = parteiIterator.next();
		}

		final JLabel label = new JLabel("Gesamtsitzzahl: ");
		int gesamtsitzzahl = wahl.getSitzzahl();

		parteiNamen = new String[parteien.length];
		for (int i = 0; i < parteien.length; i++) {
			parteiNamen[i] = parteien[i].getName();
		}

		sitzzahlAbs = new JSpinner(new SpinnerNumberModel(gesamtsitzzahl / 4,
				0, 600, 1));
		sitzzahlAbs.setEnabled(true);
		sitzzahlRel = new JSpinner(new SpinnerNumberModel(30, 0, 100, 1));
		sitzzahlRel.setEnabled(false);
		sitzzahlGesamt = new JSpinner(new SpinnerNumberModel(gesamtsitzzahl,
				598, 7500, 1));
		parteiAuswahl = new JComboBox<String>(parteiNamen);
		parteiAuswahl.setEditable(false);

		String[] relOrAbs = { "relative Sitzzahl", "absolute Sitzzahl" };
		sitzzahlArt = new JComboBox<String>(relOrAbs);
		sitzzahlArt.setSelectedIndex(1);
		sitzzahlArt.setEditable(false);

		sitzzahlArt.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				switch (sitzzahlArt.getSelectedIndex()) {
				case 1:
					sitzzahlAbs.setEnabled(true);
					sitzzahlRel.setEnabled(false);
					break;
				case 0:
					sitzzahlAbs.setEnabled(false);
					sitzzahlRel.setEnabled(true);
					break;
				default:
					break;
				}

			}
		});

		generieren = new JButton("Generieren");
		generieren.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GeneriereWahldatenFenster.this.gui.setEnabled(true);
				generieren();
			}
		});

		box1 = new JCheckBox();
		box2 = new JCheckBox();
		box1.setSelected(true);
		box2.setSelected(true);

		abweichung1 = new JSpinner(new SpinnerNumberModel(5, 0, 100, 1));
		abweichung2 = new JSpinner(new SpinnerNumberModel(5, 0, 100, 1));
		final JLabel erlaubteAbweichung1 = new JLabel("Erlaubte Abweichung: ");
		final JLabel erlaubteAbweichung2 = new JLabel("Erlaubte Abweichung: ");

		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		Container cpCenter = new Container();
		cpCenter.setLayout(new GridLayout(2, 1));
		Container cpLeft = new Container();
		cpLeft.setLayout(new GridLayout(2, 1));
		Container cp1 = new Container();
		cp1.setLayout(new GridLayout(1, 5));
		Container cp2 = new Container();
		cp2.setLayout(new GridLayout(1, 7));
		cpLeft.add(box1);
		cpLeft.add(box2);
		cp1.add(label);
		cp1.add(sitzzahlGesamt);
		cp1.add(erlaubteAbweichung1);
		cp1.add(abweichung1);
		cp2.add(parteiAuswahl);
		cp2.add(sitzzahlArt);
		cp2.add(sitzzahlAbs);
		cp2.add(sitzzahlRel);
		cp2.add(erlaubteAbweichung2);
		cp2.add(abweichung2);
		cpCenter.add(cp1);
		cpCenter.add(cp2);
		contentPane.add(cpLeft, BorderLayout.WEST);
		contentPane.add(cpCenter, BorderLayout.CENTER);
		contentPane.add(generieren, BorderLayout.SOUTH);

		// sorgt dafür, dass die Einschränkung der Gesamtsitzzahl nur verändert
		// werden kann, wenn box1 ausgewählt ist
		box1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				sitzzahlGesamt.setEnabled(box1.isSelected());
				label.setEnabled(box1.isSelected());
				erlaubteAbweichung1.setEnabled(box1.isSelected());
				abweichung1.setEnabled(box1.isSelected());
			}
		});

		// sorgt dafür, dass die Einschränkung der Sitzzahleiner Partei nur
		// verändert werden kann, wenn box2 ausgewählt ist
		box2.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				parteiAuswahl.setEnabled(box2.isSelected());
				sitzzahlArt.setEnabled(box2.isSelected());

				if (sitzzahlArt.getSelectedIndex() == 1) {
					sitzzahlAbs.setEnabled(box2.isSelected());
				} else {
					sitzzahlRel.setEnabled(box2.isSelected());
				}

				erlaubteAbweichung2.setEnabled(box2.isSelected());
				abweichung2.setEnabled(box2.isSelected());
			}
		});

		this.setPreferredSize(new Dimension(800, 200));
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void aktualisieren(String neueNachricht) {
		statusLabel.setText(neueNachricht);
		this.repaint();
	}

	/**
	 * Generiert eine {@link Bundestagswahl} entsprechend der vom Benutzer
	 * enigestellten {@link Einschraenkung}en und fügt sie der {@link GUI}
	 * hinzu.
	 */
	public void generieren() {
		// Instantiiert Generierer und Constraints
		RandomisierterGenerierer generierer = new RandomisierterGenerierer(wahl);
		if (box1.isSelected()) {
			SitzzahlEinschraenkung einschraenkung1 = new SitzzahlEinschraenkung(
					(int) sitzzahlGesamt.getValue(),
					(int) abweichung1.getValue());
			generierer.addEinschraenkung(einschraenkung1);
		}
		if (box2.isSelected()) {
			if (sitzzahlAbs.isEnabled()) {
				MandatszahlAbsolut einschraenkung2 = new MandatszahlAbsolut(
						(int) sitzzahlAbs.getValue(),
						(int) abweichung2.getValue(),
						parteien[parteiAuswahl.getSelectedIndex()].toString());
				generierer.addEinschraenkung(einschraenkung2);
			} else if (sitzzahlRel.isEnabled()) {
				MandatszahlRelativ einschraenkung2 = new MandatszahlRelativ(
						(int) sitzzahlRel.getValue(),
						(int) abweichung2.getValue(),
						parteien[parteiAuswahl.getSelectedIndex()].toString());
				generierer.addEinschraenkung(einschraenkung2);
			}
		}
		this.getContentPane().removeAll();
		this.add(statusLabel, BorderLayout.CENTER);
		this.add(abbrechenKnopf, BorderLayout.SOUTH);
		this.repaint();
		// Hilfsvariablen zur Ergebnis- & Parameterübergabe
		// Erzeugt neuen Thread, der generiert
		List<String> wichtigeParteien = new ArrayList<>();
		/*
		 * if (box2.isSelected()) {
		 * wichtigeParteien.add(parteien[parteiAuswahl.getSelectedIndex()]
		 * .toString()); }
		 */
		worker = new GeneriererWorkerThread(generierer, gui, this,
				wichtigeParteien);
		abbrechenKnopf.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			/**
			 * Schießt den Worker-Thread ab. Muss leider die veraltete Methode Thread.stop() verwenden.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				worker.stop();
				GeneriereWahldatenFenster.this.setVisible(false);
				GeneriereWahldatenFenster.this.dispose();
			}
		});
		this.aktualisieren("Die Berechnung wurde begonnen!");
		generierer.registriereBeobachter(this);
		worker.start();
		// Rest der Generierung im WorkerThread, damit EDT frei bleibt
	}
}

/**
 * 
 */
package edu.kit.iti.formal.mandatsverteilung.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.importer.CSVParserFabrikMitAutomatischenKandidaten;
import edu.kit.iti.formal.mandatsverteilung.importer.CSVWahldatenImporter;
import edu.kit.iti.formal.mandatsverteilung.importer.WahldatenImporter;

// import java.io.File;

/**
 * Importfenster, über das der Benutzer {@link Bundestagswahl}en importieren
 * kann
 * 
 * @author Peter
 * 
 */
public class ImportFenster extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Datei mit Wahkreisen und Stimmen einer {@link Bundestagswahl}
	 */
	private File datei1;

	/**
	 * Datei mit Kandidaten einer {@link Bundestagswahl}
	 */
	private File datei2;

	/**
	 * Datei mit Bundesländern einer {@link Bundestagswahl}
	 */
	private File datei3;

	/**
	 * JComboBox mit der das zu importierende Dateiformat ausgewählt werden
	 * kann.
	 */
	protected JComboBox<String> dateiFormat;

	/**
	 * Textfeld, das den Pfad von datei1 enthält
	 */
	protected JTextField eingabe1;

	/**
	 * Textfeld, das den Pfad von datei2 enthält
	 */
	protected JTextField eingabe2;

	/**
	 * Textfeld, das den Pfad von datei2 enthält
	 */
	protected JTextField eingabe3;

	/**
	 * JFileChooser über den die zu importierenden Dateien ausgewählt werden
	 * können
	 */
	protected JFileChooser fc;

	/**
	 * FileFilter, der Dateien im FileChooser filtert
	 */
	private FileFilter filter;

	/**
	 * String-Array, das die Namen der verschiedenen Formate, die importiert
	 * werden können, enthält.
	 */
	private final String[] formate = { "CSV",
			"CSV mit erweiterten Landeslisten" }; // kann ggf.
	// durch
	// weitere
	// importierbare
	// Formate ergänzt werden

	/**
	 * Die zugehörige {@link GUI}
	 */
	private final GUI gui;

	/**
	 * JButton, über den den Import gestartet wird
	 */
	public JButton importieren;

	/**
	 * JButton, der einen FileChooser öffnet, über den dann datei1 asgewählt
	 * werden kann.
	 */
	public JButton suche1;

	/**
	 * JButton, der einen FileChooser öffnet, über den dann datei2 asgewählt
	 * werden kann.
	 */
	public JButton suche2;

	/**
	 * JButton, der einen FileChooser öffnet, über den dann datei3 asgewählt
	 * werden kann.
	 */
	public JButton suche3;

	/**
	 * Konstruktor für ein Importfenster mit zugewiesener (Haupt-)GUI-Klasse
	 * 
	 * @param gui
	 *            GUI-Klasse, die dem Importfenster zugewiesen werden soll
	 */
	public ImportFenster(GUI gui, boolean modal) {
		super(gui, modal);
		this.gui = gui;
		this.setTitle("Importieren");
		dateiFormat = new JComboBox<String>(formate);
		dateiFormat.setSelectedIndex(0);

		final JLabel label1 = new JLabel("Wahlkreise und Stimmen: ");
		final JLabel label2 = new JLabel("Kandidaten: ");
		final JLabel label3 = new JLabel("Bundesländer: ");

		final JLabel format = new JLabel("Dateiformat: ");

		fc = new JFileChooser();
		File f = new File("./src/main/resources/");
		fc.setCurrentDirectory(f);
		fc.setAcceptAllFileFilterUsed(false);
		filter = new FileFilter() {

			@Override
			public boolean accept(File arg0) {
				return arg0.isDirectory()
						|| arg0.getName().toLowerCase().endsWith(".csv");
			}

			@Override
			public String getDescription() {
				return "*.csv";
			}

		};
		fc.setFileFilter(filter);

		// Folgendes dient dazu, die Anzahl der einzulesenden Dateien zu
		// variieren, falls ein anderes Dateiformat unterstützt werden soll,
		// für
		// das mehr / weniger Dateien eingelesen werden müssen
		dateiFormat.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				switch (dateiFormat.getSelectedItem().toString()) {
					case "CSV mit erweiterten Landeslisten":
					case "CSV":
						label1.setEnabled(true);
						label2.setEnabled(true);
						label3.setEnabled(true);
						eingabe1.setEnabled(true);
						eingabe2.setEnabled(true);
						eingabe3.setEnabled(true);
						suche1.setEnabled(true);
						suche2.setEnabled(true);
						suche3.setEnabled(true);
						filter = new FileFilter() {

							@Override
							public boolean accept(File arg0) {
								return arg0.isDirectory()
										|| arg0.getName().toLowerCase()
												.endsWith(".csv");
							}

							@Override
							public String getDescription() {
								return "*.csv";
							}

						};
						break;
					default:
						break;
				}

				fc.setFileFilter(filter);
			}
		});

		eingabe1 = new JTextField();
		eingabe1.setEditable(false);
		eingabe2 = new JTextField();
		eingabe2.setEditable(false);
		eingabe3 = new JTextField();
		eingabe3.setEditable(false);

		suche1 = new JButton("Suchen...");
		suche2 = new JButton("Suchen...");
		suche3 = new JButton("Suchen...");

		suche1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				fc.setVisible(true);
				if (isModal()) {
					fc.showOpenDialog(null);
				}
				if (fc.getSelectedFile() != null) {
					datei1 = fc.getSelectedFile();
				}
				eingabe1.setText(datei1.toString());

				fc.setSelectedFile(null);
				fc.setVisible(false);
			}
		});

		suche2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				fc.setVisible(true);
				if (isModal()) {
					fc.showOpenDialog(null);
				}
				if (fc.getSelectedFile() != null) {
					datei2 = fc.getSelectedFile();
				}
				eingabe2.setText(datei2.toString());
				fc.setSelectedFile(null);
				fc.setVisible(false);
			}
		});

		suche3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				fc.setVisible(true);
				if (isModal()) {
					fc.showOpenDialog(null);
				}
				if (fc.getSelectedFile() != null) {
					datei3 = fc.getSelectedFile();
				}
				eingabe3.setText(datei3.toString());
				fc.setSelectedFile(null);
				fc.setVisible(false);
			}
		});

		importieren = new JButton("Importieren");
		importieren.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				ImportFenster.this.importieren();
				ImportFenster.this.gui.setEnabled(true);
			}
		});

		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());

		Container cp1 = new Container();
		Container cp2 = new Container();
		cp1.setLayout(new GridLayout(1, 2));
		cp2.setLayout(new GridLayout(3, 3));

		cp1.add(format);
		cp1.add(dateiFormat);
		cp2.add(label1);
		cp2.add(eingabe1);
		cp2.add(suche1);
		cp2.add(label2);
		cp2.add(eingabe2);
		cp2.add(suche2);
		cp2.add(label3);
		cp2.add(eingabe3);
		cp2.add(suche3);

		contentPane.add(cp1, BorderLayout.NORTH);
		contentPane.add(cp2, BorderLayout.CENTER);
		contentPane.add(importieren, BorderLayout.SOUTH);

		Dimension dim = new Dimension(500, 170);
		this.setPreferredSize(dim);
		this.setMinimumSize(dim);

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * Veranlasst den Import einer Wahl
	 */
	public void importieren() {

		// Wenn zu wenige Dateien ausgewählt wurden, wird eine Fehlermeldung
		// angezeigt, die dies dem Benutzer mitteilt.
		if ((datei1 == null) || (datei2 == null) || (datei3 == null)) {
			if (isModal()) {
				JOptionPane
						.showMessageDialog(
								null,
								"Es wurden zu wenige Dateien ausgewählt. \n"
										+ "Bitte die erforderliche Anzahl an Dateien auswählen.",
								"Es ist ein Fehler aufgetreten!",
								JOptionPane.ERROR_MESSAGE);
			}
		} else {

			// Wurden ausreichend viele Dateien ausgewählt, so wird versucht,
			// diese zu importieren
			try {

				WahldatenImporter importer = null;

				// Folgendes dient der Erweiterbarkeit; insbesondere dazu, das
				// Hinzufügen weiterer unterstützter Dateiformate zu
				// vereinfachen
				switch (dateiFormat.getSelectedItem().toString()) {
					case "CSV":
						importer = new CSVWahldatenImporter(datei1, datei2,
								datei3);
						break;
					case "CSV mit erweiterten Landeslisten":
						importer = new CSVWahldatenImporter(datei1, datei2,
								datei3);
						importer.setFabrik(new CSVParserFabrikMitAutomatischenKandidaten());
						break;
					default:
						break;
				}

				gui.neueWahl(importer.importieren());
				this.setVisible(false);
				this.dispose();

			} catch (IOException e) {
				// Entsprechen die ausgewählten Dateien nicht dem erforderlichen
				// Format, so wird eine Fehlermeldung angezeigt.
				if (isModal()) {
					JOptionPane.showMessageDialog(null, e.getMessage(),
							"Es ist ein Fehler aufgetreten!",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				if (isModal()) {
					JOptionPane.showMessageDialog(null,
							"Die Zeilenläge einer der Dateien ist zu groß!",
							"Es ist ein Fehler aufgetreten!",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception e) {
				if (isModal()) {
					JOptionPane.showMessageDialog(null,
							"Es ist ein unbekannter Fehler aufgetreten!",
							"Es ist ein Fehler aufgetreten!",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}

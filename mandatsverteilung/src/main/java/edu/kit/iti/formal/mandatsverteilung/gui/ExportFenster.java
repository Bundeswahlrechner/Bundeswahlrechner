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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.jfree.data.category.CategoryDataset;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.exporter.CSVExporter;

/**
 * Exportfenster, über das der Benutzer Wahlen exportieren kann
 * 
 * @author Peter, Pascal
 * 
 */
public class ExportFenster extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name der verschiedenen Möglichkeiten die exportiert werden können
	 */
	private final String[] auswahl = { "Daten exportieren",
			"Grafik exportieren" };

	public Container contentPane;
	private CSVExporter csvExporter;
	/**
	 * Datei mit Ergebnissen einer {@link Bundestagswahl}, die erstellt werden
	 * soll.
	 */
	protected File datei1;
	/**
	 * Textfeld, das den Pfad von datei1 enthält
	 */
	protected JTextField eingabe1;
	/**
	 * Zum Exportieren von Stabdiagrammen.
	 */
	private final EinWahlStabdiagrammAnsicht EWSA;
	/**
	 * JComboBox mit der das zu exprtierende Format ausgewählt werden kann.
	 */
	protected JComboBox<String> exportAuswahl;
	/**
	 * JButton, über den den Export gestartet wird.
	 */
	protected JButton exportieren;

	/**
	 * JFileChooser über den die pfade der zu exportierenden Dateien ausgewählt
	 * werden können.
	 */
	protected final JFileChooser fc;
	/**
	 * Die zugehörige {@link GUI}
	 */
	private final GUI gui;

	/**
	 * JButton, der einen FileChooser öffnet, über den dann der Pfad zu datei1
	 * asgewählt werden kann.
	 */
	public JButton suchen1;

	/**
	 * Konstruktor für ein Exportfenster mit zugewiesener (Haupt-)GUI-Klasse
	 * 
	 * @param gui
	 *            GUI-Klasse, die dem Importfenster zugewiesen werden soll
	 */

	public ExportFenster(GUI gui, boolean modal) {
		super(gui, "Exportieren", modal);
		this.gui = gui;
		// this.setTitle();
		contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		EWSA = new EinWahlStabdiagrammAnsicht(gui.aktiveWahl());

		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		final JLabel label1 = new JLabel("Ziel: ");

		eingabe1 = new JTextField();
		eingabe1.setEditable(false);

		exportieren = new JButton("Exportieren");
		exportieren.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				exportiereWahl();
			}
		});
		suchen1 = new JButton("Suchen...");
		suchen1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				fc.setVisible(true);
				if (isModal()) {
					fc.showOpenDialog(null);
				}
				if (fc.getSelectedFile() != null) {
					datei1 = fc.getSelectedFile();
				}
				eingabe1.setText(fc.getSelectedFile().toString());
				fc.setVisible(false);
			}

		});

		exportAuswahl = new JComboBox<String>(auswahl);
		exportAuswahl.setSelectedIndex(0);

		Container cp1 = new Container();
		cp1.setLayout(new GridLayout(1, 3));
		cp1.add(label1);
		cp1.add(eingabe1);
		cp1.add(suchen1);

		contentPane.add(exportAuswahl, BorderLayout.NORTH);
		contentPane.add(cp1, BorderLayout.CENTER);
		contentPane.add(exportieren, BorderLayout.SOUTH);

		Dimension dim = new Dimension(300, 120);
		this.setPreferredSize(dim);
		this.setMinimumSize(dim);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * Veranlasst den Export der Wahl im gerade ausgewählten Tab der GUI.
	 * 
	 */
	private void exportiereWahl() {
		if (datei1 != null) {
			if (datei1.isDirectory()) {
				csvExporter = new CSVExporter(gui.aktiveWahl());
				if (exportAuswahl.getSelectedIndex() == 0) {
					csvExporter.exportiereCSVDatei(datei1.getPath());
				} else {
					CategoryDataset dataset = EWSA.erstelleDatensatz(
							EWSA.bundestagsDaten, EWSA.bundestagsKopfZeile, 1);
					try {
						csvExporter.exportierePNG(EWSA.erstelleDiagramm(
								dataset, "Bundestag", "parteien", "Sitze"),
								new FileOutputStream(datei1.getPath()
										+ File.separator
										+ "BundestagsGrafik.png"), 500, 500);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					dataset = EWSA.erstelleDatensatz(EWSA.bundeslaenderDaten,
							EWSA.bundeslaenderKopfZeile, 2);
					try {
						csvExporter.exportierePNG(EWSA.erstelleDiagramm(
								dataset, "Bundesländer", "parteien", "Sitze"),
								new FileOutputStream(datei1.getPath()
										+ File.separator
										+ "BundeslaenderGrafik.png"), 500, 500);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			} else {
				JOptionPane.showMessageDialog(null,
						"Es muss nur der Ordner in den sie "
								+ "exportieren möchten angegeben werden");
			}
		}
		this.setVisible(false);
		this.dispose();
	}
}

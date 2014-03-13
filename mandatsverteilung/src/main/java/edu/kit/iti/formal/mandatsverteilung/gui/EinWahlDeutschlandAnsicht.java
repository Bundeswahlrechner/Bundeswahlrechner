package edu.kit.iti.formal.mandatsverteilung.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kreiswahlvorschlag;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Landesliste;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Wahlkreis;

/**
 * Visuelle Darstellung des Ergebnisses einer {@link Bundestagswahl}. Diese
 * {@link EinWahlAnsicht} besteht aus einer {@link Deutschlandkarte}, bei der
 * die Bundesländer jeweils in der Farbe der {@link Partei} mit den meisten
 * Zweitstimmen gefärbt ist, und zusätzlich aus einer {@code Deutschlandkarte}
 * pro {@code Partei}, bei der jeweils die Bundesländer in den Farben der
 * jeweiligen {@code Partei} gefärbt sind.
 * 
 * @author Peter Steinmetz
 * 
 */
public class EinWahlDeutschlandAnsicht extends EinWahlAnsicht {

	/**
	 * Private Klasse, die die Ergebnisse einer {@link Bundestagswahl} in einer
	 * Deutschlandkarte visualisiert, bei der die Budesländer verschieden
	 * eingefärbt werden können.
	 * 
	 * @author Peter Steinmetz
	 * 
	 */
	private class Deutschlandkarte extends JLayeredPane {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Die Bundesländer der {@link Bundestagswahl}
		 */
		private final Bundesland[] bundeslaender;

		/**
		 * Die Bilder der Bundesländer
		 */
		private final BufferedImage[] laender;

		/**
		 * Die dargestellte {@link Bundestagswahl}
		 */
		private final Bundestagswahl wahl;

		/**
		 * Erzeugt eine Deutschlandkarte bei der die Bundesländer jeweils in der
		 * Farbe der {@link Partei} mit den meisten Zweitstimmen gefärbt sind.
		 * 
		 * @param wahl
		 *            die {@link Bundestagswahl}, deren Ergebnis visualisiert
		 *            wird
		 */
		protected Deutschlandkarte(Bundestagswahl wahl) {
			this.wahl = wahl;
			bundeslaender = new Bundesland[wahl.bundeslaenderAnzahl()];
			laender = new BufferedImage[bundeslaender.length];
			initialisierePNGs();

			JLabel hintergrund = new JLabel(new ImageIcon(
					GUI.class.getResource("/Hintergrund.png")));
			hintergrund.setBounds(0, 0, hintergrund.getIcon().getIconWidth(),
					hintergrund.getIcon().getIconHeight());
			this.add(hintergrund, new Integer(0));
			JLabel[] schicht = new JLabel[laender.length];

			for (int i = 0; i < laender.length; i++) {
				schicht[i] = new JLabel(new ImageIcon(setzeGewinnerFarbe(
						laender[i], bundeslaender[i])));
				schicht[i].setBounds(0, 0, laender[i].getWidth(),
						laender[i].getHeight());
				this.add(schicht[i], new Integer(i + 1));
			}

			this.setPreferredSize(hintergrund.getSize());
			this.setVisible(true);
		}

		/**
		 * Erzeugt eine Deutschlandkarte, bei der die Bundesländer in den Farben
		 * einer {@link Partei} gefärbt sind, wobei die Deckkraft jeweils von
		 * der Anzahl der Zweitstimmen im jeweiligen {@link Bundesland} abhängt.
		 * Ist eine {@link Partei} in einem {@link Bundesland} nicht vertreten,
		 * bzw. hat keine Zweitstimmen erhalten, so bleibt das Bundesland weiß.
		 * 
		 * @param wahl
		 *            zu visualisiernde {@link Bundestagswahl}
		 * @param partei
		 *            {@link Partei}, deren Farben die Karte erhalten soll
		 */
		protected Deutschlandkarte(Bundestagswahl wahl, Partei partei) {
			this.wahl = wahl;
			bundeslaender = new Bundesland[wahl.bundeslaenderAnzahl()];
			laender = new BufferedImage[bundeslaender.length];
			initialisierePNGs();
			JLabel[] schicht = new JLabel[laender.length];
			JLabel hintergrund = new JLabel(new ImageIcon(
					GUI.class.getResource("/Hintergrund.png")));
			hintergrund.setBounds(0, 0, hintergrund.getIcon().getIconWidth(),
					hintergrund.getIcon().getIconHeight());
			this.add(hintergrund, new Integer(0));
			for (int i = 0; i < laender.length; i++) {
				schicht[i] = new JLabel(new ImageIcon(nachParteiFaerben(
						laender[i], bundeslaender[i], partei)));
				schicht[i].setBounds(0, 0, laender[i].getWidth(),
						laender[i].getHeight());
				this.add(schicht[i], new Integer(i + 1));
			}

			this.setPreferredSize(hintergrund.getSize());
			this.setVisible(true);
		}

		/**
		 * Initialisiert die Bundesländer und die zugehörigen Bilder
		 */
		private void initialisierePNGs() {
			try {

				// initialisiere Baden-Württemberg
				laender[0] = ImageIO.read(EinWahlDeutschlandAnsicht.class
						.getResourceAsStream("/BW.png"));
				bundeslaender[0] = wahl.getBundeslandAbk("BW");

				// initialisiere Bayern
				laender[1] = ImageIO.read(GUI.class
						.getResourceAsStream("/Bayern.png"));
				bundeslaender[1] = wahl.getBundeslandAbk("BY");

				// initialisiere Schleswig-Holstein
				laender[2] = ImageIO.read(GUI.class
						.getResourceAsStream("/SH.png"));
				bundeslaender[2] = wahl.getBundeslandAbk("SH");

				// initialisiere Hamburg
				laender[3] = ImageIO.read(GUI.class
						.getResourceAsStream("/Hamburg.png"));
				bundeslaender[3] = wahl.getBundeslandAbk("HH");

				// initialisiere Niedersachsen
				laender[4] = ImageIO.read(GUI.class
						.getResourceAsStream("/Niedersachsen.png"));
				bundeslaender[4] = wahl.getBundeslandAbk("NI");

				// initialisiere Bremen
				laender[5] = ImageIO.read(GUI.class
						.getResourceAsStream("/Bremen.png"));
				bundeslaender[5] = wahl.getBundeslandAbk("HB");

				// initialisiere Rheinland-Pfalz
				laender[6] = ImageIO.read(GUI.class
						.getResourceAsStream("/RP.png"));
				bundeslaender[6] = wahl.getBundeslandAbk("RP");

				// initialisiere Saarland
				laender[7] = ImageIO.read(GUI.class
						.getResourceAsStream("/Saarland.png"));
				bundeslaender[7] = wahl.getBundeslandAbk("SL");

				// initialisiere Berlin
				laender[8] = ImageIO.read(GUI.class
						.getResourceAsStream("/Berlin.png"));
				bundeslaender[8] = wahl.getBundeslandAbk("BE");

				// initialisiere Brandenburg
				laender[9] = ImageIO.read(GUI.class
						.getResourceAsStream("/Brandenburg.png"));
				bundeslaender[9] = wahl.getBundeslandAbk("BB");

				// initialisiere Mecklenburg-Vorpommern
				laender[10] = ImageIO.read(GUI.class
						.getResourceAsStream("/MVP.png"));
				bundeslaender[10] = wahl.getBundeslandAbk("MV");

				// initialisiere Sachsen
				laender[11] = ImageIO.read(GUI.class
						.getResourceAsStream("/Sachsen.png"));
				bundeslaender[11] = wahl.getBundeslandAbk("SN");

				// initialisiere Sachsen-Anhalt
				laender[12] = ImageIO.read(GUI.class
						.getResourceAsStream("/Sachsen-A.png"));
				bundeslaender[12] = wahl.getBundeslandAbk("ST");

				// initialisiere Thüringen
				laender[13] = ImageIO.read(GUI.class
						.getResourceAsStream("/Thuringen.png"));
				bundeslaender[13] = wahl.getBundeslandAbk("TH");

				// initialisiere Nordrhein-Westfalen
				laender[14] = ImageIO.read(GUI.class
						.getResourceAsStream("/NRW.png"));
				bundeslaender[14] = wahl.getBundeslandAbk("NW");

				// initialisiere Hessen
				laender[15] = ImageIO.read(GUI.class
						.getResourceAsStream("/Hessen.png"));
				bundeslaender[15] = wahl.getBundeslandAbk("HE");

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		/**
		 * Färbt ein {@link Bundesland} in den Farben einer {@link Partei},
		 * wobei die Deckkraft von der jeweiligen Zweitstimmenzahl abhängt. Ist
		 * eine {@link Partei} in einem {@link Bundesland} nicht vertreten, bzw.
		 * hat keine Zweitstimmen erhalten, so bleibt das Bundesland weiß.
		 * 
		 * @param partei
		 *            {@link Partei} deren Farbe das Bild erhalten soll
		 * @return gefärbtes Bild
		 */
		private BufferedImage nachParteiFaerben(BufferedImage bild,
				Bundesland bundesland, Partei partei) {
			// Das Bild muss nur neu gefärbt werden, wenn dei Partei im
			// Bundesland eine Landesliste hat.
			if (partei.getLandesliste(bundesland) != null) {

				int stimmenAnzahl = 0;
				Iterator<Landesliste> it = bundesland.landeslistenIterator();
				while (it.hasNext()) {
					stimmenAnzahl += it.next().berechneZweitstimmen();
				}
				double faktor = 0;
				if (stimmenAnzahl > 0) {
					// Der Faktor kann evtl noch verändert werden.
					faktor = (2.7 * (partei.getLandesliste(bundesland)
							.berechneZweitstimmen())) / (stimmenAnzahl);
				}
				if (faktor > 1) {
					faktor = 1;
				}

				Color farbe = partei.getFarbe();
				// verändere den Alphawert der Farbe der Partei in Abhängigkeit
				// des prozentualen Anteils an Zweitstimmen der Partei im
				// Bundesland
				Color neueFarbe = new Color(farbe.getRed(), farbe.getGreen(),
						farbe.getBlue(), (int) Math.round(farbe.getAlpha()
								* faktor));

				// Iteriere über das Bild und färbe alle weißen Pixel
				for (int j = 0; j < bild.getHeight(); j++) {
					for (int i = 0; i < bild.getWidth(); i++) {
						if (bild.getRGB(i, j) == Color.WHITE.getRGB()) {
							bild.setRGB(i, j, neueFarbe.getRGB());
						}

					}
				}
			}
			return bild;
		}

		/**
		 * Färbt ein Bild in der Farbe der {@link Partei}, die in einem
		 * {@link Bundesland} die meisten Zweitstimmen erhalten hat.
		 * 
		 * @param bild
		 *            das Bild ds gefärbt werden soll
		 * @param bundesland
		 *            das betrachtete {@link Bundesland}
		 * @return gefärtes Bild
		 */
		private BufferedImage setzeGewinnerFarbe(BufferedImage bild,
				Bundesland bundesland) {
			int max = 0;
			int gewinnerIndex = 0;
			for (int i = 0; i < parteien.length; i++) {
				if (parteien[i].getLandesliste(bundesland) != null) {
					int wert = parteien[i].getLandesliste(bundesland)
							.berechneZweitstimmen();
					if (wert > max) {
						gewinnerIndex = i;
						max = wert;
					}
				}
			}

			Color farbe = parteien[gewinnerIndex].getFarbe();

			// Iteriere über das Bild und färbe alle weißen Pixel
			for (int j = 0; j < bild.getHeight(); j++) {
				for (int i = 0; i < bild.getWidth(); i++) {
					if (bild.getRGB(i, j) == Color.WHITE.getRGB()) {
						bild.setRGB(i, j, farbe.getRGB());
					}
				}
			}

			return bild;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Die im Bundestag vertretenen {@link Partei}en
	 */
	private Partei[] parteien;

	final protected JTabbedPane tabs;
	protected JButton zeigeWahlkreiskarteButton;

	/**
	 * Erzeugt eine neue EinWahlDeutschlandAnsicht, der eine
	 * {@link Bundestagswahl} zugewiesen wird.
	 * <p>
	 * Eine EinWahlDeutschlandAnsicht besteht aus einer {@link Deutschlandkarte}
	 * , bei der die Bundesländer jeweils in der Farbe der {@link Partei} mit
	 * den meisten Zweitstimmen gefärbt ist, und aus einer
	 * {@link Deutschlandkarte} pro {@link Partei}, bei der jeweils die
	 * Bundesländer in den Farben der jeweiligen {@linkPartei} gefärbt sind.
	 * 
	 * @param wahl
	 *            zuzuweisende {@link Bundestagswahl}
	 */
	public EinWahlDeutschlandAnsicht(final Bundestagswahl wahl,
			final boolean inBrowserOeffnen) {
		super(wahl);
		tabs = new JTabbedPane();

		Collection<Partei> parteiCollection = wahl.eingezogeneParteien();

		parteien = parteiCollection
				.toArray(new Partei[parteiCollection.size()]);
		tabs.addTab("Gewinneransicht", null, new Deutschlandkarte(wahl),
				"Zu diesem Tab wechseln");

		for (int i = 0; i < parteien.length; i++) {
			tabs.addTab(parteien[i].getName(), null, null,
					"Zu diesem Tab wechseln");
		}

		tabs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (tabs.getSelectedComponent() == null) {
					int index = tabs.getSelectedIndex();
					tabs.setComponentAt(index, new Deutschlandkarte(wahl,
							parteien[index - 1]));
				}
			}

		});

		tabs.setTabPlacement(JTabbedPane.RIGHT);
		tabs.setBounds(0, 0, 1000, 640);
		this.add(tabs);
		this.setBounds(0, 0, 1000, 640);
		this.setSize(1000, 640);
		this.setLayout(new BorderLayout());
		this.add(tabs, BorderLayout.CENTER);

		zeigeWahlkreiskarteButton = new JButton("Zeige Wahlkreiskarte");
		String prefix = "?";
		String parameter = "";
		Iterator<Wahlkreis> wahlkreisIterator = wahl.wahlkreisIterator();
		while (wahlkreisIterator.hasNext()) {
			Wahlkreis wahlkreis = wahlkreisIterator.next();
			Kreiswahlvorschlag gewinner = wahlkreis.getGewinner();
			Partei partei = gewinner.getPartei();
			if (partei != null) {
				Color color = partei.getFarbe();
				String hr = Integer.toHexString(color.getRed());
				String hg = Integer.toHexString(color.getGreen());
				String hb = Integer.toHexString(color.getBlue());
				if (hr.length() == 1) {
					hr += "0";
				}
				if (hg.length() == 1) {
					hg += "0";
				}
				if (hb.length() == 1) {
					hb += "0";
				}
				String value = hr + hg + hb;
				int key = wahlkreis.getNummer();
				parameter = parameter + prefix + key + "=" + value;
				prefix = "&";
			}
		}

		final String finalParameter = parameter;
		zeigeWahlkreiskarteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Temporäre Datei anlegen (Windows Workaround)
				File temp;
				try {
					temp = File.createTempFile("karte", ".html");
				} catch (IOException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage(),
							"Temporäre Datei konnte nicht angelegt werden!",
							JOptionPane.ERROR_MESSAGE);
					e2.printStackTrace();
					return;
				}
				temp.deleteOnExit();
				// Karten-URL erstellen
				// Falls die App in Eclipse ausgeführt wird, finde Datei im
				// resources Ordner
				File karte = new File(
						"src/main/resources/wahlkreiskarte/index.html");
				if (!karte.exists()) {
					// Falls die App als JAR ausgeführt wird: auf die externe
					// Wahlkreiskarte zurückgreifen
					// da der Browser nicht auf Dateien im JAR zugreifen kann
					karte = new File("external/wahlkreiskarte/index.html");
				}
				String url = karte.toURI().toString() + finalParameter;
				// URL in temporäre Datei speichern
				BufferedWriter writer;
				try {
					writer = new BufferedWriter(new FileWriter(temp));
					String source = "<script language=\"javascript\" type=\"text/javascript\">window.location.href=\""
							+ url + "\";</script>";
					writer.write(source);
					writer.close();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(),
							"Temporäre Datei konnte nicht geschrieben werden!",
							JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					return;
				}
				// Browser öffnen
				try {
					if (inBrowserOeffnen) {
						Desktop.getDesktop().browse(temp.toURI()); // open the
																	// default
																	// web
																	// browser
																	// (don't
																	// open in
																	// test
																	// cases)
					}
				} catch (IOException exception) {
					JOptionPane.showMessageDialog(null, exception.getMessage(),
							"Es ist ein Fehler aufgetreten!",
							JOptionPane.ERROR_MESSAGE);
					exception.printStackTrace();
					return;
				}
			}
		});
		this.add(zeigeWahlkreiskarteButton, BorderLayout.SOUTH);

		this.setVisible(true);
	}
}

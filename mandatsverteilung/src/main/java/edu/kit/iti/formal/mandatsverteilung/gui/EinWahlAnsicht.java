package edu.kit.iti.formal.mandatsverteilung.gui;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kandidat;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kreiswahlvorschlag;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Landesliste;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Wahlkreis;

/**
 * Oberklasse der EinwahlAnsichten. Speichert alle wichtigen Daten der Wahl in
 * spezifischen Arrays, die von den bestimmten Ansichten wieder ausgelesen
 * werden.
 * 
 * @author Patrick Stöckle
 * @version 1.0
 * 
 */
public class EinWahlAnsicht extends JPanel {
	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Doppeltes Objectarray mit den Daten der Bundeslaenderansicht.
	 */
	protected Object[][] bundeslaenderDaten;
	/**
	 * Objectarray mit der Kopfzeile der Bundeslaenderansicht.
	 */
	protected Object[] bundeslaenderKopfZeile;
	/**
	 * Hilfsattribut, um schnell auf die Bundeslaender zugreifen zu koenne.
	 */
	private List<Bundesland> bundeslaenderListe;

	/**
	 * Doppeltes Objectarray mit den Daten der Bundestagsansicht.
	 */
	protected Object[][] bundestagsDaten;
	/**
	 * Objectarray mit der Kopfzeile der Bundestagsansicht.
	 */
	protected Object[] bundestagsKopfZeile;

	/**
	 * Doppeltes Objectarray mit den Daten der Kandidatenansicht.
	 */
	protected Object[][] kandidatDaten;
	/**
	 * Objectarray mit der Kopfzeile der Kandidatenansicht..
	 */
	protected Object[] kandidatKopfZeile;

	/**
	 * Hilfsattribut, um schnell auf die Abgeordneten zugreifen zu koenne.
	 */
	private List<Kandidat> kandidatListe;
	/**
	 * Hilfsattribut, um schnell auf die Parteien zugreifen zu koenne.
	 */
	protected final List<Partei> parteienListe;

	/**
	 * Doppeltes Objectarray mit den Daten der Prozentansicht.
	 */
	protected Object[][] prozentansichtDaten;
	/**
	 * Objectarray mit der Kopfzeile der Prozentansicht.
	 */
	protected Object[] prozentansichtKopfZeile;

	/**
	 * Doppeltes Objectarray mit den Daten der Wahlkreisansicht.
	 */
	protected Object[][] wahlkreisDaten;
	/**
	 * Doppeltes Objectarray mit den Daten der Wahlkreiserststimmenansicht.
	 */
	protected Object[][] wahlkreisErststimmen;

	/**
	 * Objectarray mit der Kopfzeile der Wahlkreiserststimmen.
	 */
	protected Object[] wahlkreisErststimmenKopfzeile;

	/**
	 * Doppeltes Objectarray mit den Daten der Wahlkreisgewinneransicht.
	 */
	protected Object[][] wahlkreisGewinnerDaten;
	/**
	 * Objectarray mit der Kopfzeile der Wahlkreisgewinneransicht.
	 */
	protected Object[] wahlkreisGewinnerKopfZeile;
	/**
	 * Objectarray mit der Kopfzeile der Wahlkreisansicht.
	 */
	protected Object[] wahlkreisKopfZeile;
	/**
	 * Hilfsattribut, um schnell auf die Wahlkreise zugreifen zu koenne.
	 */
	private List<Wahlkreis> wahlkreisListe;

	/**
	 * Erstellt die Struktur für eine Wahlansicht. Dazu werden die verschiedenen
	 * Kopfzeilen und Daten initialisiert.
	 * 
	 * @param wahl
	 *            Bundestagswahl
	 */
	public EinWahlAnsicht(Bundestagswahl wahl) {

		// Erstelle das Parteiarray
		parteienListe = (List<Partei>) wahl.eingezogeneParteien();
		Collections.sort(parteienListe);
		if (wahl.getParteiloseMandatstraeger() != 0) {
			parteienListe.add(new Partei("Parteilose"));
		}

		initialisiereListen(wahl);
		// Initialisiere die parameter
		int wahlkreisAnzahl = wahl.wahlkreisAnzahl();
		int bundeslaenderAnzahl = wahl.bundeslaenderAnzahl();
		int parteienAnzahl = parteienListe.size();
		int sitzZahl = wahl.getSitzzahl();
		int stimmenanzahl = wahl.berechneZweitstimmen();

		initialisiereKopfZeilen(parteienAnzahl, wahlkreisAnzahl,
				bundeslaenderAnzahl);
		initialisiereWahlkreisDaten(wahlkreisAnzahl, parteienAnzahl);
		initialisiereBundeslaenderDaten(bundeslaenderAnzahl, parteienAnzahl);
		initialisiereBundestagsDaten(parteienAnzahl, sitzZahl);
		initialisiereWahlkreisGewinnerDaten(wahlkreisAnzahl);
		initialisiereKandidatDaten(sitzZahl);
		initialisiereProzentansichtsDaten(stimmenanzahl);
		if (wahl.getParteiloseMandatstraeger() != 0) {
			bundestagsDaten[0][bundestagsDaten[0].length - 1] = wahl
					.getParteiloseMandatstraeger();
			for (int i = 0; i < bundeslaenderAnzahl; i++) {
				bundeslaenderDaten[i][bundeslaenderDaten[i].length - 1] = bundeslaenderListe
						.get(i).getParteiloseMandatstraeger();
			}
		}
	}

	/**
	 * Konstruktor für Vergleiche.
	 * 
	 * @param wahl
	 *            Wahl
	 * @param parteienListe
	 *            Parteien im Vergleich
	 */
	public EinWahlAnsicht(Bundestagswahl wahl, List<Partei> parteienListe) {
		this.parteienListe = new LinkedList<>();
		boolean parteilose = false;
		for (Partei partei : parteienListe) {
			if (partei.getName().equals("Parteilose")) {
				parteilose = true;
				this.parteienListe.add(new Partei("Parteilose"));
			}
			Iterator<Partei> parteienIterator = wahl.parteienIterator();
			while (parteienIterator.hasNext()) {
				Partei parteiInWahl = parteienIterator.next();
				if (parteiInWahl.getName().equals(partei.getName())) {
					this.parteienListe.add(parteiInWahl);

				}
			}
		}

		// Initialisiere die parameter
		int wahlkreisAnzahl = wahl.wahlkreisAnzahl();
		int bundeslaenderAnzahl = wahl.bundeslaenderAnzahl();
		int parteienAnzahl = this.parteienListe.size();
		int sitzZahl = wahl.getSitzzahl();
		int stimmenanzahl = wahl.berechneZweitstimmen();

		initialisiereListen(wahl);
		initialisiereKopfZeilen(parteienAnzahl, wahlkreisAnzahl,
				bundeslaenderAnzahl);
		initialisiereWahlkreisDaten(wahlkreisAnzahl, parteienAnzahl);
		initialisiereBundeslaenderDaten(bundeslaenderAnzahl, parteienAnzahl);
		initialisiereBundestagsDaten(parteienAnzahl, sitzZahl);
		initialisiereWahlkreisGewinnerDaten(wahlkreisAnzahl);
		initialisiereKandidatDaten(sitzZahl);
		initialisiereProzentansichtsDaten(stimmenanzahl);
		if (parteilose) {
			bundestagsDaten[0][bundestagsDaten[0].length - 1] = wahl
					.getParteiloseMandatstraeger();
			for (int i = 0; i < bundeslaenderAnzahl; i++) {
				bundeslaenderDaten[i][bundeslaenderDaten[i].length - 1] = bundeslaenderListe
						.get(i).getParteiloseMandatstraeger();
			}

		}
	}

	/**
	 * Initialisiert das Bundeslaenderdaten Objectarray. Dabei steht jede Zeile
	 * fuer ein Bundesland. Die erste Spalte ist die Bundeslandnummer, die
	 * zweite Spalte der Bundeslandname und die folgenden Spalten die Sitzzahl
	 * der jeweiligen Partei.
	 * 
	 * @param bundeslaenderAnzahl
	 *            Anzahl der Bundeslaender
	 * @param parteienAnzahl
	 *            Anzahl der Parteien
	 */
	private void initialisiereBundeslaenderDaten(int bundeslaenderAnzahl,
			int parteienAnzahl) {
		bundeslaenderDaten = new Object[bundeslaenderAnzahl][];
		Iterator<Bundesland> bundeslandIterator = bundeslaenderListe.iterator();
		Iterator<Partei> parteienIterator;

		for (int i = 0; i < bundeslaenderAnzahl; i++) {
			Bundesland bl = bundeslandIterator.next();
			bundeslaenderDaten[i] = new Object[parteienAnzahl + 2];
			bundeslaenderDaten[i][0] = i + 1;
			bundeslaenderDaten[i][1] = bl.getName();

			parteienIterator = parteienListe.iterator();
			for (int j = 0; j < parteienAnzahl; j++) {
				Partei partei = parteienIterator.next();
				Landesliste ll = partei.getLandesliste(bl);
				Integer temp = 0;
				if (ll != null) {
					temp = ll.getUnterverteilungSitzzahl();
				} else {
					temp = partei.wahlkreisgewinnerInBundesland(bl);

				}
				bundeslaenderDaten[i][j + 2] = temp;
			}
		}
	}

	/**
	 * Initialisiert das Bundestags Objectarray. Eine Zeile. Die erste Spalte
	 * enthält die Gesamtsitzzahl ,die restlichen die Sitzzahlen der jeweiligen
	 * Parteien.
	 * 
	 * @param parteienAnzahl
	 *            Anzahl der Parteien
	 */
	private void initialisiereBundestagsDaten(int parteienAnzahl, int sitzzahl) {
		bundestagsDaten = new Object[1][];
		Iterator<Partei> parteienIterator = parteienListe.iterator();
		bundestagsDaten[0] = new Object[parteienAnzahl + 1];

		bundestagsDaten[0][0] = sitzzahl;
		for (int i = 0; i < parteienAnzahl; i++) {
			Partei partei = parteienIterator.next();
			bundestagsDaten[0][i + 1] = partei.getOberverteilungSitzzahl();
		}
	}

	/**
	 * Initialisiert die Kandidatsdaten. Für jeden Sitz im Bundestag eine Zeile.
	 * 1. Spalte: Nummer des Mandats. 2. Spalte: Name des Abgeordneten. 3.
	 * Spalte: Art des Mandats. 4. Spalte: Landesliste oder Name des
	 * Wahlkreises.
	 */
	private void initialisiereKandidatDaten(int sitzzahl) {
		kandidatDaten = new Object[sitzzahl][];
		Iterator<Kandidat> kandidatenIterator = kandidatListe.iterator();

		for (int i = 0; i < sitzzahl; i++) {
			kandidatDaten[i] = new Object[5];
			// provisorischer Bugfix
			// es sollte keine leeren Stellen in der Tabelle geben
			// anscheinend enthält die kandidatenListe zu wenige Abgeordnete
			// TODO{
			Kandidat kandidat = kandidatenIterator.next();
			kandidatDaten[i][0] = i + 1;
			kandidatDaten[i][1] = kandidat.getName();
			if (kandidat.hatAusgleichsmandat()) {
				kandidatDaten[i][2] = "Ausgleichsmandat";
			} else {
				if (kandidat.istWahlkreisGewinner()) {
					kandidatDaten[i][2] = "Direktmandat";
				} else {
					kandidatDaten[i][2] = "Landeslistenplatz";
				}
			}
			if (kandidat.istWahlkreisGewinner()) {
				Kreiswahlvorschlag kreiswahlvorschlag = (Kreiswahlvorschlag) kandidat;
				if (kreiswahlvorschlag.getPartei() != null) {
					kandidatDaten[i][3] = kreiswahlvorschlag.getPartei()
							.getName();
				} else {
					kandidatDaten[i][3] = "Parteilose";
				}
				kandidatDaten[i][4] = kreiswahlvorschlag.getWahlkreis()
						.getName();
			} else {
				kandidatDaten[i][3] = kandidat.getLandesliste().getPartei()
						.getName();
				kandidatDaten[i][4] = kandidat.getLandesliste().getBundesland()
						.getName();
			}
		}

	}

	/**
	 * Initialisiert die Kopfzeilen. wahlkreisKopfZeile:
	 * Wahlkreisnummer;Wahlkreisname;Partei1;... bundeslaenderKopfZeile:
	 * Bundeslandnummer;Bundeslandname;Partei1;... bundestagsKopfZeile:
	 * Gesamtsitzzahl;Partei1; ...;.
	 * wahlkreisGewinnerKopfZeile:Wahlkreisnummer;Wahlkreisname;Gewinner;Partei;
	 * kandidatKopfZeile: Nummer;Name;Mandatsart;Landesliste/Wahlkreis;
	 * 
	 * @param parteienAnzahl
	 *            Anzahl der Parteien
	 * @param wahlkreisAnzahl
	 *            Anzahl der Wahlkreise
	 * @param bundeslaenderAnzahl
	 *            Anzahl der Bundeslaender
	 */
	private void initialisiereKopfZeilen(int parteienAnzahl,
			int wahlkreisAnzahl, int bundeslaenderAnzahl) {
		wahlkreisKopfZeile = new Object[parteienAnzahl + 2];
		wahlkreisErststimmenKopfzeile = new Object[parteienAnzahl + 2];
		wahlkreisGewinnerKopfZeile = new Object[4];
		kandidatKopfZeile = new Object[5];
		bundeslaenderKopfZeile = new Object[parteienAnzahl + 2];
		bundestagsKopfZeile = new Object[parteienAnzahl + 1];
		prozentansichtKopfZeile = new Object[parteienAnzahl + 1];

		wahlkreisKopfZeile[0] = "Wahlkreisnummer";
		wahlkreisKopfZeile[1] = "Wahlkreisname";

		wahlkreisErststimmenKopfzeile[0] = "Wahlkreisnummer";
		wahlkreisErststimmenKopfzeile[1] = "Wahlkreisname";

		bundeslaenderKopfZeile[0] = "Bundeslandnummer";
		bundeslaenderKopfZeile[1] = "Bundeslandname";

		bundestagsKopfZeile[0] = "Gesamtsitzzahl";
		prozentansichtKopfZeile[0] = "Gesamtstimmen";

		wahlkreisGewinnerKopfZeile[0] = "Wahlkreisnummer";
		wahlkreisGewinnerKopfZeile[1] = "Wahlkreisname";
		wahlkreisGewinnerKopfZeile[2] = "Gewinner";
		wahlkreisGewinnerKopfZeile[3] = "Partei";

		kandidatKopfZeile[0] = "Nummer";
		kandidatKopfZeile[1] = "Name";
		kandidatKopfZeile[2] = "Mandatsart";
		kandidatKopfZeile[3] = "Partei";
		kandidatKopfZeile[4] = "Bundesland/Wahlkreis";

		Iterator<Partei> parteienIterator = parteienListe.iterator();
		for (int i = 0; i < parteienAnzahl; i++) {
			String parteiName = parteienIterator.next().getName();
			wahlkreisKopfZeile[i + 2] = parteiName;
			wahlkreisErststimmenKopfzeile[i + 2] = parteiName;
			bundeslaenderKopfZeile[i + 2] = parteiName;
			bundestagsKopfZeile[i + 1] = parteiName;
			prozentansichtKopfZeile[i + 1] = parteiName;
		}

	}

	private void initialisiereListen(Bundestagswahl wahl) {
		wahlkreisListe = new LinkedList<>();
		bundeslaenderListe = new LinkedList<>();
		kandidatListe = new LinkedList<>();

		// Erstelle die Kandidatliste
		Iterator<Kandidat> kIterator = wahl.getAbgeordnete().iterator();
		while (kIterator.hasNext()) {
			Kandidat kandidat = kIterator.next();
			kandidatListe.add(kandidat);
		}

		// Erstelle die Wahlkreisliste
		Iterator<Wahlkreis> wahlkreisIterator = wahl.wahlkreisIterator();
		while (wahlkreisIterator.hasNext()) {
			Wahlkreis wahlkreis = wahlkreisIterator.next();
			wahlkreisListe.add(wahlkreis);
		}
		Collections.sort(wahlkreisListe);

		// Erstelle die Bundeslaenderliste
		Iterator<Bundesland> bundeslandIterator = wahl.bundeslaenderIterator();
		while (bundeslandIterator.hasNext()) {
			Bundesland bundesland = bundeslandIterator.next();
			bundeslaenderListe.add(bundesland);
		}
		Collections.sort(bundeslaenderListe);
	}

	private void initialisiereProzentansichtsDaten(int stimmenanzahl) {
		prozentansichtDaten = new Object[1][];
		Iterator<Partei> parteienIterator = parteienListe.iterator();
		prozentansichtDaten[0] = new Object[parteienListe.size() + 1];

		prozentansichtDaten[0][0] = stimmenanzahl;
		int i = 0;
		while (parteienIterator.hasNext()) {
			Partei partei = parteienIterator.next();
			prozentansichtDaten[0][i + 1] = ((double) partei
					.berechneZweitstimmen()) / ((double) stimmenanzahl) * 100;
			i++;
		}
	}

	/**
	 * Initialisiert das Wahlkreisdaten array. Eine Zeile für jeden Wahlkreis.
	 * <ol>
	 * <li>Spalte: Wahlkreisnummer
	 * <li>Spalte: Wahlkreisname
	 * <li>Spalte: Partei1
	 * <li>...
	 * </ol>
	 * 
	 * 
	 * @param wahlkreisAnzahl
	 *            Anzahl der Wahlkreise.
	 * @param parteienAnzahl
	 *            Anzahl der Parteien.
	 */
	private void initialisiereWahlkreisDaten(int wahlkreisAnzahl,
			int parteienAnzahl) {
		wahlkreisErststimmen = new Object[wahlkreisAnzahl][];
		wahlkreisDaten = new Object[wahlkreisAnzahl][];
		Iterator<Wahlkreis> wahlkreisIterator = wahlkreisListe.iterator();
		Iterator<Partei> parteienIterator = parteienListe.iterator();

		for (int i = 0; i < wahlkreisAnzahl; i++) {
			Wahlkreis wk = wahlkreisIterator.next();

			wahlkreisDaten[i] = new Object[parteienAnzahl + 2];
			wahlkreisErststimmen[i] = new Object[parteienAnzahl + 2];

			wahlkreisDaten[i][0] = wk.getNummer();
			wahlkreisErststimmen[i][0] = wk.getNummer();

			wahlkreisDaten[i][1] = wk.getName();
			wahlkreisErststimmen[i][1] = wk.getName();

			parteienIterator = parteienListe.iterator();
			for (int j = 0; j < parteienAnzahl; j++) {
				Partei p = parteienIterator.next();
				wahlkreisDaten[i][j + 2] = wk.getParteiZweitstimmen(p);
				boolean fehlt = true;
				Iterator<Kreiswahlvorschlag> kreIterator = wk
						.getWahlkreisBewerber().iterator();
				while (kreIterator.hasNext() && fehlt) {
					Kreiswahlvorschlag kreiswahlvorschlag = kreIterator.next();
					if (p.equals(kreiswahlvorschlag.getPartei())) {
						wahlkreisErststimmen[i][j + 2] = kreiswahlvorschlag
								.getStimmen();
						fehlt = false;
					}
				}
				if (wahlkreisErststimmen[i][j + 2] == null) {
					wahlkreisErststimmen[i][j + 2] = 0;
				}

			}
		}
	}

	/**
	 * Initialisiert das Wahlkreisgewinnerarray. Eine Zeile für jeden Wahlkreis.
	 * <ol>
	 * <li>Spalte: Wahlkreisnummer
	 * <li>Spalte: Wahlkreisname
	 * <li>Spalte: Name
	 * <li>Spalte: Partei
	 * </ol>
	 * 
	 * 
	 * @param wahlkreisAnzahl
	 *            Anzahl der Wahlkreise
	 */
	private void initialisiereWahlkreisGewinnerDaten(int wahlkreisAnzahl) {
		wahlkreisGewinnerDaten = new Object[wahlkreisAnzahl][];
		Iterator<Wahlkreis> wahlkreisIterator = wahlkreisListe.iterator();

		for (int i = 0; i < wahlkreisAnzahl; i++) {
			Wahlkreis wk = wahlkreisIterator.next();
			wahlkreisGewinnerDaten[i] = new Object[4];
			wahlkreisGewinnerDaten[i][0] = wk.getNummer();
			wahlkreisGewinnerDaten[i][1] = wk.getName();
			wahlkreisGewinnerDaten[i][2] = wk.getGewinner().getName();
			if (wk.getGewinner().getPartei() != null) {
				wahlkreisGewinnerDaten[i][3] = wk.getGewinner().getPartei()
						.getName();
			} else {
				wahlkreisGewinnerDaten[i][3] = "Parteilose";
			}

		}

	}
}

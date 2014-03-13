package edu.kit.iti.formal.mandatsverteilung.gui;

import java.util.List;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;

/**
 * Oberklasse der NWahlVergleichsAnsichten. Speichert alle wichtigen Daten der
 * Wahlen in spezifischen Arrays, die von den bestimmten Ansichten wieder
 * ausgelesen werden.
 * 
 * @author Patrick Stöckle
 * @version 1.0
 * 
 */
public class NWahlenVergleichsAnsicht extends Vergleichsansicht {
	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Wahlansichten.
	 */
	protected final EinWahlAnsicht[] wahlAnsichten;

	/**
	 * Konstruktor.
	 * 
	 * @param wahlen
	 *            zu vergleichende Wahlen
	 * @param parteienListe
	 *            Parteien im Vergleich
	 */
	public NWahlenVergleichsAnsicht(Bundestagswahl[] wahlen,
			List<Partei> parteienListe) {
		wahlAnsichten = new EinWahlAnsicht[wahlen.length];
		for (int i = 0; i < wahlen.length; i++) {
			wahlAnsichten[i] = new EinWahlAnsicht(wahlen[i], parteienListe);
		}
		int wahlkreisAnzahl = wahlen[0].wahlkreisAnzahl();
		int bundeslaenderAnzahl = wahlen[0].bundeslaenderAnzahl();
		for (int i = 0; i < wahlen.length; i++) {
			if (wahlen[i].wahlkreisAnzahl() != wahlkreisAnzahl) {
				throw new IllegalArgumentException(
						"Anzahl der Wahlkreise ist unterschiedlich!");
			}
			if (wahlen[i].bundeslaenderAnzahl() != bundeslaenderAnzahl) {
				throw new IllegalArgumentException(
						"Anzahl der Bundesländer ist unterschiedlich!");
			}
		}
		initialisiereWahlkreisGewinnerTabelle();
		initialisiereWahlkreiseTabelle();
		initialisiereBundeslaenderTabelle();
		initialisiereBundestagsTablle();
	}

	/**
	 * Initialisiert das Bundeslaenderdaten Objectarray. Dabei steht jede Zeile
	 * fuer ein Bundesland. Die erste Spalte ist die Bundeslandnummer, die
	 * zweite Spalte der Bundeslandname und die folgenden Spalten die Sitzzahl
	 * der jeweiligen Partei für jede Wahl.
	 */
	private void initialisiereBundeslaenderTabelle() {
		bundeslaenderVergleichsDaten = new Object[wahlAnsichten[0].bundeslaenderDaten.length][];
		bundeslaenderVergleichsKopfZeile = new Object[2
				+ (wahlAnsichten[0].bundeslaenderKopfZeile.length - 2)
				* wahlAnsichten.length];
		bundeslaenderVergleichsKopfZeile[0] = "Bundeslandnummer";
		bundeslaenderVergleichsKopfZeile[1] = "Bundeslandname";

		int spalte = 2;
		int wahl = 0;
		int partei = 2;
		while (spalte < bundeslaenderVergleichsKopfZeile.length) {
			bundeslaenderVergleichsKopfZeile[spalte] = "Wahl " + (wahl + 1)
					+ ": " + wahlAnsichten[wahl].bundeslaenderKopfZeile[partei];
			spalte++;
			wahl++;
			if (wahl == wahlAnsichten.length) {
				wahl = 0;
				partei++;
			}
		}
		for (int i = 0; i < bundeslaenderVergleichsDaten.length; i++) {
			bundeslaenderVergleichsDaten[i] = new Object[2
					+ (wahlAnsichten[0].bundeslaenderKopfZeile.length - 2)
					* wahlAnsichten.length];
			bundeslaenderVergleichsDaten[i][0] = wahlAnsichten[0].bundeslaenderDaten[i][0];
			bundeslaenderVergleichsDaten[i][1] = wahlAnsichten[0].bundeslaenderDaten[i][1];
			spalte = 2;
			wahl = 0;
			partei = 2;
			while (spalte < bundeslaenderVergleichsDaten[i].length) {
				bundeslaenderVergleichsDaten[i][spalte] = wahlAnsichten[wahl].bundeslaenderDaten[i][partei];
				spalte++;
				wahl++;
				if (wahl == wahlAnsichten.length) {
					wahl = 0;
					partei++;
				}
			}
		}

	}

	/**
	 * Initialisiert das Bundestags Objectarray. Eine Zeile. Die erste Spalte
	 * enthält die Gesamtsitzzahl ,die restlichen die Sitzzahlen der jeweiligen
	 * Parteien in den jeweiligen Wahlen.
	 */
	private void initialisiereBundestagsTablle() {
		bundestagsVergleichsDaten = new Object[1][];
		bundestagsVergleichsKopfZeile = new Object[wahlAnsichten[0].bundestagsKopfZeile.length
				* wahlAnsichten.length];
		int spalte = 0;
		int wahl = 0;
		int partei = 0;
		while (spalte < bundestagsVergleichsKopfZeile.length) {
			bundestagsVergleichsKopfZeile[spalte] = "Wahl " + (wahl + 1) + ": "
					+ wahlAnsichten[wahl].bundestagsKopfZeile[partei];
			spalte++;
			wahl++;
			if (wahl == wahlAnsichten.length) {
				wahl = 0;
				partei++;
			}
		}
		for (int i = 0; i < bundestagsVergleichsDaten.length; i++) {
			bundestagsVergleichsDaten[i] = new Object[wahlAnsichten[0].bundestagsKopfZeile.length
					* wahlAnsichten.length];
			bundestagsVergleichsDaten[i][0] = wahlAnsichten[0].bundestagsDaten[i][0];
			spalte = 0;
			wahl = 0;
			partei = 0;
			while (spalte < bundestagsVergleichsDaten[i].length) {
				bundestagsVergleichsDaten[i][spalte] = wahlAnsichten[wahl].bundestagsDaten[i][partei];
				spalte++;
				wahl++;
				if (wahl == wahlAnsichten.length) {
					wahl = 0;
					partei++;
				}
			}
		}

	}

	/**
	 * Initialisiert das Wahlkreisdaten array. Eine Zeile für jeden Wahlkreis.
	 * <ol>
	 * <li>Spalte: Wahlkreisnummer
	 * <li>Spalte: Wahlkreisname
	 * <li>Spalte: Wahl1: Partei1
	 * <li>Spalte: Wahl2: Partei1
	 * <li>...
	 * </ol>
	 */
	private void initialisiereWahlkreiseTabelle() {
		wahlkreisVergleichsDaten = new Object[wahlAnsichten[0].wahlkreisDaten.length][];
		wahlkreisVergleichsKopfzeile = new Object[2
				+ (wahlAnsichten[0].wahlkreisKopfZeile.length - 2)
				* wahlAnsichten.length];
		wahlkreisVergleichsKopfzeile[0] = wahlAnsichten[0].wahlkreisKopfZeile[0];
		wahlkreisVergleichsKopfzeile[1] = wahlAnsichten[0].wahlkreisKopfZeile[1];
		int spalte = 2;
		int wahl = 0;
		int partei = 2;
		while (spalte < wahlkreisVergleichsKopfzeile.length) {

			wahlkreisVergleichsKopfzeile[spalte] = "Wahl " + (wahl + 1) + ": "
					+ wahlAnsichten[wahl].wahlkreisKopfZeile[partei];
			spalte++;
			wahl++;
			if (wahl == wahlAnsichten.length) {
				wahl = 0;
				partei++;
			}
		}
		for (int i = 0; i < wahlkreisVergleichsDaten.length; i++) {
			wahlkreisVergleichsDaten[i] = new Object[2
					+ (wahlAnsichten[0].wahlkreisKopfZeile.length - 2)
					* wahlAnsichten.length];
			wahlkreisVergleichsDaten[i][0] = wahlAnsichten[0].wahlkreisDaten[i][0];
			wahlkreisVergleichsDaten[i][1] = wahlAnsichten[0].wahlkreisDaten[i][1];
			spalte = 2;
			wahl = 0;
			partei = 2;
			while (spalte < wahlkreisVergleichsDaten[i].length) {
				wahlkreisVergleichsDaten[i][spalte] = wahlAnsichten[wahl].wahlkreisDaten[i][partei];
				spalte++;
				wahl++;
				if (wahl == wahlAnsichten.length) {
					wahl = 0;
					partei++;
				}
			}
		}
	}

	/**
	 * Initialisiert das Wahlkreisgewinnerarray. Eine Zeile für jeden Wahlkreis.
	 * <ol>
	 * <li>Spalte: Wahlkreisnummer
	 * <li>Spalte: Wahlkreisname
	 * <li>Spalte: Wahl1: Name
	 * <li>Spalte: Wahl1: Partei
	 * <li>Spalte: Wahl2: Name
	 * <li>...
	 * </ol>
	 */
	private void initialisiereWahlkreisGewinnerTabelle() {
		wahlkreisGewinnerVergleichsDaten = new Object[wahlAnsichten[0].wahlkreisGewinnerDaten.length][];
		wahlkreisGewinnerVergleichsKopfZeile = new Object[2 + (wahlAnsichten.length * 2)];
		wahlkreisGewinnerVergleichsKopfZeile[0] = wahlAnsichten[0].wahlkreisGewinnerKopfZeile[0];
		wahlkreisGewinnerVergleichsKopfZeile[1] = wahlAnsichten[0].wahlkreisGewinnerKopfZeile[1];
		for (int i = 0; i < wahlAnsichten.length; i++) {
			wahlkreisGewinnerVergleichsKopfZeile[2 + i * 2] = "Wahl " + (i + 1)
					+ ": " + wahlAnsichten[i].wahlkreisGewinnerKopfZeile[2];
			wahlkreisGewinnerVergleichsKopfZeile[3 + i * 2] = "Wahl " + (i + 1)
					+ ": " + wahlAnsichten[i].wahlkreisGewinnerKopfZeile[3];
		}
		for (int i = 0; i < wahlkreisGewinnerVergleichsDaten.length; i++) {
			wahlkreisGewinnerVergleichsDaten[i] = new Object[2 + (wahlAnsichten.length * 2)];
			wahlkreisGewinnerVergleichsDaten[i][0] = wahlAnsichten[0].wahlkreisGewinnerDaten[i][0];
			wahlkreisGewinnerVergleichsDaten[i][1] = wahlAnsichten[0].wahlkreisGewinnerDaten[i][1];
			for (int j = 0; j < wahlAnsichten.length; j++) {
				wahlkreisGewinnerVergleichsDaten[i][j * 2 + 2] = wahlAnsichten[j].wahlkreisGewinnerDaten[i][2];
				wahlkreisGewinnerVergleichsDaten[i][j * 2 + 3] = wahlAnsichten[j].wahlkreisGewinnerDaten[i][3];
			}
		}
	}

}

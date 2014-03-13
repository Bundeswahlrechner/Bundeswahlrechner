package edu.kit.iti.formal.mandatsverteilung.gui;

import java.util.List;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;

/**
 * Oberklasse der ZweiWahlVergleichsAnsichten. Speichert alle wichtigen Daten
 * der Wahlen in spezifischen Arrays, die von den bestimmten Ansichten wieder
 * ausgelesen werden.
 * 
 * @author Patrick Stöckle
 * @version 1.0
 * 
 */
public abstract class ZweiWahlenVergleichsAnsicht extends Vergleichsansicht {
	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Erste Wahl.
	 */
	protected final EinWahlAnsicht wahlAnsicht1;
	/**
	 * Zweite Wahl.
	 */
	protected final EinWahlAnsicht wahlAnsicht2;

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
	public ZweiWahlenVergleichsAnsicht(Bundestagswahl wahl1,
			Bundestagswahl wahl2, List<Partei> parteienList) {
		wahlAnsicht1 = new EinWahlAnsicht(wahl1, parteienList);
		wahlAnsicht2 = new EinWahlAnsicht(wahl2, parteienList);
		if (wahl2.wahlkreisAnzahl() != wahl1.wahlkreisAnzahl()) {
			throw new IllegalArgumentException(
					"Anzahl der Wahlkreise ist unterschiedlich!");
		}
		if (wahl1.bundeslaenderAnzahl() != wahl2.bundeslaenderAnzahl()) {
			throw new IllegalArgumentException(
					"Anzahl der Bundesländer ist unterschiedlich!");
		}

		initialisiereBundeslaenderTabelle();
		initialisiereBundestagsTablle();
		initialisiereWahlkreiseTabelle();
		initialisiereWahlkreisGewinnerTabelle();
	}

	/**
	 * Initialisiert das Bundeslaenderdaten Objectarray. Dabei steht jede Zeile
	 * fuer ein Bundesland. Die erste Spalte ist die Bundeslandnummer, die
	 * zweite Spalte der Bundeslandname und die folgenden Spalten die Sitzzahl
	 * der jeweiligen Partei für jede Wahl.
	 */
	private void initialisiereBundeslaenderTabelle() {
		Object[][] bundeslaenderDaten1 = wahlAnsicht1.bundeslaenderDaten;
		Object[][] bundeslaenderDaten2 = wahlAnsicht2.bundeslaenderDaten;

		Object[] bundeslaenderKopfZeile1 = wahlAnsicht1.bundeslaenderKopfZeile;
		Object[] bundeslaenderKopfZeile2 = wahlAnsicht2.bundeslaenderKopfZeile;

		bundeslaenderVergleichsDaten = new Object[bundeslaenderDaten1.length][];
		bundeslaenderVergleichsKopfZeile = new Object[bundeslaenderKopfZeile1.length * 3 - 4];
		bundeslaenderVergleichsKopfZeile[0] = "Bundeslandnummer";
		bundeslaenderVergleichsKopfZeile[1] = "Bundeslandname";

		for (int i = 0; i < bundeslaenderKopfZeile1.length - 2; i++) {
			bundeslaenderVergleichsKopfZeile[i * 3 + 2] = "Wahl 1: "
					+ bundeslaenderKopfZeile1[i + 2];
			bundeslaenderVergleichsKopfZeile[i * 3 + 3] = "Wahl 2: "
					+ bundeslaenderKopfZeile2[i + 2];
			bundeslaenderVergleichsKopfZeile[i * 3 + 4] = "Differenz: "
					+ bundeslaenderKopfZeile2[i + 2];
		}
		for (int i = 0; i < bundeslaenderVergleichsDaten.length; i++) {
			bundeslaenderVergleichsDaten[i] = new Object[bundeslaenderKopfZeile1.length * 3 - 4];
			bundeslaenderVergleichsDaten[i][0] = bundeslaenderDaten1[i][0];
			bundeslaenderVergleichsDaten[i][1] = bundeslaenderDaten1[i][1];

			for (int j = 0; j < bundeslaenderDaten1[i].length - 2; j++) {
				bundeslaenderVergleichsDaten[i][j * 3 + 2] = bundeslaenderDaten1[i][j + 2];
				bundeslaenderVergleichsDaten[i][j * 3 + 3] = bundeslaenderDaten2[i][j + 2];
				Integer dif = ((Integer) bundeslaenderDaten2[i][j + 2])
						- (Integer) bundeslaenderDaten1[i][j + 2];
				bundeslaenderVergleichsDaten[i][j * 3 + 4] = dif;
			}
		}

	}

	/**
	 * Initialisiert das Bundestags Objectarray. Eine Zeile. Die erste Spalte
	 * enthält die Gesamtsitzzahl ,die restlichen die Sitzzahlen der jeweiligen
	 * Parteien in den jeweiligen Wahlen.
	 */
	private void initialisiereBundestagsTablle() {
		Object[][] bundestagsDaten1 = wahlAnsicht1.bundestagsDaten;
		Object[][] bundestagsDaten2 = wahlAnsicht2.bundestagsDaten;

		Object[] bundestagsKopfzeile1 = wahlAnsicht1.bundestagsKopfZeile;
		Object[] bundestagsKopfzeile2 = wahlAnsicht2.bundestagsKopfZeile;

		bundestagsVergleichsDaten = new Object[bundestagsDaten1.length][];
		bundestagsVergleichsKopfZeile = new Object[bundestagsKopfzeile1.length * 3];

		for (int i = 0; i < bundestagsKopfzeile1.length; i++) {
			bundestagsVergleichsKopfZeile[i * 3] = "Wahl 1: "
					+ bundestagsKopfzeile1[i];
			bundestagsVergleichsKopfZeile[i * 3 + 1] = "Wahl 2: "
					+ bundestagsKopfzeile2[i];
			bundestagsVergleichsKopfZeile[i * 3 + 2] = "Differenz: "
					+ bundestagsKopfzeile2[i];
		}
		for (int i = 0; i < bundestagsVergleichsDaten.length; i++) {
			bundestagsVergleichsDaten[i] = new Object[bundestagsDaten1[i].length * 3];
			for (int j = 0; j < bundestagsDaten1[i].length; j++) {
				bundestagsVergleichsDaten[i][j * 3] = bundestagsDaten1[i][j];
				bundestagsVergleichsDaten[i][j * 3 + 1] = bundestagsDaten2[i][j];
				Integer dif = ((Integer) bundestagsDaten2[i][j])
						- ((Integer) bundestagsDaten1[i][j]);
				bundestagsVergleichsDaten[i][j * 3 + 2] = dif;
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
		Object[][] wahlkreisDaten1 = wahlAnsicht1.wahlkreisDaten;
		Object[][] wahlkreisDaten2 = wahlAnsicht2.wahlkreisDaten;

		Object[] wahlkreisKopfZeile1 = wahlAnsicht1.wahlkreisKopfZeile;
		Object[] wahlkreisKopfZeile2 = wahlAnsicht2.wahlkreisKopfZeile;

		wahlkreisVergleichsDaten = new Object[wahlkreisDaten1.length][];
		wahlkreisVergleichsKopfzeile = new Object[wahlkreisKopfZeile1.length * 3 - 4];
		wahlkreisVergleichsKopfzeile[0] = wahlkreisKopfZeile1[0];
		wahlkreisVergleichsKopfzeile[1] = wahlkreisKopfZeile1[1];
		for (int i = 0; i < wahlkreisKopfZeile1.length - 2; i++) {
			wahlkreisVergleichsKopfzeile[i * 3 + 2] = "Wahl 1: "
					+ wahlkreisKopfZeile1[i + 2];
			wahlkreisVergleichsKopfzeile[i * 3 + 3] = "Wahl 2: "
					+ wahlkreisKopfZeile2[i + 2];
			wahlkreisVergleichsKopfzeile[i * 3 + 4] = "Differenz"
					+ wahlkreisKopfZeile2[i + 2];
		}
		for (int i = 0; i < wahlkreisVergleichsDaten.length; i++) {
			wahlkreisVergleichsDaten[i] = new Object[wahlkreisVergleichsKopfzeile.length];
			wahlkreisVergleichsDaten[i][0] = wahlkreisDaten1[i][0];
			wahlkreisVergleichsDaten[i][1] = wahlkreisDaten1[i][1];

			for (int j = 0; j < wahlkreisDaten1[i].length - 2; j++) {
				wahlkreisVergleichsDaten[i][j * 3 + 2] = wahlkreisDaten1[i][j + 2];
				wahlkreisVergleichsDaten[i][j * 3 + 3] = wahlkreisDaten2[i][j + 2];
				Integer dif = ((Integer) wahlkreisDaten2[i][j + 2])
						- ((Integer) wahlkreisDaten1[i][j + 2]);
				wahlkreisVergleichsDaten[i][j * 3 + 4] = dif;
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
		Object[][] wahlkreisGewinnerDaten1 = wahlAnsicht1.wahlkreisGewinnerDaten;
		Object[][] wahlkreisGewinnerDaten2 = wahlAnsicht2.wahlkreisGewinnerDaten;

		Object[] wahlkreisGewinnerKopfZeile1 = wahlAnsicht1.wahlkreisGewinnerKopfZeile;
		Object[] wahlkreisGewinnerKopfZeile2 = wahlAnsicht2.wahlkreisGewinnerKopfZeile;

		wahlkreisGewinnerVergleichsDaten = new Object[wahlkreisGewinnerDaten1.length][];
		wahlkreisGewinnerVergleichsKopfZeile = new Object[6];
		int k = 0;
		wahlkreisGewinnerVergleichsKopfZeile[k] = wahlkreisGewinnerKopfZeile1[k++];
		wahlkreisGewinnerVergleichsKopfZeile[k] = wahlkreisGewinnerKopfZeile1[k++];
		wahlkreisGewinnerVergleichsKopfZeile[k] = "Wahl1: "
				+ wahlkreisGewinnerKopfZeile1[k++];
		wahlkreisGewinnerVergleichsKopfZeile[k] = wahlkreisGewinnerKopfZeile1[k++];

		wahlkreisGewinnerVergleichsKopfZeile[k++] = "Wahl2: "
				+ wahlkreisGewinnerKopfZeile2[2];
		wahlkreisGewinnerVergleichsKopfZeile[k] = wahlkreisGewinnerKopfZeile2[3];

		for (int i = 0; i < wahlkreisGewinnerVergleichsDaten.length; i++) {
			wahlkreisGewinnerVergleichsDaten[i] = new Object[wahlkreisGewinnerVergleichsKopfZeile.length];
			k = 0;
			wahlkreisGewinnerVergleichsDaten[i][k] = wahlkreisGewinnerDaten1[i][k++];
			wahlkreisGewinnerVergleichsDaten[i][k] = wahlkreisGewinnerDaten1[i][k++];
			wahlkreisGewinnerVergleichsDaten[i][k] = wahlkreisGewinnerDaten1[i][k++];
			wahlkreisGewinnerVergleichsDaten[i][k] = wahlkreisGewinnerDaten1[i][k++];
			wahlkreisGewinnerVergleichsDaten[i][k++] = wahlkreisGewinnerDaten2[i][2];
			wahlkreisGewinnerVergleichsDaten[i][k++] = wahlkreisGewinnerDaten2[i][3];
		}
	}
}

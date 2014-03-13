package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import java.util.Iterator;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;

/**
 * Modelliert ein Wahlverfahren mit Sperrklausel zur Berechnung des Ergebnisses
 * einer Bundestagswahl.
 * 
 * @author tillneuber
 * @version 1.1
 */
public abstract class WahlverfahrenMitSperrklausel extends Wahlverfahren {

	/**
	 * Der {@link Sperrklauselpruefer} der bei diesem
	 * {@code WahlverfahrenMitSperrklausel} eingesetzt wird.
	 */
	protected Sperrklauselpruefer spk;

	/**
	 * Erstellt ein neues Wahlverfahren mit Sperrklausel, dass die
	 * Ergebnisberechnung für die übergebene {@link Bundestagswahl} unter
	 * Zuhilfenahme des übergebenen {@link Zuteilungsverfahren}s ermöglicht.
	 * 
	 * Beachte: Bevor die Berechnung durchgeführt werden kann muss ein
	 * {@link Sperrklauselpruefer} mit der entsprechenden Methode festgelegt
	 * werden.
	 * 
	 * @param bundestagswahl
	 *            die {@code Bundestagswahl} die berechnet werden soll
	 * @param zuteilungsverfahren
	 *            das bei der Berechnung verwendete {@code Zuteilungsverfahren}
	 */
	protected WahlverfahrenMitSperrklausel(Bundestagswahl bundestagswahl,
			Zuteilungsverfahren zuteilungsverfahren) {
		super(bundestagswahl, zuteilungsverfahren);
	}

	/**
	 * Liefert den {@link Sperrklauselpruefer}, der in diesem
	 * {@code WahlverfahrenMitSperrklauselpruefer} verwendet wird.
	 * 
	 * @return der verwendete {@code Sperrklauselpruefer}
	 */
	public Sperrklauselpruefer getSperrklauselpruefer() {
		return spk;
	}

	/**
	 * Legt den {@link Sperrklauselpruefer} fest, der bei der Ergebnisberechnung
	 * eingesetzt wird.
	 * 
	 * @param spk
	 *            der eingesetzte {@code Sperrklauselpruefer}
	 */
	public void setzeSperrklauselpruefer(Sperrklauselpruefer spk) {
		this.spk = spk;
	}

	/**
	 * Führt die Überprüfung der Sperrklausel mittels des
	 * {@link Sperrklauselpruefer}s für alle {@link Partei}en der
	 * {@link Bundestagswahl} durch.
	 */
	protected void ueberpruefeSperrklausel() {
		// Iteriere über alle Parteien, überprüfe mittels der
		// Sperrklauselprüfers, ob die aktuell betrachtete Partei die
		// Sperrklausel überwunden hat und setze das entsprechende Attribut
		Iterator<Partei> it = this.getBundestagswahl().parteienIterator();
		while (it.hasNext()) {
			Partei partei = it.next();
			partei.setSperrklauselUeberwunden(spk
					.ueberwindetSperrklausel(partei));
		}
	}

}

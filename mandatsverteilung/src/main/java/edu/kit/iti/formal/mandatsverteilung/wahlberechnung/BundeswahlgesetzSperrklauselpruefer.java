package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;

/**
 * Der {@code BundeswahlgesetzSperrklauselpruefer} ermöglicht die Überprüfung
 * der im Bundeswahlgesetz definierten Sperrklausel für eine {@link Partei}.
 * 
 * @author tillneuber
 * @version 1.1
 */
public class BundeswahlgesetzSperrklauselpruefer implements Sperrklauselpruefer {

	/**
	 * Gibt die absolute Anzahl an Zweitstimmen an, die eine Partei benötigt, um
	 * die Prozenthürde zu überwinden.
	 */
	private int benoetigteZweitstimmen = Integer.MIN_VALUE;

	/**
	 * Gibt die Anzahl an Direktmandaten an, die eine Partei insgesamt benötigt,
	 * um die Grundmandatsklausel zu überwinden.
	 */
	private int grundmandate = 3;

	/**
	 * Die Höhe der Prozenthürde als Dezimalzahl.
	 */
	private double prozenthuerde = 0.05;

	/**
	 * Erzeugt einen neuen {@code BundeswahlgesetzSperrklauselpruefer} mit einer
	 * Prozenthürde in Höhe von 5% und 3 benötigten Direktmandaten zur
	 * Überwindung der Grundmandatsklausel.
	 */
	public BundeswahlgesetzSperrklauselpruefer() {
	}

	/**
	 * Erzeugt einen neuen {@code BundeswahlgesetzSperrklauselpruefer} mit einer
	 * Prozenthürde und Grundmandatsklausel in der spezifizierten Höhe.
	 * 
	 * @param prozenthuerde
	 *            die Höhe der Prozenthürde als Dezimalzahl (Bsp.: 5% wird als
	 *            0.05 übergeben)
	 * @param grundmandate
	 *            die zur Überwindung der Grundmandatsklausel nötigen
	 *            Direktmandate
	 */
	public BundeswahlgesetzSperrklauselpruefer(double prozenthuerde,
			int grundmandate) {
		this.prozenthuerde = prozenthuerde;
		this.grundmandate = grundmandate;
	}

	@Override
	public BundeswahlgesetzSperrklauselpruefer clone() {
		BundeswahlgesetzSperrklauselpruefer result = new BundeswahlgesetzSperrklauselpruefer();
		result.benoetigteZweitstimmen = benoetigteZweitstimmen;
		result.grundmandate = grundmandate;
		result.prozenthuerde = prozenthuerde;
		return result;
	}

	/**
	 * Gibt die Zahl an Grundmandaten zurück, die zur Überwindung der
	 * Sperrklausel nötig sind.
	 * 
	 * @return die benötigten Grundmandate
	 */
	public int getGrundmandate() {
		return grundmandate;
	}

	/**
	 * Liefert die Höhe der Prozenthürde dieses {@code Sperrklauselpruefer}s.
	 * Diese gibt an, wie viel Prozent der insgesamt abgegebenen Zweitstimmen
	 * eine Partei auf sich vereinen muss, um die Sperrklausel zu überwinden.
	 * 
	 * @return die Höhe der Prozenthürde
	 */
	public double getProzenthuerde() {
		return prozenthuerde;
	}

	/**
	 * Setzt die Zahl der insgesamt bei der Wahl abgegebenen, gültigen
	 * Zweitstimmen als Basis zur Überprüfung der Prozenthürde.
	 * 
	 * @param gesamteZweitstimmenzahl
	 *            insgesamt bei der Wahl abgegebene Zweitstimmenzahl
	 */
	public void setGesamteZweitstimmenzahl(int gesamteZweitstimmenzahl) {
		benoetigteZweitstimmen = (int) Math.ceil(prozenthuerde
				* gesamteZweitstimmenzahl);
	}

	/**
	 * Überprüft, ob die übergebene {@link Partei} die Sperrklausel überwindet.
	 * Dies ist der Fall, wenn die {@code Partei}
	 * <ul>
	 * <li>eine nationale Minderheit ist oder
	 * <li>die Prozenthürde überwindet (Anteil der Zweitstimmen einer Partei ist
	 * höher oder gleich dem durch die Höhe der Prozenthürde spezifizierten
	 * Anteil an der Gesamtzahl der Zweitstimmen) oder</li>
	 * <li>die Grundmandatsklausel überwindet (die Partei erreicht mindestens so
	 * viele Zweitstimmen wie durch die Höhe der Grundmandatsklausel
	 * festgelegt).</li>
	 * </ul>
	 * 
	 * @param partei
	 *            die {@code Partei}, für die die Überprüfung vorgenommen wird
	 * @return {@code true}, wenn die Sperrklausel überwunden wird, andernfalls
	 *         {@code false}
	 */
	@Override
	public boolean ueberwindetSperrklausel(Partei partei) {
		if (benoetigteZweitstimmen == Integer.MIN_VALUE) {
			throw new IllegalStateException(
					"setGesamteZweitstimmenzahl muss aufgerufen werden, damit benoetigteZweitstimmen korrekt ermittelt werden kann");
		}
		// Verschiedene Bedingungen für das Überwinden der Sperrklausel werden
		// aus Performancegründen in dieser Reihenfolge überprüft.
		return (partei.isNationaleMinderheit()
				|| (partei.berechneZweitstimmen() >= benoetigteZweitstimmen) || (partei
				.berechneDirektmandatszahl() >= grundmandate));
	}
}

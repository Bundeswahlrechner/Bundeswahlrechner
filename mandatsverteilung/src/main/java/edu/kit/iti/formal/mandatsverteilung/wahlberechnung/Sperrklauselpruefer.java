package edu.kit.iti.formal.mandatsverteilung.wahlberechnung;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;

/**
 * Implementierende Klassen stellen eine Methode bereit um zu überprüfen, ob
 * eine übergebene Partei die Sperrklausel überwindet.
 * 
 * @author tillneuber
 * @version 1.0
 */
public interface Sperrklauselpruefer extends Cloneable {

	public Sperrklauselpruefer clone();

	/**
	 * Überprüft, ob die übergebene {@link Partei} die Sperrklausel überwindet.
	 * 
	 * @param partei
	 *            die {@code Partei}, für die die Überprüfung vorgenommen wird
	 * @return {@code true}, wenn die Sperrklausel überwunden wird, andernfalls
	 *         {@code false}
	 */
	public boolean ueberwindetSperrklausel(Partei partei);

}

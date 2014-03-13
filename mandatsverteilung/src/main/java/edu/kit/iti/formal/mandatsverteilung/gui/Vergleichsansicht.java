package edu.kit.iti.formal.mandatsverteilung.gui;

import javax.swing.JPanel;

/**
 * Oberklasse der VergleichsAnsichten. Stellt die Struktur für die
 * Vergleichsansichten, die von den Unterklassen initialisiert und gefüllt
 * werden müssen.
 * 
 * 
 * @author Patrick Stöckle
 * @version 1.0
 * 
 */
public abstract class Vergleichsansicht extends JPanel {
	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Doppeltes Objectarray mit den Daten der Bundeslaenderansicht.
	 */
	protected Object[][] bundeslaenderVergleichsDaten;
	/**
	 * Objectarray mit der Kopfzeile der Bundeslaenderansicht.
	 */
	protected Object[] bundeslaenderVergleichsKopfZeile;
	/**
	 * Doppeltes Objectarray mit den Daten der Bundestagsansicht.
	 */
	protected Object[][] bundestagsVergleichsDaten;
	/**
	 * Objectarray mit der Kopfzeile der Bundestagsansicht.
	 */
	protected Object[] bundestagsVergleichsKopfZeile;
	/**
	 * Doppeltes Objectarray mit den Daten der Wahlkreisgewinneransicht.
	 */
	protected Object[][] wahlkreisGewinnerVergleichsDaten;
	/**
	 * Objectarray mit der Kopfzeile der Wahlkreisgewinneransicht.
	 */
	protected Object[] wahlkreisGewinnerVergleichsKopfZeile;
	/**
	 * Doppeltes Objectarray mit den Daten der Kandidatenansicht.
	 */
	protected Object[][] wahlkreisVergleichsDaten;
	/**
	 * Objectarray mit der Kopfzeile der Wahlkreisansicht.
	 */
	protected Object[] wahlkreisVergleichsKopfzeile;
}

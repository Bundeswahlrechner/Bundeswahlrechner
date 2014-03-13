package edu.kit.iti.formal.mandatsverteilung.datenhaltung;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Zuteilbar;

/**
 * Die Klasse Partei repräsentiert eine Partei, die bei einer Bundestagswahl zur
 * Wahl steht.
 */
public class Partei implements Cloneable, Zuteilbar, Comparable<Partei> {

	/**
	 * Die Anzahl der Ausgleichsmandate, die auf diese Partei Partei entfallen.
	 * Beachte: Diese Zahl ist u.U. geringer als die Zahl der als
	 * Ausgleichsmandatsträgern gekennzeichneten Kandidaten dieser Partei, da
	 * durch Ausgleichsmandate in einigen Bundesländer ggf. weniger
	 * Landeslistenkandidaten ein Mandat erhalten.
	 */
	private int ausgleichsmandate;

	private Color farbe;

	/**
	 * Die jeweilige Landesliste für die Bundesländern, in denen die Partei mit
	 * einer Landesliste antritt.
	 */
	private final Map<Bundesland, Landesliste> landeslisten;

	/** Die garantierte Mandatszahl, die dieser Partei zusteht. */
	private int mindestsitzzahl;

	/**
	 * Name der Partei. Sofern vorhanden die Kurzbezeichnung statt des
	 * vollständigen Namens. Beispiel: "CDU", nicht
	 * "Christlich Demokratische Union Deutschlands"
	 */
	private final String name;

	/**
	 * Angabe, ob es sich bei der Partei um eine nationale Minderheit im Sinne
	 * von § 6, Abs. 3, Satz 2 BWahlG handelt.
	 */
	private boolean nationaleMinderheit;

	/**
	 * Gibt die Oberverteilungssitzzahl an, die anschließend auf die
	 * Landeslisten verteilt wird. Beachte: Diese Zahl gibt am Ende der
	 * Berechnung alle Mandate an, die dieser Partei zustehen, inklusive der
	 * Wahlkreisgewinner ohne erfolgreiche Landesliste. Die Summe der
	 * Unterverteilungssitzzahlen muss also nicht zwangsläufig der
	 * Oberverteilungssitzzahl entsprechen.
	 */
	private int oberverteilungSitzzahl;

	/**
	 * Gibt an, ob diese Partei die Sperrklausel überwunden hat.
	 */
	private boolean sperrklauselUeberwunden;

	/**
	 * Erzeugt eine neue Partei.
	 * 
	 * @param name
	 *            - Name der Partei. Sofern vorhanden die Kurzbezeichnung statt
	 *            des vollständigen Namens.
	 */
	public Partei(String name) {
		super();
		this.name = name;
		landeslisten = new HashMap<Bundesland, Landesliste>();
	}

	public int berechneDirektmandatszahl() {
		int direktmandate = 0;
		for (Bundesland bl : landeslisten.keySet()) {
			direktmandate += wahlkreisgewinnerInBundesland(bl);
		}
		return direktmandate;
	}

	/**
	 * Berechnet die Anzahl der Zweitstimmen, die bundesweit auf die Partei
	 * entfallen.
	 * 
	 * @return Die Anzahl der Zweitstimmen, die bundesweit auf die Partei
	 *         entfallen.
	 */
	public int berechneZweitstimmen() {
		int zweitstimmen = 0;
		Iterator<Landesliste> iterator = landeslisten.values().iterator();
		while (iterator.hasNext()) {
			Landesliste landesliste = iterator.next();
			zweitstimmen += landesliste.berechneZweitstimmen();
		}
		return zweitstimmen;
	}

	@Override
	public Partei clone() {
		Partei result = new Partei(name);
		result.setFarbe(farbe);
		return result;
	}

	@Override
	public int compareTo(Partei o) {
		if (o == null) {
			return 0;
		}
		return o.oberverteilungSitzzahl - oberverteilungSitzzahl;
	}

	/**
	 * Liefert die Zahl an Ausgleichsmandaten, die diese {@code Partei} erhalten
	 * hat.
	 * <p>
	 * Beachte: Diese Zahl ist u.U. geringer als die Zahl der als
	 * Ausgleichsmandatsträgern gekennzeichneten Kandidaten dieser Partei, da
	 * durch Ausgleichsmandate in einigen Bundesländer ggf. weniger
	 * Landeslistenkandidaten ein Mandat erhalten.
	 * 
	 * @return die Zahl an Ausgleichsmandaten
	 */
	public int getAusgleichsmandate() {
		return ausgleichsmandate;
	}

	/**
	 * @return the farbe
	 */
	public Color getFarbe() {
		return farbe;
	}

	/**
	 * Gibt die Landesliste zurück, mit der die Partei im übergebenen Bundesland
	 * antritt, oder null, falls die Partei im übergebenen Bundesland nicht mit
	 * einer Landesliste antritt.
	 * 
	 * @param bundesland
	 *            - Das Bundesland, für das die Landesliste der Partei
	 *            zurückgegeben werden soll.
	 * @return Die Landesliste, mit der die Partei im Bundesland antritt, oder
	 *         null, falls sie das nicht tut.
	 */
	public Landesliste getLandesliste(Bundesland bundesland) {
		return landeslisten.get(bundesland);
	}

	/**
	 * @return the mindestsitzzahl
	 */
	public int getMindestsitzzahl() {
		return mindestsitzzahl;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the oberverteilungSitzzahl
	 */
	public int getOberverteilungSitzzahl() {
		return oberverteilungSitzzahl;
	}

	@Override
	public int getZuteilungswert() {
		return berechneZweitstimmen();
	}

	/**
	 * @return the nationaleMinderheit
	 */
	public boolean isNationaleMinderheit() {
		return nationaleMinderheit;
	}

	/**
	 * @return the sperrklauselUeberwunden
	 */
	public boolean isSperrklauselUeberwunden() {
		return sperrklauselUeberwunden;
	}

	/**
	 * Löscht diese Partei, sodass es von der GC aufgeräumt werden kann.
	 */
	public void loesche() {
		farbe = null;
		for (Landesliste l : landeslisten.values()) {
			l.loesche();
		}
	}

	/**
	 * Setzt die Zahl an Ausgleichsmandaten, die diese {@code Partei} bundesweit
	 * erhalten hat.
	 * <p>
	 * Beachte: Diese Zahl ist u.U. geringer als die Zahl der als
	 * Ausgleichsmandatsträgern gekennzeichneten Kandidaten dieser Partei, da
	 * durch Ausgleichsmandate in einigen Bundesländer ggf. weniger
	 * Landeslistenkandidaten ein Mandat erhalten.
	 * 
	 * @param agm
	 *            die Zahl an Ausgleichsmandaten
	 */
	public void setAusgleichsmandate(int agm) {
		ausgleichsmandate = agm;
	}

	/**
	 * @param farbe
	 *            the farbe to set
	 */
	public void setFarbe(Color farbe) {
		this.farbe = farbe;
	}

	public void setLandesliste(Bundesland bl, Landesliste liste) {
		landeslisten.put(bl, liste);
		liste.setPartei(this);
		liste.setBundesland(bl);
	}

	/**
	 * @param mindestsitzzahl
	 *            the mindestsitzzahl to set
	 */
	public void setMindestsitzzahl(int mindestsitzzahl) {
		this.mindestsitzzahl = mindestsitzzahl;
	}

	/**
	 * @param nationaleMinderheit
	 *            the nationaleMinderheit to set
	 */
	public void setNationaleMinderheit(boolean nationaleMinderheit) {
		this.nationaleMinderheit = nationaleMinderheit;
	}

	/**
	 * @param oberverteilungSitzzahl
	 *            the oberverteilungSitzzahl to set
	 */
	public void setOberverteilungSitzzahl(int oberverteilungSitzzahl) {
		this.oberverteilungSitzzahl = oberverteilungSitzzahl;
	}

	/**
	 * @param sperrklauselUeberwunden
	 *            the sperrklauselUeberwunden to set
	 */
	public void setSperrklauselUeberwunden(boolean sperrklauselUeberwunden) {
		this.sperrklauselUeberwunden = sperrklauselUeberwunden;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Berechnet die Zahl der Direktmandate, die die Partei im übergebenen
	 * Bundesland erringen konnte.
	 * 
	 * @param bundesland
	 *            - das Bundesland, für das die Zahl der durch die Partei
	 *            gewonnen Direktmandate gezählt werden soll
	 * @return Die Anzahl der Direktmandate, die die Partei im übergebenen
	 *         Bundesland erringen konnte
	 */
	public int wahlkreisgewinnerInBundesland(Bundesland bundesland) {
		int wahlkreisgewinner = 0;
		Iterator<Kreiswahlvorschlag> iterator = wahlkreisgewinnerIterator(bundesland);
		while (iterator.hasNext()) {
			iterator.next();
			wahlkreisgewinner++;
		}
		return wahlkreisgewinner;
	}

	public Iterator<Kreiswahlvorschlag> wahlkreisgewinnerIterator(
			Bundesland bundesland) {
		return new WahlkreisgewinnerIterator(bundesland.wahlkreisIterator(),
				this);
	}

}

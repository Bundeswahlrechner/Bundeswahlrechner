package edu.kit.iti.formal.mandatsverteilung.datenhaltung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Zuteilbar;

/**
 * Die Klasse Landesliste repräsentiert eine Landesliste, mit der eine Partei in
 * einem Bundesland antritt.
 */
public class Landesliste implements Zuteilbar {

	/** Das {@link Bundesland}, in dem diese {@code Landesliste} antritt. */
	private Bundesland bl;

	/**
	 * Die Liste aller Kandidaten auf der Landesliste.
	 */
	private final ArrayList<Kandidat> liste;

	/** Die {@link Partei} die zu der diese {@code Landesliste} gehört. */
	private Partei partei;

	/**
	 * Gibt die Pseudoverteilungssitzzahl dieser {@code Landesliste} an, die
	 * durch Verteilung des vorläufigen Sitzkontingents des {@link Bundesland}
	 * es, in dem diese {@code Landesliste} liegt, auf alle {@code Landesliste}n
	 * im {@code Bundeesland} errechnet wird. Die Pseudoverteilungssitzzahl
	 * dient der nachfolgenden Berechnung der Mindessitzzahlen.
	 */
	private int pseudoverteilungSitzzahl;

	/**
	 * Gibt die endgültige Zahl an Sitze an, die die {@link Partei} dieser
	 * {@code Landesliste} in dem {@link Bundesland} der {@code Landesliste}
	 * errungen hat.
	 * <p>
	 * Beachte: Diese Zahl enthält auch die Direktmandatsträger.
	 */
	private int unterverteilungSitzzahl;

	/**
	 * Bildet die Wahlkreise auf die Zweitstimmen ab, die im jeweiligen
	 * Wahlkreis für diese Landesliste abgegeben wurden.
	 */
	private final Map<Wahlkreis, Integer> wahlkreiszweitstimmen;

	/**
	 * Erzeugt eine neue, leere Landesliste.
	 */
	public Landesliste() {
		super();
		liste = new ArrayList<Kandidat>();
		wahlkreiszweitstimmen = new HashMap<Wahlkreis, Integer>();
		pseudoverteilungSitzzahl = 0;
		unterverteilungSitzzahl = 0;
	}

	/**
	 * Fügt dieser {@code Landesliste} den übergebenen {@link Kandidat}en mit
	 * dem übergebenen Landeslistenplatz hinzu. Gleichzeitig werden die
	 * entsprechenden Werte im {@code Kandidat}en über den passenden Aufruf von
	 * {@link Kandidat#setLandesliste(Landesliste, int)} gesetzt.
	 * <p>
	 * Beachte: Um konsistente Zustände zu garantieren dürfen keine Lücken in
	 * der Landesliste entstehen, d.h. es müssen - nachdem das Hinzufügen
	 * abgeschlossen ist - alle Landeslistenplätze von 1 bis n - wobei n der
	 * Anzahl der Landeslistenkandidaten entspricht - besetzt sein. Zudem darf
	 * ein Listenplatz nicht mehrfach besetzt werden.
	 * 
	 * @param kandidat
	 *            der {@code Kandidat} der hinzugefügt werden soll
	 * @param listenplatz
	 *            der Landeslistenplatz des neuen {@code Kandidat}en
	 * @author tillneuber
	 */
	public void addKandidat(Kandidat kandidat, int listenplatz) {
		if (listenplatz < 1) {
			throw new IllegalArgumentException(
					"Ein Kandidat kann nur auf einen positiven Landeslistenplatz gesetzt werden.");
		}
		kandidat.setLandesliste(this, listenplatz);
		// Schaffe Platz, um den neuen Kandidaten einfügen zu können
		while (liste.size() < listenplatz) {
			liste.add(null);
		}
		if (liste.get(listenplatz - 1) != null) {
			throw new IllegalArgumentException("Der Landeslistenplatz "
					+ listenplatz + " ist bereits besetzt.");
		}
		liste.set(listenplatz - 1, kandidat);
	}

	/**
	 * Berechnet die Zweitstimmen, die auf die Landesliste entfallen.
	 * 
	 * @return die Anzahl der Zweitstimmen, die auf die Landesliste entfallen
	 */
	public int berechneZweitstimmen() {
		int result = 0;
		for (Wahlkreis wk : wahlkreiszweitstimmen.keySet()) {
			result += wahlkreiszweitstimmen.get(wk);
		}
		return result;
	}

	/**
	 * Kopiert die derzeitige Landesliste <b>ohne</b> ihre bisherigen
	 * Assoziationen (Bundesländer, Partei, Kandidaten, etc.) Momentan sinnlos,
	 * da Konstruktor parameterlos.
	 */
	@Override
	public Landesliste clone() {
		return new Landesliste();
	}

	public Bundesland getBundesland() {
		return bl;
	}

	/**
	 * Liefert die Größe der {@code Landesliste}, also die Zahl an
	 * {@link Kandidat}en, die auf ihr stehen.
	 * 
	 * @return die Landeslistengröße
	 */
	public int getListengroesse() {
		return liste.size();
	}

	public Partei getPartei() {
		return partei;
	}

	/**
	 * @return the pseudoverteilungSitzzahl
	 */
	public int getPseudoverteilungSitzzahl() {
		return pseudoverteilungSitzzahl;
	}

	/**
	 * @return the unterverteilungSitzzahl
	 */
	public int getUnterverteilungSitzzahl() {
		return unterverteilungSitzzahl;
	}

	/**
	 * 
	 * @param wk
	 * @return
	 */
	public int getWahlkreisZweitstimmen(Wahlkreis wk) {
		Integer zweitstimmen = wahlkreiszweitstimmen.get(wk);
		if (zweitstimmen == null) {
			return 0;
		}
		return zweitstimmen;
	}

	@Override
	public int getZuteilungswert() {
		return this.berechneZweitstimmen();
	}

	/**
	 * Liefert einen Iterator über alle Kandidaten in der Liste zurück.
	 * Hoffentlich nach Plätzen sortiert!
	 * 
	 * @author Jan
	 * @return Iterator über alle Kandidaten auf der Liste
	 */
	public Iterator<Kandidat> kandidatenIterator() {
		return liste.iterator();
	}

	/**
	 * Löscht dieses Bundesland, sodass es von der GC aufgeräumt werden kann.
	 */
	public void loesche() {
		for (Kandidat k : liste) {
			k.loesche();
		}
		bl = null;
		partei = null;
	}

	public Iterator<Kandidat> mandateIterator() {
		Iterator<Kandidat> kandidatenIterator = liste.iterator();
		return new LandeslistenMandateIterator(kandidatenIterator,
				unterverteilungSitzzahl
						- partei.wahlkreisgewinnerInBundesland(bl));
	}

	/**
	 * Nicht direkt benutzten, stattdessen Partei.setLandesliste verwenden
	 * 
	 * @param bl
	 */
	protected void setBundesland(Bundesland bl) {
		this.bl = bl;
	}

	/**
	 * Nicht direkt benutzten, stattdessen Partei.setLandesliste verwenden
	 * 
	 * @param partei
	 */
	protected void setPartei(Partei partei) {
		this.partei = partei;
	}

	/**
	 * @param pseudoverteilungSitzzahl
	 *            the pseudoverteilungSitzzahl to set
	 */
	public void setPseudoverteilungSitzzahl(int pseudoverteilungSitzzahl) {
		this.pseudoverteilungSitzzahl = pseudoverteilungSitzzahl;
	}

	/**
	 * @param unterverteilungSitzzahl
	 *            the unterverteilungSitzzahl to set
	 */
	public void setUnterverteilungSitzzahl(int unterverteilungSitzzahl) {
		this.unterverteilungSitzzahl = unterverteilungSitzzahl;
	}

	public void setWahlkreiszweitstimmen(Wahlkreis wk, int stimmen) {
		wahlkreiszweitstimmen.put(wk, stimmen);
	}
}

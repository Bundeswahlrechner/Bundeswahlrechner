package edu.kit.iti.formal.mandatsverteilung.datenhaltung;

import java.util.LinkedList;
import java.util.List;

/**
 * Modelliert einen Wahlkreis bei der {@link Bundestagswahl}.
 */
public class Wahlkreis implements Cloneable, Comparable<Wahlkreis> {

    /** Die Kreiswahlvorschläge, die in diesem Wahlkreis antreten. */
    private final List<Kreiswahlvorschlag> bewerber;

    /** Das Bundesland, zu dem dieser Wahlkreis gehört. */
    private Bundesland bl;

    /** Der nach Erststimmen in diesem Wahlkreis gewählte Direktmandatsträger. */
    private Kreiswahlvorschlag gewinner;

    /** Der Name dieses Wahlkreises. */
    private final String name;

    /** Die Nummer dieses Wahlkreises. */
    private final int nummer;

    /**
     * Legt einen neuen Wahlkreis an.
     * 
     * @param name
     *            Name des neuen Wahlkreises
     * @param nummer
     *            Wahlkreisnummer
     */
    public Wahlkreis(String name, int nummer) {
        this.name = name;
        this.nummer = nummer;
        bewerber = new LinkedList<Kreiswahlvorschlag>();
    }

    /**
     * Legt einen neuen Wahlkreis an.
     * <p>
     * Beachte: Der Konstruktor ruft die Methode
     * {@link Bundesland#addWahlkreis(Wahlkreis)} auf dem übergebenen
     * {@code Bundesland} auf.
     * 
     * @param name
     *            Name des neuen Wahlkreises
     * @param nummer
     *            Wahlkreisnummer
     * @param bl
     *            Das Bundesland, zu dem dieser Wahlkreis gehört.
     */
    public Wahlkreis(String name, int nummer, Bundesland bl) {
        this.name = name;
        this.nummer = nummer;
        bewerber = new LinkedList<Kreiswahlvorschlag>();
        bl.addWahlkreis(this);
    }

    /**
     * Fügt den übergebenen {@code Kreiswahlvorschlag} den Bewerbern in diesem
     * Wahlkreis hinzu. Beachte: Um einen konsistenten Zustand zu garantieren,
     * sollte diese Methode nur vom Konstruktor der Klasse
     * {@code Kreiswahlvorschlag} aufgerufen werden.
     * 
     * @param kwv
     *            der {@link Kreiswahlvorschlag}, der der Bewerberliste
     *            hinzugefügt werden soll
     */
    protected void addKWV(Kreiswahlvorschlag kwv) {
        if (kwv == null) {
            throw new IllegalArgumentException(
                    "Null kann nicht als Wahlkreisbewerber gesetzt werden");
        }
        bewerber.add(kwv);
    }

    @Override
    public Wahlkreis clone() {
        Wahlkreis result = new Wahlkreis(name, nummer);
        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Als Vergleichswert werden die Wahlkreisnummern verwendet.
     */
    @Override
    public int compareTo(Wahlkreis o) {
        if (o == null) {
            return 0;
        }
        return name.compareTo(o.name);
    }

    /**
     * Gibt das {@link Bundesland} zurück in dem dieser {@code Wahlkreis} liegt.
     * 
     * @return das {@code Bundesland} in dem dieser {@code Wahlkreis} liegt
     */
    public Bundesland getBundesland() {
        return bl;
    }

    /**
     * Gibt den Gewinner - also den durch Erststimmen gewählten
     * Direktmandatsträger - dieses Wahlkreises zurück.
     * 
     * @return der Wahlkreisgewinner
     */
    public Kreiswahlvorschlag getGewinner() {
        return gewinner;
    }

    /**
     * Gibt den Wahlkreisnamen zurück.
     * 
     * @return der Wahlkreisname
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt die Wahlkreisnummer des Wahlkreises zurück.
     * 
     * @return die Wahlkreisnummer
     */
    public int getNummer() {
        return nummer;
    }

    /**
     * Gibt die Zahl der Zweitstimmen, die die übergebene {@link Partei} in
     * diesem {@code Wahlkreis} errungen hat zurück.
     * 
     * @param partei
     *            die {@code Partei}, für die die Zweitstimmenzahl bestimmt
     *            werden soll
     * @return die Zahl an Zweitstimmen, die die {@code Partei} in diesem
     *         {@code Wahlkreis} bekommen hat
     */
    public int getParteiZweitstimmen(Partei partei) {
        Landesliste ll = partei.getLandesliste(bl);
        if (ll != null) {
            return ll.getWahlkreisZweitstimmen(this);
        } else {
            return 0;
        }
    }

    /**
     * Gibt eine Liste mit den Kreiswahlvorschlägen zurück, die in diesem
     * {@code Wahlkreis} antreten.
     * 
     * @return Liste mit Kreiswahlvorschlägen, die in diesem {@code Wahlkreis}
     *         antreten
     */
    public List<Kreiswahlvorschlag> getWahlkreisBewerber() {
        return bewerber;
    }

    /**
     * Löscht diesen Wahlkreis, sodass es von der GC aufgeräumt werden kann.
     */
    public void loesche() {
        for (Kreiswahlvorschlag k : bewerber) {
            k.loesche();
        }
        bl = null;
        gewinner = null;
    }

    /**
     * Ordnet diesem Wahlkreis das {@link Bundesland} zu, in dem er liegt.
     * <p>
     * Beachte: Um einen Konsistenten Zustand zu garantieren sollte diese
     * Methode nur von {@link Bundesland#addWahlkreis(Wahlkreis)} aufgerufen
     * werden.
     * 
     * @param bl
     *            das {@code Bundesland} des Wahlkreises
     */
    protected void setBundesland(Bundesland bl) {
        this.bl = bl;
    }

    /**
     * Setzt den {@link Kreiswahlvorschlag}, der diesen {@code Wahlkreis}
     * gewonnen hat.
     * <p>
     * Beachte: Um konsistente Zustände zu garantieren muss sichergestellt
     * werden, dass der gesetzte Wahlkreisgewinner auch Bewerber in diesem
     * Wahlkreis war.
     * 
     * @param gewinner
     *            der {@code Kreiswahlvorschlag} der diesen {@code Wahlkreis}
     *            gewonnen hat
     */
    public void setGewinner(Kreiswahlvorschlag gewinner) {
        if (!(bewerber.contains(gewinner))) {
            throw new IllegalArgumentException(
                    "Der Wahlkreisgewinner tritt nicht als Wahlkreisbewerber an");
        }
        this.gewinner = gewinner;
    }

    /**
     * Setzt die Zweitstimmen, die eine {@link Partei} in diesem
     * {@code Wahlkreis} erhalten hat.
     * <p>
     * Beachte: Die übergebene {@code Partei} muss im {@link Bundesland} dieses
     * {@code Wahlkreises} eine {@link Landesliste} stellen.
     * 
     * @param partei
     *            die {@code Partei} für die die Wahlkreiszweitstimmen gesetzt
     *            werden
     * @param zweitstimmen
     *            die Anzahl der Zweitstimmen der {@code Partei}
     */
    public void setParteiZweitstimmen(Partei partei, int zweitstimmen) {
        if (partei.getLandesliste(bl) != null) {
            partei.getLandesliste(bl).setWahlkreiszweitstimmen(this,
                    zweitstimmen);
        } else {
            throw new IllegalArgumentException(
                    "Es können keine Zweitstimmen für eine Partei abgegeben werden, die im Bundesland dieses Wahlkreises keine Landesliste eingereicht hat");
        }

    }

}

package edu.kit.iti.formal.mandatsverteilung.datenhaltung;

/**
 * Modelliert eine natürliche Person die zur Bundestagswahl antritt.
 * 
 * @version 1.0
 */
public class Kandidat implements Comparable<Kandidat> {

    /**
     * Gibt an, ob dieser {@code Kandidat} über ein Auslgeichsmandat im Sinne
     * des Bundeswahlgesetzes in den Bundestag eingezogen ist.
     */
    private boolean hatAusgleichsmandat;

    /**
     * Die {@link Landesliste} für die dieser {@code Kandidat} antritt, oder
     * {@code null}, falls keine solche Liste existiert.
     */
    private Landesliste landesliste;

    /**
     * Der Listenplatz dieses {@code Kandidat}en oder -1, wenn der
     * {@code Kandiat} nicht auf einer {@link Landesliste} antritt.
     */
    private int listenplatz = -1;

    /**
     * Name des Kandidaten im Format "Nachname, Vorname" bzw.
     * "Nachname, Titel Vorname". Beispiel: "Mustermann, Max" bzw.
     * "Mustermann, Dr. Max".
     */
    private final String name;

    /**
     * Erzeugt einen neuen Kandidaten.
     * 
     * @param name
     *            - Name des Kandidaten im Format "Nachname, Vorname" bzw.
     *            "Nachname, Titel Vorname".
     */
    public Kandidat(String name) {
        super();
        this.name = name;
        hatAusgleichsmandat = false;
    }

    @Override
    public int compareTo(Kandidat o) {
        return this.nameMitParteiname().compareTo(o.nameMitParteiname());
    }

    /**
     * Liefert die {@link Landesliste} auf der dieser {@code Kandidat}
     * gegebenenfalls antritt.
     * 
     * @return die {@code Landesliste} auf der der {@code Kandidat} steht oder
     *         {@code null}, falls der {@code Kandidat} für keine solche Liste
     *         antritt
     */
    public Landesliste getLandesliste() {
        return landesliste;
    }

    /**
     * Liefert den Landeslistenplatz dieses {@code Kandidat}en.
     * 
     * @return der Landeslistenplatz des {@code Kandidat}en oder -1, wenn der
     *         {@code Kandidat} auf keiner {@link Landesliste} steht
     */
    public int getLandeslistenplatz() {
        return listenplatz;
    }

    /**
     * @return Name des Kandidaten im Format "Nachname, Vorname" bzw.
     *         "Nachname, Titel Vorname".
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt an, ob dieser {@code Kandidat} über die Ausgleichsmandatsregelung
     * des Bundeswahlgesetzes 2013 in den Bundestag eingezogen ist.
     * 
     * @return {@code true} falls dieser {@code Kandidat} ein
     *         Ausgleichsmandatsträger ist, andernfalls {@code false}
     */
    public boolean hatAusgleichsmandat() {
        return hatAusgleichsmandat;
    }

    /**
     * Prüft, ob der Kandidat ein Direktkandidat ist und ggf. Gewinner seines
     * Wahlkreises ist. Kandidaten treten nicht in Wahlkreisen an, sie sind also
     * niemals Wahlkreisgewinner. Direktkandidaten werden durch Unterklassen
     * modelliert.
     * 
     * @return {@code true}, falls der Kandidat den Wahlkreis gewonnen hat,
     *         {@code false} andernfalls
     */
    public boolean istWahlkreisGewinner() {
        return false;
    }

    /**
     * Löscht diesen Kandidat, sodass es von der GC aufgeräumt werden kann.
     */
    public void loesche() {
        landesliste = null;
    }

    /**
     * Name des Kandidaten und seine Parteizugehörigkeit. Wird unter anderem für
     * die Sortierung und die Ausgabe verwendet.
     * 
     * @return Parteiname Kandidatenname
     */
    private String nameMitParteiname() {
        return this.parteiName() + " " + name;
    }

    /**
     * Name der Landesliste, auf der der Kandidat antritt oder "parteilos".
     * 
     * @return Name der Partei oder "parteilos"
     */
    protected String parteiName() {
        if (landesliste != null) {
            return landesliste.getPartei().getName();
        } else {
            return "parteilos";
        }
    }

    /**
     * Markiert diesen {@link Kandidat}en als Ausgleichsmandatsträger.
     */
    public void setAusgleichsmandat(boolean hatAusgleichsmandat) {
        this.hatAusgleichsmandat = hatAusgleichsmandat;
    }

    /**
     * Setzt die {@link Landesliste}, für die dieser {@code Kandidat} antritt
     * und den zugehörigen Landeslistenplatz.
     * <p>
     * Beachte: Diese Methode sollte nur von der {@code Landesliste} aufgerufen
     * werden. Benutze hierzu {@link Landesliste#addKandidat(Kandidat, int)}.
     * 
     * @param landesliste
     *            die {@code Landesliste} für die der {@code Kandidat} antritt
     * @param listenplatz
     *            der Landeslistenplatz des {@code Kandidat}en
     */
    protected void setLandesliste(Landesliste landesliste, int listenplatz) {
        this.landesliste = landesliste;
        this.listenplatz = listenplatz;
    }

    @Override
    public String toString() {
        return nameMitParteiname();
    }
}

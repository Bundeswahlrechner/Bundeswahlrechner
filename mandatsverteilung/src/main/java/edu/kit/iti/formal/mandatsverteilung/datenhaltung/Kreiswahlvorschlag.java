package edu.kit.iti.formal.mandatsverteilung.datenhaltung;

/**
 * Modelliert einen Kreiswahlvorschlag, der in einem {@link Wahlkreis} zur Wahl
 * antritt.
 * 
 * @version 1.0
 */
public class Kreiswahlvorschlag extends Kandidat {

    /**
     * Die {@link Partei}, für die dieser {@code Kandidat} auf Wahlkreisebene
     * antritt oder {@code null} falls
     * <ul>
     * <li>
     * der {@code Kreiswahlvorschlag} für keine Partei antritt oder</li>
     * <li>
     * noch keine Partei gesetzt wurde.</li>
     * </ul>
     */
    private Partei partei;

    /**
     * Speichert die Erststimmen, die dieser {@code Kreiswahlvorschlag} erhält.
     */
    private int stimmen;

    /**
     * Der {@link Wahlkreis} in dem dieser {@code Kreiswahlvorschlag} antritt
     * oder {@code null}, falls noch kein {@code Wahlkreis} gesetzt wurde.
     */
    private Wahlkreis wahlkreis;

    /**
     * Erzeugt einen neuen {@code Kreiswahlvorschlag} der im übergebenen
     * {@code Wahlkreis} antritt. Beachte: Der Konstruktor ruft die Methode
     * {@link Wahlkreis#addKWV(Kreiswahlvorschlag)} mit dem neu erzeugten
     * {@code Kreiswahlvorschlag} als Parameter auf, um einen konsistenten
     * Zustand zu gewährleisten.
     * 
     * @param name
     *            der Name des Kreiswahlvorschlags
     * @param wk
     *            der {@link Wahlkreis} in dem dieser Kreiswahlvorschlag antritt
     */
    public Kreiswahlvorschlag(String name, Wahlkreis wk) {
        super(name);
        wahlkreis = wk;
        wk.addKWV(this);
        partei = null;
    }

    /**
     * Gibt die {@link Partei} zurück, für die dieser {@code Kreiswahlvorschlag}
     * auf Wahlkreisebene antritt.
     * 
     * @return die {@code Partei}, für die der KWV antritt, oder {@code null},
     *         wenn der Bewerber für keine Partei antritt
     */
    public Partei getPartei() {
        return partei;
    }

    /**
     * Liefert die Erststimmen, die dieser {@code Kreiswahlvorschlag} erhalten
     * hat.
     * 
     * @return die Erststimmen
     */
    public int getStimmen() {
        return stimmen;
    }

    /**
     * Gibt den {@code Wahlkreis} zurück, in dem dieser
     * {@code Kreiswahlvorschlag} antritt.
     * 
     * @return der oben spezifizierte {@link Wahlkreis}
     */
    public Wahlkreis getWahlkreis() {
        return wahlkreis;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean istWahlkreisGewinner() {
        return this.equals(wahlkreis.getGewinner());
    }

    /**
     * Löscht diesen KWV, sodass es von der GC aufgeräumt werden kann.
     */
    @Override
    public void loesche() {
        this.setLandesliste(null, 0);
        partei = null;
        wahlkreis = null;
    }

    /**
     * Name der Landesliste, auf der der Kandidat antritt, oder falls auf keiner
     * Landesliste, dann Name der Partei für die er als Direktkandidat antritt,
     * oder "parteilos".
     * 
     * @return Name der Partei oder "parteilos"
     */
    @Override
    protected String parteiName() {
        if (this.getLandesliste() != null) {
            return this.getLandesliste().getPartei().getName();
        } else if (partei != null) {
            return partei.getName();
        } else {
            return "parteilos";
        }
    }

    /**
     * Setzt die {@code Partei} für die dieser {@code Kreiswahlvorschlag} auf
     * Wahlkreisebene antritt. Beachte: Falls dieser {@code Kreiswahlvorschlag}
     * auf Wahlkreisebene für keine Partei antritt, muss diese Methode nicht
     * aufgerufen werden - der Aufruf kann aber optional mit dem Parameter
     * {@code null} erfolgen.
     * 
     * @param partei
     *            die {@link Partei}, für die dieser {@code Kreiswahlvorschlag}
     *            auf Wahlkreisebene antritt
     */
    public void setPartei(Partei partei) {
        this.partei = partei;
    }

    /**
     * Setzt die Erststimmen, die dieser {@code Kreiswahlvorschlag} erhalten
     * hat.
     * 
     * @param stimmen
     *            die Erststimmen dieses {@code Kreiswahlvorschlag}s
     */
    public void setStimmen(int stimmen) {
        this.stimmen = stimmen;
    }

    /**
     * Liefert eine {@code String}-Repräsentation dieses
     * {@code Kreiswahlvorschlag}s in der Form 'Parteiname' + 'Name'. Der
     * Parteiname entspricht dem Parteinamen der {@link Landesliste} auf der
     * dieser {@code Kreiswahlvorschlag} antritt. Falls diese nicht existiert
     * wird der Name der Partei gewählt, für die der {@code Kreiswahlvorschlag}
     * als Wahlkreisbewerber kandidiert. Sollte auch diese nicht existieren wird
     * der {@code String} "parteilos" verwendet.
     * 
     * @return eine {@code String}-Repräsentation dieses
     *         {@code Kreiswahlvorschlag}s
     */
    @Override
    public String toString() {
        // Zwar ist der Code identisch zur Oberklasse, aber das Verhalten und
        // der Kommentar anders, da durch dynamische Bindung eine andere
        // Implementierung von parteiName() aufgerufen wird
        return super.toString();
    }
}

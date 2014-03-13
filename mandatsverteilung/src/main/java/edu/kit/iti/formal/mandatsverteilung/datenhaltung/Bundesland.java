package edu.kit.iti.formal.mandatsverteilung.datenhaltung;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Wahlverfahren;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Zuteilbar;

/**
 * Die Klasse Bundesland repräsentiert ein Bundesland der Bundesrepublik
 * Deutschland mit den zugehörigen Wahlkreisen.
 * 
 * @version 1.0
 */
public class Bundesland implements Cloneable, Zuteilbar, Comparable<Bundesland> {

    /**
     * Eine Abkürzung für den Namen des Bundeslandes. Beispiel: "BW"
     */
    private final String abkuerzung;

    /** Die {@link Bundestagswahl} zu der dieses {@code Bundesland} gehört. */
    private Bundestagswahl bt;

    /**
     * Die Anzahl der Einwohner des Bundeslandes im Sinne von § 6, Abs. 2, Satz
     * 1 BWahlG.
     */
    private final int einwohnerzahl;

    /**
     * Das im Rahmen eines {@link Wahlverfahren}s basierend auf der
     * Einwohnerzahl errechnete, vorläufige Sitzkontingent dieses
     * {@code Bundesland}s. Hierbei handelt es sich lediglich um ein
     * Zwischenergebnis, dass für die Pseudoverteilung benötigt wird.
     */
    private int erstesSitzkontigent;

    /**
     * Der Name des Bundeslandes. Beispiel: "Baden-Württemberg"
     */
    private final String name;

    /**
     * Gibt die Zahl an parteilosen Kreiswahlvorschlägen an, die in diesem
     * Bundesland ein Direktmandat gewonnen haben.
     */
    private int parteiloseMandatstraeger;

    /**
     * Die Wahlkreise innerhalb des Bundeslandes.
     */
    private final Set<Wahlkreis> wahlkreise;

    /**
     * Erzeugt ein Bundesland.
     * 
     * @param name
     *            - Der name des Bundeslandes.
     * @param abkuerzung
     *            - Eine Abkürzung für den Namen des Bundeslandes.
     * @param einwohnerzahl
     *            - Die Einwohnerzahl des Bundeslandes im Sinne von § 6, Abs. 2,
     *            Satz 1 BWahlG.
     */
    public Bundesland(String name, String abkuerzung, int einwohnerzahl) {
        super();
        this.name = name;
        this.abkuerzung = abkuerzung;
        this.einwohnerzahl = einwohnerzahl;
        wahlkreise = new HashSet<Wahlkreis>();
    }

    /**
     * Fügt diesem {@code Bundesland} den übergebenen {@link Wahlkreis} hinzu.
     * Gleichzeitig, wird im {@code Wahlkreis} dieses {@code Bundesland} über
     * die Methode {@link Wahlkreis#setBundesland(Bundesland)} gesetzt und der
     * {@code Wahlkreis} wird der {@link Bundestagswahl} dieses
     * {@code Bundesland}es hinzugefügt.
     * 
     * @param wahlkreis
     *            der {@code Wahlkreis}, der diesem {@code Bundesland}
     *             hinzugefügt werden soll
     * @return {@code true}, falls der Wahlkreis noch nicht hinzugefügt wurde,
     *         {@code false} andernfalls
     */
    public boolean addWahlkreis(Wahlkreis wahlkreis) {
        // Setze dieses Bundesland im übergebenen Wahlkreis um spätere
        // Rücknavigation zu ermöglichen.
        wahlkreis.setBundesland(this);
        // Setze den Wahlkreis in der Bundestagswahl.
        bt.addWahlkreis(wahlkreis);
        return wahlkreise.add(wahlkreis);
    }

    /**
     * Gibt die Anzahl der Wahlkreise zurück
     * 
     * @return Anzahl der Wahlkeise
     */
    public int anzahlWahlkreise() {
        return wahlkreise.size();
    }

    @Override
    public Bundesland clone() {
        Bundesland result = new Bundesland(name, abkuerzung, einwohnerzahl);
        return result;
    }

    @Override
    public int compareTo(Bundesland o) {
        if (o == null) {
            return 0;
        }
        return name.compareTo(o.name);
    }

    /**
     * @return Die Abkürzung für den Namen des Bundeslandes.
     */
    public String getAbkuerzung() {
        return abkuerzung;
    }

    /**
     * @return Die Einwohnerzahl des Bundeslandes im Sinne von § 6, Abs. 2, Satz
     *         1 BWahlG.
     */
    public int getEinwohnerzahl() {
        return einwohnerzahl;
    }

    /**
     * Das erste Sitzkontigent
     * 
     * @return das erste Sitzkontigent
     */
    public int getErstesSitzkontigent() {
        return erstesSitzkontigent;
    }

    /**
     * @return Der name des Bundeslandes.
     */
    public String getName() {
        return name;
    }

    /**
     * Liefert die Zahl parteiloser Mandatsträger in diesem Bundesland.
     * 
     * @return die Anzahl parteiloser Mandatsträger
     */
    public int getParteiloseMandatstraeger() {
        return parteiloseMandatstraeger;
    }

    @Override
    public int getZuteilungswert() {
        return this.getEinwohnerzahl();
    }

    /**
     * Liefert einen {@link Iterator} für die {@link Landesliste}n in diesem
     * {@code Bundesland}.
     * 
     * @return der {@code Iterator} für die {@code Landesliste}n
     */
    public Iterator<Landesliste> landeslistenIterator() {
        List<Landesliste> liste = new LinkedList<Landesliste>();
        Iterator<Partei> it = bt.parteienIterator();
        while (it.hasNext()) {
            Landesliste ll = it.next().getLandesliste(this);
            if (!(ll == null)) {
                liste.add(ll);
            }
        }
        return liste.listIterator();
    }

    /**
     * Löscht dieses Bundesland, sodass es von der GC aufgeräumt werden kann.
     */
    public void loesche() {
        for (Wahlkreis w : wahlkreise) {
            w.loesche();
        }
        bt = null;
    }

    public void setBundestagswahl(Bundestagswahl bt) {
        this.bt = bt;
    }

    /**
     * Setzt das vorläufige Sitzkontingent dieses {@code Bundesland}es. Dieser
     * Wert ist lediglich ein Zwischenergebnis für die spätere Pseudoverteilung.
     * <p>
     * Beachte: Diese Methode sollte nur von einem {@link Wahlverfahren}
     * aufgerufen werden, um konsistente Berechnungsergebnisse zu garantieren.
     * 
     * @param erstesSitzkontigent
     *            das vorläufige Sitzkontingent
     */
    public void setErstesSitzkontigent(int erstesSitzkontigent) {
        this.erstesSitzkontigent = erstesSitzkontigent;
    }

    /**
     * Setzt die Zahl an parteilosen Mandatsträgern in diesem Bundesland.
     * 
     * @param parteiloseMandatstraeger
     *            die Zahl der parteilosen Mandatsträger
     */
    public void setParteiloseMandatstraeger(int parteiloseMandatstraeger) {
        this.parteiloseMandatstraeger = parteiloseMandatstraeger;
    }

    /**
     * Testmethode.
     * 
     * @author Jan
     */
    @Override
    public String toString() {
        return "Bundesland " + name + "(" + abkuerzung + ") mit "
                + einwohnerzahl + " Einwohnern";
    }

    /**
     * Erzeugt einen Iterator über alle Wahlkreise des Bundeslandes.
     * 
     * @return Iterator über alle Wahlkreise des Bundeslandes
     */
    public Iterator<Wahlkreis> wahlkreisIterator() {
        return wahlkreise.iterator();
    }

}

package edu.kit.iti.formal.mandatsverteilung.datenhaltung;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Wahlverfahren;

/**
 * Modelliert eine Bundestagswahl
 * 
 * @version 1.0
 */
public class Bundestagswahl implements Cloneable {
    /**
     * Mappt die Namen der Bundesländer auf die entsprechenden Abkürzungen.
     */
    private final Map<String, String> abkuerzungen;
    /**
     * Die vom Benutzer gewählten Berechnungsoptionen.
     */
    private Berechnungsoptionen berechnungsoptionen;
    /**
     * Bildet die Abkürzungen der Bundesländer auf die entsprechenden
     * Bundesländer ab.
     */
    private final Map<String, Bundesland> bundeslaender;

    /**
     * Bildet die Parteinamen auf die entsprechenden Parteien ab.
     */
    private final Map<String, Partei> parteien;

    /**
     * Gibt die Gesamtzahl an parteilosen Kreiswahlvorschlägen an, die ein
     * Direktmandat gewonnen haben.
     */
    private int parteiloseMandatstraeger;
    /**
     * TODO: Eventuell in der QS-Phase auf Gesamtsitzzahl inklusive
     * Wahlkreisgewinner ohne erfolgreiche Landesliste ändern, wenn dieser
     * Spezialfall implementiert wurde.
     * <p>
     * Gesamtsitzzahl im Deutschen Bundestag abzüglich der Wahlkreisgewinner
     * ohne erfolgreiche Landesliste.
     */
    private int sitzzahl;
    /**
     * Die {@link Wahlkreis}e dieser {@code Bundestagswahl}, auf die durch ihre
     * Wahlkreisnummer zugegriffen werden kann.
     */
    private final Map<Integer, Wahlkreis> wahlkreise;
    /**
     * Das {@link Wahlverfahren} dieser {@code Bundestagswahl}
     */
    private Wahlverfahren wahlverfahren;

    /**
     * Erzeugt eine neue Bundestagswahl ohne Parteien, Wahlkreise und
     * Bundesländer.
     */
    public Bundestagswahl() {
        super();
        parteien = new HashMap<String, Partei>();
        wahlkreise = new HashMap<Integer, Wahlkreis>();
        bundeslaender = new HashMap<String, Bundesland>();
        abkuerzungen = new HashMap<String, String>();
        berechnungsoptionen = new Berechnungsoptionen();
    }

    /**
     * Macht der {@code Bundestagswahl} ein {@link Bundesland} bekannt -
     * gleichzeitig wird diese Bundestagswahl im übergebenen Bundesland gesetzt.
     * Falls ein Bundesland mit der gleichen Abkürzung bereits hinzugefügt
     * wurde, wird dieses ersetzt und {@code false} zurückgegeben, andernfalls
     * {@code true}.
     * 
     * @param bundesland
     *            das Bundesland, das hinzugefügt werden soll.
     * @return {@code false}, falls ein Bundesland mit der gleichen Abkürzung
     *         bereits hinzugefügt wurde, {@code true} andernfalls
     */
    public boolean addBundesland(Bundesland bundesland) {
        String abkuerzung = bundesland.getAbkuerzung();
        String name = bundesland.getName();
        String pr = abkuerzungen.put(name, abkuerzung);
        Bundesland prev = bundeslaender.put(abkuerzung, bundesland);
        bundesland.setBundestagswahl(this);
        return ((prev == null) && (pr == null));
    }

    /**
     * Macht der Bundestagswahl eine Partei bekannt. Falls eine Partei mit dem
     * gleichen Namen bereits hinzugefügt wurde, wird diese ersetzt und false
     * zurückgegeben, andernfalls true.
     * 
     * @param partei
     *            - Die Partei, die hinzugefügt werden soll.
     * @return false, falls eine Partei mit dem gleichen Namen bereits
     *         existierte, true andernfalls
     */
    public boolean addPartei(Partei partei) {
        String name = partei.getName();
        Partei prev = parteien.put(name, partei);
        return prev == null;
    }

    /**
     * Macht dieser {@code Bundestagswahl} einen {@link Wahlkreis} bekannt.
     * Falls ein {@code Wahlkreis} mit der gleichen Nummer bereits hinzugefügt
     * wurde, wird dieser ersetzt und false zurückgegeben, andernfalls true.
     * <p>
     * Beachte: Nur zur Verwendung innerhalb des Packages. Um konsistente
     * Zustände zu garantieren, sollten Wahlkreise nur über die Methode
     * {@link Bundesland#addWahlkreis(Wahlkreis)} einem {@link Bundesland}
     * hinzugefügt werden - dieser Methodenaufruf macht diese
     * {@code Bundestagswahl} auch mit dem übergebenen {@code Wahlkreis}
     * bekannt.
     * 
     * @param wahlkreis
     *            der Wahlkreis, der hinzugefügt werden soll.
     * @return {@code false}, falls ein Wahlkreis mit der gleichen Nummer
     *         bereits existierte, {@code true} andernfalls
     */
    protected boolean addWahlkreis(Wahlkreis wahlkreis) {
        Integer nummer = wahlkreis.getNummer();
        Wahlkreis prev = wahlkreise.put(nummer, wahlkreis);
        return prev == null;
    }

    /**
     * Berechnet die Anzahl der Zweitstimmen, die bundesweit auf alle Parteien
     * entfallen.
     * 
     * @return Die Anzahl der Zweitstimmen, die bundesweit auf alle Parteien
     *         entfallen.
     */
    public int berechneZweitstimmen() {
        int zweitstimmen = 0;
        Iterator<Partei> iterator = this.parteienIterator();
        while (iterator.hasNext()) {
            Partei partei = iterator.next();
            zweitstimmen += partei.berechneZweitstimmen();
        }
        return zweitstimmen;
    }

    /**
     * Gibt die Anzahl der Bundesländer zurück
     * 
     * @return Anzahl der Bundesländer
     */
    public int bundeslaenderAnzahl() {
        return bundeslaender.size();
    }

    /**
     * Erzeugt einen Iterator über alle Bundesländer.
     * 
     * @return Iterator über alle Bundesländer
     */
    public Iterator<Bundesland> bundeslaenderIterator() {
        return bundeslaender.values().iterator();
    }

    @Override
    public Bundestagswahl clone() {
        /*
         * Ablauf wie im Importer: Erst Bundesländer, dann Wahlkreise,
         * <Parteien, Landeslisten und Kandidaten/KWVs> in dieser Reihenfolge
         */
        Wahlverfahren save = wahlverfahren;
        Bundestagswahl result = new Bundestagswahl();
        result.setBerechnungsoptionen(this.getBerechnungsoptionen().clone());
        result.setSitzzahl(sitzzahl);
        result.setWahlverfahren(this.getWahlverfahren().clone());
        wahlverfahren = save;
        Iterator<Bundesland> ib = this.bundeslaenderIterator();
        Bundesland curBundesland;
        // Wahlkreise + Bundesländer erzeugen
        while (ib.hasNext()) {
            curBundesland = ib.next();
            Bundesland newBundesland = curBundesland.clone();
            result.addBundesland(newBundesland);
        }
        // Parteien + Landeslisten + Kandidaten/KWVs erzeugen und Assoziationen
        // setzen
        Iterator<Partei> ip = this.parteienIterator();
        Partei curPartei;
        while (ip.hasNext()) {
            curPartei = ip.next();
            // Neue Partei erzeugt, fehlt: Landeslisten (inkl. WK-Zweitstimmen),
            // Kandidaten, KWVs
            Partei newPartei = curPartei.clone();
            result.addPartei(newPartei);
            ib = this.bundeslaenderIterator();
            while (ib.hasNext()) {
                curBundesland = ib.next();
                Landesliste curLL = curPartei.getLandesliste(curBundesland);
                if (curLL != null) {
                    // Klone alle Kandidaten, die keine KWVs sind
                    Iterator<Kandidat> ik = curLL.kandidatenIterator();
                    Kandidat curKandidat;
                    Landesliste newLL = curLL.clone();
                    while (ik.hasNext()) {
                        curKandidat = ik.next();
                        if (!(curKandidat instanceof Kreiswahlvorschlag)) {
                            // Kein KWV, also klonen (KWVs über Wahlkreise
                            // klonen)
                            Kandidat newKandidat = new Kandidat(
                                    curKandidat.getName());
                            newLL.addKandidat(newKandidat,
                                    curKandidat.getLandeslistenplatz());
                        } else {
                            // Mache nichts, KWVs werden später über die
                            // Wahlkreise geklont
                            // (Da sonst doppeltes Klonen)
                        }
                    }
                    newPartei.setLandesliste(
                            result.getBundeslandName(curBundesland.getName()),
                            newLL);
                }
            }
        }
        Iterator<Wahlkreis> wkIt = this.wahlkreisIterator();
        while (wkIt.hasNext()) {
            Wahlkreis curWk = wkIt.next();
            Wahlkreis newWk = curWk.clone();
            Bundesland newBl = result.getBundeslandAbk(curWk.getBundesland()
                    .getAbkuerzung());
            newBl.addWahlkreis(newWk);
            Iterator<Partei> parteiIt = result.parteienIterator();
            while (parteiIt.hasNext()) {
                Partei newPartei = parteiIt.next();
                Landesliste newLL = newPartei.getLandesliste(newBl);
                if (newLL != null) {
                    newWk.setParteiZweitstimmen(newPartei, curWk
                            .getParteiZweitstimmen(this.getPartei(newPartei
                                    .getName())));
                }
            }
            for (Kreiswahlvorschlag curKWV : curWk.getWahlkreisBewerber()) {
                Kreiswahlvorschlag newKWV = new Kreiswahlvorschlag(
                        curKWV.getName(),
                        result.getWahlkreis(curWk.getNummer()));
                if (curKWV.getPartei() != null) {
                    newKWV.setPartei(result.getPartei(curKWV.getPartei()
                            .getName()));
                }
                newKWV.setStimmen(curKWV.getStimmen());
                if (curKWV.getLandesliste() != null) {
                    Bundesland blLL = result.getBundeslandAbk(curKWV
                            .getLandesliste().getBundesland().getAbkuerzung());
                    Landesliste newLL = result.getPartei(
                            curKWV.getLandesliste().getPartei().getName())
                            .getLandesliste(blLL);
                    newLL.addKandidat(newKWV, curKWV.getLandeslistenplatz());
                }
            }
        }
        // Versuche, Wahlkreisgewinner zu kopieren
        Iterator<Wahlkreis> iw = this.wahlkreisIterator();
        Wahlkreis curWahlkreis;
        Wahlkreis newWahlkreis;
        while (iw.hasNext()) {
            curWahlkreis = iw.next();
            newWahlkreis = result.getWahlkreis(curWahlkreis.getNummer());
            Kreiswahlvorschlag curGewinner = curWahlkreis.getGewinner();
            if (curGewinner != null) {
                List<Kreiswahlvorschlag> bewerber = newWahlkreis
                        .getWahlkreisBewerber();
                for (Kreiswahlvorschlag k : bewerber) {
                    String newName = k.getName();
                    String curName = curGewinner.getName();
                    if (newName.equals(curName)) {
                        newWahlkreis.setGewinner(k);
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Liefert die Partien, die mit mindestens einem Sitz im Bundestag vertreten
     * sind.
     * <p>
     * Beachte: Die Zahl dieser Parteien kann größer sein als die Zahl der
     * Parteien, die die Sperrklausel überwinden.
     * 
     * @return
     */
    public Collection<Partei> eingezogeneParteien() {
        Collection<Partei> erfolgreicheParteien = new LinkedList<Partei>();
        for (Partei partei : parteien.values()) {
            if (partei.getOberverteilungSitzzahl() > 0) {
                erfolgreicheParteien.add(partei);
            }
        }
        return erfolgreicheParteien;
    }

    /**
     * Liefert alle {@link Kandidat}en, die in den Bundestag eingezogen sind.
     * 
     * @return alle Bundestagsabgeordneten
     */
    public Collection<Kandidat> getAbgeordnete() {
        // Füge zunächst alle Wahlkreisgewinner der Abgeordnetenliste hinzu.
        List<Kandidat> abgeordnete = new LinkedList<Kandidat>();
        Iterator<Wahlkreis> wkIt = this.wahlkreisIterator();
        while (wkIt.hasNext()) {
            Wahlkreis wk = wkIt.next();
            Kandidat gewinner = wk.getGewinner();
            if (gewinner != null) { // Notwendig für umfangreiche Testfälle
                abgeordnete.add(gewinner);
            }
        }
        // Iteriere über alle Parteien und anschließend über alle Bundesländer
        // und prüfe, ob eine Landesliste der aktuell betrachteten Partei im
        // aktuellen Bundesland existiert.
        Iterator<Partei> parteienIt = this.parteienIterator();
        while (parteienIt.hasNext()) {
            Partei partei = parteienIt.next();
            // Landeslistenabeordnete kommen nur von Parteien, die die
            // Sperrklausel überwunden haben.
            if (partei.isSperrklauselUeberwunden()) {
                Iterator<Bundesland> blIt = this.bundeslaenderIterator();
                while (blIt.hasNext()) {
                    Bundesland bl = blIt.next();
                    Landesliste ll = partei.getLandesliste(bl);
                    if (ll != null) {
                        Iterator<Kandidat> llKandidaten = ll.mandateIterator();
                        // Füge die Abgeordneten dieser Landesliste der
                        // Abgeordnetenliste hinzu.
                        while (llKandidaten.hasNext()) {
                            Kandidat k = llKandidaten.next();
                            abgeordnete.add(k);
                        }
                    }
                }
            }
        }
        // Sortierung der Abgerodneten erst alphabetisch nach Partei, dann
        // parteiintern nach Namen.
        Collections.sort(abgeordnete);
        return abgeordnete;
    }

    /**
     * @return Die vom Benutzer gewählten Berechnungsoptionen.
     */
    public Berechnungsoptionen getBerechnungsoptionen() {
        return berechnungsoptionen;
    }

    /**
     * Gibt das Bundesland mit der übergebenen Abkürzung zurück, oder null,
     * falls kein Bundesland mit dieser Abkürzung bekannt ist.
     * 
     * @param abkuerzung
     *            - Abkürzung des Namens des Bundeslandes
     * @return Das Bundesland mit der übergebenen Abkürzung, oder null, falls
     *         kein Bundesland mit dieser Abkürzung exisitiert.
     */
    public Bundesland getBundeslandAbk(String abkuerzung) {
        return bundeslaender.get(abkuerzung);
    }

    /**
     * Gibt das Bundesland mit dem übergebenen Namen zurück, oder null, falls
     * kein Bundesland mit diesem Namen bekannt ist.
     * 
     * @param name
     *            - Name des Bundeslandes
     * @return Das Bundesland mit dem übergebenen Namen, oder null, falls kein
     *         Bundesland mit diesem Namen exisitiert.
     */
    public Bundesland getBundeslandName(String name) {
        String abkuerzung = abkuerzungen.get(name);
        return this.getBundeslandAbk(abkuerzung);
    }

    /**
     * Gibt die Partei mit dem übergebenen Namen zurück, oder null, falls keine
     * Partei mit diesem Namen bekannt ist.
     * 
     * @param name
     *            - der Name der Partei
     * @return Die Partei mit dem übergebenen Namen, oder null, falls keine
     *         Partei mit diesem Namen exisitiert.
     */
    public Partei getPartei(String name) {
        return parteien.get(name);
    }

    /**
     * Liefert die Zahl parteiloser Mandatsträger im Bundesgebiet.
     * 
     * @return die Anzahl parteiloser Mandatsträger
     */
    public int getParteiloseMandatstraeger() {
        return parteiloseMandatstraeger;
    }

    /**
     * @return Gesamtsitzzahl im Deutschen Bundestag
     */
    public int getSitzzahl() {
        return sitzzahl;
    }

    /**
     * Gibt den Wahlkreis mit der übergebenen Nummer zurück, oder null, falls
     * kein Wahlkreis mit dieser Nummer existiert.
     * 
     * @param nummer
     *            - die Wahlkreisnummer
     * @return Der Wahlkreis mit der übergebenen Wahlkreisnummer, oder null,
     *         falls die Wahlkreisnummer nicht existiert.
     */
    public Wahlkreis getWahlkreis(Integer nummer) {
        return wahlkreise.get(nummer);
    }

    /**
     * @return the wahlverfahren
     */
    public Wahlverfahren getWahlverfahren() {
        return wahlverfahren;
    }

    /**
     * Gibt die Anzahl der Parteien zurück
     * 
     * @return Anzahl der Parteien
     */
    public int parteienAnzahl() {
        return parteien.size();
    }

    /**
     * Erzeugt einen Iterator über alle Parteien.
     * 
     * @return Iterator über alle Parteien
     */
    public Iterator<Partei> parteienIterator() {
        return parteien.values().iterator();
    }

    /**
     * @param berechnungsoptionen
     *            - Die vom Benutzer gewählten Berechnungsoptionen.
     */
    public void setBerechnungsoptionen(Berechnungsoptionen berechnungsoptionen) {
        this.berechnungsoptionen = berechnungsoptionen;
    }

    /**
     * Setzt die Zahl an parteilosen Mandatsträgern im Bundesgebiet.
     * 
     * @param parteiloseMandatstraeger
     *            die Zahl der parteilosen Mandatsträger
     */
    public void setParteiloseMandatstraeger(int parteiloseMandatstraeger) {
        this.parteiloseMandatstraeger = parteiloseMandatstraeger;
    }

    /**
     * @param sitzzahl
     *            - Gesamtsitzzahl im Deutschen Bundestag
     */
    public void setSitzzahl(int sitzzahl) {
        this.sitzzahl = sitzzahl;
    }

    /**
     * @param wahlverfahren
     *            the wahlverfahren to set
     */
    public void setWahlverfahren(Wahlverfahren wahlverfahren) {
        if (this.wahlverfahren != wahlverfahren) {
            this.wahlverfahren = wahlverfahren;
            wahlverfahren.setBundestagswahl(this);
        }
    }

    /**
     * Gibt die Anzahl der Wahlkreise zurück
     * 
     * @return Anzahl der Wahlkreise
     */
    public int wahlkreisAnzahl() {
        return wahlkreise.size();
    }

    /**
     * Erzeugt einen Iterator über alle Wahlkreise bundesweit.
     * 
     * @return Iterator über alle Wahlkreise bundesweit
     */
    public Iterator<Wahlkreis> wahlkreisIterator() {
        return wahlkreise.values().iterator();
    }
}
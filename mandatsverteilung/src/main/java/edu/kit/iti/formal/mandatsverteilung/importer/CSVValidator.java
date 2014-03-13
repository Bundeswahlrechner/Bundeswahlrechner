package edu.kit.iti.formal.mandatsverteilung.importer;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kandidat;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kreiswahlvorschlag;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Landesliste;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Wahlkreis;

/**
 * Validiert Wahlen, die durch das CSV-Format importiert wurden, schrittweise.
 * Zugunsten besserer Laufzeiten gibt es hier mehrere Returns. Möglicherweise
 * kann man diesen Validierer auch für spätere nicht-CSV-Importer verwenden, auf
 * jeden Fall ist eine Erweiterung mittels eines Interfaces möglich. Zum
 * Beispiel: interface Validator mit den Methoden boolean
 * validiere{Wahlkreise|Kandidaten|Stimmen|Bundeslaender} (Bundestagswahl b).
 * 
 * @author Jan
 * 
 */
public class CSVValidator {

    void validiereBundeslaender(Bundestagswahl b) throws IOException {
        Iterator<Bundesland> ib = b.bundeslaenderIterator();
        Bundesland curBundesland;
        while (ib.hasNext()) {
            curBundesland = ib.next();
            this.validiereBundesland(curBundesland);
        }
    }

    /**
     * Validiert ein gegebenes Bundesland. Momentan wird überprüft: Name,
     * Abkürzung, Einwohnerzahl und Anzahl Wahlkreise sind gesetzt und ungleich
     * 0/null. Wahlkreisanzahl entspricht der Zahl Wahlkreise, die man über den
     * Iterator bekommt, und kein Wahlkreis ist doppelt vorhanden.
     * 
     * @param bl
     *            Das überprüfte Bundesland
     */
    @SuppressWarnings("unused")
    private void validiereBundesland(Bundesland bl) throws IOException {
        Dictionary<String, Wahlkreis> bisherigeWahlkreise = new Hashtable<>();
        int tmp = bl.anzahlWahlkreise();
        String abk = bl.getAbkuerzung();
        int tmpZahl = bl.getEinwohnerzahl();
        String name = bl.getName();
        int wert = bl.getZuteilungswert();
        Iterator<Wahlkreis> iw = bl.wahlkreisIterator();
        int counter = 0;
        Wahlkreis cur;
        while (iw.hasNext()) {
            cur = iw.next();
            counter++;
            if ((bisherigeWahlkreise.put(cur.getName(), cur) != null)) {
                throw new IOException("Zwei Wahlkreise haben den selben Namen");
            }
        }
        if ((tmp == 0) || (abk == null) || (tmpZahl == 0) || (name == null)
                || (wert == 0)) {
            throw new IOException("Ein Wert im Wahlkreis ist nicht gesetzt");
        }
    }

    void validiereKandidaten(Bundestagswahl b) throws IOException {
        Iterator<Bundesland> ib = b.bundeslaenderIterator();
        Bundesland curBundesland;
        while (ib.hasNext()) {
            curBundesland = ib.next();
            Iterator<Partei> ip = b.parteienIterator();
            Partei curPartei;
            while (ip.hasNext()) {
                curPartei = ip.next();
                Landesliste curLL = curPartei.getLandesliste(curBundesland);
                if (curLL != null) {
                    this.validiereLandesliste(curLL);
                }
            }
        }
        Iterator<Wahlkreis> iw = b.wahlkreisIterator();
        Wahlkreis cur;
        while (iw.hasNext()) {
            cur = iw.next();
            this.validiereWahlkreisKWVs(cur);
        }

    }

    /**
     * Überprüft, dass alle Invarianten für eine gegebene Landesliste erfüllt
     * sind. Das sind momentan: Es gibt von 1 bis Listengröße Kandidaten, die
     * nicht null sind. Kein Kandidat steht zweimal auf der Landesliste (Wird
     * über Namensgleichheit festgestellt)
     * 
     * @param ll
     *            die zu überprüfende Landesliste
     */
    private void validiereLandesliste(Landesliste ll) throws IOException {
        Dictionary<String, Kandidat> bisherigeKandidaten = new Hashtable<>();
        int anzahl = ll.getListengroesse();
        Iterator<Kandidat> ik = ll.kandidatenIterator();
        Kandidat cur;
        for (int i = 1; i <= anzahl; i++) {
            cur = ik.next();
            if ((cur == null)
                    || (bisherigeKandidaten.put(cur.getName(), cur) != null)
                    || (cur.getLandesliste() != ll)
                    || (cur.getLandeslistenplatz() != i)) {
                throw new IOException("In der Landesliste der "
                        + ll.getPartei().getName() + " in "
                        + ll.getBundesland().getName()
                        + " ist ein Attribut nicht gesetzt.");
            }
        }
    }

    private void validiereWahlkreisKWVs(Wahlkreis w) throws IOException {
        boolean hasStimmen = false;
        List<Kreiswahlvorschlag> bewerber = w.getWahlkreisBewerber();
        for (Kreiswahlvorschlag k : bewerber) {
            if (k.getStimmen() > 0) {
                hasStimmen = true;
            }
        }
        if (!hasStimmen) {
            throw new IOException("Alle Stimmen in Wahlkreis Nr. "
                    + w.getNummer() + "sind 0!");
        }
    }
}

package edu.kit.iti.formal.mandatsverteilung.generierer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kreiswahlvorschlag;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Wahlkreis;
import edu.kit.iti.formal.mandatsverteilung.gui.GeneriererBeobachter;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.BundeswahlgesetzSperrklauselpruefer;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Sperrklauselpruefer;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.Wahlverfahren;
import edu.kit.iti.formal.mandatsverteilung.wahlberechnung.WahlverfahrenMitSperrklausel;

/**
 * Generiert eine Bundestagswahl mit gewünschten Eigenschaften basierend auf
 * einer gegebenen Bundestagswahl.
 * 
 * @author Jan
 * 
 */
public class RandomisierterGenerierer implements GeneriererSubjekt {

    private static int genauigkeit;

    private static double veraenderteWahlkreiseErststimmen = 0.005;
    private static double veraenderteWahlkreiseZweitstimmen = 0.75;

    public static int getGenauigkeit() {
        return genauigkeit;
    }

    /**
     * Hilfsvariable, die anzeigt, wie viele Bundesländer die derzeitige
     * Bundestagswahl besitzt.
     */
    @SuppressWarnings("unused")
    private final int anzahlBundeslaender;
    /**
     * Hilfsvariable, die anzeigt, wie viele Parteien die derzeitige
     * Bundestagswahl besitzt.
     */
    @SuppressWarnings("unused")
    private final int anzahlParteien;

    private int benoetigteDirektmandate;
    private final List<GeneriererBeobachter> beobachter;
    /**
     * Anfänglich übergebene Wahl. Wird verwendet, damit generiere keine
     * Parameter benötigt.
     */
    private final Bundestagswahl btw;
    /**
     * Liste aller Einschränkungen für diese Wahl.
     */
    private final List<Einschraenkung> constraints;
    private final Map<String, Boolean> eingezogen;

    /**
     * Random-Objekt, für Zufallszahlen.
     */
    private final Random ran;

    /**
     * Starttemperatur des Simulated-Annealing-Algorithmus.
     */
    private double temperatur;

    public RandomisierterGenerierer(Bundestagswahl b) {
        ran = new Random();
        beobachter = new LinkedList<>();
        btw = b.clone().clone();
        genauigkeit = 100;
        constraints = new ArrayList<>();
        anzahlBundeslaender = btw.bundeslaenderAnzahl();
        anzahlParteien = btw.parteienAnzahl();
        temperatur = 20.0;
        eingezogen = new HashMap<>();
        benoetigteDirektmandate = 0;
        Wahlverfahren w = btw.getWahlverfahren();
        if (w instanceof WahlverfahrenMitSperrklausel) {
            Sperrklauselpruefer s = ((WahlverfahrenMitSperrklausel) w)
                    .getSperrklauselpruefer();
            if (s instanceof BundeswahlgesetzSperrklauselpruefer) {
                benoetigteDirektmandate = ((BundeswahlgesetzSperrklauselpruefer) s)
                        .getGrundmandate();
            }
        }
    }

    public void addEinschraenkung(Einschraenkung e) {
        constraints.add(e);
    }

    private void aendereErststimmen(Bundestagswahl result,
            int zuVeraenderndeWahlkreise, int anzahlWahlkreise) {
        if (ran.nextInt(2) == 0) {
            for (Wahlkreis randomWahlkreis : this.getRandomWahlkreise(
                    zuVeraenderndeWahlkreise, anzahlWahlkreise, result)) {
                // Vertausche Gewinner in einem zufälligen Wahlkreis
                // Die beiden, die vertauscht werden, können dieselben sein
                Kreiswahlvorschlag winner = randomWahlkreis.getGewinner();
                int erststimmenVonGewinner = winner.getStimmen();
                List<Kreiswahlvorschlag> verlierer = randomWahlkreis
                        .getWahlkreisBewerber();
                Kreiswahlvorschlag neuerGewinner = verlierer.get(ran
                        .nextInt(verlierer.size()));
                winner.setStimmen(neuerGewinner.getStimmen());
                neuerGewinner.setStimmen(erststimmenVonGewinner);
            }
        }
    }

    private void aendereZweitstimmen(Bundestagswahl result,
            int anzahlWahlkreise, List<Partei> wichtigeParteien) {
        /*
         * Verteile Zweitstimmen neu. Prinzip: Eine zufällig bestimmte Partei
         * bekommt eine zufällige Zahl an Stimmen abgezogen. Solange es davon
         * noch unverteilte Stimmen gibt, wird erneut eine zufällige Partei
         * gezogen und ein Anteil zwischen 0 und 100% erwürfelt. Die neu
         * gezogene Partei erhält nun diesen Anteil von den ursprünglich
         * abgezogenen Stimmen oder alle noch unverteilten Stimmen der
         * Geber-Partei (das Minimum, da sonst zu viele Stimmen verteilt
         * werden.)
         */
        int zuVeraenderndeWahlkreise = (int) (anzahlWahlkreise
                * veraenderteWahlkreiseZweitstimmen * ran.nextDouble());
        Bundesland curBundesland = null;
        List<Partei> curParteien = new ArrayList<>();
        for (Wahlkreis randomWahlkreis : this.getRandomWahlkreise(
                zuVeraenderndeWahlkreise, anzahlWahlkreise, result)) {
            Bundesland newBundesland = randomWahlkreis.getBundesland();
            if (!newBundesland.equals(curBundesland)) {
                // Zwischengemerktes Ergebnis nicht korrekt, Bundesland und
                // Parteienliste neu setzen
                curBundesland = newBundesland;
                Iterator<Partei> ip = result.parteienIterator();
                Partei cur;
                while (ip.hasNext()) {
                    cur = ip.next();
                    if (cur.getLandesliste(curBundesland) != null) {
                        curParteien.add(cur);
                    }
                }
            }
            List<Partei> wichtigeParteienKopie = new ArrayList<>();
            for (Partei p : wichtigeParteien) {
                wichtigeParteienKopie.add(p);
            }
            Partei geberPartei = null;
            if (ran.nextDouble() < this.parteienWahrscheinlichkeit()) {
                while ((geberPartei == null)
                        && !wichtigeParteienKopie.isEmpty()) {
                    int index = ran.nextInt(wichtigeParteienKopie.size());
                    Partei randomWichtigePartei = wichtigeParteienKopie
                            .get(index);
                    if (randomWichtigePartei.getLandesliste(curBundesland) != null) {
                        geberPartei = randomWichtigePartei;
                    }
                }
                if (geberPartei == null) {
                    geberPartei = this.getRandomPartei(curParteien);
                }
            } else {
                geberPartei = this.getRandomPartei(curParteien);
            }
            int gesamteZweitstimmen = randomWahlkreis
                    .getParteiZweitstimmen(geberPartei);
            int zuVerteilendeStimmen = (int) (ran.nextDouble() * gesamteZweitstimmen);
            // Ziehe Zweitstimmen von Partei ab, falls diese im aktuellen
            // Wahlkreis Zweitstimmen erhalten hat.
            if (geberPartei.getLandesliste(randomWahlkreis.getBundesland()) != null) {
                randomWahlkreis.setParteiZweitstimmen(geberPartei,
                        (gesamteZweitstimmen - zuVerteilendeStimmen));
            }
            int ehemalsZuVerteilendeStimmen = zuVerteilendeStimmen;
            while (zuVerteilendeStimmen > 0) {
                Partei nehmerPartei = this.getRandomPartei(curParteien);
                int verteilteStimmen = (int) Math
                        .round((ran.nextDouble() * ehemalsZuVerteilendeStimmen));
                if (verteilteStimmen > zuVerteilendeStimmen) {
                    verteilteStimmen = zuVerteilendeStimmen;
                }
                zuVerteilendeStimmen -= verteilteStimmen;
                int gesamteZweitstimmenNehmer = randomWahlkreis
                        .getParteiZweitstimmen(nehmerPartei);
                if (nehmerPartei
                        .getLandesliste(randomWahlkreis.getBundesland()) != null) {
                    randomWahlkreis.setParteiZweitstimmen(nehmerPartei,
                            (gesamteZweitstimmenNehmer + verteilteStimmen));
                }
            }
        }
    }

    @Override
    public void benachrichtigeBeobachter(String neueNachricht) {
        for (GeneriererBeobachter b : beobachter) {
            b.aktualisieren(neueNachricht);
        }
    }

    private int berechneAbstand(Bundestagswahl cur) {
        int result = 0;
        for (Einschraenkung e : constraints) {
            result += e.ueberpruefeErgebnis(cur);
        }
        return result;
    }

    @Override
    public void entferneBeobachter(GeneriererBeobachter b) {
        beobachter.remove(b);
    }

    public Bundestagswahl generiere() {
        return this.generiere(new ArrayList<String>());
    }

    /**
     * Versucht, eine Stimmverteilung, deren resultierende Sitzverteilung den
     * Einschränkungen genügt, zu erzeugen. Hierzu wird das
     * Simulated-Annealing-Verfahren verwendet. Dem Generierer muss eine
     * Bundestagswahl sowie eine Liste von Einschränkungen übergeben werden.
     * Nach einem gewissen Timeout wird abgebrochen und die bisher beste Lösung
     * zurückgegeben.
     * 
     * @return Eine Bundestagswahl, deren Stimmverteilung zu einem vorher
     *         gewünschten Ergebnis führt.
     */
    public Bundestagswahl generiere(List<String> parteien) {
        /*
         * Verwendetes Simulated Annealing: Man bestimmt eine Starttemperatur T
         * (eventuell später vom Nutzer eingebbar). Diese wird als Startwert
         * einer Nullfolge übergeben und die derzeitige Temperatur als Parameter
         * betrachtet. Für jede Temperatur legt eine weitere Folge die Anzahl
         * Iterationen über zufällige Lösungen aus der näheren Umgebung fest.
         * Ist eine solche Lösung besser als die derzeitige, wird sie zur
         * derzeitigen. Ist sie besser als die bisher beste, wird sie zur bisher
         * besten. Ist sie nicht besser als die derzeitige, wird sie mit einer
         * gewissen Wahrscheinlichkeit, die mit zunehmender Temperatur abnimmt,
         * trotzdem zur derzeitigen Lösung (aber niemals zur bisher besten).
         * Nach einem gewissen Abbruchkriterium (Bei uns: Güte der Lösung oder
         * Zeit-Ablauf, je nachdem, was zuerst eintritt) wird die bisher beste
         * Lösung ausgegeben.
         */
        Bundestagswahl anfangsWahl = btw;
        anfangsWahl.getWahlverfahren().berechneWahlergebnis();
        this.setzeEingezogeneParteien(anfangsWahl);
        int baseDist = this.berechneAbstand(anfangsWahl);
        if (baseDist == 0) {
            return anfangsWahl;
        } else {
            Bundestagswahl bestSolution = anfangsWahl.clone();
            int bestDistance = baseDist;
            Bundestagswahl currentSolution = anfangsWahl;
            int currentDistance = baseDist;
            int bisherBerechnet = 0;
            long startingTime = System.currentTimeMillis();
            // Maximale Laufzeit, erstmal 10 Minuten
            long maxRunTime = 100000;
            // Entspricht dem Abbruchkriterium beim Simulated Annealing
            while (((System.currentTimeMillis() - startingTime) <= maxRunTime)
                    && (bestDistance != 0)) {
                int maxSchrittzahl = this.getNächsteSchrittzahl();
                this.nächsteTemperatur();
                for (int i = 0; i < maxSchrittzahl; i++) {
                    String message = new String("<html>Bisher wurden "
                            + bisherBerechnet
                            + " Bundestagswahlen berechnet.<br>"
                            + "Der Anfangsabstand der Lösung war " + baseDist
                            + ", der Abstand der bisher besten Lösung ist "
                            + bestDistance + ".</html>");
                    this.benachrichtigeBeobachter(message);
                    // CurrentSolution = BTW der letzten Iteration
                    // NewSolution = Neu berechnete BTW
                    Bundestagswahl newSolution = this.getNachbar(
                            currentSolution, parteien);
                    int newDistance = this.berechneAbstand(newSolution);
                    bisherBerechnet++;
                    if (newDistance <= currentDistance) {
                        // Akzeptiere immer, da lokale Verbesserung
                        currentSolution = newSolution;
                        currentDistance = newDistance;
                        if (currentDistance < bestDistance) {
                            // Neues Maximum gefunden
                            bestSolution = currentSolution.clone();
                            bestDistance = currentDistance;
                            if (bestDistance == 0) {
                                bestSolution.getWahlverfahren()
                                        .berechneWahlergebnis();
                                return bestSolution;
                            }
                        }
                    } else {
                        // Lokale Verschlechterung, akzeptiere mit
                        // Wahrscheinlichkeit abhängig von Temperatur
                        double zufallsZahl = ran.nextDouble();
                        double akzeptanzWahrscheinlichkeit = this
                                .getWahrscheinlichkeit(newDistance,
                                        currentDistance);
                        if (zufallsZahl < akzeptanzWahrscheinlichkeit) {
                            // Akzeptiere trotzdem
                            currentSolution = newSolution;
                            currentDistance = newDistance;
                        }
                    }
                }
            }
            bestSolution.getWahlverfahren().berechneWahlergebnis();
            return bestSolution;
        }
    }

    /**
     * Methode, die eine Bundestagswahl entgegennimmt, sie klont und die
     * Stimmzahlen der Kopie randomisiert verändert. Entspricht dem
     * find_neighbour() beim Simulated Annealing. Hierbei wird die
     * Gesamt-Stimmenzahl beibehalten.
     * 
     * @param zuVeraendernd
     *            Bundestagswahl, deren Stimmzahlen verändert werden
     * @param parteien
     *            Parteien, die bei der Generierung hauptsächlich verändert
     *            werden
     */
    private Bundestagswahl getNachbar(Bundestagswahl zuVeraendernd,
            List<String> parteien) {
        List<Partei> wichtigeParteien = new ArrayList<>();
        List<Boolean> geberParteien = new ArrayList<>();
        Bundestagswahl result = zuVeraendernd.clone();
        for (String s : parteien) {
            wichtigeParteien.add(result.getPartei(s));
            geberParteien.add(ran.nextBoolean());
        }

        int anzahlWahlkreise = result.wahlkreisAnzahl();
        int zuVeraenderndeWahlkreise = (int) (anzahlWahlkreise
                * RandomisierterGenerierer.veraenderteWahlkreiseErststimmen * ran
                .nextDouble());
        // Verändere Erststimmen
        this.aendereErststimmen(result, zuVeraenderndeWahlkreise,
                anzahlWahlkreise);
        this.aendereZweitstimmen(result, anzahlWahlkreise, wichtigeParteien);
        this.lasseNeueParteiEinziehen(result);
        result.getWahlverfahren().berechneWahlergebnis();
        return result;
    }

    /**
     * Methode, die die Anzahl Iterationen für die derzeitige Temperatur
     * zurückgibt. Momentan konstant.
     * 
     * @return Anzahl Iterationen für die innere Schleife beim Simulated
     *         Annealing
     */
    private int getNächsteSchrittzahl() {
        return 30;
    }

    /**
     * Hilfsmethode, die eine zufällige Partei zurückgibt.
     * 
     * @param eine
     *            Liste aller zur Wahl stehender Parteien
     * @return eine zufällige Partei
     */
    private Partei getRandomPartei(List<Partei> parteien) {
        Partei result = parteien.get(ran.nextInt(parteien.size()));
        return result;
    }

    /**
     * Gibt eine ArrayList mit <i>zuZiehen</i> vielen, zufällig gewählten
     * Wahlkreisen aus.
     * 
     * @param zuZiehen
     *            Wie viele Wahlkreise zufällig bestimmt werden sollen
     * @param anzahlWahlkreise
     *            Wie viele Wahlkreise es insgesamt gibt (eigentlich unnötig,
     *            eventuell kleiner Performancegewinn)
     * @return Eine Liste mit zufälligen Wahlkreisen
     */
    private List<Wahlkreis> getRandomWahlkreise(int zuZiehen,
            int anzahlWahlkreise, Bundestagswahl curWahl) {
        // Grundplan: Mache Lottoziehung mit anzahlWahlkreise vielen Zahlen,
        // dann sortiere diese Liste, dann nehme Wahlkreisiterator und füge
        // jeden Wahlkreis, dessen Stelle im Iterator in der Liste auftaucht,
        // der Ausgabe hinzu
        ArrayList<Integer> zahlen = new ArrayList<>();
        for (int i = 0; i < anzahlWahlkreise; i++) {
            zahlen.add(i);
        }
        // Erzeugt zahlen-Liste 1234...
        ArrayList<Integer> zufallszahlen = new ArrayList<>();
        for (int i = 0; i < zuZiehen; i++) {
            // Entfernt die zahl an einer zufälligen Stelle aus der Liste und
            // fügt sie dem Ergebnis hinzu
            zufallszahlen.add(zahlen.remove(ran.nextInt(zahlen.size())));
        }
        Collections.sort(zufallszahlen);
        ArrayList<Wahlkreis> result = new ArrayList<>();
        int counterList = 0;
        int counterIterator = 0;
        Iterator<Wahlkreis> iw = curWahl.wahlkreisIterator();
        Wahlkreis cur;
        while ((counterList < zuZiehen) && iw.hasNext()) {
            counterIterator++;
            cur = iw.next();
            if (counterIterator == zufallszahlen.get(counterList)) {
                // Wir sind im x-ten Schritt, füge Wahlkreis Ergebnis hinzu,
                // erhöhe Listencounter
                result.add(cur);
                counterList++;
            }
        }
        return result;
    }

    private double getWahrscheinlichkeit(int newDistance, int curDistance) {
        double exponent = (newDistance - curDistance) / (temperatur);
        double result = Math.pow(Math.E, -exponent);
        return result;
    }

    /**
     * Methode, die sehr selten eine Partei nimmt, und dieser Direktmandate
     * gibt.
     * 
     * @param result
     *            Die Bundestagswahl (wird dabei verändert)
     */
    private void lasseNeueParteiEinziehen(Bundestagswahl result) {
        if (ran.nextInt(15) == 0) {
            for (String s : eingezogen.keySet()) {
                if (!eingezogen.get(s)) {
                    int counter = benoetigteDirektmandate;
                    Iterator<Wahlkreis> iw = result.wahlkreisIterator();
                    Wahlkreis curWahlkreis;
                    while (counter > 0 && iw.hasNext()) {
                        curWahlkreis = iw.next();
                        for (Kreiswahlvorschlag k : curWahlkreis
                                .getWahlkreisBewerber()) {
                            Partei p = k.getPartei();
                            if (p != null && p.getName().equals(s)) {
                                this.swapStimmen(k, curWahlkreis.getGewinner());
                                counter--;
                                break;
                            }
                        }
                    }
                    boolean tmp = eingezogen.put(s, true);
                    assert !tmp;
                    break;
                }
            }
        }
    }

    /**
     * Methode, die die nächste Temperatur berechnet und zurückgibt. Wird für
     * die äußere Schleife beim Simulated-Annealing benutzt.
     * 
     * @return neue Temperatur (konvergiert gegen 0)
     */
    private double nächsteTemperatur() {
        temperatur *= 0.75;
        return temperatur;
    }

    private double parteienWahrscheinlichkeit() {
        return 0.66;
    }

    @Override
    public void registriereBeobachter(GeneriererBeobachter b) {
        beobachter.add(b);
    }

    /**
     * Instantiiert die HashMap, die verfolgt, ob eine Partei eingezogen ist,
     * oder nicht. Funktioniert am besten für sehr wenige Parteienconstraints
     * von Parteien, die noch nie eingezogen sind.
     * 
     * @param anfangsWahl
     */
    private void setzeEingezogeneParteien(Bundestagswahl anfangsWahl) {
        Iterator<Partei> ip = anfangsWahl.parteienIterator();
        Partei curPartei;
        while (ip.hasNext()) {
            curPartei = ip.next();
            eingezogen.put(curPartei.getName(),
                    curPartei.isSperrklauselUeberwunden());
        }
    }

    /**
     * Tauscht die Erststimmen der übergebenen Kreiswahlvorschläge. Sollten sich
     * im gleichen Wahlkreis befinden.
     * 
     * @param k1
     *            Der erste KWV
     * @param k2
     *            Der zweite KWV
     */
    private void swapStimmen(Kreiswahlvorschlag k1, Kreiswahlvorschlag k2) {
        assert k1.getWahlkreis() == k2.getWahlkreis();
        int tmp = k1.getStimmen();
        k1.setStimmen(k2.getStimmen());
        k2.setStimmen(tmp);
    }
}

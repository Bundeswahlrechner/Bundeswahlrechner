package edu.kit.iti.formal.mandatsverteilung.importer;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundesland;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Kreiswahlvorschlag;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Landesliste;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Partei;
import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Wahlkreis;

/**
 * Klasse, die zunächst die Wahlkreise und später die Stimmzahlen einer
 * Bundestagswahl erzeugt/setzt. Benötigt hierzu eine Bundestagswahl und eine
 * Stimmergebnis-Datei. (z.B. die kerg.csv vom Bundeswahlleiter)
 * 
 * @author Jan, Pascal
 * @version 1.0
 */
class CSVWahlkreiseUndStimmenParser extends CSVParser implements
        WahlkreiseUndStimmenParser {
    /**
     * Legt fest, welche ID das Bundesgebiet in der Datei hat.
     */
    private static final int bundesgebietID = 99;
    /**
     * Interne ID dafür, dass die Bundesgebiet-Zeile erreicht wurde. Sollte
     * nicht als Bundesland-ID in der kerg.csv auftauchen
     */
    private static final int bundesID = Integer.MAX_VALUE;
    /**
     * Interne ID dafür, dass die ID-Zelle sich nicht in einen Integer umwandeln
     * ließ. Sollte nicht als Bundesland-ID in der kerg.csv auftauchen.
     */
    private static final int errorID = Integer.MIN_VALUE;
    /**
     * Legt fest, in der wievielten Zelle die "gehört zu"-Information. (ob es
     * sich also um ein Bundesland oder einen Wahlkreis handelt) steht
     */
    private static final int gehoertZuZelle = 2;
    /**
     * Legt fest, in der wievielten Zelle die wahlkreisID steht. Hierbei wird
     * von 0 an gezählt.
     */
    private static final int idZelle = 0;
    /**
     * Legt fest, in welcher Zelle der Name eines Bundeslands/Wahlkreises steht.
     */
    private static final int namensZelle = 1;
    /**
     * Legt fest, ab welcher Spalte die Parteien in der Datei beginnen.
     */
    private static final int parteiBeginnZelle = 19;
    /**
     * Legt fest in welcher zeile die Parteinamen stehen. Es wird von 0 an
     * gezählt.
     */
    private static final int parteienZeile = 2;
    /**
     * Legt fest, nach wie vielen Zellen die Farbe der Partei steht.
     */
    private static final int parteiFarbenOffset = 1;
    /**
     * Legt fest, in welcher Zeile die Wahlkreise beginnen. Hierbei wird von 0
     * an gezählt
     */
    private static final int wahlkreisBeginnZeile = 5;

    public static Color getRandomColor() {
        Random ran = new Random();
        Color result = new Color(ran.nextFloat(), ran.nextFloat(),
                ran.nextFloat());
        return result;
    }

    /**
     * Aktuell betrachtetes Bundesland.
     */
    private Bundesland curBundesland;
    /**
     * Aktuell betrachtete Partei.
     */
    private Partei curPartei;
    /**
     * Derzeitig betrachtete Wahlkreise.
     */
    private final List<Wahlkreis> curWahlkreise;
    /**
     * Gibt an, ob Wahlkreise erzeugt oder Stimmen gesetzt werden sollen.
     */
    private boolean ersterDurchlauf;
    private Partei[] parteien;
    private File wahlkreiseUndStimmDatei;

    /**
     * Stream zu den zu parsenden Daten.
     */
    private InputStream wahlkreiseUndStimmenStream;

    /**
     * Erzeugt einen WahlkreiseUndStimmenParser. Werden später Files übergeben,
     * so ist der gewünschte Aufrufablauf setzeDaten(), parseWahlkreise() und
     * später parseStimmen() Benutzt man jedoch die setzeDaten()-Version mit
     * InputStreams, muss nach parseWahlkreise() noch reinitialisiere()
     * aufgerufen werden mit einem neuen Stream zu den Daten.
     */
    CSVWahlkreiseUndStimmenParser() {
        super();
        beginneBeiZeile = wahlkreisBeginnZeile;
        curWahlkreise = new ArrayList<Wahlkreis>();
        ersterDurchlauf = true;
    }

    /**
     * Erzeugt einen Wahlkreis mit den angegebenen Eigenschaften.
     * 
     * @param name
     *            Name des Wahlkreises
     * @param nummer
     *            Nummer des Wahlkreises
     * @return einen neuen Wahlkreis
     */
    private Wahlkreis erzeugeWahlkreis(String name, int nummer) {
        Wahlkreis result = new Wahlkreis(name, nummer);
        return result;
    }

    @Override
    protected String fehlerhafteDatei() {
        return "Der Fehler trat in der Wahlkreise und Stimmen-Datei auf.";
    }

    private Color getParteiColor(Partei p, String s) {
        Color result;
        switch (p.getName()) {
        case "CDU":
            result = Color.black;
            break;
        case "SPD":
            result = Color.red;
            break;
        case "FDP":
            result = Color.yellow;
            break;
        case "DIE LINKE":
            result = Color.magenta;
            break;
        case "GRÜNE":
            result = Color.green;
            break;
        case "CSU":
            result = Color.blue;
            break;
        case "PIRATEN":
            result = Color.orange;
            break;
        default:
            if (!s.isEmpty()) {
                // Versuche, Farbe aus Datei zu lesen
                try {
                    result = Color.decode(s);
                } catch (NumberFormatException e) {
                    result = getRandomColor();
                }
            } else {
                // Zufällige Farbe
                result = getRandomColor();
            }
            break;
        }
        return result;
    }

    @Override
    public void parseStimmen() throws IOException {
        if (wahlkreiseUndStimmDatei != null) {
            wahlkreiseUndStimmenStream = new FileInputStream(
                    wahlkreiseUndStimmDatei);
        }
        this.parseDatei(wahlkreiseUndStimmenStream);
    }

    @Override
    public void parseWahlkreise() throws IOException {
        this.parseDatei(wahlkreiseUndStimmenStream);
        ersterDurchlauf = false;
        beginneBeiZeile = parteienZeile;
    }

    @Override
    protected void parseZeile(String[] zeile) throws IOException {
        if (ersterDurchlauf) {
            if (zeile.length <= gehoertZuZelle) {
                throw new IOException(this.fehlerInZelle(gehoertZuZelle)
                        + ImportFehler.FALSCHE_ZEILEN_LAENGE.toString());
            }
            int temp = 0;
            try {
                temp = Integer.parseInt(zeile[gehoertZuZelle]);
            } catch (NumberFormatException e) {
                if (zeile[gehoertZuZelle].isEmpty()) {
                    temp = bundesID;
                } else {
                    temp = errorID;
                }
            }
            switch (temp) {
            case bundesgebietID:
                curBundesland = wahl.getBundeslandName(zeile[namensZelle]);
                if (curBundesland == null) {
                    throw new IOException(this.fehlerInZelle(namensZelle)
                            + ImportFehler.KEIN_BUNDESLAND_GEFUNDEN.toString());
                }
                Iterator<Wahlkreis> it = curWahlkreise.iterator();
                while (it.hasNext()) {
                    Wahlkreis w = it.next();
                    boolean tmp = curBundesland.addWahlkreis(w);
                    if (!tmp) {
                        throw new IOException(
                                this.fehlerInZelle(1)
                                        + ImportFehler.ZWEITER_WAHLKREIS_IN_DATEI
                                                .toString());
                    }
                    it.remove();
                }
                break;
            case errorID:
                throw new IOException(this.fehlerInZelle(gehoertZuZelle)
                        + ImportFehler.KEINE_ZAHL_GEFUNDEN.toString());
                // Hier kein break notwendig, da Exception
            case bundesID: // Wird momentan ignoriert
                break;
            default:
                int i = 0;
                try {
                    i = Integer.parseInt(zeile[idZelle]);
                } catch (NumberFormatException e) {
                    throw new IOException(this.fehlerInZelle(idZelle)
                            + ImportFehler.KEINE_ZAHL_GEFUNDEN.toString());
                }
                curWahlkreise.add(this.erzeugeWahlkreis(zeile[namensZelle], i));
                break;
            }

        } else {
            int parteilosBeginnSpalte = zeile.length - 5;
            int parteiSpaltenLaenge = 4;
            switch (zeile[gehoertZuZelle]) {
            case "gehört": {
                int aktuelleSpalte = parteiBeginnZelle;
                parteien = new Partei[(parteilosBeginnSpalte - parteiBeginnZelle) / 4];
                int i = 0;
                while (aktuelleSpalte < parteilosBeginnSpalte) {
                    Partei curPartei = wahl.getPartei(zeile[aktuelleSpalte]);
                    if (curPartei == null) {
                        throw new IOException(
                                this.fehlerInZelle(aktuelleSpalte)
                                        + ImportFehler.KEINE_PARTEI_GEFUNDEN
                                                .toString()
                                        + zeile[aktuelleSpalte]);
                    }
                    String colorString = zeile[aktuelleSpalte
                            + parteiFarbenOffset];
                    Color curParteiColor = this.getParteiColor(curPartei,
                            colorString);
                    curPartei.setFarbe(curParteiColor);
                    parteien[i] = curPartei;
                    i++;
                    aktuelleSpalte += parteiSpaltenLaenge;
                }
            }
                for (int j = 0; j < 2; j++) {
                    zeile = reader.readNext();
                    aktuelleZeile++;
                }

            case "": // Bundesgebietzeile, man kann aufhören
                break;
            default: // Hoffentlich eine Wahlkreis/Bundeslandzeile, sonst Error
                int id = 0;
                try {
                    id = Integer.parseInt(zeile[gehoertZuZelle]);
                } catch (NumberFormatException e) {
                    throw new IOException(this.fehlerInZelle(gehoertZuZelle)
                            + ImportFehler.KEINE_ZAHL_GEFUNDEN.toString());
                }
                if (id == bundesgebietID) {
                    // Bundesland-Zeile, also ignorieren
                } else {
                    int wahlkreisID = 0;
                    try {
                        wahlkreisID = Integer.parseInt(zeile[idZelle]);
                    } catch (NumberFormatException e) {
                        throw new IOException(this.fehlerInZelle(idZelle)
                                + ImportFehler.KEINE_ZAHL_GEFUNDEN.toString());
                    }
                    Wahlkreis curWahlkreis = wahl.getWahlkreis(wahlkreisID);
                    // Nehme an, dass Parteien im Array gleich sortiert sind wie
                    // in Datei
                    int parteiCounter = 0;
                    int aktuelleSpalte = parteiBeginnZelle;
                    int erststimmenSpaltenOffset = 0;
                    int zweitstimmenSpaltenOffset = 2;
                    while (aktuelleSpalte < parteilosBeginnSpalte) {
                        curPartei = parteien[parteiCounter];
                        if (!zeile[aktuelleSpalte + erststimmenSpaltenOffset]
                                .isEmpty()) {
                            int aktuelleErststimmen = 0;
                            try {
                                aktuelleErststimmen = Integer
                                        .parseInt(zeile[aktuelleSpalte
                                                + erststimmenSpaltenOffset]);
                            } catch (NumberFormatException e) {
                                throw new IOException(
                                        this.fehlerInZelle(aktuelleSpalte
                                                + erststimmenSpaltenOffset)
                                                + ImportFehler.KEINE_ZAHL_GEFUNDEN
                                                        .toString());
                            }
                            this.setzeErststimmen(aktuelleErststimmen,
                                    curWahlkreis, curPartei);
                        }
                        if (!zeile[aktuelleSpalte + zweitstimmenSpaltenOffset]
                                .isEmpty()) {
                            int aktuelleZweitstimmen = 0;
                            try {
                                aktuelleZweitstimmen = Integer
                                        .parseInt(zeile[aktuelleSpalte
                                                + zweitstimmenSpaltenOffset]);
                            } catch (NumberFormatException e) {
                                throw new IOException(
                                        this.fehlerInZelle(aktuelleSpalte
                                                + zweitstimmenSpaltenOffset)
                                                + ImportFehler.KEINE_ZAHL_GEFUNDEN
                                                        .toString());
                            }
                            this.setzeZweitstimmen(aktuelleZweitstimmen,
                                    curWahlkreis, curPartei);
                        }
                        aktuelleSpalte += parteiSpaltenLaenge;
                        parteiCounter++;
                    }
                    // Kümmere um unabhängige Bewerber
                    if (!zeile[aktuelleSpalte].isEmpty()) {
                        int unabhErststimmen = 0;
                        try {
                            unabhErststimmen = Integer
                                    .parseInt(zeile[aktuelleSpalte]);
                        } catch (NumberFormatException e) {
                            throw new IOException(
                                    this.fehlerInZelle(aktuelleSpalte)
                                            + ImportFehler.KEINE_ZAHL_GEFUNDEN
                                                    .toString());
                        }
                        this.setzeUnabhaengigeErststimmen(unabhErststimmen,
                                curWahlkreis);
                    }
                }
            }
        }
    }

    public void reinitialisiere(InputStream in) {
        wahlkreiseUndStimmenStream = in;
    }

    @Override
    public void setzeDaten(File f, Bundestagswahl b) throws IOException {
        wahlkreiseUndStimmDatei = f;
        wahlkreiseUndStimmenStream = new FileInputStream(f);
        wahl = b;
    }

    @Override
    public void setzeDaten(InputStream in, Bundestagswahl b) {
        wahlkreiseUndStimmenStream = in;
        wahl = b;
    }

    private void setzeErststimmen(int erststimmen, Wahlkreis wahlkreis,
            Partei partei) {
        List<Kreiswahlvorschlag> list = wahlkreis.getWahlkreisBewerber();
        for (int i = 0; i < list.size(); i++) {
            Kreiswahlvorschlag kwv = list.get(i);
            if ((kwv.getPartei() != null) && kwv.getPartei().equals(partei)) {
                kwv.setStimmen(erststimmen);
                return;
            }
        }
    }

    private void setzeUnabhaengigeErststimmen(int erststimmen,
            Wahlkreis wahlkreis) {
        List<Kreiswahlvorschlag> list = wahlkreis.getWahlkreisBewerber();
        for (int i = 0; i < list.size(); i++) {
            Kreiswahlvorschlag kwv = list.get(i);
            if (kwv.getPartei() == null) {
                kwv.setStimmen(erststimmen);
                return;
            }
        }
    }

    private void setzeZweitstimmen(int zweitstimmen, Wahlkreis wahlkreis,
            Partei partei) {
        Bundesland cur = wahlkreis.getBundesland();
        Landesliste ll = partei.getLandesliste(cur);
        if (ll == null) {
            partei.setLandesliste(cur, new Landesliste());
            ll = partei.getLandesliste(cur);
        }
        ll.setWahlkreiszweitstimmen(wahlkreis, zweitstimmen);
    }
}

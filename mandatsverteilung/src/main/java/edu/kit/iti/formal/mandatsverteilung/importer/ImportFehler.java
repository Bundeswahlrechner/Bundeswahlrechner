package edu.kit.iti.formal.mandatsverteilung.importer;

enum ImportFehler {
    FALSCHE_ZEILEN_LAENGE("In dieser Zeile gibt es zu viele/zu wenige Zellen!"), KEIN_BUNDESLAND_GEFUNDEN(
            "Kein Bundesland mit diesem Namen gefunden"), KEIN_WAHLKREIS_GEFUNDEN(
            "Einen Wahlkreis mit dieser Nummer gibt es nicht."), KEINE_PARTEI_GEFUNDEN(
            "Keine Partei mit diesem Namen gefunden: "), KEINE_PARTEIEN_ZEILE(
            "Es wurde am Anfang der Stimmdatei keine Zeile, in der die Parteien gelistet sind, gefunden."), KEINE_ZAHL_GEFUNDEN(
            "In dieser Zelle sollte eine Zahl stehen!"), NICHT_GENUEGEND_ZELLEN(
            "In dieser Zeile sind nicht ausreichend Zellen vorhanden"), ZWEITER_WAHLKREIS_IN_DATEI(
            "Es gab bereits einen Wahlkreis mit dieser Nummer");

    String beschreibung;

    private ImportFehler(String s) {
        beschreibung = s;
    }

    @Override
    public String toString() {
        return beschreibung;
    }

}

package edu.kit.iti.formal.mandatsverteilung.generierer;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

public class MandatszahlRelativ extends Einschraenkung {
    /**
     * Die Partei, für die die Einschränkung gelten soll.
     */
    protected String partei;

    public MandatszahlRelativ(int wert, int abweichung, String partei) {
        assert wert > 0;
        assert abweichung > 0;
        assert partei != null;
        this.wert = wert;
        this.abweichung = abweichung;
        this.partei = partei;
        gewichtung = 1.0;
    }

    @Override
    int ueberpruefeErgebnis(Bundestagswahl b) {
        int genauigkeit = RandomisierterGenerierer.getGenauigkeit();
        double ergebnis = (double) b.getPartei(partei)
                .getOberverteilungSitzzahl() / (double) b.getSitzzahl();
        return this.minDistance((int) (100 * ergebnis * genauigkeit),
                genauigkeit * wert, genauigkeit * abweichung);

    }

}

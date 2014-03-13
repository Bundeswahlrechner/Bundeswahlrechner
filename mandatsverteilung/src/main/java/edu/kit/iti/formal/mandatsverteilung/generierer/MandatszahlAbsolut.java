package edu.kit.iti.formal.mandatsverteilung.generierer;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

public class MandatszahlAbsolut extends Einschraenkung {
    /**
     * Die Partei, für die die Einschränkung gelten soll.
     */
    protected String partei;

    public MandatszahlAbsolut(int wert, int abweichung, String partei) {
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
        int ergebnis = b.getPartei(partei).getOberverteilungSitzzahl();
        return this.minDistance(ergebnis * genauigkeit, wert * genauigkeit,
                abweichung * genauigkeit);
    }

}

package edu.kit.iti.formal.mandatsverteilung.generierer;

import edu.kit.iti.formal.mandatsverteilung.datenhaltung.Bundestagswahl;

/**
 * Modelliert eine Einschränkung an das Ergebnis des Generierers, dass der
 * Bundestag eine bestimmte Größe haben soll.
 * 
 * @author Jan
 * 
 */
public class SitzzahlEinschraenkung extends Einschraenkung {

    public SitzzahlEinschraenkung(int wert, int abweichung) {
        assert wert > 0;
        assert abweichung > 0;
        this.wert = wert;
        this.abweichung = abweichung;
        gewichtung = 1.0;
    }

    @Override
    int ueberpruefeErgebnis(Bundestagswahl b) {
        int tatsaechlicheSitzzahl = b.getSitzzahl();
        int genauigkeit = RandomisierterGenerierer.getGenauigkeit();
        double minD = (minDistance(genauigkeit * tatsaechlicheSitzzahl,
                genauigkeit * wert, genauigkeit * abweichung));
        return (int) (gewichtung * minD);
    }
}
